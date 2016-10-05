package com.m4thg33k.pipedream.client.render.models;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.core.util.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.*;

public class SphereModels {

    private static Map<String, ModelSphere> cache = new HashMap<String, ModelSphere>();
    private static List<String> names = new ArrayList<String>();
    public static String BASE_TEXTURE = "pipeDreamTankSphere";

//    public static void preInit()
//    {
//        Map<String, Fluid> fluidMap = FluidRegistry.getRegisteredFluids();
//
//        for (String name: fluidMap.keySet())
//        {
//            LogHelper.info("Caching model for " + name + " with " + fluidMap.get(name).getStill());
//            cache.put(name, new ModelSphere(fluidMap.get(name).getStill()));
//        }
//
//        LogHelper.info("Cached " + cache.size() + " models!");
//        LogHelper.info("breakpoint");
//    }

    public static ModelSphere getSphereFromFluid(Fluid fluid)
    {
        if (fluid == null)
        {
            return null;
        }
        return getSphereFromFluidName(fluid.getName());
    }

    public static ModelSphere getSphereFromFluidName(String name)
    {
        if (!(names.contains(name))) {
            createModel(name);
        }
        return fetchModel(name);
    }

    private static void createModel(String name)
    {
        if (name.equals(BASE_TEXTURE))
        {
            cache.put(name, new ModelSphere(new ResourceLocation(PipeDream.MODID, "blocks/tankSphere")));
        }
        else {
            cache.put(name, new ModelSphere(FluidRegistry.getFluid(name).getStill()));
            names.add(name);
        }
    }

    private static ModelSphere fetchModel(String name)
    {
        return cache.get(name);
    }
}
