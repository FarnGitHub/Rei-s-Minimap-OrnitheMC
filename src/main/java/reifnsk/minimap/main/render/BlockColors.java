package reifnsk.minimap.main.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.Sprite;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class BlockColors {
    private static final ObjectArrayList<BlockColor> list = new ObjectArrayList<>();
    private static final BlockColor AIR_BLOCK = instance(16711935);
    private static final int BLOCK_NUM = Block.BLOCKS.length;
    private static final BlockColor[] blockColors = new BlockColor[BLOCK_NUM * 16 + 1];
    private static final boolean[] useMetadata = new boolean[BLOCK_NUM];
    private static BlockColor[] textureColors;

    public static boolean useMetadata(int meta) {
        return useMetadata[meta];
    }

    public static BlockColor getBlockColor(int id, int meta) {
        return blockColors[pointer(id, meta)];
    }

    private static BlockColor instance(int color) {
        return instance(color, TintType.NONE);
    }

    private static BlockColor instance(int color, TintType tint) {
        BlockColor blockColor = new BlockColor(color, tint);
        int index = list.indexOf(blockColor);
        if(index == -1) {
            list.add(blockColor);
            return blockColor;
        } else {
            return list.get(index);
        }
    }

    public static void updateBlockColor() {
        textureColors = new BlockColor[BLOCK_NUM * 16 + 1];
        Arrays.fill(textureColors, AIR_BLOCK);
        TempBlockAccess access = new TempBlockAccess();

        for(int blockId = 0; blockId < BLOCK_NUM; ++blockId) {
            Block block = Block.BLOCKS[blockId];
            if(block != null) {
                access.blockId = blockId;
                BufferedImage[] buffer = getBlockImage(block);

                int render = block.getRenderType();

                for(int meta = 0; meta < 16; ++meta) {
                    try {
                        boolean isTorch = block instanceof TorchBlock;
                        access.meta = meta;
                        block.updateBoundingBox(access, 0, 0, 0);
                        double minX = block.minX;
                        double minZ = block.minZ;
                        double maxX = block.maxX;
                        double maxZ = block.maxZ;
                        switch(render) {
                            case 1:
                                int crossColor = calcColorInt(buffer[meta], minX, minZ, maxX, maxZ);
                                if((crossColor & 0xFF000000) != 0) {
                                    int crossAlpha = Math.max(crossColor >>> 24, 48) << 24;
                                    setTextureColor(blockId, meta, crossColor & 0xFFFFFF | crossAlpha);
                                }
                                break;
                            case 2:
                                int torchColor1 = calcColorInt(buffer[meta], 0.4375D, 0.4375D, 0.5625D, 0.5625D);
                                int torchColor2 = calcColorInt(buffer[meta], 0.375D, 0.375D, 0.625D, 0.625D);
                                int a1 = torchColor1 >> 24 & 255;
                                int a2 = torchColor2 >> 24 & 255;
                                int aCombined = a1 + a2;
                                if(aCombined != 0) {
                                    int r = ((torchColor1 >> 16 & 255) * a1 + (torchColor2 >> 16 & 255) * a2) / aCombined;
                                    int g = ((torchColor1 >> 8 & 255) * a1 + (torchColor2 >> 8 & 255) * a2) / aCombined;
                                    int b = ((torchColor1 & 255) * a1 + (torchColor2 & 255) * a2) / aCombined;
                                    setTextureColor(blockId, meta, Integer.MIN_VALUE | r << 16 | g << 8 | b);
                                    break;
                                } else {
                                    torchColor1 = calcColorInt(buffer[meta], 0.25D, 0.25D, 0.75D, 0.75D);
                                    torchColor2 = calcColorInt(buffer[meta], 0.0D, 0.0D, 1.0D, 1.0D);
                                    a1 = torchColor1 >> 24 & 255;
                                    a2 = torchColor2 >> 24 & 255;
                                    aCombined = a1 + a2;
                                    if(aCombined != 0) {
                                        int r = ((torchColor1 >> 16 & 255) * a1 + (torchColor2 >> 16 & 255) * a2) / aCombined;
                                        int g = ((torchColor1 >> 8 & 255) * a1 + (torchColor2 >> 8 & 255) * a2) / aCombined;
                                        int b = ((torchColor1 & 255) * a1 + (torchColor2 & 255) * a2) / aCombined;
                                        setTextureColor(blockId, meta, Integer.MIN_VALUE | r << 16 | g << 8 | b);
                                        break;
                                    }
                                }
                            case 3:
                                setTextureColor(blockId, meta, calcColorInt(buffer[meta], minX, minZ, maxX, maxZ));
                                break;
                            case 4:
                                BufferedImage buffered = buffer[meta];
                                if(blockId == 8 || blockId == 9) {
                                    buffer[meta] = new BufferedImage(1, 1, 2);
                                    buffer[meta].setRGB(0, 0, -1960157441);
                                }

                                if(blockId == 10 || blockId == 11) {
                                    buffer[meta] = new BufferedImage(1, 1, 2);
                                    buffer[meta].setRGB(0, 0, -2530028);
                                }

                                int fluidColor = calcColorInt(buffered, 0.0D, 0.0D, 1.0D, 1.0D);
                                if(blockId == 8 || blockId == 10) {
                                    int a = fluidColor >> 30 & 255;
                                    int r = fluidColor >> 16 & 255;
                                    int g = fluidColor >> 8 & 255;
                                    int b = fluidColor & 255;
                                    r = (int)((double)r * 0.9D);
                                    g = (int)((double)g * 0.9D);
                                    b = (int)((double)b * 0.9D);
                                    fluidColor = a << 30 | r << 16 | g << 8 | b;
                                }

                                setTextureColor(blockId, meta, fluidColor);
                                break;
                            case 5:
                                float redstoneStrength = (float)meta / 15.0F;
                                int redstoneColor = calcColorInt(buffer[meta], minX, minZ, maxX, maxZ);
                                if((redstoneColor & 0xFF000000) != 0) {
                                    int r = Math.max(redstoneColor >> 24 & 255, 108);
                                    int g = (int)((float)(redstoneColor >> 16 & 255) * Math.max(0.3F, redstoneStrength * 0.6F + 0.4F));
                                    int b = (int)((float)(redstoneColor >> 8 & 255) * Math.max(0.0F, redstoneStrength * redstoneStrength * 0.7F - 0.5F));
                                    setTextureColor(blockId, meta, r << 24 | g << 16 | b << 8);
                                }
                                break;
                            case 6:
                                int cropColor = calcColorInt(buffer[meta], minX, minZ, maxX, maxZ);
                                if((cropColor & 0xFF000000) != 0) {
                                    int cropAlpha = Math.max(cropColor >>> 24, 32) << 24;
                                    setTextureColor(blockId, meta, cropColor & 0xFFFFFF | cropAlpha);
                                }
                                break;
                            case 8:
                                int ladderColor = calcColorInt(buffer[meta], minX, minZ, maxX, maxZ);
                                if((ladderColor & 0xFF000000) != 0) {
                                    int ladderAlpha = Math.min(ladderColor >>> 24, 40) << 24;
                                    setTextureColor(blockId, meta, ladderColor & 0xFFFFFF | ladderAlpha);
                                }
                                break;
                            case 11:
                                int fenceColor = calcColorInt(buffer[meta], minX, minZ, maxX, maxZ);
                                if((fenceColor & 0xFF000000) != 0) {
                                    int fenceAlpha = Math.min(fenceColor >>> 24, 96) << 24;
                                    setTextureColor(blockId, meta, fenceColor & 0xFFFFFF | fenceAlpha);
                                }
                                break;
                            case 16:
                                if(meta >= 10 && meta <= 13) {
                                    setTextureColor(blockId, meta, calcColorInt(buffer[meta], 0.0D, 0.25D, 1.0D, 1.0D));
                                    break;
                                }

                                setTextureColor(blockId, meta, calcColorInt(buffer[meta], minX, minZ, maxX, maxZ));
                                break;
                            case 17:
                                if((meta & 7) != 0 && (meta & 7) != 1) {
                                    setTextureColor(blockId, meta, calcColorInt(buffer[meta], 0.0D, 0.0D, 1.0D, 0.25D));
                                    break;
                                }

                                setTextureColor(blockId, meta, calcColorInt(buffer[meta], minX, minZ, maxX, maxZ));
                                break;
                            default:
                                setTextureColor(blockId, meta, calcColorInt(buffer[meta], minX, minZ, maxX, maxZ));
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        Arrays.fill(useMetadata, false);
        Arrays.fill(blockColors, AIR_BLOCK);

        for(int id = 0; id < BLOCK_NUM; ++id) {
            BlockColor baseColor = textureColors[id << 4];

            blockColors[id << 4] = baseColor;
            if (!Objects.equals(baseColor, AIR_BLOCK)) {
                for(int meta = 1; meta < 16; ++meta) {
                    int index = pointer(id, meta);
                    if(textureColors[index] != AIR_BLOCK && textureColors[index] != baseColor) {
                        blockColors[index] = textureColors[index];
                        useMetadata[id] = true;
                    } else {
                        blockColors[index] = baseColor;
                    }
                }
            }
        }

        textureColors = null;
        System.gc();
    }

    private static void setTextureColor(int id, int meta, int color) {
        if(textureColors == null) {
            return;
        }

        TintType tint = TintType.NONE;
        switch(id) {
            case 2:
            case 106:
                tint = TintType.GRASS;
                break;
            case 8:
            case 9:
            case 79:
                tint = TintType.WATER;
                break;
            case 18:
                int subMeta = meta & 3;
                if(subMeta == 0) {
                    tint = TintType.FOLIAGE;
                }

                if(subMeta == 1) {
                    tint = TintType.PINE;
                }

                if(subMeta == 2) {
                    tint = TintType.BIRCH;
                }

                if(subMeta == 3) {
                    tint = TintType.FOLIAGE;
                }
                break;
            case 20:
                tint = TintType.GLASS;
                break;
            case 31:
                if(meta == 1 || meta == 2) {
                    tint = TintType.TALL_GRASS;
                }
        }

        textureColors[pointer(id, meta)] = instance(color, tint);
    }

    private static int pointer(int id, int meta) {
        return id << 4 | meta;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static BufferedImage[] getBlockImage(Block block) {
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
                    int size = width * height;
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

    private static BufferedImage imageWithVanillaMapColor(Block block) {
        BufferedImage buffered = GLTextureBufferedImage.create(1,1);
        buffered.setRGB(0,0, (255 << 24) | block.material.mapColor.color);
        return buffered;
    }

    public static int convertARGBtoABGR(int argb) {
        return (argb & 0xFF00FF00) | ((argb & 0xFF) << 16) | ((argb >> 16) & 0xFF);
    }

    public static Sprite getSprite(Block block, int meta) {
        try {
            BakedModel baked = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(block.getDefaultState());
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
            return block.getAtlas().getTexture(block.getTexture(1, meta)).getSprite();
        } catch (Exception e) {
            return null;
        }
    }

    private static int calcColorInt(BufferedImage image, double minXm, double minYm, double maxXm, double maxYm) {
        int minX = (int)Math.floor((double)image.getWidth() * minXm);
        int minY = (int)Math.floor((double)image.getHeight() * minYm);
        int maxX = (int)Math.floor((double)image.getWidth() * maxXm);
        int maxY = (int)Math.floor((double)image.getHeight() * maxYm);
        long a = 0L;
        long r = 0L;
        long g = 0L;
        long b = 0L;

        for(int y = minY; y < maxY; ++y) {
            for(int x = minX; x < maxX; ++x) {
                int rgb = image.getRGB(x, y);
                int aC = rgb >> 24 & 255;
                a += aC;
                r += (rgb >> 16 & 255) * aC;
                g += (rgb >> 8 & 255) * aC;
                b += (rgb & 255) * aC;
            }
        }

        if(a == 0L) {
            return 16711935;
        } else {
            int size = image.getWidth() * image.getHeight();
            double fM = 1.0D / (double)a;
            a /= size;
            r = Math.min(255, Math.max(0, (int)((double)r * fM)));
            g = Math.min(255, Math.max(0, (int)((double)g * fM)));
            b = Math.min(255, Math.max(0, (int)((double)b * fM)));
            return (int)(a << 24 | r << 16 | g << 8 | b);
        }
    }

    static class TempBlockAccess implements BlockView {
        private int blockId;
        private BlockEntity blockEnt;
        private float naturalBrightness;
        private float luminance;
        private int meta;
        private Material material;
        private boolean opaque;
        private boolean suffocate;
        private BiomeSource biome;

        private TempBlockAccess() {
        }

        public int getBlockId(int i1, int i2, int i3) {
            return this.blockId;
        }

        public BlockEntity getBlockEntity(int i1, int i2, int i3) {
            return this.blockEnt;
        }

        public float getNaturalBrightness(int i1, int i2, int i3, int i4) {
            return this.naturalBrightness;
        }

        public float method_1782(int i1, int i2, int i3) {
            return this.luminance;
        }

        public int getBlockMeta(int i1, int i2, int i3) {
            return this.meta;
        }

        public Material getMaterial(int i1, int i2, int i3) {
            return this.material;
        }

        public boolean method_1783(int i1, int i2, int i3) {
            return this.opaque;
        }

        public boolean shouldSuffocate(int i1, int i2, int i3) {
            return this.suffocate;
        }

        public BiomeSource method_1781() {
            return this.biome;
        }
    }
}
