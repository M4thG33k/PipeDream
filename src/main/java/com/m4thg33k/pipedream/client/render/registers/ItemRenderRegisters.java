package com.m4thg33k.pipedream.client.render.registers;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.blocks.ModBlocks;
import com.m4thg33k.pipedream.client.render.TankRenderHelper;
import com.m4thg33k.pipedream.items.ModItems;
import com.m4thg33k.pipedream.lib.Names;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemRenderRegisters {


    public static void registerItemRenderers() {

        for(int i = 0; i<2; i++)
        {
            ModelLoader.setCustomModelResourceLocation(ModItems.itemWrench, i, new ModelResourceLocation(PipeDream.MODID+":"+ Names.ITEM_WRENCH, "meta="+i));
        }


        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.blockTank), 0, TankRenderHelper.SMART_MODEL);
    }
}
