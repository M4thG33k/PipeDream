package com.m4thg33k.pipedream.client.render.tile;

import com.m4thg33k.pipedream.client.render.models.ModelTankValve;
import com.m4thg33k.pipedream.core.util.LogHelper;
import com.m4thg33k.pipedream.tiles.TileTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;

public class RenderTileTank extends RenderTileSymmetricBase<TileTank>{

    ModelTankValve tankValve;

    @Override
    public void renderTileEntityAt(TileTank te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te != null && !te.getWorld().isBlockLoaded(te.getPos(), false))
        {
            return;
        }

        if (tankValve == null)
        {
            tankValve = new ModelTankValve();
        }

        int connections = (te == null) ? 3 : te.getFluidConnectionsAsInteger();

        //double worldTime = (te.getWorld() == null) ? 0 : (double)(ClientTickHandler.ticksInGame + partialTicks) + new Random(te.getPos().hashCode()).nextInt(360);

        // get matrix set to the correct location
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();

        GlStateManager.color(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        renderValves(connections);

        GlStateManager.popMatrix();

    }

    private void renderValves(int connections)
    {
        for (int i=0; i<6; i++)
        {
            if (((connections>>i) & 1) == 1)
            {
                GlStateManager.pushMatrix();
                rotate(facings[i]);
                tankValve.renderModel();
                GlStateManager.popMatrix();
            }
        }
    }
}
