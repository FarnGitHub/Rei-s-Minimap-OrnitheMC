package reifnsk.minimap.compat;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class VanillaCompatImpl implements CompatImpl {

	@Override
	public int getWorldHeight(World world) {
		return 128;
	}

	@Override
	public List<Biome> getBiomes() {
		List<Biome> biomes = new ArrayList<>();
		for(Field f : Biome.class.getFields()) {
			try {
				if (f.getType().equals(Biome.class)) biomes.add((Biome) f.get(Biome.class));
			} catch (IllegalAccessException ignored) {
			}
		}
		return biomes;
	}

	@Override
	public BufferedImage[] getBlockImage(Block block) {
		BufferedImage[] textureSet = new BufferedImage[16];
		BufferedImage atlas = readImage(getTextureAtlas(block));
		int width = Math.max(1, atlas.getWidth() >> 4);
		int height = Math.max(1, atlas.getHeight() >> 4);

		for (int meta = 0; meta < textureSet.length; ++meta) {
			int index = getTextureIndex(block, meta);
			int x = (index & 15) * atlas.getWidth() >> 4;
			int y = (index >> 4) * atlas.getHeight() >> 4;
			textureSet[meta] = atlas.getSubimage(x, y, width, height);
		}
		return textureSet;
	}

	@Override
	public int getGrassColor(BlockView view, int x, int z) {
		return Block.GRASS_BLOCK.getColorMultiplier(view, x, 0, z);
	}

	@Override
	public int getFoliageColor(BlockView view, int x, int z) {
		return Block.LEAVES.getColorMultiplier(view, x, 0, z);
	}

	private static String getTextureAtlas(Block block) {
		try {
			Method m = block.getClass().getMethod("getTextureFile");
			if(m.getReturnType() == String.class && m.getParameterCount() == 0) {
				return (String) m.invoke(block);
			}
		} catch (Exception ignored) {
		}
		return "/terrain.png";
	}

	private static BufferedImage readImage(String resource) {
		try {
			InputStream stream = Minecraft.INSTANCE.texturePacks.selected.getResource(resource);
			return ImageIO.read(stream);
		} catch (IOException e) {
			BufferedImage blankImage = new BufferedImage(1, 1, 2);
			blankImage.setRGB(0, 0, 16711935);
			return blankImage;
		}
	}

	private static int getTextureIndex(Block block, int meta) {
		try {
			return block.getTexture(1, meta);
		} catch (Exception e) {
			return 0;
		}
	}
}
