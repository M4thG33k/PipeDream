package com.m4thg33k.pipedream.core.proxy;

import com.m4thg33k.pipedream.blocks.ModBlocks;
import com.m4thg33k.pipedream.tiles.ModTiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preinit(FMLPreInitializationEvent event)
    {
        ModBlocks.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        ModTiles.init();
    }

    public void postinit(FMLPostInitializationEvent event)
    {

    }
}
