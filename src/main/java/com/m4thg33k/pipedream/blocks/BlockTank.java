package com.m4thg33k.pipedream.blocks;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.core.util.LogHelper;
import com.m4thg33k.pipedream.items.ItemWrench;
import com.m4thg33k.pipedream.items.ModItems;
import com.m4thg33k.pipedream.lib.Names;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTank extends BaseBlockTESR{

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
        if (worldIn.isRemote)
        {
            return true;
        }
        LogHelper.info(hitX + ", " + hitY + ", " + hitZ);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null && tile instanceof TileTank)// && heldItem != null && heldItem.getItem() instanceof ItemWrench)
        {
            if (heldItem != null) {
                if (heldItem.getItem() == ModItems.itemWrench) {
                    LogHelper.info("wrench");
                    switch (heldItem.getItemDamage()) {
                        case 1:
                            ((TileTank) tile).toggleFluidConnection(side.getOpposite());
                            break;
                        default:
                            ((TileTank) tile).toggleFluidConnection(side);
                    }
                }
                else
                {
                    if (heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,EnumFacing.UP))
                    {
                        IFluidHandler container = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                        IFluidHandler tank = ((TileTank) tile).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

                        int amount = tank.fill(container.drain(1000,false),false);
                        if (amount == 1000)
                        {
                            tank.fill(container.drain(1000,!playerIn.isCreative()),true);
                            tile.markDirty();
                        }
                        else
                        {
                            amount = container.fill(tank.drain(1000,false),false);
                            if (amount == 1000)
                            {
                                container.fill(tank.drain(1000,true), !playerIn.isCreative() || (playerIn.isCreative() && !FluidContainerRegistry.isBucket(heldItem)));
                                tile.markDirty();
                            }
                        }
                        LogHelper.info("Has fluid handler capability");
                    }
                    else
                    {
                        if (FluidContainerRegistry.isBucket(heldItem))
                        {
                            IFluidHandler tank = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                            FluidStack inBucket = FluidContainerRegistry.getFluidForFilledItem(heldItem);
                            LogHelper.info(inBucket);
                            if (inBucket != null)
                            {
                                int amount = tank.fill(inBucket, false);
                                if (amount == inBucket.amount)
                                {
                                    if (!playerIn.isCreative())
                                    {
                                        playerIn.setHeldItem(EnumHand.MAIN_HAND,new ItemStack(Items.BUCKET,1));
                                    }
                                    tank.fill(inBucket, true);
                                    tile.markDirty();
                                }
                            }
                            LogHelper.info("Bucket");
                        }
                    }
                }
            }
            else
            {
                if (playerIn.isSneaking()) {
                    ((TileTank) tile).incrementFluidConnectionType(side);
                }
                LogHelper.info("Tank has " + ((TileTank)tile).getAmount() + '\t' + ((TileTank)tile).getPercentage());
            }


        }

        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileEntity tile = source.getTileEntity(pos);
        double factor = tile == null || !(tile instanceof TileTank) ? 0.1 : 0.05+0.25*((TileTank) tile).getPercentage();
        double small = Math.min(0.5 - factor, 0.4);
        double big = Math.max(0.5 + factor, 0.6);
        AxisAlignedBB toReturn = new AxisAlignedBB(small, small, small, big, big, big);
//        AxisAlignedBB toReturn = new AxisAlignedBB(0.35,0.35,0.35,0.65,0.65,0.65);

//        int connections = ((TileTank)source.getTileEntity(pos)).getFluidConnectionsAsInteger();
//        if (connections%2 == 1)
//        {
//            toReturn = toReturn.union(new AxisAlignedBB(0.35,0.0,0.35,0.35,0.35,0.35));
//        }
//        if ((connections&2) == 2)
//        {
//            toReturn = toReturn.union(new AxisAlignedBB(0.65,0.65,0.65,0.65,1.0,0.65));
//        }
//        if ((connections&4)==4)
//        {
//            toReturn = toReturn.union(new AxisAlignedBB(0.35,0.35,0,0.35,0.65,0.65));
//        }

        return toReturn;
    }

//    @Override
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
//        collidingBoxes.add(new AxisAlignedBB(0.35,0.35,0.35,0.65,0.65,0.65));
//    }
}
