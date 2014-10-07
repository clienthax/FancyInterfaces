package uk.co.haxyshideout.FancyInterfaces.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import uk.co.haxyshideout.FancyInterfaces.gui.GuiPlayerList;

public class EventHandler {

	GuiPlayerList guiPlayerList = new GuiPlayerList();

	@SubscribeEvent
	public void onOverlay(RenderGameOverlayEvent event) {
		if(Minecraft.getMinecraft().currentScreen instanceof GuiPlayerList) {
		//	if(event.isCancelable())
		//		event.setCanceled(true);
		}
		if(event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
			event.setCanceled(true);
			guiPlayerList.setResolution(event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
			Minecraft.getMinecraft().displayGuiScreen(guiPlayerList);
		}
	}

}
