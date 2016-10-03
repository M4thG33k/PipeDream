package com.m4thg33k.pipedream.tiles;

import com.m4thg33k.pipedream.lib.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {
    public static void init()
    {
        String prefix = "tile.pipedream";
        GameRegistry.registerTileEntity(TileTank.class, prefix + Names.TILE_TANK);
    }
}
