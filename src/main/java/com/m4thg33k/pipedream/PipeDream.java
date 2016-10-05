package com.m4thg33k.pipedream;

import com.m4thg33k.pipedream.blocks.ModBlocks;
import com.m4thg33k.pipedream.core.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = PipeDream.MODID, name = PipeDream.MODNAME, version = PipeDream.VERSION)
public class PipeDream {

    public static final String MODID = "pipedream";
    public static final String VERSION = "@VERSION@";
    public static final String MODNAME = "PipeDream";

    @Mod.Instance
    public static PipeDream INSTANCE = new PipeDream();

    @SidedProxy(clientSide = "com.m4thg33k.pipedream.core.proxy.ClientProxy", serverSide = "com.m4thg33k.pipedream.core.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preinit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postinit(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        //Initialize commands here
    }

    public static CreativeTabs tabPipeDream = new CreativeTabs("tabPipeDream") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.blockTank);
        }
    };
}
