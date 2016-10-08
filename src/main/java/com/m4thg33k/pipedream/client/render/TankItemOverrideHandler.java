package com.m4thg33k.pipedream.client.render;

import com.google.common.collect.ImmutableList;
import com.m4thg33k.pipedream.client.render.models.TankItemModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TankItemOverrideHandler extends ItemOverrideList {

    public static final TankItemOverrideHandler INSTANCE = new TankItemOverrideHandler();
    public static IBakedModel baseModel;

    public TankItemOverrideHandler()
    {
        super(ImmutableList.<ItemOverride> of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        if (stack == null)
        {
            return baseModel;
        }
        return new TankItemModel(baseModel).handleItemState(stack);
    }
}
