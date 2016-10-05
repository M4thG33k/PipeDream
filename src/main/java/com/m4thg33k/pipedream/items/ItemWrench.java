package com.m4thg33k.pipedream.items;

import com.m4thg33k.pipedream.core.interfaces.IDismantleable;
import com.m4thg33k.pipedream.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends ItemBaseItem{

    public ItemWrench()
    {
        super(Names.ITEM_WRENCH);

        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();

        if (player.isSneaking() && block instanceof IDismantleable)
        {
            if (((IDismantleable)block).isDismantleable(player, world, pos))
            {
                ((IDismantleable)block).dismantle(player, world, pos);
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
        }

        if (playerIn.isSneaking())
        {
            itemStackIn.setItemDamage((itemStackIn.getItemDamage()+1)%2);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
        }

        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }
}
