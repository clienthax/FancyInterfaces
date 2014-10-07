package uk.co.haxyshideout.FancyInterfaces.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import uk.co.haxyshideout.FancyInterfaces.gui.GuiPlayerList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class SkinLoader implements Runnable {

	private final String username;

	public SkinLoader(String username)
	{
		this.username = username;
	}

	@Override
	public void run() {
		try {
			BufferedImage skinImage = ImageIO.read(new URL("http://skins.minecraft.net/MinecraftSkins/" + username + ".png"));
			GuiPlayerList.addCachedHead(username, faceFromSkin(skinImage));
			GuiPlayerList.delFetching(username);
		}
		catch (IOException ioException){
			try {
				BufferedImage skinImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(AbstractClientPlayer.locationStevePng).getInputStream());
				GuiPlayerList.addCachedHead(username, faceFromSkin(skinImage));
				GuiPlayerList.delFetching(username);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private BufferedImage faceFromSkin(BufferedImage skinImage) {
		//head is at 8,8, width , height = 8
		//head helmet front is at 40,8, width, height = 8
		BufferedImage merged = new BufferedImage(8,8, BufferedImage.TYPE_INT_ARGB);
		BufferedImage face = skinImage.getSubimage(8,8,8,8);
		BufferedImage overlay = skinImage.getSubimage(40,8,8,8);
		Graphics g = merged.getGraphics();
		//draw the accessory over the top of the face
		g.drawImage(face, 0, 0, null);
		g.drawImage(overlay, 8, 8, null);
		return merged;
	}
}
