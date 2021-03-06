package com.m4thg33k.pipedream.tiles;

import com.m4thg33k.pipedream.core.connections.FluidConnections;
import com.m4thg33k.pipedream.core.connections.QuantifiedConnections;
import com.m4thg33k.pipedream.core.interfaces.IDismantleableTile;
import com.m4thg33k.pipedream.core.util.LogHelper;
import com.m4thg33k.pipedream.network.PipeDreamNetwork;
import com.m4thg33k.pipedream.network.packets.PacketTankFilling;
import com.m4thg33k.pipedream.particles.ParticleFluidOrb;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class TileTank extends TileEntity implements ITickable, IDismantleableTile, IFluidHandler{

    public static final int CAPACITY = 8 * Fluid.BUCKET_VOLUME;
    protected FluidTank tank = new FluidTank(CAPACITY);
    protected FluidConnections fluidConnections = new FluidConnections();
    protected QuantifiedConnections fluidConnectionTypes = new QuantifiedConnections(3);
    protected int STANDARD = 0;
    protected int PUSH = 1;
    protected int PULL = 2;

    protected int MAX_FLOW = 100;

    protected HashMap<EnumFacing, SideHandler> sideHandlers = new HashMap<EnumFacing, SideHandler>();

    protected final static String fluidNBT = "tankFluidNBT";


    public TileTank()
    {
        for (EnumFacing facing: EnumFacing.VALUES)
        {
            sideHandlers.put(facing, new SideHandler(facing));
        }

        sideHandlers.put(null, new SideHandler(null));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound);
        fluidConnections.readFromNBT(compound);
        if (compound.hasKey(fluidNBT))
        {
            fluidConnectionTypes.readFromNBT(compound.getCompoundTag(fluidNBT));
        }
    }

    @Override
    public NBTTagCompound getItemNBT(EnumFacing playerFacing)
    {

        if (playerFacing == null)
        {
            playerFacing = EnumFacing.NORTH;
        }
        NBTTagCompound compound = this.writeToNBT(new NBTTagCompound());
        compound.removeTag("x");
        compound.removeTag("y");
        compound.removeTag("z");

        compound.setInteger("PlayerFacing", playerFacing.ordinal());

        return compound;
    }

    @Override
    public void readItemNBT(EnumFacing playerFacing, NBTTagCompound compound)
    {
        if (compound == null)
        {
            return;
        }
        this.readFromNBT(compound);
        EnumFacing oldFacing = EnumFacing.values()[compound.getInteger("PlayerFacing")];
        while (oldFacing != playerFacing)
        {
            oldFacing = this.rotateConnections(oldFacing);
        }

        this.markDirty();
        this.worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 1);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound = tank.writeToNBT(compound);
        compound = fluidConnections.writeToNBT(compound);
        compound.setTag(fluidNBT, fluidConnectionTypes.writeToNBT(new NBTTagCompound()));
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
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//        LogHelper.info("Packet Received");
        this.readFromNBT(pkt.getNbtCompound());
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
//            worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
            return (T) sideHandlers.get(facing);
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

    public int getAmount()
    {
        return tank.getFluidAmount();
    }

    public double getPercentage()
    {
        return tank.getFluidAmount()/((double)tank.getCapacity());
    }

    public Fluid getFluid()
    {
        if (tank.getFluidAmount() != 0) {
            return tank.getFluid().getFluid();
        }
        return null;
    }

    public int[] getFluidConnectionTypes()
    {
        return fluidConnectionTypes.getConnections();
    }

    public void incrementFluidConnectionType(EnumFacing side)
    {
        fluidConnectionTypes.incrementSideValue(side);
        this.markDirty();
        this.worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 1);
    }

    @Override
    public void update() {
        if (worldObj.isRemote)
        {
            return;
        }
        attemptFluidPush();

        attemptFluidPull();
    }

    protected void moveFromTo(boolean movingOut)
    {
        boolean isDirty = false;
        for (EnumFacing side: EnumFacing.values())
        {
            if (fluidConnectionTypes.getConnectionValue(side) == (movingOut ? PUSH : PULL) && fluidConnections.isSideConnected(side)) {
                TileEntity tile = worldObj.getTileEntity(pos.offset(side));
                if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())) {
                    IFluidHandler from = movingOut ? this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
                    IFluidHandler to = movingOut ? tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()) : this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);

                    int amount = to.fill(from.drain(MAX_FLOW, false), false);
                    if (amount > 0) {
                        to.fill(from.drain(amount, true), true);
                        isDirty = true;
                    }
                }
            }
        }

        if (isDirty)
        {
            performUpdate();
        }
    }

    protected void attemptFluidPull()
    {
        this.moveFromTo(false);
    }

    protected void attemptFluidPush()
    {
        this.moveFromTo(true);
    }

    protected EnumFacing rotateConnections(EnumFacing old)
    {
        fluidConnections.rotateAboutYAxis();
        fluidConnectionTypes.rotateAboutYAxis();
        return old.rotateYCCW();
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int amount = tank.fill(resource, doFill);
        if (doFill && amount>0)
        {
            performUpdate();
        }
        return amount;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        FluidStack toReturn = tank.drain(resource, doDrain);
        if (doDrain && toReturn != null && toReturn.amount > 0)
        {
            performUpdate();
        }
        return toReturn;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack toReturn = tank.drain(maxDrain, doDrain);
        if (doDrain && toReturn != null && toReturn.amount > 0)
        {
            performUpdate();
        }
        return toReturn;
    }

    private void performUpdate()
    {
        this.markDirty();
        this.worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
    }

    public double getRadius()
    {
        return 0.65 * this.getPercentage();
    }

    public int getLightValue()
    {
        if (tank.getFluid() == null || tank.getFluid().amount == 0)
        {
            return 0;
        }
        return tank.getFluid().getFluid().getLuminosity();
//        double percentage = this.getPercentage();
//        if (percentage == 0)
//        {
//            return 0;
//        }
//        LogHelper.info(tank.getFluid().getFluid().getLuminosity() + " " + percentage);
//        return (int)Math.ceil(tank.getFluid().getFluid().getLuminosity() * percentage);
    }

    private class SideHandler implements IFluidHandler{
        private EnumFacing side;

        public SideHandler(EnumFacing side)
        {
            this.side = side;
        }
        @Override
        public IFluidTankProperties[] getTankProperties() {
            return tank.getTankProperties();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (side != null && fluidConnectionTypes.getConnectionValue(side) == PULL || fluidConnectionTypes.getConnectionValue(side) == STANDARD) {
                int amount = tank.fill(resource, doFill);
                if (doFill && amount > 0) {
                    PipeDreamNetwork.sendToAllAround(new PacketTankFilling(pos, side, true, tank.getFluid().getFluid().getName(), amount), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                    performUpdate();
                }
                return amount;
            }
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (side != null && fluidConnectionTypes.getConnectionValue(side) == PUSH || fluidConnectionTypes.getConnectionValue(side) == STANDARD) {
                FluidStack moved = tank.drain(resource, doDrain);
                if (doDrain && moved != null && moved.amount > 0) {
                    PipeDreamNetwork.sendToAllAround(new PacketTankFilling(pos, side, false, moved.getFluid().getName(), moved.amount), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                    performUpdate();
                }
                return moved;
            }
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (side != null && fluidConnectionTypes.getConnectionValue(side) == PUSH || fluidConnectionTypes.getConnectionValue(side) == STANDARD) {
                FluidStack moved = tank.drain(maxDrain, doDrain);
                if (doDrain && moved != null && moved.amount > 0) {
                    PipeDreamNetwork.sendToAllAround(new PacketTankFilling(pos, side, false, moved.getFluid().getName(), moved.amount), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                    performUpdate();
                }
                return moved;
            }
            return null;
        }

    }
}
