package reifnsk.minimap.main;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MonsterEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.CharacterUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import reifnsk.minimap.main.gui.screen.GuiOptionScreen;
import reifnsk.minimap.main.gui.screen.GuiScreenInterface;
import reifnsk.minimap.main.gui.screen.GuiWaypointEditorScreen;
import reifnsk.minimap.main.gui.screen.GuiWaypointScreen;
import reifnsk.minimap.main.render.*;
import reifnsk.minimap.main.option.EnumOption;
import reifnsk.minimap.main.option.EnumOptionValue;
import reifnsk.minimap.main.option.KeyInput;
import reifnsk.minimap.main.cache.ChunkCache;
import reifnsk.minimap.main.cache.Environment;
import reifnsk.minimap.main.waypoint.Waypoint;
import reifnsk.minimap.main.waypoint.WaypointEntity;

@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal", "unchecked", "BusyWait", "ResultOfMethodCallIgnored", "CallToPrintStackTrace", "SameParameterValue"})
public class ReiMinimap implements Runnable {
	public static final String version = String.format("%s [%s]", "v3.0_01", "Beta 1.7.3");
	public static final File directory = new File(Minecraft.getRunDirectory(), "mods" + File.separatorChar + "rei_minimap");
	private float[] lightBrightnessTable = this.generateLightBrightnessTable(0.125F);
	private static final int[] updateFrequencys = new int[]{2, 5, 10, 20, 40};
	public static final ReiMinimap instance = new ReiMinimap();
	public static Biome[] bgbList;
	public Minecraft theMinecraft;
	private final Tessellator tessellator = Tessellator.INSTANCE;
	private World theWorld;
	private PlayerEntity thePlayer;
	private InGameHud ingameGUI;
	private ScreenScaler scaledResolution;
	private String errorString;
	private boolean multiplayer;
	private SocketAddress currentServer;
	private String currentLevelName;
	private int currentDimension;
	private int scWidth;
	private int scHeight;
	private GLTextureBufferedImage texture = GLTextureBufferedImage.create(256, 256);
	private ChunkCache chunkCache = new ChunkCache(6);
	public final Thread mcThread;
	private Thread workerThread;
	private Lock lock = new ReentrantLock();
	private Condition condition = this.lock.newCondition();
	private StripCounter stripCounter = new StripCounter(289);
	private int stripCountMax1 = 0;
	private int stripCountMax2 = 0;
	private Screen guiScreen;
	private int posX;
	private int posY;
	private double posYd;
	private int posZ;
	private int chunkCoordX;
	private int chunkCoordZ;
	private float sin;
	private float cos;
	private int lastX;
	private int lastY;
	private int lastZ;
	private int skylightSubtracted;
	private boolean isUpdateImage;
	private boolean isCompleteImage;
	private boolean enable = true;
	private boolean showMenuKey = true;
	private boolean filtering = true;
	private int mapPosition = 2;
	private int textureView = 0;
	private float mapOpacity = 1.0F;
	private float largeMapOpacity = 1.0F;
	private boolean largeMapLabel = false;
	private int lightmap = 0;
	private int lightType = 0;
	private boolean undulate = true;
	private boolean transparency = true;
	private boolean environmentColor = true;
	private boolean omitHeightCalc = true;
	private int updateFrequencySetting = 2;
	private boolean threading = false;
	private int threadPriority = 1;
	private boolean hideSnow = false;
	private boolean showChunkGrid = false;
	private boolean showSlimeChunk = false;
	private boolean heightmap = true;
	private boolean showCoordinate = true;
	private int fontScale = 1;
	private int mapScale = 1;
	private int largeMapScale = 1;
	private int coordinateType = 1;
	private boolean visibleWaypoints = true;
	private boolean deathPoint = false;
	private boolean useStencil = false;
	private boolean notchDirection = true;
	private boolean roundmap = false;
	private boolean fullmap = false;
	private boolean forceUpdate;
	private boolean marker = true;
	private boolean markerLabel = true;
	private boolean markerIcon = true;
	private boolean markerDistance = true;
	private long currentTimeMillis;
	private long currentTime;
	private long previousTime;
	private int renderType = 0;
	private TreeMap<Integer, List<Waypoint>> wayPtsMap = new TreeMap<>();
	private List<Waypoint> wayPts = new ArrayList<>();
	private int waypointDimension;
	private static final double[] ZOOM_LIST;
	private int defaultZoom = 1;
	private int flagZoom = 1;
	private int largeZoom = 0;
	private double targetZoom = 1.0D;
	private double currentZoom = 1.0D;
	private float zoomVisible;
	private int grassColor;
	private int foliageColor;
	private int foliageColorPine;
	private int foliageColorBirch;
	private long delay;
	private boolean delayFlag;
	private TexturePack texturePack;
	private int[] temperatureColor;
	private int[] humidityColor;
	private HashMap<Integer, String> dimensionName = new HashMap<>();
	private HashMap<Integer, Double> dimensionScale = new HashMap<>();
	private List<ChatHudLine> chatLineList;
	private long chatTime;
	private boolean configEntitiesRadar;
	private boolean configEntityPlayer;
	private boolean configEntityAnimal;
	private boolean configEntityMob;
	private boolean configEntitySquid;
	private boolean configEntitySlime;
	private boolean configEntityLiving;
	private boolean configEntityLightning;
	private boolean configEntityDirection;
	private boolean allowCavemap;
	private boolean allowEntitiesRadar;
	private boolean allowEntityPlayer;
	private boolean allowEntityAnimal;
	private boolean allowEntityMob;
	private boolean allowEntitySquid;
	private boolean allowEntitySlime;
	private boolean allowEntityLiving;
	private boolean visibleEntitiesRadar;
	private boolean visibleEntityPlayer;
	private boolean visibleEntityAnimal;
	private boolean visibleEntityMob;
	private boolean visibleEntitySquid;
	private boolean visibleEntitySlime;
	private boolean visibleEntityLiving;
	long ntime;
	int count;
	static float[] temp;
	private float[] lightmapRed;
	private float[] lightmapGreen;
	private float[] lightmapBlue;

	static {
		ZOOM_LIST = new double[]{0.5D, 1.0D, 1.5D, 2.0D, 4.0D, 8.0D};
		temp = new float[10];
		float f6 = 0.0F;

		int i8;
		for(i8 = 0; i8 < temp.length; ++i8) {
			temp[i8] = (float)(1.0D / Math.sqrt(i8 + 1));
			f6 += temp[i8];
		}

		f6 = 0.3F / f6;

		for(i8 = 0; i8 < temp.length; ++i8) {
			temp[i8] *= f6;
		}

		f6 = 0.0F;

		for(i8 = 0; i8 < 10; ++i8) {
			f6 += temp[i8];
		}
	}

	public boolean getAllowCavemap() {
		return this.allowCavemap;
	}

	public boolean getAllowEntitiesRadar() {
		return this.allowEntitiesRadar;
	}

	private ReiMinimap() {
		if(this.theMinecraft == null) {
			this.theMinecraft = Minecraft.INSTANCE;
		}
		this.dimensionName.put(0, "Overworld");
		this.dimensionScale.put(0, 1.0D);
		this.dimensionName.put(-1, "Nether");
		this.dimensionScale.put(-1, 8.0D);
		this.chatTime = 0L;
		this.configEntitiesRadar = false;
		this.configEntityPlayer = true;
		this.configEntityAnimal = true;
		this.configEntityMob = true;
		this.configEntitySquid = true;
		this.configEntitySlime = true;
		this.configEntityLiving = true;
		this.configEntityLightning = true;
		this.configEntityDirection = true;
		this.ntime = 0L;
		this.count = 0;
		this.lightmapRed = new float[256];
		this.lightmapGreen = new float[256];
		this.lightmapBlue = new float[256];
		if(!directory.exists()) {
			directory.mkdirs();
		}

		if(!directory.isDirectory()) {
			this.errorString = "[Rei's Minimap] ERROR: Failed to create the rei_minimap folder.";
			error(this.errorString);
		}

		this.loadOptions();
		this.mcThread = Thread.currentThread();
	}

	public void onTickInGame(Minecraft mc) {
		this.currentTimeMillis = System.currentTimeMillis();
		GL11.glPushAttrib(1048575);
		GL11.glPushClientAttrib(-1);
		GL11.glPushMatrix();

		try {
			if(mc == null) {
				return;
			}

			if(this.errorString != null) {
				this.scaledResolution = new ScreenScaler(mc.options, mc.displayWidth, mc.displayHeight);
				mc.textRenderer.drawWithShadow(this.errorString, this.scaledResolution.getScaledWidth() - mc.textRenderer.getWidth(this.errorString) - 2, 2, -65536);
				return;
			}

			if(bgbList == null) {
				List<Biome> biomeList = new ArrayList<>();
				for(Biome theOverWorldBiome : BiomeAPI.getOverworldProvider().getBiomes()) {
					if(theOverWorldBiome == null) continue;
					biomeList.add(theOverWorldBiome);
				}

				for(Biome theNetherBiome : BiomeAPI.getNetherProvider().getBiomes()) {
					if(theNetherBiome == null) continue;
					biomeList.add(theNetherBiome);
				}

				bgbList = biomeList.toArray(new Biome[0]);
			}

			if(this.texturePack != mc.texturePacks.selected) {
				this.texturePack = mc.texturePacks.selected;
				BlockColors.updateBlockColor();
				this.temperatureColor = GLTexture.TEMPERATURE.getData();
				this.humidityColor = GLTexture.HUMIDITY.getData();
			}

			this.thePlayer = this.theMinecraft.player;
			if(this.theWorld != this.theMinecraft.world) {
				this.ingameGUI = this.theMinecraft.inGameHud;
				this.chatLineList = this.ingameGUI.messages;
				this.delay = this.currentTimeMillis + 500L;
				this.isUpdateImage = false;
				this.texture.unregister();
				this.theWorld = this.theMinecraft.world;
				this.theWorld.spawnGlobalEntity(new WaypointEntity(this.theMinecraft));
				this.multiplayer = this.thePlayer instanceof MultiplayerClientPlayerEntity;
				if(this.theWorld != null) {
					Environment.setWorld(this.theWorld);
					boolean canLoad;
					String worldName;
					if(this.multiplayer) {
						SocketAddress socket = getServerSocketAdress();
						if(socket == null) {
							throw new MinimapException("SMP ADDRESS ACQUISITION FAILURE");
						}

						canLoad = this.currentServer != socket;
						if(canLoad) {
							String string47 = socket.toString().replaceAll("[\r\n]", "");
							Matcher matcher50 = Pattern.compile("(.*)/(.*):([0-9]+)").matcher(string47);
							if(!matcher50.matches()) {
								String string55 = socket.toString().replaceAll("[a-z]", "a").replaceAll("[A-Z]", "A").replaceAll("[0-9]", "*");
								throw new MinimapException("SMP ADDRESS FORMAT EXCEPTION: " + string55);
							}

							worldName = matcher50.group(1);
							if(worldName.isEmpty()) {
								worldName = matcher50.group(2);
							}

							if(!matcher50.group(3).equals("25565")) {
								worldName = worldName + "[" + matcher50.group(3) + "]";
							}

							char[] c10 = CharacterUtils.INVALID_CHARS_WORLD_NAME;
							int i9 = CharacterUtils.INVALID_CHARS_WORLD_NAME.length;

							for(int i8 = 0; i8 < i9; ++i8) {
								char c52 = c10[i8];
								worldName = worldName.replace(c52, '_');
							}

							this.currentLevelName = worldName;
							this.currentServer = socket;
						}
					} else {
						worldName = this.theWorld.getProperties().getName();
						if(worldName == null) {
							throw new MinimapException("WORLD_NAME ACQUISITION FAILURE");
						}

						char[] invalidChar = CharacterUtils.INVALID_CHARS_WORLD_NAME;
						int invalidCharWorld = CharacterUtils.INVALID_CHARS_WORLD_NAME.length;

						for(int index = 0; index < invalidCharWorld; ++index) {
							char theChar = invalidChar[index];
							worldName = worldName.replace(theChar, '_');
						}

						canLoad = !worldName.equals(this.currentLevelName) || this.currentServer != null;
						if(canLoad) {
							this.currentLevelName = worldName;
						}

						this.currentServer = null;
					}

					this.currentDimension = this.thePlayer.dimensionId;
					this.waypointDimension = this.currentDimension;
					if(canLoad) {
						this.chatTime = System.currentTimeMillis();
						this.allowCavemap = true;
						this.allowEntitiesRadar = true;
						this.allowEntityPlayer = true;
						this.allowEntityAnimal = true;
						this.allowEntityMob = true;
						this.allowEntitySlime = true;
						this.allowEntitySquid = true;
						this.allowEntityLiving = true;
						this.loadWaypoints();
					}

                    this.wayPts = this.wayPtsMap.computeIfAbsent(this.waypointDimension, k -> new ArrayList<>());
                }

				this.stripCounter.reset();
			}

			this.delayFlag = this.currentTimeMillis < this.delay;
            Environment.calcEnvironment();
			this.visibleEntitiesRadar = this.allowEntitiesRadar && this.configEntitiesRadar;
			this.visibleEntityPlayer = this.allowEntityPlayer && this.configEntityPlayer;
			this.visibleEntityAnimal = this.allowEntityAnimal && this.configEntityAnimal;
			this.visibleEntityMob = this.allowEntityMob && this.configEntityMob;
			this.visibleEntitySlime = this.allowEntitySlime && this.configEntitySlime;
			this.visibleEntitySquid = this.allowEntitySquid && this.configEntitySquid;
			this.visibleEntityLiving = this.allowEntityLiving && this.configEntityLiving;
			int displayWidth = this.theMinecraft.displayWidth;
			int displayHeight = this.theMinecraft.displayHeight;
			this.scaledResolution = new ScreenScaler(this.theMinecraft.options, displayWidth, displayHeight);
			GL11.glScaled(1.0D / (double)this.scaledResolution.scaleFactor, 1.0D / (double)this.scaledResolution.scaleFactor, 1.0D);
			this.scWidth = mc.displayWidth;
			this.scHeight = mc.displayHeight;
			KeyInput.update();
			if(mc.currentScreen != null) {
				if(this.fullmap) {
					this.currentZoom = this.targetZoom = ZOOM_LIST[this.flagZoom];
					this.fullmap = false;
					this.forceUpdate = true;
					this.stripCounter.reset();
				}
			} else {
				if(!this.fullmap) {
					if(KeyInput.TOGGLE_ZOOM.isKeyPush()) {
						if(Keyboard.isKeyDown(this.theMinecraft.options.sneakKey.code)) {
							this.flagZoom = (this.flagZoom == 0 ? ZOOM_LIST.length : this.flagZoom) - 1;
						} else {
							this.flagZoom = (this.flagZoom + 1) % ZOOM_LIST.length;
						}
					} else if(KeyInput.ZOOM_IN.isKeyPush() && this.flagZoom < ZOOM_LIST.length - 1) {
						++this.flagZoom;
					} else if(KeyInput.ZOOM_OUT.isKeyPush() && this.flagZoom > 0) {
						--this.flagZoom;
					}

					this.targetZoom = ZOOM_LIST[this.flagZoom];
				} else {
					if(KeyInput.TOGGLE_ZOOM.isKeyPush()) {
						if(Keyboard.isKeyDown(this.theMinecraft.options.sneakKey.code)) {
							this.largeZoom = (this.largeZoom == 0 ? ZOOM_LIST.length : this.largeZoom) - 1;
						} else {
							this.largeZoom = (this.largeZoom + 1) % ZOOM_LIST.length;
						}
					} else if(KeyInput.ZOOM_IN.isKeyPush() && this.largeZoom < ZOOM_LIST.length - 1) {
						++this.largeZoom;
					} else if(KeyInput.ZOOM_OUT.isKeyPush() && this.largeZoom > 0) {
						--this.largeZoom;
					}

					this.targetZoom = ZOOM_LIST[this.largeZoom];
				}

				if(KeyInput.TOGGLE_ENABLE.isKeyPush()) {
					this.enable = !this.enable;
					this.stripCounter.reset();
					this.forceUpdate = true;
				}

				if(KeyInput.TOGGLE_RENDER_TYPE.isKeyPush()) {
					if(Keyboard.isKeyDown(this.theMinecraft.options.sneakKey.code)) {
						--this.renderType;
						if(this.renderType < 0) {
							this.renderType = EnumOption.RENDER_TYPE.getValueNum() - 1;
						}

						if(!this.allowCavemap && EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
							--this.renderType;
						}
					} else {
						++this.renderType;
						if(!this.allowCavemap && EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
							++this.renderType;
						}

						if(this.renderType >= EnumOption.RENDER_TYPE.getValueNum()) {
							this.renderType = 0;
						}
					}

					this.stripCounter.reset();
					this.forceUpdate = true;
				}

				if(KeyInput.TOGGLE_WAYPOINTS_DIMENSION.isKeyPush()) {
					if(Keyboard.isKeyDown(this.theMinecraft.options.sneakKey.code)) {
						this.prevDimension();
					} else {
						this.nextDimension();
					}
				}

				if(KeyInput.TOGGLE_WAYPOINTS_VISIBLE.isKeyPush()) {
					this.visibleWaypoints = !this.visibleWaypoints;
				}

				if(KeyInput.TOGGLE_WAYPOINTS_MARKER.isKeyPush()) {
					this.marker = !this.marker;
				}

				if(KeyInput.TOGGLE_LARGE_MAP.isKeyPush()) {
					this.fullmap = !this.fullmap;
					this.currentZoom = this.targetZoom = ZOOM_LIST[this.fullmap ? this.largeZoom : this.flagZoom];
					this.forceUpdate = true;
					this.stripCounter.reset();
					if(this.threading) {
						this.lock.lock();

						try {
							this.stripCounter.reset();
							this.mapCalc(false);
						} finally {
							this.lock.unlock();
						}
					}
				}

				if(KeyInput.TOGGLE_LARGE_MAP_LABEL.isKeyPush() && this.fullmap) {
					this.largeMapLabel = !this.largeMapLabel;
				}

				if(this.allowEntitiesRadar && KeyInput.TOGGLE_ENTITIES_RADAR.isKeyPush()) {
					this.configEntitiesRadar = !this.configEntitiesRadar;
				}

				if(KeyInput.SET_WAYPOINT.isKeyPushUp()) {
					this.waypointDimension = this.currentDimension;
					this.wayPts = this.wayPtsMap.get(this.waypointDimension);
					mc.setScreen(new GuiWaypointEditorScreen(mc, null));
				}

				if(KeyInput.WAYPOINT_LIST.isKeyPushUp()) {
					mc.setScreen(new GuiWaypointScreen(null));
				}

				if(KeyInput.MENU_KEY.isKeyPush()) {
					mc.setScreen(new GuiOptionScreen());
				}
			}

			if(!this.allowCavemap && EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
				this.renderType = 0;
			}

			if(this.deathPoint && this.theMinecraft.currentScreen instanceof DeathScreen && !(this.guiScreen instanceof DeathScreen)) {
				String deathString = "Death Point";
				int dX = MathHelper.floor(this.thePlayer.x);
				int dY = MathHelper.floor(this.thePlayer.y);
				int dZ = MathHelper.floor(this.thePlayer.z);
				Random rand = new Random();
				float rX = rand.nextFloat();
				float rY = rand.nextFloat();
				float rZ = rand.nextFloat();
				boolean hasWaypoint = false;
				Iterator<Waypoint> iterator = this.wayPts.iterator();

				while(true) {
					if(iterator.hasNext()) {
						Waypoint waypoint = iterator.next();
						if(waypoint.type != 1 || waypoint.x != dX || waypoint.y != dY || waypoint.z != dZ || !waypoint.enable) {
							continue;
						}

						hasWaypoint = true;
					}

					if(!hasWaypoint) {
						this.wayPts.add(new Waypoint(deathString, dX, dY, dZ, true, rX, rY, rZ, 1));
						this.saveWaypoints();
					}
					break;
				}
			}

			this.guiScreen = this.theMinecraft.currentScreen;
			if(!this.enable || !validScreen(mc.currentScreen)) {
				return;
			}

			if(this.threading) {
				if(this.workerThread == null || !this.workerThread.isAlive()) {
					this.workerThread = new Thread(this);
					this.workerThread.setPriority(3 + this.threadPriority);
					this.workerThread.setDaemon(true);
					this.workerThread.start();
				}
			} else {
				this.mapCalc(true);
			}

			if(this.lock.tryLock()) {
				try {
					if(this.isUpdateImage) {
						this.isUpdateImage = false;
						this.texture.setMinFilter(this.filtering);
						this.texture.setMagFilter(this.filtering);
						this.texture.setClampTexture(true);
						this.texture.register();
					}

					this.condition.signal();
				} finally {
					this.lock.unlock();
				}
			}

			this.currentTime = System.nanoTime();
			double zoom1 = (double)(this.currentTime - this.previousTime) * 1.0E-9D;
			this.zoomVisible = (float)((double)this.zoomVisible - zoom1);
			if(this.currentZoom != this.targetZoom) {
				double zoom2 = Math.max(0.0D, Math.min(1.0D, zoom1 * 4.0D));
				this.currentZoom += (this.targetZoom - this.currentZoom) * zoom2;
				if(Math.abs(this.currentZoom - this.targetZoom) < 5.0E-4D) {
					this.currentZoom = this.targetZoom;
				}

				this.zoomVisible = 3.0F;
			}

			this.previousTime = this.currentTime;
			if(this.texture.getId() != 0) {
				if(this.fullmap) {
					this.renderFullMap();
				} else if(this.roundmap) {
					this.renderRoundMap();
				} else {
					this.renderSquareMap();
				}
			}
		} catch (RuntimeException runtimeException34) {
			runtimeException34.printStackTrace();
			this.errorString = "[Rei's Minimap] ERROR: " + runtimeException34.getMessage();
			error("mainloop runtime exception", runtimeException34);
		} finally {
			GL11.glPopMatrix();
			GL11.glPopClientAttrib();
			GL11.glPopAttrib();
		}

		if(this.count != 0) {
			this.theMinecraft.textRenderer.drawWithShadow(String.format("%12d", this.ntime / (long)this.count), 2, 12, -1);
		}

		Thread.yield();
	}

	public void run() {
		if(this.theMinecraft != null) {
			Thread thread1 = Thread.currentThread();

			while(true) {
				while(!this.enable || thread1 != this.workerThread || !this.threading) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException interruptedException18) {
						return;
					}

					this.lock.lock();

					label199: {
						try {
							this.condition.await();
							break label199;
						} catch (InterruptedException ignored) {
						} finally {
							this.lock.unlock();
						}

						return;
					}

					if(thread1 != this.workerThread) {
						return;
					}
				}

				try {
					if(this.renderType == 0) {
						Thread.sleep((long)(updateFrequencys[updateFrequencys.length - this.updateFrequencySetting - 1] * 2));
					} else {
						Thread.sleep((long)(updateFrequencys[updateFrequencys.length - this.updateFrequencySetting - 1] * 6));
					}
				} catch (InterruptedException interruptedException17) {
					return;
				}

				this.lock.lock();

				try {
					this.mapCalc(false);
					if(this.isCompleteImage || this.isUpdateImage) {
						this.condition.await();
					}
					continue;
				} catch (InterruptedException ignored) {
				} catch (Exception exception22) {
					continue;
				} finally {
					this.lock.unlock();
				}

				return;
			}
		}
	}

	private void startDrawingQuads() {
		this.tessellator.startQuads();
	}

	private void draw() {
		this.tessellator.draw();
	}

	private void addVertexWithUV(double d1, double d3, double d5, double d7, double d9) {
		this.tessellator.vertex(d1, d3, d5, d7, d9);
	}

	private void mapCalc(boolean z1) {
		if(!this.delayFlag) {
			if(this.theWorld != null && this.thePlayer != null) {
				Thread thread2 = Thread.currentThread();
				double d3;
				if(this.stripCounter.count() == 0) {
					this.posX = MathHelper.floor(this.thePlayer.x);
					this.posY = MathHelper.floor(this.thePlayer.y);
					this.posYd = this.thePlayer.y;
					this.posZ = MathHelper.floor(this.thePlayer.z);
					this.chunkCoordX = this.thePlayer.chunkX;
					this.chunkCoordZ = this.thePlayer.chunkZ;
					this.skylightSubtracted = this.calculateSkylightSubtracted(this.theWorld.getTime(), 0.0F);
					if(this.lightType == 0) {
						switch(this.lightmap) {
						case 0:
							this.updateLightmap(this.theWorld.getTime(), 0.0F);
							break;
						case 1:
							this.updateLightmap(6000L, 0.0F);
							break;
						case 2:
							this.updateLightmap(18000L, 0.0F);
							break;
						case 3:
							this.updateLightmap(6000L, 0.0F);
						}
					}

					d3 = Math.toRadians(this.roundmap && !this.fullmap ? 45.0F - this.thePlayer.yaw : (float)(this.notchDirection ? 225 : -45));
					this.sin = (float)Math.sin(d3);
					this.cos = (float)Math.cos(d3);
					this.grassColor = GrassColors.getColor(0.5D, 1.0D);
					this.foliageColor = FoliageColors.getColor(0.5D, 1.0D);
					this.foliageColorPine = FoliageColors.getSpruceColor();
					this.foliageColorBirch = FoliageColors.getBirchColor();
				}

				if(this.fullmap) {
					this.stripCountMax1 = 289;
					this.stripCountMax2 = 289;
				} else {
					d3 = Math.ceil(4.0D / this.currentZoom) * 2.0D + 1.0D;
					this.stripCountMax1 = (int)(d3 * d3);
					d3 = Math.ceil(4.0D / this.targetZoom) * 2.0D + 1.0D;
					this.stripCountMax2 = (int)(d3 * d3);
				}

				if(this.renderType == 1) {
					if(!this.forceUpdate && z1) {
						this.biomeCalcStrip(thread2);
					} else {
						this.biomeCalc(thread2);
					}
				} else if(this.renderType == 2) {
					if(!this.forceUpdate && z1) {
						this.temperatureCalcStrip(thread2);
					} else {
						this.temperatureCalc(thread2);
					}
				} else if(this.renderType == 3) {
					if(!this.forceUpdate && z1) {
						this.humidityCalcStrip(thread2);
					} else {
						this.humidityCalc(thread2);
					}
				} else if(this.renderType == 4) {
					if(!this.forceUpdate && z1) {
						this.caveCalcStrip();
					} else {
						this.caveCalc();
					}
				} else if(!this.forceUpdate && z1) {
					this.surfaceCalcStrip(thread2);
				} else {
					this.surfaceCalc(thread2);
				}

				if(this.isCompleteImage) {
					this.forceUpdate = false;
					this.isCompleteImage = false;
					this.stripCounter.reset();
					this.lastX = this.posX;
					this.lastY = this.posY;
					this.lastZ = this.posZ;
				}

			}
		}
	}

	private void surfaceCalc(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < i2) {
			Point point3 = this.stripCounter.next();
			Chunk chunk4 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point3.x, this.chunkCoordZ + point3.y);
			this.surfaceCalc(chunk4, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalcStrip(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);
		int i3 = updateFrequencys[this.updateFrequencySetting];

		for(int i4 = 0; i4 < i3 && this.stripCounter.count() < i2; ++i4) {
			Point point5 = this.stripCounter.next();
			Chunk chunk6 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point5.x, this.chunkCoordZ + point5.y);
			this.surfaceCalc(chunk6, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalc(Chunk chunk1, Thread thread2) {
		if(!this.delayFlag) {
			if(chunk1 != null && !(chunk1 instanceof EmptyChunk)) {
				int i3 = 128 + chunk1.x * 16 - this.posX;
				int i4 = 128 + chunk1.z * 16 - this.posZ;
				boolean z5 = this.showSlimeChunk && this.currentDimension == 0 && this.chunkCache.isSlimeSpawn(chunk1.x, chunk1.z);
				PixelColor pixelColor6 = new PixelColor(this.transparency);
				Chunk chunk7 = null;
				Chunk chunk8 = null;
				Chunk chunk9 = null;
				Chunk chunk10 = null;
				Chunk chunk11;
				Chunk chunk12;
				Chunk chunk13;
				Chunk chunk14;
				if(this.undulate) {
					chunk9 = this.getChunk(chunk1.world, chunk1.x, chunk1.z - 1);
					chunk10 = this.getChunk(chunk1.world, chunk1.x, chunk1.z + 1);
					chunk7 = this.getChunk(chunk1.world, chunk1.x - 1, chunk1.z);
					chunk8 = this.getChunk(chunk1.world, chunk1.x + 1, chunk1.z);
				}

				for(int i15 = 0; i15 < 16; ++i15) {
					int i16 = i4 + i15;
					if(i16 >= 0) {
						if(i16 >= 256) {
							break;
						}

						for(int i17 = 0; i17 < 16; ++i17) {
							int i18 = i3 + i17;
							if(i18 >= 0) {
								if(i18 >= 256) {
									break;
								}

								pixelColor6.clear();
								int i19 = !this.omitHeightCalc && !this.heightmap && !this.undulate ? this.getWorldHeight() : Math.min(this.getWorldHeight(), chunk1.getHeight(i17, i15));
								int i20 = this.omitHeightCalc ? i19 : this.getWorldHeight();
								this.surfaceCalc(chunk1, i17, i20, i15, pixelColor6, null, thread2);
								float f21;
								if(this.heightmap) {
									f21 = this.undulate ? 0.15F : 0.6F;
									double d22 = (double)i19 - this.posYd;
									float f24 = (float)Math.log10(Math.abs(d22) * 0.125D + 1.0D) * f21;
									if(d22 >= 0.0D) {
										pixelColor6.red += f24 * (1.0F - pixelColor6.red);
										pixelColor6.green += f24 * (1.0F - pixelColor6.green);
										pixelColor6.blue += f24 * (1.0F - pixelColor6.blue);
									} else {
										f24 = Math.abs(f24);
										pixelColor6.red -= f24 * pixelColor6.red;
										pixelColor6.green -= f24 * pixelColor6.green;
										pixelColor6.blue -= f24 * pixelColor6.blue;
									}
								}

								f21 = 1.0F;
								if(this.undulate) {
									chunk11 = i17 == 0 ? chunk7 : chunk1;
									chunk12 = i17 == 15 ? chunk8 : chunk1;
                                    chunk13 = i15 == 0 ? chunk9 : chunk1;
                                    chunk14 = i15 == 15 ? chunk10 : chunk1;
									int i26 = chunk11.getHeight(i17 - 1 & 15, i15);
									int i23 = chunk12.getHeight(i17 + 1 & 15, i15);
                                    int i29 = chunk13.getHeight(i17, i15 - 1 & 15);
									int i25 = chunk14.getHeight(i17, i15 + 1 & 15);
									f21 += Math.max(-4.0F, Math.min(3.0F, (float)(i26 - i23) * this.sin + (float)(i29 - i25) * this.cos)) * 0.14142136F * 0.8F;
								}

								if(z5) {
									pixelColor6.red = (float)((double)pixelColor6.red * 1.2D);
									pixelColor6.green = (float)((double)pixelColor6.green * 0.5D);
									pixelColor6.blue = (float)((double)pixelColor6.blue * 0.5D);
								}

								if(this.showChunkGrid && (i17 == 0 || i15 == 0)) {
									pixelColor6.red = (float)((double)pixelColor6.red * 0.7D);
									pixelColor6.green = (float)((double)pixelColor6.green * 0.7D);
									pixelColor6.blue = (float)((double)pixelColor6.blue * 0.7D);
								}

								byte b27 = ftob(pixelColor6.red * f21);
								byte b28 = ftob(pixelColor6.green * f21);
								byte b30 = ftob(pixelColor6.blue * f21);
								if(this.transparency) {
									this.texture.setRGBA(i18, i16, b27, b28, b30, ftob(pixelColor6.alpha));
								} else {
									this.texture.setRGB(i18, i16, b27, b28, b30);
								}
							}
						}
					}
				}

			}
		}
	}

	private void biomeCalc(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < i2) {
			Point point3 = this.stripCounter.next();
			Chunk chunk4 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point3.x, this.chunkCoordZ + point3.y);
			this.biomeCalc(chunk4, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void biomeCalcStrip(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);
		int i3 = updateFrequencys[this.updateFrequencySetting];

		for(int i4 = 0; i4 < i3 && this.stripCounter.count() < i2; ++i4) {
			Point point5 = this.stripCounter.next();
			Chunk chunk6 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point5.x, this.chunkCoordZ + point5.y);
			this.biomeCalc(chunk6, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void biomeCalc(Chunk chunk1, Thread thread2) {
		if(!this.delayFlag) {
			if(chunk1 != null) {
				int i3 = 128 + chunk1.x * 16 - this.posX;
				int i4 = 128 + chunk1.z * 16 - this.posZ;

				for(int i5 = 0; i5 < 16; ++i5) {
					int i6 = i5 + i4;
					if(i6 >= 0) {
						if(i6 >= 256) {
							break;
						}

						for(int i7 = 0; i7 < 16; ++i7) {
							int i8 = i7 + i3;
							if(i8 >= 0) {
								if(i8 >= 256) {
									break;
								}

								int i9 = Environment.getEnvironment(chunk1, i7, i5, thread2).getBiomeColor();
								byte b10 = (byte)(i9 >> 16);
								byte b11 = (byte)(i9 >> 8);
								byte b12 = (byte)(i9);
								this.texture.setRGB(i8, i6, b10, b11, b12);
							}
						}
					}
				}

			}
		}
	}

	private void temperatureCalc(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < i2) {
			Point point3 = this.stripCounter.next();
			Chunk chunk4 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point3.x, this.chunkCoordZ + point3.y);
			this.temperatureCalc(chunk4, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void temperatureCalcStrip(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);
		int i3 = updateFrequencys[this.updateFrequencySetting];

		for(int i4 = 0; i4 < i3 && this.stripCounter.count() < i2; ++i4) {
			Point point5 = this.stripCounter.next();
			Chunk chunk6 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point5.x, this.chunkCoordZ + point5.y);
			this.temperatureCalc(chunk6, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void temperatureCalc(Chunk chunk1, Thread thread2) {
		if(!this.delayFlag) {
			if(chunk1 != null && !(chunk1 instanceof EmptyChunk)) {
				int i3 = 128 + chunk1.x * 16 - this.posX;
				int i4 = 128 + chunk1.z * 16 - this.posZ;

				for(int i5 = 0; i5 < 16; ++i5) {
					int i6 = i5 + i4;
					if(i6 >= 0) {
						if(i6 >= 256) {
							break;
						}

						for(int i7 = 0; i7 < 16; ++i7) {
							int i8 = i7 + i3;
							if(i8 >= 0) {
								if(i8 >= 256) {
									break;
								}

								double f9 = Environment.getEnvironment(chunk1, i7, i5, thread2).getTemperature();
								int i10 = (int)(f9 * 255.0D);
								this.texture.setRGB(i8, i6, this.temperatureColor[i10]);
							}
						}
					}
				}

			}
		}
	}

	private void humidityCalc(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < i2) {
			Point point3 = this.stripCounter.next();
			Chunk chunk4 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point3.x, this.chunkCoordZ + point3.y);
			this.humidityCalc(chunk4, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void humidityCalcStrip(Thread thread1) {
		int i2 = Math.max(this.stripCountMax1, this.stripCountMax2);
		int i3 = updateFrequencys[this.updateFrequencySetting];

		for(int i4 = 0; i4 < i3 && this.stripCounter.count() < i2; ++i4) {
			Point point5 = this.stripCounter.next();
			Chunk chunk6 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point5.x, this.chunkCoordZ + point5.y);
			this.humidityCalc(chunk6, thread1);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void humidityCalc(Chunk chunk1, Thread thread2) {
		if(!this.delayFlag) {
			if(chunk1 != null && !(chunk1 instanceof EmptyChunk)) {
				int i3 = 128 + chunk1.x * 16 - this.posX;
				int i4 = 128 + chunk1.z * 16 - this.posZ;

				for(int i5 = 0; i5 < 16; ++i5) {
					int i6 = i5 + i4;
					if(i6 >= 0) {
						if(i6 >= 256) {
							break;
						}

						for(int i7 = 0; i7 < 16; ++i7) {
							int i8 = i7 + i3;
							if(i8 >= 0) {
								if(i8 >= 256) {
									break;
								}

								double f9 = Environment.getEnvironment(chunk1, i7, i5, thread2).getHumidity();
								int i10 = (int)(f9 * 255.0D);
								this.texture.setRGB(i8, i6, this.humidityColor[i10]);
							}
						}
					}
				}

			}
		}
	}

	private static byte ftob(float f0) {
		return (byte)Math.max(0, Math.min(255, (int)(f0 * 255.0F)));
	}

	private void surfaceCalc(Chunk chunk, int x, int y, int z, PixelColor pColor, TintType tint, Thread thread) {
		int id = chunk.getBlockId(x, y, z);
		if(id != 0 && (!this.hideSnow || id != 78)) {
			int meta = BlockColors.useMetadata(id) ? chunk.getBlockMeta(x, y, z) : 0;
			BlockColor blockColor = BlockColors.getBlockColor(id, meta);
			if(this.transparency) {
				if(blockColor.alpha < 1.0F && y > 0) {
					this.surfaceCalc(chunk, x, y - 1, z, pColor, blockColor.tintType, thread);
					if(blockColor.alpha == 0.0F) {
						return;
					}
				}
			} else if(blockColor.alpha == 0.0F && y > 0) {
				this.surfaceCalc(chunk, x, y - 1, z, pColor, blockColor.tintType, thread);
				return;
			}

			if(this.lightType == 0) {
                int i11 = switch (this.lightmap) {
                    case 3 -> 15;
                    case 0, 1, 2 -> y < this.getWorldHeight() ? chunk.getLight(LightType.SKY, x, y + 1, z) : 15;
                    default -> 0;
                };

				int i23 = Math.max(Block.BLOCKS_LIGHT_LUMINANCE[id], chunk.getLight(LightType.BLOCK, x, y + 1, z));
				int i26 = i11 << 4 | i23;
				float f27 = this.lightmapRed[i26];
				float f29 = this.lightmapGreen[i26];
				float f30 = this.lightmapBlue[i26];
				if(blockColor.tintType == TintType.WATER && tint == TintType.WATER) {
					return;
				}

				if(this.environmentColor) {
					Environment environment31;
					int i33;
					switch(blockColor.tintType) {
						case GRASS:
							environment31 = Environment.getEnvironment(chunk, x, z, thread);
							i33 = environment31.getGrassColor();
							pColor.composite(blockColor.alpha, i33, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
							return;
						case TALL_GRASS:
							long j32 = x * 3129871L + z * 6129781L + y;
							j32 = j32 * j32 * 42317861L + j32 * 11L;
							int i34 = (int) ((long) x + ((j32 >> 14 & 31L) - 16L));
							int i20 = (int) ((long) z + ((j32 >> 24 & 31L) - 16L));
							int i21 = Environment.getEnvironment(chunk, i34, i20, thread).getGrassColor();
							pColor.composite(blockColor.alpha, i21, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
							return;
						case FOLIAGE:
							environment31 = Environment.getEnvironment(chunk, x, z, thread);
							i33 = environment31.getFoliageColor();
							pColor.composite(blockColor.alpha, i33, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
							return;
						default:
							break;
					}
				} else {
					switch(blockColor.tintType) {
					case GRASS:
						pColor.composite(blockColor.alpha, this.grassColor, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
						return;
					case TALL_GRASS:
						pColor.composite(blockColor.alpha, this.grassColor, f27 * blockColor.red * 0.9F, f29 * blockColor.green * 0.9F, f30 * blockColor.blue * 0.9F);
						return;
					case FOLIAGE:
						pColor.composite(blockColor.alpha, this.foliageColor, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
						return;
					}
				}

				if(blockColor.tintType == TintType.PINE) {
					pColor.composite(blockColor.alpha, this.foliageColorPine, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
					return;
				}

				if(blockColor.tintType == TintType.BIRCH) {
					pColor.composite(blockColor.alpha, this.foliageColorBirch, f27 * blockColor.red, f29 * blockColor.green, f30 * blockColor.blue);
					return;
				}

				if(blockColor.tintType == TintType.GLASS && tint == TintType.GLASS) {
					return;
				}

				pColor.composite(blockColor.alpha, blockColor.red * f27, blockColor.green * f29, blockColor.blue * f30);
			} else {
				int i11 = switch (this.lightmap) {
                    case 1 -> y < this.getWorldHeight() ? chunk.getLight(x, y + 1, z, 0) : 15;
                    case 2 -> y < this.getWorldHeight() ? chunk.getLight(x, y + 1, z, 11) : 4;
                    case 3 -> 15;
                    case 0 -> y < this.getWorldHeight() ? chunk.getLight(x, y + 1, z, this.skylightSubtracted) : 15 - this.skylightSubtracted;
                    default -> 0;
                };

				float f12 = this.lightBrightnessTable[i11];
				if(blockColor.tintType == TintType.WATER && tint == TintType.WATER) {
					return;
				}

				if(this.environmentColor) {
					Environment environment13;
					int i14;
					switch(blockColor.tintType) {
					case GRASS:
						environment13 = Environment.getEnvironment(chunk, x, z, thread);
						i14 = environment13.getGrassColor();
						pColor.composite(blockColor.alpha, i14, f12 * 0.6F);
						return;
					case TALL_GRASS:
						long j25 = x * 3129871L + z * 6129781L + y;
						j25 = j25 * j25 * 42317861L + j25 * 11L;
						int i28 = (int)((long)x + ((j25 >> 14 & 31L) - 16L));
						int i16 = (int)((long)z + ((j25 >> 24 & 31L) - 16L));
						int i17 = Environment.getEnvironment(chunk, i28, i16, thread).getGrassColor();
						pColor.composite(blockColor.alpha, i17, f12 * 0.5F);
						return;
					case FOLIAGE:
						environment13 = Environment.getEnvironment(chunk, x, z, thread);
						i14 = environment13.getFoliageColor();
						pColor.composite(blockColor.alpha, i14, f12 * 0.5F);
						return;
					default:
						break;
					}
				} else {
					switch(blockColor.tintType) {
					case GRASS:
						pColor.composite(blockColor.alpha, this.grassColor, f12 * blockColor.red, f12 * blockColor.green, f12 * blockColor.blue);
						return;
					case TALL_GRASS:
						pColor.composite(blockColor.alpha, this.grassColor, f12 * blockColor.red * 0.9F, f12 * blockColor.green * 0.9F, f12 * blockColor.blue * 0.9F);
						return;
					case FOLIAGE:
						pColor.composite(blockColor.alpha, this.foliageColor, f12 * blockColor.red, f12 * blockColor.green, f12 * blockColor.blue);
						return;
					default:
						break;
					}
				}

				if(blockColor.tintType == TintType.PINE) {
					pColor.composite(blockColor.alpha, this.foliageColorPine, f12 * blockColor.red, f12 * blockColor.green, f12 * blockColor.blue);
					return;
				}

				if(blockColor.tintType == TintType.BIRCH) {
					pColor.composite(blockColor.alpha, this.foliageColorBirch, f12 * blockColor.red, f12 * blockColor.green, f12 * blockColor.blue);
					return;
				}

				if(blockColor.tintType == TintType.GLASS && tint == TintType.GLASS) {
					return;
				}

				pColor.composite(blockColor.alpha, blockColor.red, blockColor.green, blockColor.blue, f12);
			}

		} else {
			if(y > 0) {
				this.surfaceCalc(chunk, x, y - 1, z, pColor, null, thread);
			}

		}
	}

	private void caveCalc() {
		int i1 = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < i1) {
			Point point2 = this.stripCounter.next();
			Chunk chunk3 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point2.x, this.chunkCoordZ + point2.y);
			this.caveCalc(chunk3);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalcStrip() {
		int i1 = Math.max(this.stripCountMax1, this.stripCountMax2);
		int i2 = updateFrequencys[this.updateFrequencySetting];

		for(int i3 = 0; i3 < i2 && this.stripCounter.count() < i1; ++i3) {
			Point point4 = this.stripCounter.next();
			Chunk chunk5 = this.chunkCache.get(this.theWorld, this.chunkCoordX + point4.x, this.chunkCoordZ + point4.y);
			this.caveCalc(chunk5);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalc(Chunk chunk1) {
		if(chunk1 != null && !(chunk1 instanceof EmptyChunk)) {
			int i2 = 128 + chunk1.x * 16 - this.posX;
			int i3 = 128 + chunk1.z * 16 - this.posZ;

			for(int i4 = 0; i4 < 16; ++i4) {
				int i5 = i3 + i4;
				if(i5 >= 0) {
					if(i5 >= 256) {
						break;
					}

					for(int i6 = 0; i6 < 16; ++i6) {
						int i7 = i2 + i6;
						if(i7 >= 0) {
							if(i7 >= 256) {
								break;
							}

							float f8;
							f8 = 0.0F;
							int i9;
							int i10;
							label135:
							switch(this.currentDimension) {
							case -1:
								i9 = 0;

								while(true) {
									if(i9 >= temp.length) {
										break label135;
									}

									i10 = this.posY - i9;
									if(i10 >= 0 && i10 <= this.getWorldHeight() && chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}

									i10 = this.posY + i9 + 1;
									if(i10 >= 0 && i10 <= this.getWorldHeight() && chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}

									++i9;
								}
							case 0:
								i9 = 0;

								while(true) {
									if(i9 >= temp.length) {
										break label135;
									}

									i10 = this.posY - i9;
									if(i10 > this.getWorldHeight() || i10 >= 0 && chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}

									i10 = this.posY + i9 + 1;
									if(i10 > this.getWorldHeight() || i10 >= 0 && chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}

									++i9;
								}
							case 1:
							case 2:
							case 3:
							default:
								for(i9 = 0; i9 < temp.length; ++i9) {
									i10 = this.posY - i9;
									if(i10 < 0 || i10 > this.getWorldHeight() || chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}

									i10 = this.posY + i9 + 1;
									if(i10 < 0 || i10 > this.getWorldHeight() || chunk1.getBlockId(i6, i10, i4) == 0 && chunk1.getLight(i6, i10, i4, 12) != 0) {
										f8 += temp[i9];
									}
								}
							}

							f8 = 0.8F - f8;
							this.texture.setRGB(i7, i5, ftob(0.0F), ftob(f8), ftob(0.0F));
						}
					}
				}
			}

		}
	}

	private void renderRoundMap() {
		int i1 = 1;
		if(this.mapScale == 0) {
			i1 = this.scaledResolution.scaleFactor;
		} else if(this.mapScale == 1) {
			while(this.scWidth >= (i1 + 1) * 320 && this.scHeight >= (i1 + 1) * 240) {
				++i1;
			}
		} else {
			i1 = this.mapScale - 1;
		}

		int i2 = this.fontScale - 1;
		if(this.fontScale == 0) {
			i2 = this.scaledResolution.scaleFactor + 1 >> 1;
		} else if(this.fontScale == 1) {
			i2 = i1 + 1 >> 1;
		}

		int i3 = (this.mapPosition & 2) == 0 ? 37 * i1 : this.scWidth - 37 * i1;
		int i4 = (this.mapPosition & 1) == 0 ? 37 * i1 : this.scHeight - 37 * i1;
		if((this.mapPosition & 1) == 1) {
			i4 -= ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) * i2;
		}

		GL11.glTranslated(i3, i4, 0.0D);
		GL11.glScalef((float)i1, (float)i1, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColorMask(false, false, false, false);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(this.useStencil) {
			GL11.glAlphaFunc(GL11.GL_LEQUAL, 0.1F);
			GL11.glClearStencil(0);
			GL11.glClear(1024);
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glDepthMask(false);
		} else {
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glDepthMask(true);
		}

		GL11.glPushMatrix();
		GL11.glRotatef(90.0F - this.thePlayer.yaw, 0.0F, 0.0F, 1.0F);
		GLTexture.ROUND_MAP_MASK.bind();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawCenteringRectangle(0.0D, 0.0D, 1.01D, 64.0D, 64.0D);
		if(this.useStencil) {
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
			GL11.glStencilFunc(GL11.GL_EQUAL, 1, -1);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, true);
		double d5 = 0.25D / this.currentZoom;
		double d7 = (this.thePlayer.x - (double) this.lastX) / 256D;
		double d9 = (this.thePlayer.z - (double) this.lastZ) / 256D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		this.texture.bind();
		this.startDrawingQuads();
		this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D + d5 + d7, 0.5D + d5 + d9);
		this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + d5 + d7, 0.5D - d5 + d9);
		this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D - d5 + d7, 0.5D - d5 + d9);
		this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - d5 + d7, 0.5D + d5 + d9);
		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		double d11;
		int i16;
		double d17;
		float f20;
		float f21;
		if(this.visibleEntitiesRadar) {
			d11 = this.useStencil ? 34 : 29;
            List<Entity> list = new ArrayList<Entity>(this.theWorld.entities);
			for(Entity entity14 : list) {
				if(entity14 != null) {
					i16 = this.getEntityColor(entity14);
					if(i16 != 0) {
						d17 = this.thePlayer.x - entity14.x;
						double d19 = this.thePlayer.z - entity14.z;
						f21 = (float)Math.toDegrees(Math.atan2(d17, d19));
						double d22 = Math.sqrt(d17 * d17 + d19 * d19) * this.currentZoom * 0.5D;

						try {
							GL11.glPushMatrix();
							if(d22 < d11) {
								float f24 = (float)(i16 >> 16 & 255) * 0.003921569F;
								float f25 = (float)(i16 >> 8 & 255) * 0.003921569F;
								float f26 = (float)(i16 & 255) * 0.003921569F;
								float f27 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity14.y) * 0.04D);
								float f28 = (float)Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity14.boundingBox.minY) * 0.1D));
								f24 *= f28;
								f25 *= f28;
								f26 *= f28;
								GL11.glColor4f(f24, f25, f26, f27);
								GL11.glRotatef(-f21 - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(0.0D, -d22, 0.0D);
								GL11.glRotatef(-(-f21 - this.thePlayer.yaw + 180.0F), 0.0F, 0.0F, 1.0F);
								if(this.configEntityDirection) {
									GL11.glRotatef(entity14.yaw - this.thePlayer.yaw, 0.0F, 0.0F, 1.0F);
									GLTexture.ENTITY2.bind();
									this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
								} else {
									GLTexture.ENTITY.bind();
									this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}

			if(this.configEntityLightning) {
				List<Entity> effectEntities = this.theWorld.globalEntities;

				for(Entity entity14 : effectEntities) {
					if(entity14 instanceof LightningEntity) {
						double d47 = this.thePlayer.x - entity14.x;
						double d18 = this.thePlayer.z - entity14.z;
						f20 = (float)Math.toDegrees(Math.atan2(d47, d18));
						double d58 = Math.sqrt(d47 * d47 + d18 * d18) * this.currentZoom * 0.5D;

						try {
							GL11.glPushMatrix();
							if(d58 < d11) {
								float f23 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity14.y) * 0.04D);
								GL11.glColor4f(1.0F, 1.0F, 1.0F, f23);
								GL11.glRotatef(-f20 - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(0.0D, -d58, 0.0D);
								GL11.glRotatef(-(-f20 - this.thePlayer.yaw + 180.0F), 0.0F, 0.0F, 1.0F);
								GLTexture.LIGHTNING.bind();
								this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		if(this.useStencil) {
			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		GLTexture.ROUND_MAP.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f53;
		if(this.visibleWaypoints) {
			d11 = this.getVisibleDimensionScale();

            for (Waypoint waypoint : this.wayPts) {
                if (waypoint.enable) {
                    double d45 = this.thePlayer.x - (double) waypoint.x * d11 - 0.5D;
                    d17 = this.thePlayer.z - (double) waypoint.z * d11 - 0.5D;
                    f53 = (float) Math.toDegrees(Math.atan2(d45, d17));
                    double d55 = Math.sqrt(d45 * d45 + d17 * d17) * this.currentZoom * 0.5D;

                    try {
                        GL11.glPushMatrix();
                        if (d55 < 31.0D) {
                            GL11.glColor4f(waypoint.red, waypoint.green, waypoint.blue, (float) Math.min(1.0D, Math.max(0.4D, (d55 - 1.0D) * 0.5D)));
                            Waypoint.FILE[waypoint.type].bind();
                            GL11.glRotatef(-f53 - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslated(0.0D, -d55, 0.0D);
                            GL11.glRotatef(-(-f53 - this.thePlayer.yaw + 180.0F), 0.0F, 0.0F, 1.0F);
                            this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
                        } else {
                            GL11.glColor3f(waypoint.red, waypoint.green, waypoint.blue);
                            Waypoint.MARKER[waypoint.type].bind();
                            GL11.glRotatef(-f53 - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslated(0.0D, -34.0D, 0.0D);
                            this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
                        }
                    } finally {
                        GL11.glPopMatrix();
                    }
                }
            }
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		d11 = Math.sin(Math.toRadians(this.thePlayer.yaw)) * 28.0D;
		double d43 = Math.cos(Math.toRadians(this.thePlayer.yaw)) * 28.0D;
		if(this.notchDirection) {
			GLTexture.W.bind();
			this.drawCenteringRectangle(d43, -d11, 1.0D, 8.0D, 8.0D);
			GLTexture.S.bind();
			this.drawCenteringRectangle(-d11, -d43, 1.0D, 8.0D, 8.0D);
			GLTexture.E.bind();
			this.drawCenteringRectangle(-d43, d11, 1.0D, 8.0D, 8.0D);
			GLTexture.N.bind();
			this.drawCenteringRectangle(d11, d43, 1.0D, 8.0D, 8.0D);
		} else {
			GLTexture.N.bind();
			this.drawCenteringRectangle(d43, -d11, 1.0D, 8.0D, 8.0D);
			GLTexture.W.bind();
			this.drawCenteringRectangle(-d11, -d43, 1.0D, 8.0D, 8.0D);
			GLTexture.S.bind();
			this.drawCenteringRectangle(-d43, d11, 1.0D, 8.0D, 8.0D);
			GLTexture.E.bind();
			this.drawCenteringRectangle(d11, d43, 1.0D, 8.0D, 8.0D);
		}

		try {
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GLTexture.MMARROW.bind();
			GL11.glRotatef(this.notchDirection ? 0.0F : -90.0F, 0.0F, 0.0F, 1.0F);
			this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		} catch (Exception ignored) {
		} finally {
			GL11.glPopMatrix();
		}

		GL11.glScaled(1.0D / (double)i1, 1.0D / (double)i1, 1.0D);
		TextRenderer fontRenderer46 = this.theMinecraft.textRenderer;
		i16 = (int)(this.zoomVisible * 255.0F);
		String string48;
		int i56;
		int i59;
		if(i16 > 0) {
			string48 = String.format("%2.2fx", this.currentZoom);
			int i49 = fontRenderer46.getWidth(string48);
			if(i16 > 255) {
				i16 = 255;
			}

			int i54 = 30 * i1 - i49 * i2;
			i56 = 30 * i1 - 8 * i2;
			GL11.glTranslatef((float)i54, (float)i56, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			i59 = i16 << 24 | 0xFFFFFF;
			fontRenderer46.drawWithShadow(string48, 0, 0, i59);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef((float)(-i54), (float)(-i56), 0.0F);
		}

		if(this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			GL11.glPushMatrix();
			string48 = this.getDimensionName(this.waypointDimension);
			float f51 = (float)fontRenderer46.getWidth(string48) * 0.5F * (float)i2;
			f53 = (float)(37 * i1) < f51 ? (float)(37 * i1) - f51 : 0.0F;
			if((this.mapPosition & 2) == 0) {
				f53 = -f53;
			}

			GL11.glTranslated(f53 - f51, -30 * i1, 0.0D);
			GL11.glScaled(i2, i2, 1.0D);
			fontRenderer46.drawWithShadow(string48, 0, 0, 0xFFFFFF);
			GL11.glPopMatrix();
		}

		int i50 = 32 * i1;
		String string52;
		if(this.showCoordinate) {
			String string57;
			if(this.coordinateType == 0) {
				i56 = MathHelper.floor(this.thePlayer.x);
				i59 = MathHelper.floor(this.thePlayer.boundingBox.minY);
				int i60 = MathHelper.floor(this.thePlayer.z);
				string52 = String.format("%+d, %+d", i56, i60);
				string57 = Integer.toString(i59);
			} else {
				string52 = String.format("%+1.2f, %+1.2f", this.thePlayer.x, this.thePlayer.z);
				string57 = String.format("%1.2f (%d)", this.thePlayer.y, (int)this.thePlayer.boundingBox.minY);
			}

			f20 = (float)fontRenderer46.getWidth(string52) * 0.5F * (float)i2;
			f21 = (float)fontRenderer46.getWidth(string57) * 0.5F * (float)i2;
			float f61 = (float)(37 * i1) < f20 ? (float)(37 * i1) - f20 : 0.0F;
			if((this.mapPosition & 2) == 0) {
				f61 = -f61;
			}

			GL11.glTranslatef(f61 - f20, (float)i50, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer46.drawWithShadow(string52, 0, 2, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f20 - f21, 0.0F, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer46.drawWithShadow(string57, 0, 11, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f21 - f61, (float)(-i50), 0.0F);
			i50 += 18 * i2;
		}

		if(this.showMenuKey) {
			string52 = String.format("Menu: %s key", KeyInput.MENU_KEY.getKeyName());
			f53 = (float)this.theMinecraft.textRenderer.getWidth(string52) * 0.5F * (float)i2;
			f20 = (float)(32 * i1) - f53;
			if((this.mapPosition & 2) == 0 && (float)(32 * i1) < f53) {
				f20 = (float)(-32 * i1) + f53;
			}

			GL11.glTranslatef(f20 - f53, (float)i50, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer46.drawWithShadow(string52, 0, 2, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f53 - f20, (float)(-i50), 0.0F);
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void renderSquareMap() {
		int i1 = 1;
		if(this.mapScale == 0) {
			i1 = this.scaledResolution.scaleFactor;
		} else if(this.mapScale == 1) {
			while(this.scWidth >= (i1 + 1) * 320 && this.scHeight >= (i1 + 1) * 240) {
				++i1;
			}
		} else {
			i1 = this.mapScale - 1;
		}

		int i2 = this.fontScale - 1;
		if(this.fontScale == 0) {
			i2 = this.scaledResolution.scaleFactor + 1 >> 1;
		} else if(this.fontScale == 1) {
			i2 = i1 + 1 >> 1;
		}

		int i3 = (this.mapPosition & 2) == 0 ? 37 * i1 : this.scWidth - 37 * i1;
		int i4 = (this.mapPosition & 1) == 0 ? 37 * i1 : this.scHeight - 37 * i1;
		if((this.mapPosition & 1) == 1) {
			i4 -= ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) * i2;
		}

		GL11.glTranslated(i3, i4, 0.0D);
		GL11.glScalef((float)i1, (float)i1, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColorMask(false, false, false, false);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(this.useStencil) {
			GL11.glAlphaFunc(GL11.GL_LEQUAL, 0.1F);
			GL11.glClearStencil(0);
			GL11.glClear(1024);
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glDepthMask(false);
		} else {
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glDepthMask(true);
		}

		GLTexture.SQUARE_MAP_MASK.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.001D, 64.0D, 64.0D);
		if(this.useStencil) {
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
			GL11.glStencilFunc(GL11.GL_EQUAL, 1, -1);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		double d5 = 0.25D / this.currentZoom;
		double d7 = (this.thePlayer.x - (double) this.lastX) / 256D;
		double d9 = (this.thePlayer.z - (double) this.lastZ) / 256D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		this.texture.bind();
		this.startDrawingQuads();
		if(this.notchDirection) {
			this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + d5 + d7, 0.5D + d5 + d9);
			this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D + d5 + d7, 0.5D - d5 + d9);
			this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - d5 + d7, 0.5D - d5 + d9);
			this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D - d5 + d7, 0.5D + d5 + d9);
		} else {
			this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D + d5 + d7, 0.5D + d5 + d9);
			this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + d5 + d7, 0.5D - d5 + d9);
			this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D - d5 + d7, 0.5D - d5 + d9);
			this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - d5 + d7, 0.5D + d5 + d9);
		}

		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i15;
		double d17;
		double d20;
		double d65;
		double d77;
		double d78;
		if(this.visibleEntitiesRadar) {
			float f11 = (float)(this.useStencil ? 34 : 31);
			ArrayList<Entity> arrayList12 = new ArrayList<Entity>(this.theWorld.entities);

			for(Entity entity13 : arrayList12) {
				if(entity13 != null) {
					i15 = this.getEntityColor(entity13);
					if(i15 != 0) {
						double d16 = this.thePlayer.x - entity13.x;
						double d18 = this.thePlayer.z - entity13.z;
						d16 = d16 * this.currentZoom * 0.5D;
						d18 = d18 * this.currentZoom * 0.5D;
						d20 = Math.max(Math.abs(d16), Math.abs(d18));

						try {
							GL11.glPushMatrix();
							if(d20 < (double)f11) {
								float f22 = (float)(i15 >> 16 & 255) * 0.003921569F;
								float f23 = (float)(i15 >> 8 & 255) * 0.003921569F;
								float f24 = (float)(i15 & 255) * 0.003921569F;
								float f25 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity13.y) * 0.04D);
								float f26 = (float)Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity13.boundingBox.minY) * 0.1D));
								f22 *= f26;
								f23 *= f26;
								f24 *= f26;
								GL11.glColor4f(f22, f23, f24, f25);
								double d27;
								double d29;
								float f31;
								if(this.notchDirection) {
									d27 = -d16;
									d29 = -d18;
									f31 = entity13.yaw + 180.0F;
								} else {
									d27 = d18;
									d29 = -d16;
									f31 = entity13.yaw - 90.0F;
								}

								if(this.configEntityDirection) {
									GL11.glTranslated(d27, d29, 0.0D);
									GL11.glRotatef(f31, 0.0F, 0.0F, 1.0F);
									GL11.glTranslated(-d27, -d29, 0.0D);
									GLTexture.ENTITY2.bind();
									this.drawCenteringRectangle(d27, d29, 1.0D, 8.0D, 8.0D);
								} else {
									GLTexture.ENTITY.bind();
									this.drawCenteringRectangle(d27, d29, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}

			if(this.configEntityLightning) {
				List<Entity> entites = this.theWorld.globalEntities;

				for(Entity entity13 : entites) {
					if(entity13 instanceof LightningEntity) {
						d65 = this.thePlayer.x - entity13.x;
						d17 = this.thePlayer.z - entity13.z;
						d65 = d65 * this.currentZoom * 0.5D;
						d17 = d17 * this.currentZoom * 0.5D;
						double d19 = Math.max(Math.abs(d65), Math.abs(d17));

						try {
							GL11.glPushMatrix();
							if(d19 < (double)f11) {
								float f21 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity13.y) * 0.04D);
								GL11.glColor4f(1.0F, 1.0F, 1.0F, f21);
								if(this.notchDirection) {
									d77 = -d65;
									d78 = -d17;
								} else {
									d77 = d17;
									d78 = -d65;
								}

								GLTexture.LIGHTNING.bind();
								this.drawCenteringRectangle(d77, d78, 1.0D, 8.0D, 8.0D);
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		if(this.useStencil) {
			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		GLTexture.SQUARE_MAP.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(this.visibleWaypoints) {
			double d58 = this.getVisibleDimensionScale();

            for (Waypoint waypoint61 : this.wayPts) {
                if (waypoint61.enable) {
                    d65 = this.thePlayer.x - (double) waypoint61.x * d58 - 0.5D;
                    d17 = this.thePlayer.z - (double) waypoint61.z * d58 - 0.5D;
                    d65 = d65 * this.currentZoom * 0.5D;
                    d17 = d17 * this.currentZoom * 0.5D;
                    float f75 = (float) Math.toDegrees(Math.atan2(d65, d17));
                    d20 = Math.max(Math.abs(d65), Math.abs(d17));

                    try {
                        GL11.glPushMatrix();
                        if (d20 < 31.0D) {
                            GL11.glColor4f(waypoint61.red, waypoint61.green, waypoint61.blue, (float) Math.min(1.0D, Math.max(0.4D, (d20 - 1.0D) * 0.5D)));
                            Waypoint.FILE[waypoint61.type].bind();
                            if (this.notchDirection) {
                                this.drawCenteringRectangle(-d65, -d17, 1.0D, 8.0D, 8.0D);
                            } else {
                                this.drawCenteringRectangle(d17, -d65, 1.0D, 8.0D, 8.0D);
                            }
                        } else {
                            d77 = 34.0D / d20;
                            d65 *= d77;
                            d17 *= d77;
                            d78 = Math.sqrt(d65 * d65 + d17 * d17);
                            GL11.glColor3f(waypoint61.red, waypoint61.green, waypoint61.blue);
                            Waypoint.MARKER[waypoint61.type].bind();
                            GL11.glRotatef((this.notchDirection ? 0.0F : 90.0F) - f75, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslated(0.0D, -d78, 0.0D);
                            this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
                        }
                    } finally {
                        GL11.glPopMatrix();
                    }
                }
            }
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GLTexture.N.bind();
		this.drawCenteringRectangle(0.0D, -28.0D, 1.0D, 8.0D, 8.0D);
		GLTexture.S.bind();
		this.drawCenteringRectangle(0.0D, 28.0D, 1.0D, 8.0D, 8.0D);
		GLTexture.W.bind();
		this.drawCenteringRectangle(-28.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		GLTexture.E.bind();
		this.drawCenteringRectangle(28.0D, 0.0D, 1.0D, 8.0D, 8.0D);

		try {
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GLTexture.MMARROW.bind();
			GL11.glRotatef(this.thePlayer.yaw - (this.notchDirection ? 180.0F : 90.0F), 0.0F, 0.0F, 1.0F);
			this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		} catch (Exception ignored) {
		} finally {
			GL11.glPopMatrix();
		}

		GL11.glScaled(1.0D / (double)i1, 1.0D / (double)i1, 1.0D);
		TextRenderer fontRenderer59 = this.theMinecraft.textRenderer;
		int i60 = (int)(this.zoomVisible * 255.0F);
		String string62;
		int i68;
		int i72;
		if(i60 > 0) {
			string62 = String.format("%2.2fx", this.currentZoom);
			int i64 = fontRenderer59.getWidth(string62);
			if(i60 > 255) {
				i60 = 255;
			}

			i15 = 30 * i1 - i64 * i2;
			i68 = 30 * i1 - 8 * i2;
			GL11.glTranslatef((float)i15, (float)i68, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			i72 = i60 << 24 | 0xFFFFFF;
			fontRenderer59.drawWithShadow(string62, 0, 0, i72);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef((float)(-i15), (float)(-i68), 0.0F);
		}

		float f69;
		if(this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			GL11.glPushMatrix();
			string62 = this.getDimensionName(this.waypointDimension);
			float f66 = (float)fontRenderer59.getWidth(string62) * 0.5F * (float)i2;
			f69 = (float)(37 * i1) < f66 ? (float)(37 * i1) - f66 : 0.0F;
			if((this.mapPosition & 2) == 0) {
				f69 = -f69;
			}

			GL11.glTranslated(f69 - f66, -30 * i1, 0.0D);
			GL11.glScaled(i2, i2, 1.0D);
			fontRenderer59.drawWithShadow(string62, 0, 0, 0xFFFFFF);
			GL11.glPopMatrix();
		}

		int i63 = 32 * i1;
		String string67;
		float f70;
		if(this.showCoordinate) {
			String string71;
			if(this.coordinateType == 0) {
				i68 = MathHelper.floor(this.thePlayer.x);
				i72 = MathHelper.floor(this.thePlayer.boundingBox.minY);
				int i73 = MathHelper.floor(this.thePlayer.z);
				string67 = String.format("%+d, %+d", i68, i73);
				string71 = Integer.toString(i72);
			} else {
				string67 = String.format("%+1.2f, %+1.2f", this.thePlayer.x, this.thePlayer.z);
				string71 = String.format("%1.2f (%d)", this.thePlayer.y, (int)this.thePlayer.boundingBox.minY);
			}

			f70 = (float)fontRenderer59.getWidth(string67) * 0.5F * (float)i2;
			float f76 = (float)fontRenderer59.getWidth(string71) * 0.5F * (float)i2;
			float f74 = (float)(37 * i1) < f70 ? (float)(37 * i1) - f70 : 0.0F;
			if((this.mapPosition & 2) == 0) {
				f74 = -f74;
			}

			GL11.glTranslatef(f74 - f70, (float)i63, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer59.drawWithShadow(string67, 0, 2, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f70 - f76, 0.0F, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer59.drawWithShadow(string71, 0, 11, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f76 - f74, (float)(-i63), 0.0F);
			i63 += 18 * i2;
		}

		if(this.showMenuKey) {
			string67 = String.format("Menu: %s key", KeyInput.MENU_KEY.getKeyName());
			f69 = (float)this.theMinecraft.textRenderer.getWidth(string67) * 0.5F * (float)i2;
			f70 = (float)(32 * i1) - f69;
			if((this.mapPosition & 2) == 0 && (float)(32 * i1) < f69) {
				f70 = (float)(-32 * i1) + f69;
			}

			GL11.glTranslatef(f70 - f69, (float)i63, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			fontRenderer59.drawWithShadow(string67, 0, 2, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f69 - f70, (float)(-i63), 0.0F);
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void renderFullMap() {
		int i1 = 1;
		int i2;
		if(this.largeMapScale == 0) {
			i1 = this.scaledResolution.scaleFactor;
		} else {
            i2 = this.largeMapScale == 1 ? 1000 : this.largeMapScale - 1;
            while (i1 < i2 && this.scWidth >= (i1 + 1) * 240 && this.scHeight >= (i1 + 1) * 240) {
                ++i1;
            }
        }

		i2 = this.fontScale - 1;
		if(this.fontScale == 0) {
			i2 = this.scaledResolution.scaleFactor + 1 >> 1;
		} else if(this.fontScale == 1) {
			i2 = i1 + 1 >> 1;
		}

		GL11.glTranslated((double)this.scWidth * 0.5D, (double)this.scHeight * 0.5D, 0.0D);
		GL11.glScalef((float)i1, (float)i1, 0.0F);
		double d3 = 0.234375D / this.currentZoom;
		double d5 = (this.thePlayer.x - (double) this.lastX) / 256D;
		double d7 = (this.thePlayer.z - (double) this.lastZ) / 256D;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.texture.bind();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.largeMapOpacity);
		this.startDrawingQuads();
		if(this.notchDirection) {
			this.addVertexWithUV(120.0D, 120.0D, 1.0D, 0.5D + d3 + d5, 0.5D + d3 + d7);
			this.addVertexWithUV(120.0D, -120.0D, 1.0D, 0.5D + d3 + d5, 0.5D - d3 + d7);
			this.addVertexWithUV(-120.0D, -120.0D, 1.0D, 0.5D - d3 + d5, 0.5D - d3 + d7);
			this.addVertexWithUV(-120.0D, 120.0D, 1.0D, 0.5D - d3 + d5, 0.5D + d3 + d7);
		} else {
			this.addVertexWithUV(-120.0D, 120.0D, 1.0D, 0.5D + d3 + d5, 0.5D + d3 + d7);
			this.addVertexWithUV(120.0D, 120.0D, 1.0D, 0.5D + d3 + d5, 0.5D - d3 + d7);
			this.addVertexWithUV(120.0D, -120.0D, 1.0D, 0.5D - d3 + d5, 0.5D - d3 + d7);
			this.addVertexWithUV(-120.0D, -120.0D, 1.0D, 0.5D - d3 + d5, 0.5D + d3 + d7);
		}

		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i12;
		double d13;
		double d15;
		float f18;
		float f19;
		if(this.visibleEntitiesRadar) {
			ArrayList<Entity> arrayList9 = new ArrayList<Entity>(this.theWorld.entities);

			for(Entity entity10 : arrayList9) {
				if(entity10 != null) {
					i12 = this.getEntityColor(entity10);
					if(i12 != 0) {
						d13 = this.thePlayer.x - entity10.x;
						d15 = this.thePlayer.z - entity10.z;
						d13 = d13 * this.currentZoom * 2.0D;
						d15 = d15 * this.currentZoom * 2.0D;
						double d17 = Math.max(Math.abs(d13), Math.abs(d15));

						try {
							GL11.glPushMatrix();
							if(d17 < 114.0D) {
								f19 = (float)(i12 >> 16 & 255) * 0.003921569F;
								float f20 = (float)(i12 >> 8 & 255) * 0.003921569F;
								float f21 = (float)(i12 & 255) * 0.003921569F;
								float f22 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity10.y) * 0.04D);
								float f23 = (float)Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity10.boundingBox.minY) * 0.1D));
								f19 *= f23;
								f20 *= f23;
								f21 *= f23;
								GL11.glColor4f(f19, f20, f21, f22);
								double d24;
								double d26;
								float f28;
								if(this.notchDirection) {
									d24 = -d13;
									d26 = -d15;
									f28 = entity10.yaw + 180.0F;
								} else {
									d24 = d15;
									d26 = -d13;
									f28 = entity10.yaw - 90.0F;
								}

								if(this.configEntityDirection) {
									GL11.glTranslated(d24, d26, 0.0D);
									GL11.glRotatef(f28, 0.0F, 0.0F, 1.0F);
									GL11.glTranslated(-d24, -d26, 0.0D);
									GLTexture.ENTITY2.bind();
									this.drawCenteringRectangle(d24, d26, 1.0D, 8.0D, 8.0D);
								} else {
									GLTexture.ENTITY.bind();
									this.drawCenteringRectangle(d24, d26, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}

			if(this.configEntityLightning) {
				List<Entity> entities = this.theWorld.globalEntities;

				for(Entity entity10 : entities) {
					if(entity10 instanceof LightningEntity) {
						double d66 = this.thePlayer.x - entity10.x;
						double d14 = this.thePlayer.z - entity10.z;
						d66 = d66 * this.currentZoom * 2.0D;
						d14 = d14 * this.currentZoom * 2.0D;
						double d16 = Math.max(Math.abs(d66), Math.abs(d14));

						try {
							GL11.glPushMatrix();
							if(d16 < 114.0D) {
								f18 = (float)Math.max(0.2F, 1.0D - Math.abs(this.thePlayer.y - entity10.y) * 0.04D);
								GL11.glColor4f(1.0F, 1.0F, 1.0F, f18);
								double d77;
								double d79;
								if(this.notchDirection) {
									d77 = -d66;
									d79 = -d14;
								} else {
									d77 = d14;
									d79 = -d66;
								}

								GLTexture.LIGHTNING.bind();
								this.drawCenteringRectangle(d77, d79, 1.0D, 8.0D, 8.0D);
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		GLTexture.N.bind();
		this.drawCenteringRectangle(0.0D, -104.0D, 1.0D, 16.0D, 16.0D);
		GLTexture.S.bind();
		this.drawCenteringRectangle(0.0D, 0.0D + 104.0D, 1.0D, 16.0D, 16.0D);
		GLTexture.W.bind();
		this.drawCenteringRectangle(0.0D - 104.0D, 0.0D, 1.0D, 16.0D, 16.0D);
		GLTexture.E.bind();
		this.drawCenteringRectangle(0.0D + 104.0D, 0.0D, 1.0D, 16.0D, 16.0D);

		try {
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GLTexture.MMARROW.bind();
			GL11.glRotatef(this.thePlayer.yaw - (this.notchDirection ? 180.0F : 90.0F), 0.0F, 0.0F, 1.0F);
			this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		} catch (Exception ignored) {
		} finally {
			GL11.glPopMatrix();
		}

		float f75;
		if(this.visibleWaypoints) {

            for (Waypoint waypoint57 : this.wayPts) {
                double d62 = this.getVisibleDimensionScale();
                if (waypoint57.enable) {
                    d13 = this.thePlayer.x - (double) waypoint57.x * d62 - 0.5D;
                    d15 = this.thePlayer.z - (double) waypoint57.z * d62 - 0.5D;
                    d13 = d13 * this.currentZoom * 2.0D;
                    d15 = d15 * this.currentZoom * 2.0D;
                    f75 = (float) Math.toDegrees(Math.atan2(d13, d15));
                    double d76 = Math.max(Math.abs(d13), Math.abs(d15));

                    try {
                        GL11.glPushMatrix();
                        double d78;
                        double d80;
                        if (d76 < 114.0D) {
                            GL11.glColor4f(waypoint57.red, waypoint57.green, waypoint57.blue, (float) Math.min(1.0D, Math.max(0.4D, (d76 - 1.0D) * 0.5D)));
                            Waypoint.FILE[waypoint57.type].bind();
                            if (this.notchDirection) {
                                d78 = -d13;
                                d80 = -d15;
                            } else {
                                d78 = d15;
                                d80 = -d13;
                            }

                            this.drawCenteringRectangle(d78, d80, 1.0D, 8.0D, 8.0D);
                            if (this.largeMapLabel && waypoint57.name != null && !waypoint57.name.isEmpty()) {
                                GL11.glDisable(GL11.GL_TEXTURE_2D);
                                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.627451F);
                                int i81 = this.theMinecraft.textRenderer.getWidth(waypoint57.name);
                                int i25 = (int) d78;
                                int i82 = (int) d80;
                                int i27 = i25 - (i81 >> 1);
                                int i83 = i27 + i81;
                                int i29 = i82 - 15;
                                int i30 = i82 - 5;
                                this.tessellator.startQuads();
                                this.tessellator.vertex(i27 - 1, i30, 1.0D);
                                this.tessellator.vertex(i83 + 1, i30, 1.0D);
                                this.tessellator.vertex(i83 + 1, i29, 1.0D);
                                this.tessellator.vertex(i27 - 1, i29, 1.0D);
                                this.tessellator.draw();
                                GL11.glEnable(GL11.GL_TEXTURE_2D);
                                this.theMinecraft.textRenderer.drawWithShadow(waypoint57.name, i27, i29 + 1, waypoint57.type == 0 ? -1 : -65536);
                            }
                        } else {
                            d78 = 117.0D / d76;
                            d13 *= d78;
                            d15 *= d78;
                            d80 = Math.sqrt(d13 * d13 + d15 * d15);
                            GL11.glColor3f(waypoint57.red, waypoint57.green, waypoint57.blue);
                            Waypoint.MARKER[waypoint57.type].bind();
                            GL11.glRotatef((this.notchDirection ? 0.0F : 90.0F) - f75, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslated(0.0D, -d80, 0.0D);
                            this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
                        }
                    } finally {
                        GL11.glPopMatrix();
                    }
                }
            }
		}

		int i70;
		if(this.renderType == 1) {
			GL11.glScaled(1.0D / (double)i1, 1.0D / (double)i1, 1.0D);
			GL11.glTranslated((double)this.scWidth * -0.5D, (double)this.scHeight * -0.5D, 0.0D);
			GL11.glScaled(i2, i2, 1.0D);
			int i58 = 0;
			int i61 = 4;
			Biome[] biomeGenBase69 = bgbList;
			i70 = bgbList.length;

			for(i12 = 0; i12 < i70; ++i12) {
				Biome biomeGenBase64 = biomeGenBase69[i12];
				i58 = Math.max(i58, this.theMinecraft.textRenderer.getWidth(biomeGenBase64.name));
				i61 += 10;
			}

			i58 += 16;
			int i65 = (this.mapPosition & 2) == 0 ? 2 : this.scWidth / i2 - 2 - i58;
			i12 = (this.mapPosition & 1) == 0 ? 2 : this.scHeight / i2 - 2 - i61;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.627451F);
			this.tessellator.startQuads();
			this.tessellator.vertex(i65, i12 + i61, 1.0D);
			this.tessellator.vertex(i65 + i58, i12 + i61, 1.0D);
			this.tessellator.vertex(i65 + i58, i12, 1.0D);
			this.tessellator.vertex(i65, i12, 1.0D);
			this.tessellator.draw();

			for(i70 = 0; i70 < bgbList.length; ++i70) {
				Biome biomeGenBase71 = bgbList[i70];
				int i74 = biomeGenBase71.grassColor;
				String string73 = biomeGenBase71.name;
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				this.theMinecraft.textRenderer.drawWithShadow(string73, i65 + 14, i12 + 3 + i70 * 10, 0xFFFFFF);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				f75 = (float)(i74 >> 16 & 255) * 0.003921569F;
				f18 = (float)(i74 >> 8 & 255) * 0.003921569F;
				f19 = (float)(i74 & 255) * 0.003921569F;
				GL11.glColor3f(f75, f18, f19);
				this.tessellator.startQuads();
				this.tessellator.vertex(i65 + 2, i12 + i70 * 10 + 12, 1.0D);
				this.tessellator.vertex(i65 + 12, i12 + i70 * 10 + 12, 1.0D);
				this.tessellator.vertex(i65 + 12, i12 + i70 * 10 + 2, 1.0D);
				this.tessellator.vertex(i65 + 2, i12 + i70 * 10 + 2, 1.0D);
				this.tessellator.draw();
			}

			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslated((double)this.scWidth * 0.5D, (double)this.scHeight * 0.5D, 0.0D);
			GL11.glScaled(i1, i1, 1.0D);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glScalef(1.0F / (float)i1, 1.0F / (float)i1, 1.0F);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		TextRenderer fontRenderer60;
		String string63;
		if(this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			fontRenderer60 = this.theMinecraft.textRenderer;
			string63 = this.getDimensionName(this.waypointDimension);
			float f67 = (float)(fontRenderer60.getWidth(string63) * i2) * 0.5F;
			GL11.glTranslatef(-f67, -32.0F, 0.0F);
			GL11.glScaled(i2, i2, 1.0D);
			fontRenderer60.drawWithShadow(string63, 0, 0, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(f67, 32.0F, 0.0F);
		}

		if(this.showCoordinate) {
			fontRenderer60 = this.theMinecraft.textRenderer;
			GL11.glTranslatef(0.0F, 16.0F, 0.0F);
			GL11.glScalef((float)i2, (float)i2, 1.0F);
			String string68;
			if(this.coordinateType == 0) {
				i12 = MathHelper.floor(this.thePlayer.x);
				i70 = MathHelper.floor(this.thePlayer.boundingBox.minY);
				int i72 = MathHelper.floor(this.thePlayer.z);
				string63 = String.format("%+d, %+d", i12, i72);
				string68 = Integer.toString(i70);
			} else {
				string63 = String.format("%+1.2f, %+1.2f", this.thePlayer.x, this.thePlayer.z);
				string68 = String.format("%1.2f (%d)", this.thePlayer.y, (int)this.thePlayer.boundingBox.minY);
			}

			fontRenderer60.drawWithShadow(string63, (int)((float)fontRenderer60.getWidth(string63) * -0.5F), 2, 0xFFFFFF);
			fontRenderer60.drawWithShadow(string68, (int)((float)fontRenderer60.getWidth(string68) * -0.5F), 11, 0xFFFFFF);
			GL11.glScaled(1.0D / (double)i2, 1.0D / (double)i2, 1.0D);
			GL11.glTranslatef(0.0F, -16.0F, 0.0F);
		}

	}

	public void setOption(EnumOption opt, EnumOptionValue val) {
		this.lock.lock();
		try {
			switch (opt) {

				case MINIMAP:
					this.enable = EnumOptionValue.bool(val);
					break;

				case RENDER_TYPE:
					this.renderType = opt.getValue(val);
					break;

				case DEATH_POINT:
					this.deathPoint = EnumOptionValue.bool(val);
					break;

				case MINIMAP_OPTION:
					this.theMinecraft.setScreen(new GuiOptionScreen(1));
					break;

				case SURFACE_MAP_OPTION:
					this.theMinecraft.setScreen(new GuiOptionScreen(2));
					break;

				case ENTITIES_RADAR_OPTION:
					this.theMinecraft.setScreen(new GuiOptionScreen(3));
					break;

				case MARKER_OPTION:
					this.theMinecraft.setScreen(new GuiOptionScreen(4));
					break;

				case ABOUT_MINIMAP:
					this.theMinecraft.setScreen(new GuiOptionScreen(5));
					break;

				case MAP_SHAPE:
					this.roundmap = (val == EnumOptionValue.ROUND);
					break;

				case TEXTURE:
					this.textureView = opt.getValue(val);
					if (textureView == 0) {
						GLTexture.setPack("/reifnsk/minimap/reitextures/");
					} else if (textureView == 1) {
						GLTexture.setPack("/reifnsk/minimap/zantextures/");
					}
					break;

				case DIRECTION_TYPE:
					this.notchDirection = true;
					break;

				case MAP_POSITION:
					this.mapPosition = opt.getValue(val);
					break;

				case MAP_SCALE:
					this.mapScale = opt.getValue(val);
					break;

				case MAP_OPACITY:
					if (val == EnumOptionValue.PERCENT25) this.mapOpacity = 0.25F;
					else if (val == EnumOptionValue.PERCENT50) this.mapOpacity = 0.5F;
					else if (val == EnumOptionValue.PERCENT75) this.mapOpacity = 0.75F;
					else this.mapOpacity = 1.0F;
					break;

				case LARGE_MAP_SCALE:
					this.largeMapScale = opt.getValue(val);
					break;

				case LARGE_MAP_OPACITY:
					if (val == EnumOptionValue.PERCENT25) this.largeMapOpacity = 0.25F;
					else if (val == EnumOptionValue.PERCENT50) this.largeMapOpacity = 0.5F;
					else if (val == EnumOptionValue.PERCENT75) this.largeMapOpacity = 0.75F;
					else this.largeMapOpacity = 1.0F;
					break;

				case LARGE_MAP_LABEL:
					this.largeMapLabel = EnumOptionValue.bool(val);
					break;

				case FILTERING:
					this.filtering = EnumOptionValue.bool(val);
					break;

				case SHOW_COORDINATES:
					this.coordinateType = opt.getValue(val);
					this.showCoordinate = (val != EnumOptionValue.DISABLE);
					break;

				case SHOW_MENU_KEY:
					this.showMenuKey = EnumOptionValue.bool(val);
					break;

				case FONT_SCALE:
					this.fontScale = opt.getValue(val);
					break;

				case DEFAULT_ZOOM:
					this.defaultZoom = opt.getValue(val);
					break;

				case MASK_TYPE:
					this.useStencil = (val == EnumOptionValue.STENCIL);
					break;

				case UPDATE_FREQUENCY:
					this.updateFrequencySetting = opt.getValue(val);
					break;

				case THREADING:
					this.threading = EnumOptionValue.bool(val);
					break;

				case THREAD_PRIORITY:
					this.threadPriority = opt.getValue(val);
					if (this.workerThread != null && this.workerThread.isAlive()) {
						this.workerThread.setPriority(3 + this.threadPriority);
					}
					break;

				case LIGHTING:
					this.lightmap = opt.getValue(val);
					break;

				case LIGHTING_TYPE:
					this.lightType = opt.getValue(val);
					break;

				case TERRAIN_UNDULATE:
					this.undulate = EnumOptionValue.bool(val);
					break;

				case TERRAIN_DEPTH:
					this.heightmap = EnumOptionValue.bool(val);
					break;

				case TRANSPARENCY:
					this.transparency = EnumOptionValue.bool(val);
					break;

				case ENVIRONMENT_COLOR:
					this.environmentColor = EnumOptionValue.bool(val);
					break;

				case OMIT_HEIGHT_CALC:
					this.omitHeightCalc = EnumOptionValue.bool(val);
					break;

				case HIDE_SNOW:
					this.hideSnow = EnumOptionValue.bool(val);
					break;

				case SHOW_CHUNK_GRID:
					this.showChunkGrid = EnumOptionValue.bool(val);
					break;

				case SHOW_SLIME_CHUNK:
					this.showSlimeChunk = EnumOptionValue.bool(val);
					break;

				case ENTITIES_RADAR:
					this.configEntitiesRadar = EnumOptionValue.bool(val);
					break;

				case ENTITY_PLAYER:
					this.configEntityPlayer = EnumOptionValue.bool(val);
					break;

				case ENTITY_ANIMAL:
					this.configEntityAnimal = EnumOptionValue.bool(val);
					break;

				case ENTITY_MOB:
					this.configEntityMob = EnumOptionValue.bool(val);
					break;

				case ENTITY_SLIME:
					this.configEntitySlime = EnumOptionValue.bool(val);
					break;

				case ENTITY_SQUID:
					this.configEntitySquid = EnumOptionValue.bool(val);
					break;

				case ENTITY_LIVING:
					this.configEntityLiving = EnumOptionValue.bool(val);
					break;

				case ENTITY_LIGHTNING:
					this.configEntityLightning = EnumOptionValue.bool(val);
					break;

				case ENTITY_DIRECTION:
					this.configEntityDirection = EnumOptionValue.bool(val);
					break;

				case MARKER:
					this.marker = EnumOptionValue.bool(val);
					break;

				case MARKER_ICON:
					this.markerIcon = EnumOptionValue.bool(val);
					break;

				case MARKER_LABEL:
					this.markerLabel = EnumOptionValue.bool(val);
					break;

				case MARKER_DISTANCE:
					this.markerDistance = EnumOptionValue.bool(val);
					break;

				case ENG_FORUM:
					try { Desktop.getDesktop().browse(new URI("http://www.minecraftforum.net/index.php?showtopic=482147")); }
					catch (Exception e) { error("Open Forum(en)", e); }
					break;

				case JP_FORUM:
					try { Desktop.getDesktop().browse(new URI("http://forum.minecraftuser.jp/viewtopic.php?f=13&t=153")); }
					catch (Exception e) { error("Open Forum(jp)", e); }
					break;
			}

			this.forceUpdate = true;
			this.stripCounter.reset();
			if (this.threading) {
				this.mapCalc(false);
				if (this.isCompleteImage) this.texture.register();
			}

		} finally {
			this.lock.unlock();
		}
	}

	public EnumOptionValue getOption(EnumOption opt) {
		switch (opt) {

			case MINIMAP:
				return EnumOptionValue.bool(this.enable);

			case RENDER_TYPE:
				return opt.getValue(this.renderType);

			case DEATH_POINT:
				return EnumOptionValue.bool(this.deathPoint);

			case MAP_SHAPE:
				return this.roundmap ? EnumOptionValue.ROUND : EnumOptionValue.SQUARE;

			case TEXTURE:
				return opt.getValue(this.textureView);

			case DIRECTION_TYPE:
				return this.notchDirection ? EnumOptionValue.NORTH : EnumOptionValue.EAST;

			case MAP_POSITION:
				return opt.getValue(this.mapPosition);

			case MAP_SCALE:
				return opt.getValue(this.mapScale);

			case MAP_OPACITY:
				if (this.mapOpacity == 0.25F) return EnumOptionValue.PERCENT25;
				if (this.mapOpacity == 0.5F) return EnumOptionValue.PERCENT50;
				if (this.mapOpacity == 0.75F) return EnumOptionValue.PERCENT75;
				return EnumOptionValue.PERCENT100;

			case LARGE_MAP_SCALE:
				return opt.getValue(this.largeMapScale);

			case LARGE_MAP_OPACITY:
				if (this.largeMapOpacity == 0.25F) return EnumOptionValue.PERCENT25;
				if (this.largeMapOpacity == 0.5F) return EnumOptionValue.PERCENT50;
				if (this.largeMapOpacity == 0.75F) return EnumOptionValue.PERCENT75;
				return EnumOptionValue.PERCENT100;

			case LARGE_MAP_LABEL:
				return EnumOptionValue.bool(this.largeMapLabel);

			case FILTERING:
				return EnumOptionValue.bool(this.filtering);

			case SHOW_COORDINATES:
				return opt.getValue(this.coordinateType);

			case SHOW_MENU_KEY:
				return EnumOptionValue.bool(this.showMenuKey);

			case FONT_SCALE:
				return opt.getValue(this.fontScale);

			case DEFAULT_ZOOM:
				return opt.getValue(this.defaultZoom);

			case MASK_TYPE:
				return this.useStencil ? EnumOptionValue.STENCIL : EnumOptionValue.DEPTH;

			case UPDATE_FREQUENCY:
				return opt.getValue(this.updateFrequencySetting);

			case THREADING:
				return EnumOptionValue.bool(this.threading);

			case THREAD_PRIORITY:
				return opt.getValue(this.threadPriority);

			case LIGHTING:
				return opt.getValue(this.lightmap);

			case LIGHTING_TYPE:
				return opt.getValue(this.lightType);

			case TERRAIN_UNDULATE:
				return EnumOptionValue.bool(this.undulate);

			case TERRAIN_DEPTH:
				return EnumOptionValue.bool(this.heightmap);

			case TRANSPARENCY:
				return EnumOptionValue.bool(this.transparency);

			case ENVIRONMENT_COLOR:
				return EnumOptionValue.bool(this.environmentColor);

			case OMIT_HEIGHT_CALC:
				return EnumOptionValue.bool(this.omitHeightCalc);

			case HIDE_SNOW:
				return EnumOptionValue.bool(this.hideSnow);

			case SHOW_CHUNK_GRID:
				return EnumOptionValue.bool(this.showChunkGrid);

			case SHOW_SLIME_CHUNK:
				return EnumOptionValue.bool(this.showSlimeChunk);

			case ENTITIES_RADAR:
				return EnumOptionValue.bool(this.configEntitiesRadar);

			case ENTITY_PLAYER:
				return EnumOptionValue.bool(this.configEntityPlayer);

			case ENTITY_ANIMAL:
				return EnumOptionValue.bool(this.configEntityAnimal);

			case ENTITY_MOB:
				return EnumOptionValue.bool(this.configEntityMob);

			case ENTITY_SLIME:
				return EnumOptionValue.bool(this.configEntitySlime);

			case ENTITY_SQUID:
				return EnumOptionValue.bool(this.configEntitySquid);

			case ENTITY_LIVING:
				return EnumOptionValue.bool(this.configEntityLiving);

			case ENTITY_LIGHTNING:
				return EnumOptionValue.bool(this.configEntityLightning);

			case ENTITY_DIRECTION:
				return EnumOptionValue.bool(this.configEntityDirection);

			case MARKER:
				return EnumOptionValue.bool(this.marker);

			case MARKER_ICON:
				return EnumOptionValue.bool(this.markerIcon);

			case MARKER_LABEL:
				return EnumOptionValue.bool(this.markerLabel);

			case MARKER_DISTANCE:
				return EnumOptionValue.bool(this.markerDistance);

			default:
				return opt.getValue(0);
		}
	}

	public void saveOptions() {
		File file1 = new File(directory, "option.txt");

		try {
			PrintWriter printWriter2 = new PrintWriter(file1, StandardCharsets.UTF_8);
			EnumOption[] enumOption6;
			int i5 = (enumOption6 = EnumOption.values()).length;

			for(int i4 = 0; i4 < i5; ++i4) {
				EnumOption enumOption3 = enumOption6[i4];
				if(enumOption3 != EnumOption.DIRECTION_TYPE && this.getOption(enumOption3) != EnumOptionValue.SUB_OPTION && this.getOption(enumOption3) != EnumOptionValue.VERSION && this.getOption(enumOption3) != EnumOptionValue.AUTHOR) {
					printWriter2.printf("%s: %s%n", capitalize(enumOption3.toString()), capitalize(this.getOption(enumOption3).toString()));
				}
			}

			printWriter2.flush();
			printWriter2.close();
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	private void loadOptions() {
		File file1 = new File(directory, "option.txt");
		if(file1.exists()) {
			boolean z2 = false;

			try {
				Scanner scanner3 = new Scanner(file1, StandardCharsets.UTF_8);

				while(scanner3.hasNextLine()) {
					try {
						String[] string4 = scanner3.nextLine().split(":");
						this.setOption(EnumOption.valueOf(toUpperCase(string4[0].trim())), EnumOptionValue.valueOf(toUpperCase(string4[1].trim())));
					} catch (Exception exception5) {
						System.err.println(exception5.getMessage());
						z2 = true;
					}
				}

				scanner3.close();
			} catch (Exception exception6) {
				exception6.printStackTrace();
			}

			if(z2) {
				this.saveOptions();
			}

			this.flagZoom = this.defaultZoom;
		}
	}

	public List<Waypoint> getWaypoints() {
		return this.wayPts;
	}

	public void saveWaypoints() {
		File file1 = new File(directory, this.currentLevelName + ".DIM" + this.waypointDimension + ".points");
		if(file1.isDirectory()) {
			this.chatInfo("§E[Rei's Minimap] Error Saving Waypoints");
			error("[Rei's Minimap] Error Saving Waypoints: (" + file1 + ") is directory.");
		} else {
			try {
				PrintWriter printWriter2 = new PrintWriter(file1, StandardCharsets.UTF_8);

                for (Waypoint waypoint3 : this.wayPts) {
                    printWriter2.println(waypoint3);
                }

				printWriter2.flush();
				printWriter2.close();
			} catch (Exception exception5) {
				this.chatInfo("§E[Rei's Minimap] Error Saving Waypoints");
				error("Error Saving Waypoints", exception5);
			}

		}
	}

	public void loadWaypoints() {
		this.wayPts = null;
		this.wayPtsMap.clear();
		Pattern pattern1 = Pattern.compile(Pattern.quote(this.currentLevelName) + "\\.DIM(-?[0-9])\\.points");
		int i2 = 0;
		String[] string6 = directory.list();
		int i5 = string6 == null ? 0 : string6.length;

		for(int i4 = 0; i4 < i5; ++i4) {
			String string3 = string6[i4];
			Matcher matcher7 = pattern1.matcher(string3);
			if(matcher7.matches()) {
				int i8 = Integer.parseInt(matcher7.group(1));
				ArrayList<Waypoint> arrayList9 = new ArrayList<>();

                try (Scanner scanner10 = new Scanner(new File(directory, string3), StandardCharsets.UTF_8)) {
                    while (scanner10.hasNextLine()) {
                        Waypoint waypoint11 = Waypoint.load(scanner10.nextLine());
                        if (waypoint11 != null) {
                            arrayList9.add(waypoint11);
                            ++i2;
                        }
                    }
                } catch (Exception ignored) {
                }

				this.wayPtsMap.put(i8, arrayList9);
				if(i8 == this.currentDimension) {
					this.wayPts = arrayList9;
				}
			}
		}

		if(this.wayPts == null) {
			this.wayPts = new ArrayList<>();
		}

		if(i2 != 0) {
			this.chatInfo("§E[Rei's Minimap] " + i2 + " Waypoints loaded for " + this.currentLevelName);
		}

	}

	private void chatInfo(String string1) {
		this.ingameGUI.addChatMessage(string1);
	}

	private float[] generateLightBrightnessTable(float f1) {
		float[] f2 = new float[16];

		for(int i3 = 0; i3 <= 15; ++i3) {
			float f4 = 1.0F - (float)i3 / 15.0F;
			f2[i3] = (1.0F - f4) / (f4 * 3.0F + 1.0F) * (1.0F - f1) + f1;
		}

		return f2;
	}

	private int calculateSkylightSubtracted(long j1, float f3) {
		return this.theWorld.getAmbientDarkness(f3);
	}

	private void updateLightmap(long j1, float f3) {
		float f4 = this.func_35464_b(j1, f3);

		for(int i5 = 0; i5 < 256; ++i5) {
			float f6 = f4 * 0.95F + 0.05F;
			float f7 = this.theWorld.dimension.lightLevelToLuminance[i5 / 16] * f6;
			float f8 = this.theWorld.dimension.lightLevelToLuminance[i5 % 16] * 1.55F;
			float f9 = f7 * (f4 * 0.65F + 0.35F);
			float f10 = f7 * (f4 * 0.65F + 0.35F);
			float f13 = f8 * ((f8 * 0.6F + 0.4F) * 0.6F + 0.4F);
			float f14 = f8 * (f8 * f8 * 0.6F + 0.4F);
			float f15 = f9 + f8;
			float f16 = f10 + f13;
			float f17 = f7 + f14;
			f15 = Math.min(1.0F, f15 * 0.96F + 0.03F);
			f16 = Math.min(1.0F, f16 * 0.96F + 0.03F);
			f17 = Math.min(1.0F, f17 * 0.96F + 0.03F);
			float f18 = 0.0F;
			float f19 = 1.0F - f15;
			float f20 = 1.0F - f16;
			float f21 = 1.0F - f17;
			f19 = 1.0F - f19 * f19 * f19 * f19;
			f20 = 1.0F - f20 * f20 * f20 * f20;
			f21 = 1.0F - f21 * f21 * f21 * f21;
			f15 = f15 * (1.0F - f18) + f19 * f18;
			f16 = f16 * (1.0F - f18) + f20 * f18;
			f17 = f17 * (1.0F - f18) + f21 * f18;
			this.lightmapRed[i5] = Math.max(0.0F, Math.min(1.0F, f15 * 0.96F + 0.03F));
			this.lightmapGreen[i5] = Math.max(0.0F, Math.min(1.0F, f16 * 0.96F + 0.03F));
			this.lightmapBlue[i5] = Math.max(0.0F, Math.min(1.0F, f17 * 0.96F + 0.03F));
		}

	}

	private float func_35464_b(long j1, float f3) {
		float f4 = this.calculateCelestialAngle(j1) + f3;
		float f5 = Math.max(0.0F, Math.min(1.0F, 1.0F - (MathHelper.cos(f4 * 3.141593F * 2.0F) * 2.0F + 0.2F)));
		f5 = 1.0F - f5;
		f5 *= 1.0F - this.theWorld.getRainGradient(1.0F) * 5.0F * 0.0625F;
		f5 *= 1.0F - this.theWorld.getThunderGradient(1.0F) * 5.0F * 0.0625F;
		return f5 * 0.8F + 0.2F;
	}

	private float calculateCelestialAngle(long j1) {
		int i3 = (int)(j1 % 24000L);
		float f4 = (float)(i3 + 1) * 4.1666666E-5F - 0.25F;
		if(f4 < 0.0F) {
			++f4;
		} else if(f4 > 1.0F) {
			--f4;
		}

		float f5 = f4;
		f4 = 1.0F - (float)((Math.cos((double)f4 * Math.PI) + 1.0D) * 0.5D);
		f4 = f5 + (f4 - f5) * 0.33333334F;
		return f4;
	}

	private Chunk getChunk(World world1, int i2, int i3) {
		boolean z4 = Math.abs(this.chunkCoordX - i2) <= 8 && Math.abs(this.chunkCoordZ - i3) <= 8;
		return z4 ? this.chunkCache.get(world1, i2, i3) : new EmptyChunk(world1, i2, i3);
	}

	private void drawCenteringRectangle(double d1, double d3, double d5, double d7, double d9) {
		d7 *= 0.5D;
		d9 *= 0.5D;
		this.startDrawingQuads();
		this.addVertexWithUV(d1 - d7, d3 + d9, d5, 0.0D, 1.0D);
		this.addVertexWithUV(d1 + d7, d3 + d9, d5, 1.0D, 1.0D);
		this.addVertexWithUV(d1 + d7, d3 - d9, d5, 1.0D, 0.0D);
		this.addVertexWithUV(d1 - d7, d3 - d9, d5, 0.0D, 0.0D);
		this.draw();
	}

	public static String capitalize(String string0) {
		if(string0 == null) {
			return null;
		} else {
			boolean z1 = true;
			char[] c2 = string0.toCharArray();
			int i3 = 0;

			for(int i4 = c2.length; i3 < i4; ++i3) {
				char c5 = c2[i3];
				if(c5 == 95) {
					c5 = 32;
				}

				c2[i3] = z1 ? Character.toTitleCase(c5) : Character.toLowerCase(c5);
				z1 = Character.isWhitespace(c5);
			}

			return new String(c2);
		}
	}

	public static String toUpperCase(String string0) {
		return string0 == null ? null : string0.replace(' ', '_').toUpperCase(Locale.ENGLISH);
	}

	private static boolean validScreen(Screen screen) {
		return screen == null || screen instanceof GuiScreenInterface || screen instanceof ChatScreen || screen instanceof DeathScreen;
	}

	public String getDimensionName(int i1) {
		String string2 = this.dimensionName.get(i1);
		return string2 == null ? "DIM:" + i1 : string2;
	}

	public int getWaypointDimension() {
		return this.waypointDimension;
	}

	public int getCurrentDimension() {
		return this.currentDimension;
	}

	private double getDimensionScale(int i1) {
		Double double2 = this.dimensionScale.get(i1);
		return double2 == null ? 1.0D : double2;
	}

	public double getVisibleDimensionScale() {
		return this.getDimensionScale(this.waypointDimension) / this.getDimensionScale(this.currentDimension);
	}

	public void prevDimension() {
		Entry<Integer, List<Waypoint>> entry = this.wayPtsMap.lowerEntry(this.waypointDimension);
		if(entry == null) {
			entry = this.wayPtsMap.lowerEntry(Integer.MAX_VALUE);
		}

		if(entry != null) {
			this.waypointDimension = entry.getKey();
			this.wayPts = entry.getValue();
		}

	}

	public void nextDimension() {
		Entry<Integer, List<Waypoint>> entry = this.wayPtsMap.higherEntry(this.waypointDimension);
		if(entry == null) {
			entry = this.wayPtsMap.higherEntry(Integer.MIN_VALUE);
		}

		if(entry != null) {
			this.waypointDimension = entry.getKey();
			this.wayPts = entry.getValue();
		}

	}

	private static void error(String string0, Exception exception1) {
		File file2 = new File(directory, "error.txt");
		PrintWriter printWriter3 = null;

		try {
			FileOutputStream fileOutputStream4 = new FileOutputStream(file2, true);
			printWriter3 = new PrintWriter(new OutputStreamWriter(fileOutputStream4, StandardCharsets.UTF_8));
			information(printWriter3);
			printWriter3.println(string0);
			exception1.printStackTrace(printWriter3);
			printWriter3.println();
			printWriter3.flush();
		} catch (Exception ignored) {
		} finally {
			if(printWriter3 != null) {
				printWriter3.close();
			}

		}

	}

	private static void error(String string0) {
		File file1 = new File(directory, "error.txt");
		PrintWriter printWriter2 = null;

		try {
			FileOutputStream fileOutputStream3 = new FileOutputStream(file1, true);
			printWriter2 = new PrintWriter(new OutputStreamWriter(fileOutputStream3, StandardCharsets.UTF_8));
			information(printWriter2);
			printWriter2.println(string0);
			printWriter2.println();
			printWriter2.flush();
		} catch (Exception ignored) {
		} finally {
			if(printWriter2 != null) {
				printWriter2.close();
			}

		}

	}

	private static void information(PrintWriter printWriter0) {
		printWriter0.printf("--- %1$tF %1$tT %1$tZ ---%n", System.currentTimeMillis());
		printWriter0.printf("Rei's Minimap %s [%s]%n", "v3.0_01", "1.1");
		printWriter0.printf("OS: %s (%s) version %s%n", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
		printWriter0.printf("Java: %s, %s%n", System.getProperty("java.version"), System.getProperty("java.vendor"));
		printWriter0.printf("VM: %s (%s), %s%n", System.getProperty("java.vm.name"), System.getProperty("java.vm.info"), System.getProperty("java.vm.vendor"));
		printWriter0.printf("LWJGL: %s%n", Sys.getVersion());
		printWriter0.printf("OpenGL: %s version %s, %s%n", GL11.glGetString(GL11.GL_RENDERER), GL11.glGetString(GL11.GL_VERSION), GL11.glGetString(GL11.GL_VENDOR));
	}

	public boolean isMinecraftThread() {
		return Thread.currentThread() == this.mcThread;
	}

	public int getWorldHeight() {
		return theWorld.getHeight();
	}

	private int[] getColor(String string1) {
		InputStream inputStream2 = null;
		int[] i3;

		label73: {
			int[] i6;
			try {
				inputStream2 = this.texturePack.getResource(string1);
				if(inputStream2 == null) {
					break label73;
				}

				BufferedImage bufferedImage4 = ImageIO.read(inputStream2);
				if(bufferedImage4.getWidth() != 256) {
					break label73;
				}

				i3 = new int[256 * bufferedImage4.getHeight()];
				bufferedImage4.getRGB(0, 0, 256, bufferedImage4.getHeight(), i3, 0, 256);
				i6 = i3;
			} catch (IOException iOException9) {
				break label73;
			} finally {
				close(inputStream2);
			}

			return i6;
		}

		i3 = new int[256];

		for(int i11 = 0; i11 < 256; ++i11) {
			i3[i11] = 0xFF000000 | i11 << 16 | i11 << 8 | i11;
		}

		return i3;
	}

	private static void close(InputStream inputStream0) {
		if(inputStream0 != null) {
			try {
				inputStream0.close();
			} catch (IOException iOException2) {
				iOException2.printStackTrace();
			}
		}

	}

	private int getEntityColor(Entity entity1) {
		return entity1 == this.thePlayer ? 0 : (entity1 instanceof PlayerEntity ? (this.visibleEntityPlayer ? -16711681 : 0) : (entity1 instanceof SquidEntity ? (this.visibleEntitySquid ? -16760704 : 0) : (entity1 instanceof AnimalEntity ? (this.visibleEntityAnimal ? -1 : 0) : (entity1 instanceof SlimeEntity ? (this.visibleEntitySlime ? -10444704 : 0) : (!(entity1 instanceof MonsterEntity) && !(entity1 instanceof GhastEntity) ? (entity1 instanceof LivingEntity ? (this.visibleEntityLiving ? -12533632 : 0) : 0) : (this.visibleEntityMob ? -65536 : 0))))));
	}

	public boolean getMarker() {
		return this.marker & (this.markerIcon | this.markerLabel | this.markerDistance);
	}

	public boolean getMarkerIcon() {
		return this.markerIcon;
	}

	public boolean getMarkerLabel() {
		return this.markerLabel;
	}

	public boolean getMarkerDistance() {
		return this.markerDistance;
	}

	public SocketAddress getServerSocketAdress() {
		return ((MultiplayerClientPlayerEntity)this.thePlayer).networkHandler.connection.address;
	}


}
