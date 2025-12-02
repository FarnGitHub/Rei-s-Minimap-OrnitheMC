package reifnsk.minimap;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.Arrays;

public class ChunkCache {
	private static final int threshold = 128;
	private final int shift;
	private final int size;
	private final int mask;
	private Chunk[] cache;
	private int[] count;
	private boolean[] slime;

	public ChunkCache(int i1) {
		this.shift = i1;
		this.size = 1 << this.shift;
		this.mask = this.size - 1;
		this.cache = new Chunk[this.size * this.size];
		this.count = new int[this.size * this.size];
		this.slime = new boolean[this.size * this.size];
	}

	public Chunk get(World world1, int i2, int i3) {
		int i4 = i2 & this.mask | (i3 & this.mask) << this.shift;
		Object object5 = this.cache[i4];
		if(object5 == null || ((Chunk)object5).world != world1 || !((Chunk)object5).chunkPosEquals(i2, i3) || --this.count[i4] < 0) {
			if(world1.isPosLoaded(i2 << 4, 0, i3 << 4)) {
				this.cache[i4] = (Chunk)(object5 = world1.getChunk(i2, i3));
				this.count[i4] = 128;
			} else if(object5 instanceof EmptyChunk && ((Chunk)object5).chunkPosEquals(i2, i3)) {
				this.count[i4] = 8;
			} else {
				this.cache[i4] = (Chunk)(object5 = new EmptyChunk(world1, i2, i3));
				this.count[i4] = 8;
			}

			this.slime[i4] = ((Chunk)object5).getSlimeRandom(987234911L).nextInt(10) == 0;
		}

		return (Chunk)object5;
	}

	public boolean isSlimeSpawn(int i1, int i2) {
		return this.slime[i1 & this.mask | (i2 & this.mask) << this.shift];
	}

	public void clear() {
		Arrays.fill(this.cache, (Object)null);
		Arrays.fill(this.count, 0);
	}
}
