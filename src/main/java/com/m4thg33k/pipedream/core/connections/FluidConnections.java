package com.m4thg33k.pipedream.core.connections;

import com.m4thg33k.pipedream.core.interfaces.ISideConnector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class FluidConnections implements ISideConnector {

    private boolean[] connections = new boolean[6];
    private String nbtKey = "FluidConnectionNBT";

    public FluidConnections()
    {
        // Default to all connections "on"
        this(63);
    }

    public FluidConnections(int initialConnections)
    {
        getConnectionsFromInteger(initialConnections);
    }

    private void getConnectionsFromInteger(int value)
    {
        for (int i=0; i<6; i++)
        {
            connections[i] = (1 == ((value >> i) % 2));
        }
    }

    @Override
    public boolean isSideConnected(EnumFacing side) {
        return connections[side.ordinal()];
    }

    @Override
    public void toggleSideConnection(EnumFacing side) {
        this.setConnection(side, !connections[side.ordinal()]);
    }

    @Override
    public void setConnection(EnumFacing side, boolean isConnected) {
        connections[side.ordinal()] = isConnected;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(nbtKey))
        {
            int value = compound.getInteger(nbtKey);

            getConnectionsFromInteger(value);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        int value = 0;
        for (int i=0; i< 6;i++)
        {
            if (connections[i])
            {
                value += (1 << i);
            }
        }
        compound.setInteger(nbtKey, value);

        return compound;
    }
}
