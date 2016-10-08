package com.m4thg33k.pipedream.particles;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.core.util.LogHelper;
import com.m4thg33k.pipedream.network.packets.PacketTankFilling;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.collection.parallel.ParIterableLike;

public class ParticleManager {

    public static Vec3d getSphereLocation(double radius, BlockPos blockPos, double theta, double phi)
    {
        return new Vec3d(blockPos.getX() + radius * Math.sin(theta) * Math.sin(phi),
                        blockPos.getY() + 0.5 + radius * Math.cos(phi),
                        blockPos.getZ() + radius * Math.cos(theta) * Math.sin(phi));
    }

    public static double getTheta(EnumFacing side)
    {
        if (side == null || side == EnumFacing.DOWN || side == EnumFacing.UP)
        {
            return angleToRad(PipeDream.RAND.nextInt(360));
        }
        int theta = PipeDream.RAND.nextInt(181);
        if (side == EnumFacing.EAST)
        {
            return angleToRad(theta);
        }

        if (side == EnumFacing.WEST)
        {
            return angleToRad(theta + 180);
        }

        if (side == EnumFacing.NORTH)
        {
            return angleToRad(theta + 90);
        }

        return angleToRad(theta + 270);
    }

    public static double getPhi(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return angleToRad(PipeDream.RAND.nextInt(91)+90);
        }
        if (side == EnumFacing.UP)
        {
            return angleToRad(PipeDream.RAND.nextInt(91));
        }

        return angleToRad(PipeDream.RAND.nextInt(181));
    }

    public static Vec3d getRandomSphereLocation(double radius, EnumFacing side, BlockPos blockPos)
    {
        return getSphereLocation(radius, blockPos, getTheta(side), getPhi(side));
    }

    public static double angleToRad(int angle)
    {
        return angle * Math.PI / 180.0;
    }

    public static void doDrainParticles(Vec3d end, Vec3d delta, String fluidName)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFluidOrb(Minecraft.getMinecraft().theWorld, end.xCoord + 0.5, end.yCoord, end.zCoord + 0.5, -delta.xCoord, -delta.yCoord, -delta.zCoord, fluidName, 10));
    }

    public static void doFillParticles(Vec3d start, Vec3d delta, String fluidName)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFluidOrb(Minecraft.getMinecraft().theWorld, start.xCoord, start.yCoord, start.zCoord, delta.xCoord, delta.yCoord, delta.zCoord, fluidName, 10));
    }

    public static void spawnParticles(double radius, EnumFacing side, BlockPos blockPos, Vec3d start, boolean isFilling, String name, Vec3d extra)
    {
        Vec3d end = getRandomSphereLocation(radius, side, blockPos);
        Vec3d delta = (end.subtract(new Vec3d(blockPos).add(extra))).scale(0.1);

        if (isFilling)
        {
            doFillParticles(start, delta, name);
        }
        else
        {
            doDrainParticles(end, delta, name);
        }
    }

    public static void tankFillingParticles(PacketTankFilling packet)
    {
        World world = Minecraft.getMinecraft().theWorld;
        BlockPos blockPos = packet.getPos();
        TileEntity tile = world.getTileEntity(blockPos);
        Vec3d startPos;
        String name = packet.getFluidName();

        if (tile == null || !(tile instanceof TileTank))
        {
            return;
        }
        double radius = (((TileTank) tile).getRadius()+0.1)*0.5;
        if (packet.getSide() == EnumFacing.DOWN)
        {
            startPos = new Vec3d(blockPos).addVector(0.5, 0, 0.5);
            for (int n=0; n <= packet.getAmount(); n += 100) {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(0,0,0));
            }
        }
        else if (packet.getSide() == EnumFacing.UP)
        {
            startPos = new Vec3d(blockPos).addVector(0.5, 1, 0.5);
            for (int n=0; n<= packet.getAmount(); n += 100)
            {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(0,1,0));
            }
        }
        else if (packet.getSide() == EnumFacing.NORTH)
        {
            startPos = new Vec3d(blockPos).addVector(0.5, 0.5, 0);
            for (int n=0; n <= packet.getAmount(); n += 100)
            {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(0, 0.5, -0.5));
            }
        }
        else if (packet.getSide() == EnumFacing.SOUTH)
        {
            startPos = new Vec3d(blockPos).addVector(0.5, 0.5, 1);
            for (int n=0; n <= packet.getAmount(); n += 100)
            {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(0, 0.5, 0.5));
            }
        }
        else if (packet.getSide() == EnumFacing.WEST)
        {
            startPos = new Vec3d(blockPos).addVector(0, 0.5, 0.5);
            for (int n=0; n <= packet.getAmount(); n += 100)
            {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(-0.5, 0.5, 0));
            }
        }
        else if (packet.getSide() == EnumFacing.EAST)
        {
            startPos = new Vec3d(blockPos).addVector(1, 0.5, 0.5);
            for (int n=0; n <= packet.getAmount(); n += 100)
            {
                spawnParticles(radius, packet.getSide(), blockPos, startPos, packet.isFilling(), name, new Vec3d(0.5, 0.5, 0));
            }
        }
        else // null ==> bucket filling
        {
            double theta;
            double psi;
            Vec3d end;
            Vec3d delta;

            for (int n=0; n <= packet.getAmount(); n += 100) {
                theta = getTheta(null);
                psi = getPhi(null);
                startPos = getSphereLocation(0.5, blockPos, theta, psi);
                end = getSphereLocation(radius, blockPos, theta, psi);

                if (!packet.isFilling()) {
                    Vec3d temp = startPos;
                    startPos = end;
                    end = temp;
                }

                delta = (end.subtract(startPos)).scale(0.1);

                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFluidOrb(world, startPos.xCoord + 0.5, startPos.yCoord, startPos.zCoord + 0.5, delta.xCoord, delta.yCoord, delta.zCoord, name, 10));
            }
        }
    }
}
