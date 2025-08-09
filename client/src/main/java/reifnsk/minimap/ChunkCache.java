package reifnsk.minimap;

import java.util.Arrays;

import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.World;

public class ChunkCache {
	private static final int threshold = 128;
	private final int shift;
	private final int size;
	private final int mask;
	private WorldChunk[] cache;
	private int[] count;
	private boolean[] slime;

	public ChunkCache(int scale) {
		this.shift = scale;
		this.size = 1 << this.shift;
		this.mask = this.size - 1;
		this.cache = new WorldChunk[this.size * this.size];
		this.count = new int[this.size * this.size];
		this.slime = new boolean[this.size * this.size];
	}

	public WorldChunk get(World world, int x, int z) {
		int ptr = x & this.mask | (z & this.mask) << this.shift;
		WorldChunk chunk = this.cache[ptr];
		if(chunk == null || chunk.world != world || !chunk.isAt(x, z) || ++this.count[ptr] > 128) {
			this.cache[ptr] = chunk = world.getChunkAt(x, z);
			this.count[ptr] = 0;
			this.slime[ptr] = chunk.getRandomForSlime(987234911L).nextInt(10) == 0;
		}

		return chunk;
	}

	public boolean isSlimeSpawn(int x, int z) {
		return this.slime[x & this.mask | (z & this.mask) << this.shift];
	}

	public void clear() {
		Arrays.fill(this.cache, (Object)null);
		Arrays.fill(this.count, 0);
	}
}
