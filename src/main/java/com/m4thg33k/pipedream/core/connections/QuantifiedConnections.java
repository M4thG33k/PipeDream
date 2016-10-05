package com.m4thg33k.pipedream.core.connections;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class QuantifiedConnections {

    private int[] connections = new int[6];
    private String nbtKey = "QuantifiedConnection";
    private String optKey = "QuantifiedConnectionOptions";
    private int numOptions = 1;

    public QuantifiedConnections(int numOptions)
    {
        this.numOptions = numOptions;
    }

    public QuantifiedConnections(int numOptions, int down, int up, int north, int south, int west, int east)
    {
        this.numOptions = numOptions;
        connections[0] = down;
        connections[1] = up;
        connections[2] = north;
        connections[3] = south;
        connections[4] = west;
        connections[5] = east;
    }

    private void verifyConnections()
    {
        for (int i=0;i<6;i++)
        {
            if (connections[i] < 0 || connections[i] >= numOptions)
            {
                connections[i] = 0;
            }
        }
    }

    public int getConnectionValue(EnumFacing side)
    {
        if (side == null)
        {
            return 0;
        }
        return connections[side.ordinal()];
    }

    public void incrementSideValue(EnumFacing side)
    {
        if (side==null)
        {
            return;
        }

        connections[side.ordinal()] = (connections[side.ordinal()]+1) % numOptions;
    }

    public void setSideValue(EnumFacing side, int value)
    {
        if (side==null)
        {
            return;
        }

        connections[side.ordinal()] =  value < 0 || value >= numOptions ? 0 : value;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey(nbtKey))
        {
            connections = compound.getIntArray(nbtKey);
            numOptions = compound.getInteger(optKey);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setIntArray(nbtKey, connections);
        compound.setInteger(optKey, numOptions);
        return compound;
    }

    public int[] getConnections()
    {
        return connections;
    }

    public void rotateAboutYAxis()
    {
        int[] temp = new int[6];

        for (EnumFacing side: EnumFacing.HORIZONTALS)
        {
            temp[side.ordinal()] = connections[side.rotateY().ordinal()];
        }

        for (EnumFacing side: EnumFacing.HORIZONTALS)
        {
            connections[side.ordinal()] = temp[side.ordinal()];
        }
    }


}
