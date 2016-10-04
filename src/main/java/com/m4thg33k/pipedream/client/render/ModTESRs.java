package com.m4thg33k.pipedream.client.render;

import com.m4thg33k.pipedream.client.render.tile.RenderTileTank;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModTESRs {

    public static void preinit()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new RenderTileTank());
    }
}
