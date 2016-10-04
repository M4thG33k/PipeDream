package com.m4thg33k.pipedream.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelTankValve {

    private IBakedModel valveModel;

    public ModelTankValve()
    {
        //load the model
        OBJModel model = ModelHelper.loadModel("tankValve");

        //apply textures and orient correctly
        IModel valve = ModelHelper.retexture(model, "#None", "tankValve");


        //"turn on" the models
        valveModel = ModelHelper.bake(valve);
    }

    public void renderValve()
    {
        renderModel(valveModel);
    }

    private void renderModel(IBakedModel model)
    {
        renderModel(model, -1);
    }

    private void renderModel(IBakedModel model, int color)
    {
        ModelHelper.renderModel(model, color);
    }
}
