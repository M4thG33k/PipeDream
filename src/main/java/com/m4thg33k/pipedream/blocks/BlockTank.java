package com.m4thg33k.pipedream.blocks;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.lib.Names;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTank extends BaseBlock {

    public BlockTank()
    {
        super(Names.TILE_TANK);
        this.setDefaultState(blockState.getBaseState());

        this.setRegistryName(PipeDream.MODID, Names.TILE_TANK);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTank();
    }
}
