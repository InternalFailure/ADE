package com.ade.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = "ade")
public class ADE 
{
	@SidedProxy(clientSide = "com.ade.core.ClientProxy", serverSide = "com.ade.core.CommonProxy")
	public static CommonProxy proxy;
}
