package reifnsk.minimap.compat;

import net.minecraft.block.Block;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.block.StationFlatteningBlock;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import reifnsk.minimap.main.render.GLTextureBufferedImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StapiCompatImpl implements CompatImpl{
	@Override
	public int getWorldHeight(World world) {
		return ((StationFlatteningWorld)world).getHeight();
	}

	@Override
	public List<Biome> getBiomes() {
		List<Biome> biomeList = new ArrayList<>();
		for(Biome theOverWorldBiome : BiomeAPI.getOverworldProvider().getBiomes()) {
			if(theOverWorldBiome == null) continue;
			biomeList.add(theOverWorldBiome);
		}

		for(Biome theNetherBiome : BiomeAPI.getNetherProvider().getBiomes()) {
			if(theNetherBiome == null) continue;
			biomeList.add(theNetherBiome);
		}

		return biomeList;
	}

	@Override
	public BufferedImage[] getBlockImage(Block block) {
		BufferedImage[] buffered = new BufferedImage[16];
		for(int meta = 0;meta < buffered.length; ++meta) {
			Sprite sprite = getSprite(block, meta);
			if(sprite == null) {
				buffered[meta] = imageWithVanillaMapColor(block);
			} else {
				try {
					NativeImage image = sprite.getContents().getBaseFrame();
					int width = image.getWidth();
					int height = image.getHeight();
					buffered[meta] = GLTextureBufferedImage.create(width, height);
					for(int y = 0; y < height; ++y) {
						for(int x = 0; x < width; ++x) {
							int color = convertARGBtoABGR(image.getColor(x, y));
							buffered[meta].setRGB(x, y, color);
						}
					}
				} catch (Exception e) {
					buffered[meta] = imageWithVanillaMapColor(block);
				}
			}
		}
		return buffered;
	}

	private static Sprite getSprite(Block block, int meta) {
		try {
			BakedModel baked = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(((StationFlatteningBlock)block).getDefaultState());
			// don't try to get sprite data for non json model
			if(!(baked instanceof VanillaBakedModel)) {
				//return null if it a missing sprite
				if (Objects.equals(baked.getSprite(), StationRenderAPI.getBakedModelManager().getMissingModel().getSprite()))
					return null;
				return baked.getSprite();
			}
		} catch (Exception ignored) {
		}

		try {
			//get sprite data based on top texture
			return ((CustomAtlasProvider)block).getAtlas().getTexture(block.getTexture(1, meta)).getSprite();
		} catch (Exception e) {
			return null;
		}
	}

	private static BufferedImage imageWithVanillaMapColor(Block block) {
		BufferedImage buffered = GLTextureBufferedImage.create(1,1);
		buffered.setRGB(0,0, (255 << 24) | block.material.mapColor.color);
		return buffered;
	}

	private static int convertARGBtoABGR(int argb) {
		return (argb & 0xFF00FF00) | ((argb & 0xFF) << 16) | ((argb >> 16) & 0xFF);
	}


	@Override
	public int getGrassColor(BlockView view, int x, int z) {
		return BiomeColorsImpl.GRASS_INTERPOLATOR.getColor(view.getBiomeSource(), x, z);
	}

	@Override
	public int getFoliageColor(BlockView view, int x, int z) {
		return BiomeColorsImpl.LEAVES_INTERPOLATOR.getColor(view.getBiomeSource(), x, z);
	}
}
