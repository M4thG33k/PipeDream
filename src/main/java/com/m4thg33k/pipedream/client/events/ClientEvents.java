package com.m4thg33k.pipedream.client.events;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.client.render.FluidColorHelper;
import com.m4thg33k.pipedream.core.util.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents {


    @SubscribeEvent
    public void stitchTexture(TextureStitchEvent.Pre pre)
    {
        LogHelper.info("Stitching textures into the map");

        reg(pre, "tankValve");
        reg(pre, "tankValvePull");
        reg(pre, "tankValvePush");
        reg(pre, "tankSphere");
        reg(pre, "tankItem");

//        pre.getMap().registerSprite(new ResourceLocation("pipedream","blocks/tankValve"));
//        pre.getMap().registerSprite(new ResourceLocation("pipedream","blocks/tankValvePull"));
//        pre.getMap().registerSprite(new ResourceLocation("pipedream","blocks/tankValvePush"));
//        pre.getMap().registerSprite(new ResourceLocation(PipeDream.MODID, "blocks/tankSphere"));
    }

    private void reg(TextureStitchEvent.Pre pre, String suffix)
    {
        pre.getMap().registerSprite(new ResourceLocation(PipeDream.MODID, "blocks/"+suffix));
    }

    @SubscribeEvent
    public void postStitchTexture(TextureStitchEvent.Post post) throws Exception
    {
        FluidColorHelper.postTextureStitch(post);
    }
}
