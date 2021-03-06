package com.m4thg33k.pipedream.core.interfaces;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface ISideConnector {

    boolean isSideConnected(EnumFacing side);

    void toggleSideConnection(EnumFacing side);

    void setConnection(EnumFacing side, boolean isConnected);

    void readFromNBT(NBTTagCompound compound);

    NBTTagCompound writeToNBT(NBTTagCompound compound);

    void rotateAboutYAxis();


}
