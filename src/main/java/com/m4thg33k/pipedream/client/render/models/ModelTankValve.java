package com.m4thg33k.pipedream.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModelTankValve extends ModelBaseMultiModel{

    public static final String DEFAULT = "default";
    public static final String PUSH = "push";
    public static final String PULL = "pull";


    public ModelTankValve()
    {
        super();

        Map<String, String> keys = new HashMap<String, String>();
        keys.put(DEFAULT,"tankValve");
        keys.put(PUSH, "tankValvePush");
        keys.put(PULL, "tankValvePull");

        createModelsWithTextureName("tankValve", "#valveTexture", keys);

//        //load the model
//        OBJModel model = ModelHelper.loadModel("tankValve");
//
//        //apply textures and orient correctly
//        IModel valve = ModelHelper.retexture(model, "#None", "tankValve");
//
//
//        //"turn on" the models
//        valveModel = ModelHelper.bake(valve);
    }

    public void renderModel(int data)
    {
        switch (data)
        {
            case 1:
                renderModel(PUSH);
                break;
            case 2:
                renderModel(PULL);
                break;
            default:
                renderModel(DEFAULT);
        }
    }
}
