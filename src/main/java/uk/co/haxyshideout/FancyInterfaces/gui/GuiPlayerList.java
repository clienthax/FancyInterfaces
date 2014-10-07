package uk.co.haxyshideout.FancyInterfaces.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import uk.co.haxyshideout.FancyInterfaces.helpers.GuiHelper;
import uk.co.haxyshideout.FancyInterfaces.helpers.SkinLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiPlayerList extends GuiScreen {

	public GuiPlayerList() {
	}

	public void setResolution(int width, int height) {
		this.width = width;
		this.height = height;
	}


	static HashMap<String, BufferedImage> cachedSkins = new HashMap<>();
	static List<String> fetchingSkins = new ArrayList<>();
	int buttonID = 0;
	GuiButton previousButton = new GuiButton(buttonID++, 70, 180, 60, 20, "Previous");
	GuiButton nextButton = new GuiButton(buttonID++, 290, 180, 60, 20, "Next");
	int currentPage = 0;

	@Override
	public void initGui() {
		this.buttonList.add(previousButton);
		this.buttonList.add(nextButton);
		previousButton.enabled = false;
		nextButton.enabled = false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float random) {
		NetHandlerPlayClient handler = mc.thePlayer.sendQueue;

		this.mc.mcProfiler.startSection("playerList");
		List<GuiPlayerInfo> players = (List<GuiPlayerInfo>)handler.playerInfoList;
		int rows = (int)Math.ceil(players.size() / 3.0);//needs to be rounded up
		if(rows > 9)
			rows = 9;
		int columns = 3;

		int playersPerPage = 27;

		int columnWidth = 300 / columns;
		int columnHeight = 18;//normally 9

		if (columnWidth > 150)
		{
			columnWidth = 150;
		}

		int left = (width - columns * columnWidth) / 2;
		byte border = 10;
		int extraRows = 0;
		if(players.size() > playersPerPage)
			extraRows = 2;
		else
			currentPage = 0;

		drawRect(left - 1, border - 1, left + columnWidth * columns, border + columnHeight * (rows+extraRows), Integer.MIN_VALUE);

		int pages = (players.size() / playersPerPage) + 1;

		for (int i = currentPage *playersPerPage; i < (currentPage *playersPerPage)+playersPerPage; i++)//split into pages with 27 on each page
		{
			int cellPtr = i - (currentPage * playersPerPage);
			int xPos = left + cellPtr % columns * columnWidth;
			int yPos = border + cellPtr / columns * columnHeight;


			if (i < players.size())
			{
				drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + columnHeight -1, 553648127 /*new Color(158,152,152, 100).getRGB()*/);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				GuiPlayerInfo player = players.get(i);
				ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(player.name);
				String displayName = ScorePlayerTeam.formatPlayerName(team, player.name);
				mc.fontRenderer.drawStringWithShadow(displayName, xPos, yPos, 16777215);
				//0-150 green, 151 - 300 orange, 300+ red
				EnumChatFormatting pingColour = EnumChatFormatting.DARK_RED;
				if(player.responseTime < 151)
					pingColour = EnumChatFormatting.GREEN;
				else if(player.responseTime < 300)
					pingColour = EnumChatFormatting.GOLD;



				mc.fontRenderer.drawStringWithShadow("Ping: "+pingColour+player.responseTime, xPos, yPos+9, 16777215);

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(bindFace(StringUtils.stripControlCodes(player.name)))
				{
					GuiHelper.drawImageQuad(xPos + columnWidth - 20, yPos, 17, 17, 0, 0, 1, 1, zLevel);
				}

				//for some reason ping isn't updated when a new player logs in unless you relog.. packet issue?
				//S38PacketPlayerListItem is the packet from server to update the list
			}
		}

		if(players.size() > playersPerPage)
		{
			previousButton.enabled = currentPage > 0;
			nextButton.enabled = currentPage != pages - 1;

			previousButton.drawButton(mc, mouseX, mouseY);
			nextButton.drawButton(mc, mouseX, mouseY);

			mc.fontRenderer.drawStringWithShadow((currentPage +1)+"/"+((players.size()/27)+1), 200, 185, 16777215);
		}
		else
		{
			previousButton.enabled = false;
			nextButton.enabled = false;
		}


	}

	@Override
	protected void keyTyped(char keyChar, int keyCode)
	{
		if(keyCode == 15)//Tab
			mc.displayGuiScreen(null);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.equals(previousButton))
			currentPage--;
		else if(button.equals(nextButton))
			currentPage++;
	}

	public boolean bindFace(String username)
	{
		BufferedImage skinImage;

		if(fetchingSkins.contains(username))
		{
			return false;
		}
		else {
			if (!cachedSkins.containsKey(username)) {
				fetchingSkins.add(username);
				new Thread(new SkinLoader(username)).start();
				return false;
			}
			else
			{
				skinImage = cachedSkins.get(username);
				TextureUtil.uploadTextureImage(0, skinImage);
			}
		}

		return true;
	}

	public static void addCachedHead(String username, BufferedImage skinImage) {
		cachedSkins.put(username,skinImage);
	}

	public static void delFetching(String username) {
		fetchingSkins.remove(username);
	}
}

