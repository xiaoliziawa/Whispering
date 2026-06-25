package com.lirxowo;

import com.lirxowo.effect.ModEffects;
import com.lirxowo.event.WPEvents;
import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.network.Network;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whispering implements ModInitializer {
	public static final String MOD_ID = "whispering";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEffects.init();
		AllCurios.init();
		Network.registerServerReceivers();
		WPEvents.register();
	}
}