package uk.co.haxyshideout.FancyInterfaces;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import uk.co.haxyshideout.FancyInterfaces.config.Constants;
import uk.co.haxyshideout.FancyInterfaces.events.EventHandler;

import java.util.logging.Logger;

@Mod(name = Constants.MODNAME, version = Constants.MODVERSION, acceptableRemoteVersions = "*", modid = Constants.MODID)
public class FancyInterfaces {

	public Logger logger = Logger.getLogger(Constants.MODNAME);

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Loading Fancy Interfaces mod version "+Constants.MODVERSION+" by clienthax");
		if(event.getSide().equals(Side.CLIENT))
		{
			EventHandler eventHandler = new EventHandler();
			MinecraftForge.EVENT_BUS.register(eventHandler);
		}
	}

}
