package com.m4thg33k.pipedream.items;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemWrench itemWrench = new ItemWrench();

    public static void preInit()
    {
        GameRegistry.register(itemWrench);
    }
}
