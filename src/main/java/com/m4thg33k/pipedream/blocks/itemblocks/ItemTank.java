package com.m4thg33k.pipedream.blocks.itemblocks;

import com.m4thg33k.pipedream.core.interfaces.IDismantleableTile;
import com.m4thg33k.pipedream.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class ItemTank extends ItemBlock{

    protected final int capacity = 8000;

    public ItemTank(Block block)
    {
        super(block);

        this.setUnlocalizedName(Names.TILE_TANK);
    }



    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStack(stack, capacity);
    }



    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean supers = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        if (!supers)
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof IDismantleableTile && stack.hasTagCompound())
        {
            NBTTagCompound compound = stack.getTagCompound();
            compound.setInteger("x", pos.getX());
            compound.setInteger("y", pos.getY());
            compound.setInteger("z", pos.getZ());
            ((IDismantleableTile) tile).readItemNBT(player.getHorizontalFacing(), stack.getTagCompound());
        }

        return true;
    }
}
