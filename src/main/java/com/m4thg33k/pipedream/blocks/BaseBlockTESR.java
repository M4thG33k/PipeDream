package com.m4thg33k.pipedream.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class BaseBlockTESR extends BaseBlock {

    public BaseBlockTESR(String name)
    {
        super(name);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
