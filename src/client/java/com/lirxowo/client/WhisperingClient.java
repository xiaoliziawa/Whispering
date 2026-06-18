package com.lirxowo.client;

import net.fabricmc.api.ClientModInitializer;

public class WhisperingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PioneerCoreClient.register();
		LavaHeartShieldClient.register();
		MoltenEmblemClient.register();
		ForlornTotemClient.register();
	}
}