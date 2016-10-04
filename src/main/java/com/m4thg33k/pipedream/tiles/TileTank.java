package com.m4thg33k.pipedream.tiles;

import com.m4thg33k.pipedream.core.connections.FluidConnections;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTank extends TileEntity{

    protected FluidTank tank = new FluidTank(8*Fluid.BUCKET_VOLUME);
    protected FluidConnections fluidConnections = new FluidConnections();

    public TileTank()
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound);
        fluidConnections.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        tank.writeToNBT(compound);
        fluidConnections.writeToNBT(compound);
        return compound;
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();

        return this.writeToNBT(tagCompound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = getUpdateTag();

        return new SPacketUpdateTileEntity(pos, 0, tagCompound);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (facing == null || fluidConnections.isSideConnected(facing))) || super.hasCapability(capability, facing);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull  Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (facing == null || fluidConnections.isSideConnected(facing)))
        {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }

    public int getFluidConnectionsAsInteger()
    {
        return fluidConnections.getConnectionsAsInteger();
    }

    public void toggleFluidConnection(EnumFacing facing)
    {
        fluidConnections.toggleSideConnection(facing);
        this.markDirty();
        this.worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos),0);
    }
}
