package com.m4thg33k.pipedream.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class ParticleFluidOrb extends Particle{

    private TextureAtlasSprite icon;
    private float SCALE_FACTOR = 1.0f;
    private TextureManager manager = Minecraft.getMinecraft().getTextureManager();
    private ResourceLocation blocks = TextureMap.LOCATION_BLOCKS_TEXTURE;

    protected ParticleFluidOrb(World world, double x, double y, double z)
    {
        super(world, x,y,z);
        this.particleRed = this.particleGreen = 0.0f;
        this.particleBlue = 1.0f;
        this.particleTextureJitterX = this.particleTextureJitterY = 0.0f;
    }

    public ParticleFluidOrb(World world, double x, double y, double z, double velX, double velY, double velZ, String fluidName, int life)
    {
        this(world, x, y, z);
//        LogHelper.info(x+" "+y+" "+z);
        this.motionX = velX;
        this.motionY = velY;
        this.motionZ = velZ;
        this.icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(FluidRegistry.getFluid(fluidName).getStill().toString());
        this.particleScale = SCALE_FACTOR;
        this.particleMaxAge = life;
//        this.particleTexture = icon;
    }

    @Override
    public void onUpdate() {
//        LogHelper.info(this.posX + " " + this.posY + " " + this.posZ);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public void moveEntity(double x, double y, double z) {
//        LogHelper.info("moving particle!");
        this.posX += x;
        this.posY += y;
        this.posZ += z;
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        double umin = icon.getMinU();
        double umax = icon.getMaxU();
        double vmin = icon.getMinV();
        double vmax = icon.getMaxV();

        if (this.particleTexture != null)
        {
            umin = this.particleTexture.getMinU();
            umax = this.particleTexture.getMaxU();
            vmin = this.particleTexture.getMinV();
            vmax = this.particleTexture.getMaxV();
        }

        float scale = 3.0f * this.particleScale;
        float xAdj = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float yAdj = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float zAdj = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

//        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRendererIn.pos((double)(xAdj - rotationX*scale - rotationXY*scale), (double)(yAdj - rotationZ * scale), (double)(zAdj - rotationYZ * scale - rotationXZ * scale))
                .tex(umax, vmax)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1).endVertex();
        worldRendererIn.pos((double)(xAdj - rotationX*scale + rotationXY*scale), (double)(yAdj + rotationZ * scale), (double)(zAdj - rotationYZ * scale + rotationXZ * scale))
                .tex(umax, vmin)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1).endVertex();
        worldRendererIn.pos((double)(xAdj + rotationX*scale + rotationXY*scale), (double)(yAdj + rotationZ * scale), (double)(zAdj + rotationYZ * scale + rotationXZ * scale))
                .tex(umin, vmin)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1).endVertex();
        worldRendererIn.pos((double)(xAdj + rotationX*scale - rotationXY*scale), (double)(yAdj - rotationZ * scale), (double)(zAdj + rotationYZ * scale - rotationXZ * scale))
                .tex(umin, vmax)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1).endVertex();
        worldRendererIn.finishDrawing();

//        GlStateManager.popMatrix();
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}
