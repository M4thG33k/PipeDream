package com.m4thg33k.pipedream.client.render;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.client.render.models.TankItemModel;
import com.m4thg33k.pipedream.core.util.LogHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TankRenderHelper {

    public static final String SMART_MODEL_NAME = PipeDream.MODID + ":Tank";
    public static final ModelResourceLocation SMART_MODEL = new ModelResourceLocation(SMART_MODEL_NAME, "inventory");

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        LogHelper.info("TankeRenderHalper.onModelBake");

        Object object = event.getModelRegistry().getObject(SMART_MODEL);
        if (object instanceof IBakedModel)
        {
            IBakedModel existingModel = (IBakedModel) object;
            TankItemModel customModel = new TankItemModel(existingModel);
            event.getModelRegistry().putObject(SMART_MODEL, customModel);
            TankItemOverrideHandler.baseModel = customModel;
        }
    }
}
