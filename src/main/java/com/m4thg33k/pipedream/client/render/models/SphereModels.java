package com.m4thg33k.pipedream.client.render.models;

import com.m4thg33k.pipedream.core.util.LogHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Map;

public class SphereModels {

    private static Map<String, ModelSphere> cache = new HashMap<String, ModelSphere>();

    public void init()
    {
        Map<String, Fluid> fluidMap = FluidRegistry.getRegisteredFluids();

        for (String name: fluidMap.keySet())
        {
            cache.put(name, new ModelSphere(fluidMap.get(name).getStill()));
        }

        LogHelper.info("Cached " + cache.size() + " models!");
        LogHelper.info("breakpoint");
    }

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
        if (cache.keySet().contains(name))
        {
            return cache.get(name);
        }
        return null;
    }
}
