package com.m4thg33k.pipedream.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.Map;

public class ModelBaseModel {

    protected IBakedModel myModel;

    public ModelBaseModel(String modelName, @Nullable String toReplace, @Nullable String replacement)
    {
        //load the model
        OBJModel theModel = ModelHelper.loadModel(modelName);

        if (toReplace == null || replacement == null)
        {
            //"turn on" without re-texturing
            myModel = ModelHelper.bake(theModel);
        }
        else {
            //apply textures with correct orientation
            IModel model = ModelHelper.retextureFromResourceLocation(theModel, toReplace, FluidRegistry.WATER.getStill());
//            IModel model = ModelHelper.retexture(theModel, toReplace, replacement);

            //"turn on" the model
            myModel = ModelHelper.bake(model);
        }
    }

    public ModelBaseModel(String modelName)
    {
        this(modelName, null, null);
    }

    public void renderModel()
    {
        renderModel(myModel);
    }

    protected void renderModel(IBakedModel model)
    {
        renderModel(model, -1);
    }

    protected void renderModel(IBakedModel model, int color)
    {
        ModelHelper.renderModel(model, color);
    }
}
