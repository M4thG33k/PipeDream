package com.m4thg33k.pipedream.core.proxy;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.client.events.ClientEvents;
import com.m4thg33k.pipedream.client.handler.ClientTickHandler;
import com.m4thg33k.pipedream.client.render.ModTESRs;
import com.m4thg33k.pipedream.client.render.TankRenderHelper;
import com.m4thg33k.pipedream.client.render.registers.ItemRenderRegisters;
import com.m4thg33k.pipedream.network.packets.BaseRenderingPacket;
import com.m4thg33k.pipedream.network.packets.PacketTankFilling;
import com.m4thg33k.pipedream.particles.ParticleManager;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preinit(FMLPreInitializationEvent event) {
        super.preinit(event);

        OBJLoader.INSTANCE.addDomain(PipeDream.MODID);

        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new TankRenderHelper());
        ModTESRs.preinit();
        ItemRenderRegisters.registerItemRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }


    @Override
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }

    @Override
    public void handleRenderingPacket(BaseRenderingPacket packet) {
        if (packet instanceof PacketTankFilling)
        {
            ParticleManager.tankFillingParticles((PacketTankFilling)packet);
        }
    }
}
