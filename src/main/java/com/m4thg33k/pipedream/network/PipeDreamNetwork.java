package com.m4thg33k.pipedream.network;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.network.packets.BasePacket;
import com.m4thg33k.pipedream.network.packets.BasePacketHandler;
import com.m4thg33k.pipedream.network.packets.PacketTankFilling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PipeDreamNetwork {

    public static PipeDreamNetwork INSTANCE = new PipeDreamNetwork();

    public final SimpleNetworkWrapper network;
    public final BasePacketHandler handler;
    private int id = 0;

    public PipeDreamNetwork()
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(PipeDream.MODID);
        handler = new BasePacketHandler();
    }

    public void registerPacket(Class<? extends BasePacket> packetClass)
    {
        registerPacketClient(packetClass);
        registerPacketServer(packetClass);
    }

    public static void registerPacketClient(Class<? extends BasePacket> packetClass)
    {
        registerPacketImp(packetClass, Side.CLIENT);
    }

    public static void registerPacketServer(Class<? extends BasePacket> packetClass)
    {
        registerPacketImp(packetClass, Side.SERVER);
    }

    public static void registerPacketImp(Class<? extends BasePacket> packetClass, Side side)
    {
        INSTANCE.network.registerMessage(INSTANCE.handler, packetClass, INSTANCE.id++, side);
    }

    public static void setup()
    {
        //register packets here
        registerPacketClient(PacketTankFilling.class);
    }

    public static void sendToAll(BasePacket packet)
    {
        INSTANCE.network.sendToAll(packet);
    }

    public static void sendTo(BasePacket packet, EntityPlayerMP player)
    {
        INSTANCE.network.sendTo(packet, player);
    }

    public static void sendToAllAround(BasePacket packet, NetworkRegistry.TargetPoint point)
    {
        INSTANCE.network.sendToAllAround(packet, point);
    }

    public static void sendToDimension(BasePacket packet, int dimensionID)
    {
        INSTANCE.network.sendToDimension(packet, dimensionID);
    }

    public static void sendToServer(BasePacket packet)
    {
        INSTANCE.network.sendToServer(packet);
    }

    public static void sendToClients(WorldServer world, BlockPos pos, BasePacket packet)
    {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        for (EntityPlayer player : world.playerEntities)
        {
            if (!(player instanceof EntityPlayerMP))
            {
                continue;
            }

            EntityPlayerMP playerMP = (EntityPlayerMP) player;

            if (world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition))
            {
                PipeDreamNetwork.sendTo(packet, playerMP);
            }
        }
    }
}
