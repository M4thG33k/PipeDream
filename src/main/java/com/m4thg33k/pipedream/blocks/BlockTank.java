package com.m4thg33k.pipedream.blocks;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.lib.Names;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTank extends BaseBlockTESR {

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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null && tile instanceof TileTank)
        {
            ((TileTank) tile).toggleFluidConnection(side);
        }

        return true;
    }
}
