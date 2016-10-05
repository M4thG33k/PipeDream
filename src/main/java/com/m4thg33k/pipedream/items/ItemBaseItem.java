package com.m4thg33k.pipedream.items;

import com.m4thg33k.pipedream.PipeDream;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBaseItem extends Item {

    public ItemBaseItem(String name)
    {
        super();

        this.setCreativeTab(PipeDream.tabPipeDream);
        this.setUnlocalizedName(name);

        this.setRegistryName(PipeDream.MODID, name);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "_" + stack.getItemDamage();
    }
}
