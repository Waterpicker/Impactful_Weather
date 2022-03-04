package shadowmaster435.impactfulweather.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;


@Environment(EnvType.CLIENT)
public class RainSplash extends AnimatedParticle {
    public static float upv;
    public float light;

  
    public RainSplash(ClientWorld world, double x, double y, double z, SpriteProvider sprites, float up) {
        super(world, x, y, z, sprites, up);
        this.velocityX = 0.0D;
        this.velocityY = 0;
        this.velocityZ = 0.0D;
        this.maxAge = 5;
        this.gravityStrength = 0f;
        this.alpha = 0.0f;
        this.scale = 0.0f;
        this.age = 0;
        this.scale = (float) (Math.random() / 2) + 0.2f;

        this.setSpriteForAge(this.spriteProvider);

        this.setBoundingBoxSpacing(0.02F, 0.1f);
        this.light = world.getBrightness(new BlockPos(this.x, this.y, this.z)) + 0.01f;
        this.setColor((15f / this.light),(15f / this.light), (15f / this.light));
    }
    public void tick() {
        this.light = world.getBrightness(new BlockPos(this.x, this.y, this.z)) + 0.01f;
        this.setColor((15f / this.light),(15f / this.light), (15f / this.light));
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.alpha = 1;
        this.scale = 0.2f;
        if (this.age > this.maxAge) {
            this.markDead();
        } else {
            ++this.age;
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class RainSplashFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public RainSplashFactory(FabricSpriteProvider sprites) {
            this.spriteProvider = sprites;

        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double Xv, double Yv, double Zv) {
            RainSplash rain = new RainSplash(world, x, y, z, spriteProvider, upv);
            rain.setSprite(this.spriteProvider);
            return rain;
        }
    }
}
