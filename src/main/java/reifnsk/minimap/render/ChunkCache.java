package reifnsk.minimap.render;

import java.util.Arrays;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;

public class ChunkCache {
	private static final int threshold = 128;
	private final int shift;
	private final int size;
	private final int mask;
	private Chunk[] cache;
	private int[] count;
	private boolean[] slime;

	public ChunkCache(int scale) {
		this.shift = scale;
		this.size = 1 << this.shift;
		this.mask = this.size - 1;
		this.cache = new Chunk[this.size * this.size];
		this.count = new int[this.size * this.size];
		this.slime = new boolean[this.size * this.size];
	}

	public Chunk get(World world, int x, int z) {
		int ptr = x & this.mask | (z & this.mask) << this.shift;
		Chunk chunk = this.cache[ptr];
		if(chunk == null || chunk.world != world || !chunk.chunkPosEquals(x, z) || ++this.count[ptr] > 128) {
			this.cache[ptr] = chunk = world.getChunk(x, z);
			this.count[ptr] = 0;
			this.slime[ptr] = chunk.getSlimeRandom(987234911L).nextInt(10) == 0;
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
