package com.m4thg33k.pipedream.blocks;

import com.m4thg33k.pipedream.core.interfaces.IDismantleable;
import com.m4thg33k.pipedream.core.interfaces.IDismantleableTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseBlockTESR extends BaseBlock implements IDismantleable{

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

    @Override
    public boolean isDismantleable(EntityPlayer player, World world, BlockPos pos) {
        return true;
    }



    @Override
    public void dismantle(EntityPlayer player, World world, BlockPos pos) {
        if (world.isRemote)
        {
            return;
        }
        TileEntity tile = world.getTileEntity(pos);
        ItemStack asItem = new ItemStack(world.getBlockState(pos).getBlock(), 1);
        if (tile != null && tile instanceof IDismantleableTile)
        {
            NBTTagCompound tagCompound = ((IDismantleableTile) tile).getItemNBT(player.getHorizontalFacing());

            asItem.setTagCompound(tagCompound);
        }

        EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, asItem);
        world.spawnEntityInWorld(entityItem);

        world.removeTileEntity(pos);
        world.setBlockToAir(pos);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.isRemote)
        {
            return;
        }
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile != null && tile instanceof IDismantleableTile && stack.hasTagCompound())
        {
            ((IDismantleableTile) tile).readItemNBT(placer.getHorizontalFacing(), stack.getTagCompound());
//            tile.markDirty();
//            worldIn.notifyBlockUpdate(pos, state, state, 0);
        }
    }
}
