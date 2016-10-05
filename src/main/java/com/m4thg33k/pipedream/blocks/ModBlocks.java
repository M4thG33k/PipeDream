package com.m4thg33k.pipedream.blocks;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.blocks.itemblocks.ItemTank;
import com.m4thg33k.pipedream.lib.Names;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks{

    public static BlockTank blockTank = new BlockTank();
    public static ItemTank itemTank = new ItemTank(blockTank);

    public static void preInit()
    {
        GameRegistry.register(blockTank);
        GameRegistry.register(itemTank.setRegistryName(PipeDream.MODID, Names.TILE_TANK));
    }
}
