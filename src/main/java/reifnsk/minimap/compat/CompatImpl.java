package reifnsk.minimap.compat;

import net.minecraft.block.Block;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.awt.image.BufferedImage;
import java.util.List;

public interface CompatImpl {

	int getWorldHeight(World world);

	List<Biome> getBiomes();

	BufferedImage[] getBlockImage(Block block);

	int getGrassColor(BlockView view, int x, int z);

	int getFoliageColor(BlockView view, int x, int z);
}
