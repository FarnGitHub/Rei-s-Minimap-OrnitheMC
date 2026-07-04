package reifnsk.minimap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.Sprite;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class BlockColor {
	private static final ArrayList<BlockColor> list = new ArrayList<>();
	private static final BlockColor AIR_BLOCK = instance(16711935);
	private static final int BLOCK_NUM = Block.BLOCKS.length;
	private static final BlockColor[] defaultColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final BlockColor[] textureColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final BlockColor[] blockColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final boolean[] opaqueList = new boolean[BLOCK_NUM];
	private static final boolean[] useMetadata = new boolean[BLOCK_NUM];
	private final int argb;
	public final TintType tintType;
	public final float alpha;
	public final float red;
	public final float green;
	public final float blue;

	static {
		boolean z1;
		Arrays.fill(defaultColor, AIR_BLOCK);
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
		setDefaultColor(97, 0, -9934744);
		setDefaultColor(97, 1, -6974059);
		setDefaultColor(97, 2, -8750470);
		setDefaultColor(98, 0, -8750470);
		setDefaultColor(98, 1, -9275542);
		setDefaultColor(98, 2, -9013642);
		setDefaultColor(99, 0, -3495048);
		setDefaultColor(99, 1, -7509421);
		setDefaultColor(99, 2, -7509421);
		setDefaultColor(99, 3, -7509421);
		setDefaultColor(99, 4, -7509421);
		setDefaultColor(99, 5, -7509421);
		setDefaultColor(99, 6, -7509421);
		setDefaultColor(99, 7, -7509421);
		setDefaultColor(99, 8, -7509421);
		setDefaultColor(99, 9, -7509421);
		setDefaultColor(99, 10, -3495048);
		setDefaultColor(100, 0, -3495048);
		setDefaultColor(100, 1, -4840156);
		setDefaultColor(100, 2, -4840156);
		setDefaultColor(100, 3, -4840156);
		setDefaultColor(100, 4, -4840156);
		setDefaultColor(100, 5, -4840156);
		setDefaultColor(100, 6, -4840156);
		setDefaultColor(100, 7, -4840156);
		setDefaultColor(100, 8, -4840156);
		setDefaultColor(100, 9, -4840156);
		setDefaultColor(100, 10, -3495048);
		setDefaultColor(101, 0, -2140312470);
		setDefaultColor(102, 0, 1627389951);
		setDefaultColor(103, 0, -6842076);
		setDefaultColor(104, 0, 1073780992);
		setDefaultColor(104, 1, 1209242626);
		setDefaultColor(104, 2, 1344704516);
		setDefaultColor(104, 3, 1480166151);
		setDefaultColor(104, 4, 1615693321);
		setDefaultColor(104, 5, 1751154956);
		setDefaultColor(104, 6, 1886616590);
		setDefaultColor(104, 7, 2022144016);
		setDefaultColor(105, 0, 1073780992);
		setDefaultColor(105, 1, 1209242626);
		setDefaultColor(105, 2, 1344704516);
		setDefaultColor(105, 3, 1480166151);
		setDefaultColor(105, 4, 1615693321);
		setDefaultColor(105, 5, 1751154956);
		setDefaultColor(105, 6, 1886616590);
		setDefaultColor(105, 7, 2022144016);
		setDefaultColor(106, 0, -2145432054);
		setDefaultColor(107, 0, -1061382046);
		setDefaultColor(108, 0, -6591135);
		setDefaultColor(109, 0, -8750470);

		for(int i6 = 0; i6 < BLOCK_NUM; ++i6) {
			z1 = true;

			for(int i7 = 0; z1 && i7 < 16; ++i7) {
				BlockColor blockColor8 = defaultColor[pointer(i6, i7)];
				z1 = blockColor8 == null || blockColor8.alpha != 0.0F && blockColor8.alpha != 1.0F;
			}

			opaqueList[i6] = z1;
		}

	}

	private static void calcBlockColor(BlockColor[]... blockColor0) {
		Arrays.fill(blockColor, AIR_BLOCK);
		Arrays.fill(useMetadata, false);

		for(int i1 = 0; i1 < BLOCK_NUM; ++i1) {
			BlockColor[] blockColor2 = null;
			BlockColor blockColor3 = null;
			int i6 = blockColor0.length;

			int i5;
			for(i5 = 0; i5 < i6; ++i5) {
				BlockColor[] blockColor4 = blockColor0[i5];
				if(blockColor4[i1 << 4] != null) {
					blockColor2 = blockColor4;
					blockColor3 = blockColor4[i1 << 4];
					blockColor[i1 << 4] = blockColor3;
					break;
				}
			}

			if(blockColor2 != null) {
				for(int i8 = 1; i8 < 16; ++i8) {
					i5 = pointer(i1, i8);
					if(blockColor2[i5] != AIR_BLOCK && blockColor2[i5] != blockColor3) {
						blockColor[i5] = blockColor2[i5];
						useMetadata[i1] = true;
					} else {
						blockColor[i5] = blockColor3;
					}
				}
			}
		}

	}

	public static void calcBlockColorTD() {
		calcBlockColor(textureColor, defaultColor);
	}

	public static void calcBlockColorD() {
		calcBlockColor(new BlockColor[][]{defaultColor});
	}

	public static void calcBlockColorT() {
		calcBlockColor(new BlockColor[][]{textureColor});
	}

	public static boolean useMetadata(int i0) {
		return useMetadata[i0];
	}

	public static BlockColor getBlockColor(int i0, int i1) {
		BlockColor theColor = blockColor[pointer(i0, i1)];
		if(theColor == null) {
			setDefaultColor(i0, i1, Block.BLOCKS[i0].material.mapColor.color);
		}
		return blockColor[pointer(i0, i1)];
	}

	private static BlockColor instance(int i0) {
		return instance(i0, TintType.NONE);
	}

	private static BlockColor instance(int i0, TintType tintType1) {
		BlockColor blockColor2 = new BlockColor(i0, tintType1);
		int i3 = list.indexOf(blockColor2);
		if(i3 == -1) {
			list.add(blockColor2);
			return blockColor2;
		} else {
			return list.get(i3);
		}
	}

	private static void setDefaultColor(int i0, int i1, int i2) {
		TintType tintType3 = TintType.NONE;
		switch(i0) {
		case 2:
		case 106:
			tintType3 = TintType.GRASS;
			break;
		case 8:
		case 9:
		case 79:
			tintType3 = TintType.WATER;
			break;
		case 18:
			int i4 = i1 & 3;
			if(i4 == 0) {
				tintType3 = TintType.FOLIAGE;
			}

			if(i4 == 1) {
				tintType3 = TintType.PINE;
			}

			if(i4 == 2) {
				tintType3 = TintType.BIRCH;
			}

			if(i4 == 3) {
				tintType3 = TintType.FOLIAGE;
			}
			break;
		case 20:
			tintType3 = TintType.GLASS;
			break;
		case 31:
			if(i1 == 1 || i1 == 2) {
				tintType3 = TintType.TALL_GRASS;
			}
		}

		defaultColor[pointer(i0, i1)] = instance(i2, tintType3);
	}

	public static void textureColorUpdate() {
		Minecraft mc = ReiMinimap.instance.theMinecraft;
		TexturePacks pack = mc.texturePacks;
		TexturePack texture = pack.selected;
		Arrays.fill(textureColor, AIR_BLOCK);
		TempBlockAccess access = new TempBlockAccess();
		int i45 = 0;
		boolean z5 = false;
		boolean z6 = false;

		for(int i46 = BLOCK_NUM; i45 < i46; ++i45) {
			Block block = Block.BLOCKS[i45];
			if(block != null) {
				access.blockId = i45;
				BufferedImage[] buffer = getBlockImage(block);

				int i52 = block.getRenderType();

				for(int i13 = 0; i13 < 16; ++i13) {
					try {
						boolean z14 = block instanceof TorchBlock;
						access.meta = i13;
						block.updateBoundingBox(access, 0, 0, 0);
						double d16 = block.minX;
						double d18 = block.minZ;
						double d20 = block.maxX;
						double d22 = block.maxZ;
						int i24;
						int i25;
						int i26;
						int i27;
						int i28;
						int i29;
						int i30;
						switch(i52) {
						case 0:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 1:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.max(i24 >>> 24, 48) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 2:
							i30 = calcColorInt(buffer[i13], 0.4375D, 0.4375D, 0.5625D, 0.5625D);
							int i31 = calcColorInt(buffer[i13], 0.375D, 0.375D, 0.625D, 0.625D);
							i24 = i30 >> 24 & 255;
							i25 = i31 >> 24 & 255;
							i26 = i24 + i25;
							if(i26 != 0) {
								i27 = ((i30 >> 16 & 255) * i24 + (i31 >> 16 & 255) * i25) / i26;
								i28 = ((i30 >> 8 & 255) * i24 + (i31 >> 8 & 255) * i25) / i26;
								i29 = ((i30 >> 0 & 255) * i24 + (i31 >> 0 & 255) * i25) / i26;
								setTextureColor(i45, i13, Integer.MIN_VALUE | i27 << 16 | i28 << 8 | i29);
								break;
							} else {
								i30 = calcColorInt(buffer[i13], 0.25D, 0.25D, 0.75D, 0.75D);
								i31 = calcColorInt(buffer[i13], 0.0D, 0.0D, 1.0D, 1.0D);
								i24 = i30 >> 24 & 255;
								i25 = i31 >> 24 & 255;
								i26 = i24 + i25;
								if(i26 != 0) {
									i27 = ((i30 >> 16 & 255) * i24 + (i31 >> 16 & 255) * i25) / i26;
									i28 = ((i30 >> 8 & 255) * i24 + (i31 >> 8 & 255) * i25) / i26;
									i29 = ((i30 >> 0 & 255) * i24 + (i31 >> 0 & 255) * i25) / i26;
									setTextureColor(i45, i13, Integer.MIN_VALUE | i27 << 16 | i28 << 8 | i29);
									break;
								}
							}
						case 3:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 4:
							BufferedImage bufferedImage56 = buffer[i13];
							if(i45 == 8 || i45 == 9) {
								buffer[i13] = new BufferedImage(1, 1, 2);
								buffer[i13].setRGB(0, 0, -1960157441);
								z5 = true;
							}

							if(i45 == 10 || i45 == 11) {
								buffer[i13] = new BufferedImage(1, 1, 2);
								buffer[i13].setRGB(0, 0, -2530028);
								z5 = true;
							}

							i26 = calcColorInt(bufferedImage56, 0.0D, 0.0D, 1.0D, 1.0D);
							if(i45 == 8 || i45 == 10) {
								i27 = i26 >> 30 & 255;
								i28 = i26 >> 16 & 255;
								i29 = i26 >> 8 & 255;
								i30 = i26 >> 0 & 255;
								i28 = (int)((double)i28 * 0.9D);
								i29 = (int)((double)i29 * 0.9D);
								i30 = (int)((double)i30 * 0.9D);
								i26 = i27 << 30 | i28 << 16 | i29 << 8 | i30 << 0;
							}

							setTextureColor(i45, i13, i26);
							break;
						case 5:
							float f53 = (float)i13 / 15.0F;
							i25 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i25 & 0xFF000000) != 0) {
								i26 = Math.max(i25 >> 24 & 255, 108);
								i27 = (int)((float)(i25 >> 16 & 255) * Math.max(0.3F, f53 * 0.6F + 0.4F));
								i28 = (int)((float)(i25 >> 8 & 255) * Math.max(0.0F, f53 * f53 * 0.7F - 0.5F));
								setTextureColor(i45, i13, i26 << 24 | i27 << 16 | i28 << 8);
							}
							break;
						case 6:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.max(i24 >>> 24, 32) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 7:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 8:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.min(i24 >>> 24, 40) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 9:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 10:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 11:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.min(i24 >>> 24, 96) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 12:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 13:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 14:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 15:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 16:
							if(i13 >= 10 && i13 <= 13) {
								setTextureColor(i45, i13, calcColorInt(buffer[i13], 0.0D, 0.25D, 1.0D, 1.0D));
								break;
							}

							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 17:
							if((i13 & 7) != 0 && (i13 & 7) != 1) {
								setTextureColor(i45, i13, calcColorInt(buffer[i13], 0.0D, 0.0D, 1.0D, 0.25D));
								break;
							}

							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 18:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.min(i24 >>> 24, 40) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 19:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.max(48, i24 >> 24 & 255);
								i26 = Math.min(255, Math.max(0, i13 * 32 * (i24 >> 16 & 255) / 255));
								i27 = Math.min(255, Math.max(0, (255 - i13 * 8) * (i24 >> 8 & 255) / 255));
								i28 = Math.min(255, Math.max(0, i13 * 4 * (i24 >> 0 & 255) / 255));
								setTextureColor(i45, i13, i25 << 24 | i26 << 16 | i27 << 8 | i28 << 0);
							}
							break;
						case 20:
							i24 = calcColorInt(buffer[i13], 0.0D, 0.0D, 1.0D, 1.0D);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.min(i24 >>> 24, 32) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 21:
							i24 = calcColorInt(buffer[i13], 0.0D, 0.0D, 1.0D, 1.0D);
							if((i24 & 0xFF000000) != 0) {
								i25 = Math.min(i24 >>> 24, 128) << 24;
								setTextureColor(i45, i13, i24 & 0xFFFFFF | i25);
							}
							break;
						case 22:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						case 23:
							i24 = calcColorInt(buffer[i13], d16, d18, d20, d22);
							if((i24 & 0xFF000000) != 0) {
								i25 = i24 >> 24 & 255;
								i26 = (int)((float)((i24 >> 16 & 255) * 32) * 0.003921569F);
								i27 = (int)((float)((i24 >> 8 & 255) * 128) * 0.003921569F);
								i28 = (int)((float)((i24 >> 0 & 255) * 48) * 0.003921569F);
								setTextureColor(i45, i13, i25 << 24 | i26 << 16 | i27 << 8 | i28 << 0);
							}
							break;
						case 24:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
							break;
						default:
							setTextureColor(i45, i13, calcColorInt(buffer[i13], d16, d18, d20, d22));
						}
					} catch (Exception exception42) {
					}
				}
			}
		}

		Arrays.fill(useMetadata, false);

		for(i45 = 0; i45 < BLOCK_NUM; ++i45) {
			BlockColor blockColor47 = textureColor[pointer(i45, 0)];
			if(blockColor47 != null) {
				boolean z49 = false;

				for(int i51 = 1; !z49 && i51 < 16; ++i51) {
					z49 = !blockColor47.equals(textureColor[pointer(i45, i51)]);
				}

				useMetadata[i45] = z49;
			}
		}

	}

	private static void setTextureColor(int i0, int i1, int i2) {
		if(opaqueList[i0]) {
			i2 |= 0xFF000000;
		}

		if((i2 & 0xFF000000) == 0) {
			textureColor[pointer(i0, i1)] = AIR_BLOCK;
		} else {
			TintType tintType3 = TintType.NONE;
			int i4;
			switch(i0) {
			case 2:
			case 106:
				tintType3 = TintType.GRASS;
				break;
			case 8:
			case 9:
			case 79:
				tintType3 = TintType.WATER;
				break;
			case 18:
				i4 = i1 & 3;
				if(i4 == 0) {
					tintType3 = TintType.FOLIAGE;
				}

				if(i4 == 1) {
					tintType3 = TintType.PINE;
				}

				if(i4 == 2) {
					tintType3 = TintType.BIRCH;
				}

				if(i4 == 3) {
					tintType3 = TintType.FOLIAGE;
				}
				break;
			case 20:
				tintType3 = TintType.GLASS;
				break;
			case 31:
				if(i1 == 1 || i1 == 2) {
					tintType3 = TintType.TALL_GRASS;
				}
			}

			if("ic2.common.BlockRubLeaves".equals(Block.BLOCKS[i0].getClass().getCanonicalName())) {
				tintType3 = TintType.BIRCH;
			}

			if("eloraam.world.BlockCustomLeaves".equals(Block.BLOCKS[i0].getClass().getCanonicalName())) {
				i4 = i1 & 3;
				if(i4 == 0) {
					tintType3 = TintType.FOLIAGE;
				}

				if(i4 == 1) {
					tintType3 = TintType.PINE;
				}

				if(i4 == 2) {
					tintType3 = TintType.BIRCH;
				}

				if(i4 == 3) {
					tintType3 = TintType.FOLIAGE;
				}
			}

			textureColor[pointer(i0, i1)] = instance(i2, tintType3);
		}
	}

	private static int pointer(int i0, int i1) {
		return i0 << 4 | i1;
	}

	private static BufferedImage readImage(TexturePack texturePackBase0, String string1) {
		InputStream stream = null;

		label87: {
			BufferedImage bufferedImage4;
			try {
				stream = texturePackBase0.getResource(string1);
				if(stream == null) {
					break label87;
				}

				bufferedImage4 = ImageIO.read(stream);
			} catch (IOException iOException12) {
				break label87;
			} finally {
				if(stream != null) {
					try {
						stream.close();
					} catch (IOException iOException11) {
					}
				}

			}

			return bufferedImage4;
		}

		BufferedImage bufferedImage3 = new BufferedImage(1, 1, 2);
		bufferedImage3.setRGB(0, 0, 16711935);
		return bufferedImage3;
	}

	private static int calcColorInt(BufferedImage image, double d1, double d3, double d5, double d7) {
		int i9 = (int)Math.floor((double)image.getWidth() * d1);
		int i10 = (int)Math.floor((double)image.getHeight() * d3);
		int i11 = (int)Math.floor((double)image.getWidth() * d5);
		int i12 = (int)Math.floor((double)image.getHeight() * d7);
		long j13 = 0L;
		long j15 = 0L;
		long j17 = 0L;
		long j19 = 0L;

		int i21;
		for(i21 = i10; i21 < i12; ++i21) {
			for(int i22 = i9; i22 < i11; ++i22) {
				int i23 = image.getRGB(i22, i21);
				int i24 = i23 >> 24 & 255;
				j13 += i24;
				j15 += (i23 >> 16 & 255) * i24;
				j17 += (i23 >> 8 & 255) * i24;
				j19 += (i23 & 255) * i24;
			}
		}

		if(j13 == 0L) {
			return 16711935;
		} else {
			i21 = image.getWidth() * image.getHeight();
			double d25 = 1.0D / (double)j13;
			j13 /= i21;
			j15 = Math.min(255, Math.max(0, (int)((double)j15 * d25)));
			j17 = Math.min(255, Math.max(0, (int)((double)j17 * d25)));
			j19 = Math.min(255, Math.max(0, (int)((double)j19 * d25)));
			return (int)(j13 << 24 | j15 << 16 | j17 << 8 | j19);
		}
	}

	private BlockColor(int i1, TintType tintType2) {
		if(tintType2 == null) {
			tintType2 = TintType.NONE;
		}

		float f3 = (float)(i1 >> 24 & 255) * 0.003921569F;
		float f4 = (float)(i1 >> 16 & 255) * 0.003921569F;
		float f5 = (float)(i1 >> 8 & 255) * 0.003921569F;
		float f6 = (float)(i1 & 255) * 0.003921569F;
		this.alpha = f3;
		this.red = f4;
		this.green = f5;
		this.blue = f6;
		this.argb = i1;
		this.tintType = tintType2;
	}

	public String toString() {
		return String.format("%08X:%s", new Object[]{this.argb, this.tintType});
	}

	public int hashCode() {
		return this.argb;
	}

	public boolean equals(Object object1) {
		return object1 instanceof BlockColor && this.equals((BlockColor)object1);
	}

	public boolean equals(BlockColor blockColor1) {
		return this.argb == blockColor1.argb && this.tintType == blockColor1.tintType;
	}

	private static BufferedImage[] getBlockImage(Block block) {
		BufferedImage[] buffered = new BufferedImage[16];
		for(int meta = 0;meta < buffered.length; ++meta) {
			Sprite sprite = getSprite(block, meta);
			if(sprite == null) {
				buffered[meta] = GLTextureBufferedImage.create(1,1);
				buffered[meta].setRGB(0,0, 0x00000000);
			} else {
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
			}
		}
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
