package com.m4thg33k.pipedream.client.events;

import com.m4thg33k.pipedream.core.util.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents {


    @SubscribeEvent
    public void stitchTexture(TextureStitchEvent.Pre pre)
    {
        LogHelper.info("Stitching textures into the map");

        pre.getMap().registerSprite(new ResourceLocation("pipedream","blocks/tankValve"));
    }
}
