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
            for (int n=0; n <= packet.getAmount(); n += 100) {
                Vec3d endLocation = getRandomSphereLocation(radius, packet.getSide(), blockPos);
                startPos = new Vec3d(blockPos).addVector(0.5, 0, 0.5);
                Vec3d delta = (endLocation.subtract(new Vec3d(blockPos))).scale(0.1);
                if (packet.isFilling()) {
                    doFillParticles(startPos, delta, name);
                }
                else
                {
                    doDrainParticles(endLocation, delta, name);
                }
            }
        }
        else if (packet.getSide() == EnumFacing.UP)
        {
            for (int n=0; n<= packet.getAmount(); n += 100)
            {
                Vec3d endLocation = getRandomSphereLocation(radius, packet.getSide(), blockPos);
                startPos = new Vec3d(blockPos).addVector(0.5, 1, 0.5);

                Vec3d delta = (endLocation.subtract(new Vec3d(blockPos).addVector(0, 1, 0))).scale(0.1);
                if (packet.isFilling())
                {
                    doFillParticles(startPos, delta, name);
                }
                else
                {
                    doDrainParticles(endLocation, delta, name);
                }
            }
        }
    }
}
