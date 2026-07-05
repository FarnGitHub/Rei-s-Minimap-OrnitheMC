package reifnsk.minimap.main.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.Sprite;

import java.awt.image.BufferedImage;
import java.util.Arrays;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class BlockColors {
    private static final ObjectArrayList<BlockColor> list = new ObjectArrayList<>();
    private static final BlockColor AIR_BLOCK = instance(16711935);
    private static final int BLOCK_NUM = Block.BLOCKS.length;
    private static final BlockColor[] defaultColors = new BlockColor[1537]; /* 96 * 16 + 1 */
    private static final BlockColor[] blockColors = new BlockColor[BLOCK_NUM * 16 + 1];
    private static final boolean[] useMetadata = new boolean[BLOCK_NUM];
    private static BlockColor[] textureColors;

    static {
        Arrays.fill(defaultColors, AIR_BLOCK);
        setDefaultColor(1, 0, -9934744);
        setDefaultColor(2, 0, -12096451);
        setDefaultColor(3, 0, -8825542);
        setDefaultColor(4, 0, -6974059);
        setDefaultColor(5, 0, -4417438);
        setDefaultColor(6, 0, 1816358162);
        setDefaultColor(6, 1, 1412577569);
        setDefaultColor(6, 2, 1819645267);
        setDefaultColor(7, 0, -13421773);
        setDefaultColor(8, 0, -1960157441);
        setDefaultColor(9, 0, -1960157441);
        setDefaultColor(10, 0, -2530028);
        setDefaultColor(11, 0, -2530028);
        setDefaultColor(12, 0, -2238560);
        setDefaultColor(13, 0, -7766146);
        setDefaultColor(14, 0, -7304324);
        setDefaultColor(15, 0, -7830913);
        setDefaultColor(16, 0, -9145485);
        setDefaultColor(17, 0, -10006222);
        setDefaultColor(17, 1, -13358823);
        setDefaultColor(17, 2, -3620193);
        setDefaultColor(18, 0, -1708107227);
        setDefaultColor(18, 1, -1522906074);
        setDefaultColor(18, 2, -1707912909);
        setDefaultColor(18, 3, -1707912909);
        setDefaultColor(18, 4, -1708107227);
        setDefaultColor(18, 5, -1522906074);
        setDefaultColor(18, 6, -1707912909);
        setDefaultColor(18, 7, -1707912909);
        setDefaultColor(19, 0, -1710770);
        setDefaultColor(20, 0, 1090519039);
        setDefaultColor(21, 0, -9998201);
        setDefaultColor(22, 0, -14858330);
        setDefaultColor(23, 0, -10987432);
        setDefaultColor(24, 0, -2370913);
        setDefaultColor(25, 0, -10206158);
        setDefaultColor(26, 0, -6339259);
        setDefaultColor(26, 1, -6339259);
        setDefaultColor(26, 2, -6339259);
        setDefaultColor(26, 3, -6339259);
        setDefaultColor(26, 4, -6339259);
        setDefaultColor(26, 5, -6339259);
        setDefaultColor(26, 6, -6339259);
        setDefaultColor(26, 7, -6339259);
        setDefaultColor(26, 8, -6397599);
        setDefaultColor(26, 9, -6397599);
        setDefaultColor(26, 10, -6397599);
        setDefaultColor(26, 11, -6397599);
        setDefaultColor(26, 12, -6397599);
        setDefaultColor(26, 13, -6397599);
        setDefaultColor(26, 14, -6397599);
        setDefaultColor(26, 15, -6397599);
        setDefaultColor(27, 0, -528457632);
        setDefaultColor(27, 1, -528457632);
        setDefaultColor(27, 2, -528457632);
        setDefaultColor(27, 3, -528457632);
        setDefaultColor(27, 4, -528457632);
        setDefaultColor(27, 5, -528457632);
        setDefaultColor(27, 6, -528457632);
        setDefaultColor(27, 7, -528457632);
        setDefaultColor(27, 8, -523214752);
        setDefaultColor(27, 9, -523214752);
        setDefaultColor(27, 10, -523214752);
        setDefaultColor(27, 11, -523214752);
        setDefaultColor(27, 12, -523214752);
        setDefaultColor(27, 13, -523214752);
        setDefaultColor(27, 14, -523214752);
        setDefaultColor(27, 15, -523214752);
        setDefaultColor(28, 0, -8952744);
        setDefaultColor(29, 0, -9605779);
        setDefaultColor(29, 1, -7499421);
        setDefaultColor(29, 2, -9804194);
        setDefaultColor(29, 3, -9804194);
        setDefaultColor(29, 4, -9804194);
        setDefaultColor(29, 5, -9804194);
        setDefaultColor(29, 8, -9605779);
        setDefaultColor(29, 9, -7499421);
        setDefaultColor(29, 10, -9804194);
        setDefaultColor(29, 11, -9804194);
        setDefaultColor(29, 12, -9804194);
        setDefaultColor(29, 13, -9804194);
        setDefaultColor(30, 0, 1775884761);
        setDefaultColor(31, 0, 1383747097);
        setDefaultColor(31, 1, -1571782606);
        setDefaultColor(31, 2, 1330675762);
        setDefaultColor(32, 0, 1383747097);
        setDefaultColor(33, 0, -9605779);
        setDefaultColor(33, 1, -6717094);
        setDefaultColor(33, 2, -9804194);
        setDefaultColor(33, 3, -9804194);
        setDefaultColor(33, 4, -9804194);
        setDefaultColor(33, 5, -9804194);
        setDefaultColor(33, 8, -9605779);
        setDefaultColor(33, 9, -6717094);
        setDefaultColor(33, 10, -9804194);
        setDefaultColor(33, 11, -9804194);
        setDefaultColor(33, 12, -9804194);
        setDefaultColor(33, 13, -9804194);
        setDefaultColor(34, 0, -6717094);
        setDefaultColor(34, 1, -6717094);
        setDefaultColor(34, 2, -2137423526);
        setDefaultColor(34, 3, -2137423526);
        setDefaultColor(34, 4, -2137423526);
        setDefaultColor(34, 5, -2137423526);
        setDefaultColor(34, 8, -6717094);
        setDefaultColor(34, 9, -7499421);
        setDefaultColor(34, 10, -2137423526);
        setDefaultColor(34, 11, -2137423526);
        setDefaultColor(34, 12, -2137423526);
        setDefaultColor(34, 13, -2137423526);
        setDefaultColor(35, 0, -2236963);
        setDefaultColor(35, 1, -1475018);
        setDefaultColor(35, 2, -4370744);
        setDefaultColor(35, 3, -9991469);
        setDefaultColor(35, 4, -4082660);
        setDefaultColor(35, 5, -12928209);
        setDefaultColor(35, 6, -2588006);
        setDefaultColor(35, 7, -12434878);
        setDefaultColor(35, 8, -6445916);
        setDefaultColor(35, 9, -14191468);
        setDefaultColor(35, 10, -8374846);
        setDefaultColor(35, 11, -14273895);
        setDefaultColor(35, 12, -11193573);
        setDefaultColor(35, 13, -13153256);
        setDefaultColor(35, 14, -6083544);
        setDefaultColor(35, 15, -15067369);
        setDefaultColor(37, 0, -1057883902);
        setDefaultColor(38, 0, -1057552625);
        setDefaultColor(39, 0, -1064211115);
        setDefaultColor(40, 0, -1063643364);
        setDefaultColor(41, 0, -66723);
        setDefaultColor(42, 0, -1447447);
        setDefaultColor(43, 0, -5723992);
        setDefaultColor(43, 1, -1712721);
        setDefaultColor(43, 2, -7046838);
        setDefaultColor(43, 3, -8224126);
        setDefaultColor(43, 4, -6591135);
        setDefaultColor(43, 5, -8750470);
        setDefaultColor(44, 0, -5723992);
        setDefaultColor(44, 1, -1712721);
        setDefaultColor(44, 2, -7046838);
        setDefaultColor(44, 3, -8224126);
        setDefaultColor(44, 4, -6591135);
        setDefaultColor(44, 5, -8750470);
        setDefaultColor(45, 0, -6591135);
        setDefaultColor(46, 0, -2407398);
        setDefaultColor(47, 0, -4943782);
        setDefaultColor(48, 0, -14727393);
        setDefaultColor(49, 0, -15527395);
        setDefaultColor(50, 0, 1627379712);
        setDefaultColor(51, 0, -4171263);
        setDefaultColor(52, 0, -14262393);
        setDefaultColor(53, 0, -4417438);
        setDefaultColor(54, 0, -7378659);
        setDefaultColor(55, 0, 1827466476);
        setDefaultColor(56, 0, -8287089);
        setDefaultColor(57, 0, -10428192);
        setDefaultColor(58, 0, -8038091);
        setDefaultColor(59, 0, 302029071);
        setDefaultColor(59, 1, 957524751);
        setDefaultColor(59, 2, 1444710667);
        setDefaultColor(59, 3, -1708815608);
        setDefaultColor(59, 4, -835813369);
        setDefaultColor(59, 5, -532579833);
        setDefaultColor(59, 6, -531663353);
        setDefaultColor(59, 7, -531208953);
        setDefaultColor(60, 0, -9221331);
        setDefaultColor(60, 1, -9550295);
        setDefaultColor(60, 2, -9879003);
        setDefaultColor(60, 3, -10207967);
        setDefaultColor(60, 4, -10536675);
        setDefaultColor(60, 5, -10865383);
        setDefaultColor(60, 6, -11194347);
        setDefaultColor(60, 7, -11523055);
        setDefaultColor(60, 8, -11786226);
        setDefaultColor(61, 0, -9145228);
        setDefaultColor(62, 0, -8355712);
        setDefaultColor(63, 0, -1598779307);
        setDefaultColor(64, 0, -1064934094);
        setDefaultColor(65, 0, -2139595212);
        setDefaultColor(66, 0, -8951211);
        setDefaultColor(67, 0, -6381922);
        setDefaultColor(68, 0, -1598779307);
        setDefaultColor(69, 0, -1603709901);
        setDefaultColor(70, 0, -7368817);
        setDefaultColor(71, 0, -1061043775);
        setDefaultColor(72, 0, -4417438);
        setDefaultColor(73, 0, -6981535);
        setDefaultColor(74, 0, -6981535);
        setDefaultColor(75, 0, -2141709038);
        setDefaultColor(76, 0, -2136923117);
        setDefaultColor(77, 0, -2139851660);
        setDefaultColor(78, 0, -1314833);
        setDefaultColor(79, 0, -1619219203);
        setDefaultColor(80, 0, -986896);
        setDefaultColor(81, 0, -15695840);
        setDefaultColor(82, 0, -6380624);
        setDefaultColor(83, 0, -7094428);
        setDefaultColor(84, 0, -9811658);
        setDefaultColor(85, 0, -4417438);
        setDefaultColor(86, 0, -4229867);
        setDefaultColor(87, 0, -9751501);
        setDefaultColor(88, 0, -11255757);
        setDefaultColor(89, 0, -4157626);
        setDefaultColor(90, 0, -9231226);
        setDefaultColor(91, 0, -3893474);
        setDefaultColor(92, 0, -1848115);
        setDefaultColor(93, 0, -6843501);
        setDefaultColor(94, 0, -4156525);
        setDefaultColor(95, 0, -7378659);
        setDefaultColor(96, 0, -8495827);
        setDefaultColor(96, 1, -8495827);
        setDefaultColor(96, 2, -8495827);
        setDefaultColor(96, 3, -8495827);
        setDefaultColor(96, 4, 545152301);
        setDefaultColor(96, 5, 545152301);
        setDefaultColor(96, 6, 545152301);
        setDefaultColor(96, 7, 545152301);
    }

    private static BlockColor getDefaultColor(int id, int meta) {
        int pointer = pointer(id, meta);
        return pointer < defaultColors.length ? defaultColors[pointer] : AIR_BLOCK;
    }

    public static boolean useMetadata(int meta) {
        return useMetadata[meta];
    }

    public static BlockColor getBlockColor(int id, int meta) {
        return blockColors[pointer(id, meta)];
    }

    private static BlockColor instance(int id) {
        return instance(id, TintType.NONE);
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

    private static void setDefaultColor(int id, int meta, int color) {
        int pointer = pointer(id, meta) % defaultColors.length;
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
                int i4 = meta & 3;
                if(i4 == 0) {
                    tint = TintType.FOLIAGE;
                }

                if(i4 == 1) {
                    tint = TintType.PINE;
                }

                if(i4 == 2) {
                    tint = TintType.BIRCH;
                }

                if(i4 == 3) {
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

        defaultColors[pointer] = instance(color, tint);
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
            BlockColor[] targetPallette = null;
            BlockColor baseColor;

            if ((baseColor = textureColors[id << 4]) != null) {
                targetPallette = textureColors;
                blockColors[id << 4] = baseColor;
            } else if((baseColor = getDefaultColor(id, 0)) != null) {
                targetPallette = defaultColors;
                blockColors[id << 4] = baseColor;
            }

            if(targetPallette != null) {
                for(int meta = 1; meta < 16; ++meta) {
                    int index = pointer(id, meta);
                    if(targetPallette[index] != AIR_BLOCK && targetPallette[index] != baseColor) {
                        blockColors[index] = targetPallette[index];
                        useMetadata[id] = true;
                    } else {
                        blockColors[index] = baseColor;
                    }
                }
            }
        }

        textureColors = null;

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
                buffered[meta] = emptyImage();
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
                    buffered[meta] = emptyImage();
                }
            }
        }
        return buffered;
    }

    private static BufferedImage emptyImage() {
        BufferedImage buffered = GLTextureBufferedImage.create(1,1);
        buffered.setRGB(0,0, 0x00000000);
        return buffered;
    }

    public static int convertARGBtoABGR(int argb) {
        return (argb & 0xFF00FF00) | ((argb & 0xFF) << 16) | ((argb >> 16) & 0xFF);
    }

    public static Sprite getSprite(Block block, int meta) {
        try {
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

        for(int i21 = minY; i21 < maxY; ++i21) {
            for(int i22 = minX; i22 < maxX; ++i22) {
                int i23 = image.getRGB(i22, i21);
                int i24 = i23 >> 24 & 255;
                a += i24;
                r += (i23 >> 16 & 255) * i24;
                g += (i23 >> 8 & 255) * i24;
                b += (i23 & 255) * i24;
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
