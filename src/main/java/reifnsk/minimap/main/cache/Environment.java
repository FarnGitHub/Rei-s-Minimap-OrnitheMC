package reifnsk.minimap.main.cache;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import reifnsk.minimap.main.ReiMinimap;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Environment {
    private static final ConcurrentLinkedQueue<Environment> queue = new ConcurrentLinkedQueue<>();
    private static final Environment[] envCache = new Environment[262144];
    private static BiomeSource biomeSource;
    private int x;
    private int z;
    private int grassColor = 16767248;
    private int foliageColor = 5169201;
    private float temperature = 0.5F;
    private float humidity = 1.0F;
    private boolean valid;
    private int biomeColor = 16767248;

    static {
        for(int index = 0; index < envCache.length; ++index) {
            envCache[index] = new Environment();
        }
    }

    private boolean isLocation(int x, int z) {
        return this.x == x && this.z == z;
    }

    private void setPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    private void set(int x, int z, float temp, float humid, Biome biome, int grassColor, int foliageColors) {
        this.x = x;
        this.z = z;
        this.grassColor = grassColor;
        this.foliageColor = foliageColors;
        this.temperature = temp;
        this.humidity = humid;
        this.biomeColor = biome.grassColor;
    }

    public int getGrassColor() {
        return this.grassColor;
    }

    public int getFoliageColor() {
        return this.foliageColor;
    }

    public int getBiomeColor() {
        return biomeColor;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    public static void calcEnvironment() {
        if(Thread.currentThread() == ReiMinimap.instance.mcThread && !queue.isEmpty()) {
            Environment env;
            while((env = queue.poll()) != null) {
                calcEnvironment(env);
            }
        }
    }

    public static Environment getEnvironment(int x, int z, Thread thread) {
        Environment env = envCache[(x & 511) << 9 | z & 511];
        if(!env.isLocation(x, z)) {
            env.valid = false;
            if(thread == ReiMinimap.instance.mcThread) {
                calcEnvironment(x, z, env);
            } else {
                env.setPos(x, z);
                queue.offer(env);
            }
        }

        return env;
    }

    public static Environment getEnvironment(Chunk chunk, int x, int z, Thread thread) {
        return getEnvironment(chunk.x * 16 + x, chunk.z * 16 + z, thread);
    }

    private static void calcEnvironment(Environment env) {
        calcEnvironment(env.x, env.z, env);
    }

    private static void calcEnvironment(int x, int z, Environment environment) {
        if(!environment.valid) {
            Biome biome = biomeSource.getBiomesInArea(x, z, 1, 1)[0];
            float temperature = (float) biomeSource.temperatureMap[0];
            float humidity = (float) biomeSource.downfallMap[0];
            if(biome == null)
                biome = Biome.getBiome(temperature, humidity);

            int grassColor = BiomeColorsImpl.GRASS_INTERPOLATOR.getColor(biomeSource, x, z);
            int foliageColor = BiomeColorsImpl.LEAVES_INTERPOLATOR.getColor(biomeSource, x, z);
            environment.set(x, z, temperature, humidity, biome, grassColor, foliageColor);
            environment.valid = true;
        }
    }

    public static void setWorld(World world) {
        biomeSource = world.method_1781();
        queue.clear();

        for (Environment environment : envCache) {
            environment.x = Integer.MIN_VALUE;
            environment.z = Integer.MIN_VALUE;
        }

    }
}
