package com.alrex.parcool.proxy;

import net.minecraftforge.network.SimpleChannel;

public abstract class CommonProxy {
	public abstract void registerMessages(SimpleChannel instance);

	public void init() {
	}
}
