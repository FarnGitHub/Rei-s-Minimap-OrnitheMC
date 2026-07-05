package reifnsk.minimap.main.cache;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.Arrays;

public class ChunkCache {
	private final int shift;
	private final int size;
	private final int mask;
	private Chunk[] cache;
	private int[] count;
	private boolean[] slime;

	public ChunkCache(int shift) {
		this.shift = shift;
		this.size = 1 << this.shift;
		this.mask = this.size - 1;
		this.cache = new Chunk[this.size * this.size];
		this.count = new int[this.size * this.size];
		this.slime = new boolean[this.size * this.size];
	}

	public Chunk get(World world, int x, int z) {
		int index = x & this.mask | (z & this.mask) << this.shift;
		Chunk chunk = this.cache[index];
		if(chunk == null || chunk.world != world || !chunk.chunkPosEquals(x, z) || --this.count[index] < 0) {
			if(world.isPosLoaded(x << 4, 0, z << 4)) {
				this.cache[index] = chunk = world.getChunk(x, z);
				this.count[index] = 128;
			} else if(chunk instanceof EmptyChunk && chunk.chunkPosEquals(x, z)) {
				this.count[index] = 8;
			} else {
				this.cache[index] = chunk = new EmptyChunk(world, x, z);
				this.count[index] = 8;
			}

			this.slime[index] = chunk.getSlimeRandom(987234911L).nextInt(10) == 0;
		}

		return chunk;
	}

	public boolean isSlimeSpawn(int x, int z) {
		return this.slime[x & this.mask | (z & this.mask) << this.shift];
	}

	public void clear() {
		Arrays.fill(this.cache, null);
		Arrays.fill(this.count, 0);
	}
}
