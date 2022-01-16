package shadowmaster435.impactfulweather.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shadowmaster435.impactfulweather.init.IWParticles;

import java.util.Random;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private int ticks;

    @Shadow
    private int field_20793;


    @Shadow @Final private MinecraftClient client;

    /**
     * @author shadowmaster435
     */
    @Overwrite
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g) {
        MinecraftClient instance = MinecraftClient.getInstance();
        PlayerEntity player = instance.player;
        World world = instance.world;
        assert world != null;
        assert player != null;
        BlockPos pos = player.getBlockPos();
        Random random = instance.world.random;
        int i = pos.getX();
        int k = pos.getZ();
        double x = (double)i + random.nextDouble();
        double z = (double)k + random.nextDouble();
        if (world.isRaining() && !client.isPaused()) {
            for (int l = 0; l < 8; ++l) {
                if (world.getBiome(new BlockPos(x + 32, MathHelper.lerp(world.random.nextDouble(), 63, 100), z + 32)).getCategory() == Biome.Category.DESERT) {
                    world.addParticle(IWParticles.SANDMOTE, MathHelper.lerp(world.random.nextDouble(), x - 64, x + 32), MathHelper.lerp(world.random.nextDouble(), 63, 100), z - 24, 0f, 0f, 0f);
                    if (Math.random() < 0.0075) {
                        world.addParticle(IWParticles.TUMBLEBUSH, MathHelper.lerp(world.random.nextDouble(), x - 64, x + 32), MathHelper.lerp(world.random.nextDouble(), 63, 100), z - 24, 0f, 0f, 0f);
                    }
                }
                if (
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.PLAINS ||
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.FOREST ||
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.BEACH ||
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.OCEAN ||
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.SWAMP ||
                        world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getCategory() == Biome.Category.RIVER
                   ) {
                    world.addParticle(IWParticles.RAIN, MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY() + 100, MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32), 0f, 0f, 0f);
                }
                if (world.getBiome(new BlockPos(MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY(), MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32))).getPrecipitation() == Biome.Precipitation.SNOW) {
                    if (Math.random() < 0.4) {
                        world.addParticle(IWParticles.SNOW, MathHelper.lerp(world.random.nextDouble(), x - 32, x + 32), player.getBlockPos().getY() + 100, MathHelper.lerp(world.random.nextDouble(), z - 32, z + 32), 0f, 0f, 0f);
                    }
                }
            }
        }
    }


    /**
     * @author shadowmaster435
     */
    @Overwrite
    public void tickRainSplashing(Camera camera) {
        MinecraftClient instance = MinecraftClient.getInstance();

        assert instance.world != null;
        float f = instance.world.getRainGradient(1.0f) / (MinecraftClient.isFancyGraphicsOrBetter() ? 1.0f : 2.0f);
        if (f <= 0.0f) {
            return;
        }
        Random random = new Random((long)this.ticks * 312987231L);
        ClientWorld worldView = instance.world;
        BlockPos blockPos = new BlockPos(camera.getPos());
        BlockPos blockPos2 = null;
        int i = (int)(100.0f * f * f) / (instance.options.particles == ParticlesMode.DECREASED ? 2 : 1);
        for (int j = 0; j < i; ++j) {
            int k = random.nextInt(21) - 10;
            int l = random.nextInt(21) - 10;
            BlockPos blockPos3 = worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l));
            Biome biome = worldView.getBiome(blockPos3);
            if (blockPos3.getY() <= worldView.getBottomY() || blockPos3.getY() > blockPos.getY() + 10 || blockPos3.getY() < blockPos.getY() - 10 || biome.getPrecipitation() != Biome.Precipitation.RAIN || !biome.doesNotSnow(blockPos3)) continue;
            blockPos2 = blockPos3.down();
            if (instance.options.particles == ParticlesMode.MINIMAL) break;
        }
        if (blockPos2 != null && random.nextInt(3) < this.field_20793++) {
            this.field_20793 = 0;
            if (instance.world.getBiome(blockPos).getCategory() == Biome.Category.PLAINS) {

                if (blockPos2.getY() > blockPos.getY() + 1 && worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > MathHelper.floor(blockPos.getY())) {
                instance.world.playSound(blockPos2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
            } else {
                instance.world.playSound(blockPos2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
            }
        }
        }
    }
}
