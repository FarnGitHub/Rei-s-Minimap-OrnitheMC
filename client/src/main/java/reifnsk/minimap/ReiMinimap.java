package reifnsk.minimap;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

import net.minecraft.client.entity.living.player.LocalPlayerEntity;
import net.minecraft.client.gui.ChatMessage;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.mob.SlimeEntity;
import net.minecraft.entity.living.mob.hostile.HostileEntity;
import net.minecraft.entity.living.mob.passive.animal.AnimalEntity;
import net.minecraft.entity.living.mob.water.SquidEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import reifnsk.minimap.mixin.ClientNetworkGetter;
import reifnsk.minimap.mixin.ConnectionGetter;
import reifnsk.minimap.mixin.GameGuiGetter;
import reifnsk.minimap.mixin.PlayerClientMPGetter;

public class ReiMinimap implements Runnable {
	private static final String MC_B173 = "Beta 1.7.3";
	private static final String MC_B166 = "Beta 1.6.6";
	private static final String MC_B151 = "Beta 1.5_01";
	public static final String MOD_VERSION = "v1.8";
	public static final String MC_VERSION = "Beta 1.7.3";
	public static final String version = String.format("%s [%s]", new Object[]{"v1.8", "Beta 1.7.3"});
	public boolean useModloader;
	private static final double renderZ = 1.0D;
	private static final boolean noiseAdded = false;
	private static final float noiseAlpha = 0.1F;
	public static final File directory = new File(Minecraft.getRunDirectory(), "mods" + File.separatorChar + "rei_minimap");
	private float[] lightBrightnessTable = this.generateLightBrightnessTable(0.125F);
	private static final int[] updateFrequencys = new int[]{2, 5, 10, 20, 40};
	public static final ReiMinimap instance = new ReiMinimap();
	private static final int TEXTURE_SIZE = 256;
	private Minecraft theMinecraft;
	private BufferBuilder tessellator =BufferBuilder.INSTANCE;
	private World theWorld;
	private PlayerEntity thePlayer;
	private GameGui ingameGUI;
	private Window scaledResolution;
	private String errorString;
	private boolean multiplayer;
	private SocketAddress currentServer;
	private String currentLevelName;
	private int currentDimension;
	private int scWidth;
	private int scHeight;
	private GLTextureBufferedImage texture = GLTextureBufferedImage.create(256, 256);
	private ChunkCache chunkCache = new ChunkCache(6);
	private Thread thread;
	private Lock lock = new ReentrantLock();
	private Condition condition = this.lock.newCondition();
	private StripCounter stripCounter = new StripCounter(289);
	private int stripCountMax1 = 0;
	private int stripCountMax2 = 0;
	private Screen guiScreen;
	private int posX;
	private int posY;
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
	private boolean mapTransparency = false;
	private int lightmap = 0;
	private boolean undulate = true;
	private boolean transparency = true;
	private boolean environmentColor = true;
	private boolean omitHeightCalc = true;
	private int updateFrequencySetting = 2;
	private boolean threading = false;
	private int threadPriority = 1;
	private boolean hideSnow = false;
	private boolean showSlimeChunk = false;
	private boolean heightmap = true;
	private boolean showCoordinate = true;
	private boolean visibleWaypoints = true;
	private boolean deathPoint = false;
	private boolean roundmap = false;
	private boolean fullmap = false;
	private boolean forceUpdate;
	private long currentTime;
	private long previousTime;
	private int renderType = 0;
	private static final byte[] EMPTY_CHUNK_BYTES = new byte[32768];
	private static final EmptyChunk EMPTY_CHUNK = new EmptyChunk((World)null, EMPTY_CHUNK_BYTES, 0, 0);
	private HashMap wayPtsMap = new HashMap();
	private ArrayList wayPts = new ArrayList();
	private int waypointDimension;
	private static final double[] ZOOM_LIST = new double[]{0.5D, 1.0D, 1.5D, 2.0D, 4.0D, 8.0D};
	private int defaultZoom = 1;
	private int flagZoom = 1;
	private double targetZoom = 1.0D;
	private double currentZoom = 1.0D;
	private float zoomVisible;
	private boolean chatWelcomed;
	private List chatLineList;
	private ChatMessage chatLineLast;
	private long chatTime = 0L;
	private boolean configEntitiesRadar = false;
	private boolean configEntityPlayer = true;
	private boolean configEntityAnimal = true;
	private boolean configEntityMob = true;
	private boolean configEntitySquid = true;
	private boolean configEntitySlime = true;
	private boolean configEntityDirection = false;
	private boolean allowCavemap;
	private boolean allowEntitiesRadar;
	private boolean allowEntityPlayer;
	private boolean allowEntityAnimal;
	private boolean allowEntityMob;
	private boolean allowEntitySquid;
	private boolean allowEntitySlime;
	private boolean visibleEntitiesRadar;
	private boolean visibleEntityPlayer;
	private boolean visibleEntityAnimal;
	private boolean visibleEntityMob;
	private boolean visibleEntitySquid;
	private boolean visibleEntitySlime;
	static float[] temp = new float[10];
	private static final Map obfascatorFieldMap;

	boolean getAllowCavemap() {
		return this.allowCavemap;
	}

	boolean getAllowEntitiesRadar() {
		return this.allowEntitiesRadar;
	}

	private ReiMinimap() {
		if(!directory.exists()) {
			directory.mkdirs();
		}

		if(!directory.isDirectory()) {
			throw new Error();
		} else {
			this.loadOptions();
		}
	}

	public void onTickInGame(Minecraft mc) {
		GL11.glPushAttrib(1048575);
		GL11.glPushClientAttrib(-1);

		try {
			if(mc == null) {
				return;
			}

			if(this.errorString != null) {
				this.scaledResolution = new Window(mc.options, mc.width, mc.height);
				mc.textRenderer.drawWithShadow(this.errorString, this.scaledResolution.getWidth() - mc.textRenderer.getWidth(this.errorString) - 2, 2, -65536);
				return;
			}

			if(this.theMinecraft == null) {
				this.theMinecraft = mc;
				this.ingameGUI = this.theMinecraft.gui;
				this.chatLineList = ((GameGuiGetter)ingameGUI).getChatMessage();
				this.chatLineList = (List)(this.chatLineList == null ? new ArrayList() : this.chatLineList);
			}

			this.thePlayer = this.theMinecraft.player;
			int x;
			int x1;
			int scale;
			int i49;
			if(this.theWorld != this.theMinecraft.world) {
				this.theWorld = this.theMinecraft.world;
				this.multiplayer = this.thePlayer instanceof LocalPlayerEntity;
				if(this.theWorld != null) {
					Environment.setWorldSeed(this.theWorld.getSeed());
					boolean me;
					String displayHeight;
					if(this.multiplayer) {
						displayHeight = null;
						SocketAddress socketAddress39 = ((ConnectionGetter)((ClientNetworkGetter)(((PlayerClientMPGetter)this.thePlayer).getNetworkHandler())).getConnection()).getSocketAdress();
						if(socketAddress39 == null) {
							this.errorString = "[Rei\'s Minimap] ERROR: SMP ADDRESS ACQUISITION FAILURE";
							throw new MinimapException(this.errorString);
						}

						me = this.currentServer != socketAddress39;
						if(me) {
							Matcher matcher42 = Pattern.compile("(.*)/(.*):([0-9]+)").matcher(socketAddress39.toString());
							if(matcher42.matches()) {
								displayHeight = matcher42.group(1);
								if(displayHeight.isEmpty()) {
									displayHeight = matcher42.group(2);
								}

								if(!matcher42.group(3).equals("25565")) {
									displayHeight = displayHeight + "[" + matcher42.group(3) + "]";
								}
							}

							char[] c47 = SharedConstants.INVALID_FILE_CHARS;
							i49 = c47.length;

							for(scale = 0; scale < i49; ++scale) {
								char temp = c47[scale];
								displayHeight = displayHeight.replace(temp, '_');
							}

							this.currentLevelName = displayHeight;
							this.currentServer = socketAddress39;
						}
					} else {
						displayHeight = this.theWorld.getData().getName();
						if(displayHeight == null) {
							this.errorString = "[Rei\'s Minimap] ERROR: WORLD_NAME ACQUISITION FAILURE";
							throw new MinimapException(this.errorString);
						}

						char[] elapseTime = SharedConstants.INVALID_FILE_CHARS;
						x = elapseTime.length;

						for(x1 = 0; x1 < x; ++x1) {
							char y = elapseTime[x1];
							displayHeight = displayHeight.replace(y, '_');
						}

						me = !displayHeight.equals(this.currentLevelName) || this.currentServer != null;
						if(me) {
							this.currentLevelName = displayHeight;
							me = true;
						}

						this.currentServer = null;
					}

					Integer integer45 = this.thePlayer.dimensionId;
					if(integer45 == null) {
						this.errorString = "[Rei\'s Minimap] ERROR: DIMENSION ACQUISITION FAILURE";
						throw new MinimapException(this.errorString);
					}

					this.currentDimension = integer45.intValue();
					this.waypointDimension = this.currentDimension;
					if(me) {
						this.chatTime = System.currentTimeMillis();
						this.chatWelcomed = !this.multiplayer;
						this.allowCavemap = !this.multiplayer;
						this.allowEntitiesRadar = !this.multiplayer;
						this.allowEntityPlayer = !this.multiplayer;
						this.allowEntityAnimal = !this.multiplayer;
						this.allowEntityMob = !this.multiplayer;
						this.allowEntitySlime = !this.multiplayer;
						this.allowEntitySquid = !this.multiplayer;
						this.loadWaypoints();
					}

					this.wayPts = (ArrayList)this.wayPtsMap.get(this.waypointDimension);
					if(this.wayPts == null) {
						this.wayPts = new ArrayList();
						this.wayPtsMap.put(this.waypointDimension, this.wayPts);
					}
				}

				this.stripCounter.reset();
			}

			if(!this.chatWelcomed && System.currentTimeMillis() < this.chatTime + 10000L) {
				Iterator iterator36 = this.chatLineList.iterator();

				while(iterator36.hasNext()) {
					ChatMessage chatLine46 = (ChatMessage) iterator36.next();
					if(chatLine46 == null || this.chatLineLast == chatLine46) {
						break;
					}

					Matcher matcher40 = Pattern.compile("\u00a70\u00a70((?:\u00a7[1-9a-d])+)\u00a7e\u00a7f").matcher(chatLine46.text);

					while(matcher40.find()) {
						this.chatWelcomed = true;
						char[] c44 = matcher40.group(1).toCharArray();
						x1 = c44.length;

						for(i49 = 0; i49 < x1; ++i49) {
							char c51 = c44[i49];
							switch(c51) {
							case '1':
								this.allowCavemap = true;
								break;
							case '2':
								this.allowEntityPlayer = true;
								break;
							case '3':
								this.allowEntityAnimal = true;
								break;
							case '4':
								this.allowEntityMob = true;
								break;
							case '5':
								this.allowEntitySlime = true;
								break;
							case '6':
								this.allowEntitySquid = true;
							}
						}
					}
				}

				this.chatLineLast = this.chatLineList.isEmpty() ? null : (ChatMessage) this.chatLineList.get(0);
				if(this.chatWelcomed) {
					this.allowEntitiesRadar = this.allowEntityPlayer || this.allowEntityAnimal || this.allowEntityMob || this.allowEntitySlime || this.allowEntitySquid;
					if(this.allowCavemap) {
						this.chatInfo("\u00a7E[Rei\'s Minimap] enabled: cavemapping.");
					}

					if(this.allowEntitiesRadar) {
						StringBuilder stringBuilder37 = new StringBuilder("\u00a7E[Rei\'s Minimap] enabled: entities radar (");
						if(this.allowEntityPlayer) {
							stringBuilder37.append("Player, ");
						}

						if(this.allowEntityAnimal) {
							stringBuilder37.append("Animal, ");
						}

						if(this.allowEntityMob) {
							stringBuilder37.append("Mob, ");
						}

						if(this.allowEntitySlime) {
							stringBuilder37.append("Slime, ");
						}

						if(this.allowEntitySquid) {
							stringBuilder37.append("Squid, ");
						}

						stringBuilder37.setLength(stringBuilder37.length() - 2);
						stringBuilder37.append(")");
						this.chatInfo(stringBuilder37.toString());
					}
				}
			} else {
				this.chatWelcomed = true;
			}

			this.visibleEntitiesRadar = this.allowEntitiesRadar && this.configEntitiesRadar;
			boolean z10001;
			if(this.allowEntityPlayer && this.configEntityPlayer) {
				z10001 = true;
			} else {
				z10001 = false;
			}

			this.visibleEntityPlayer = z10001;
			this.visibleEntityAnimal = this.allowEntityAnimal && this.configEntityAnimal;
			this.visibleEntityMob = this.allowEntityMob && this.configEntityMob;
			this.visibleEntitySlime = this.allowEntitySlime && this.configEntitySlime;
			this.visibleEntitySquid = this.allowEntitySquid && this.configEntitySquid;
			int i38 = this.theMinecraft.width;
			int i48 = this.theMinecraft.height;
			this.scaledResolution = new Window(this.theMinecraft.options, i38, i48);
			this.scWidth = this.scaledResolution.getWidth();
			this.scHeight = this.scaledResolution.getHeight();
			KeyInput.update();
			if(mc.screen == null) {
				if(!this.fullmap) {
					if(KeyInput.TOGGLE_ZOOM.isKeyPush()) {
						if(Keyboard.isKeyDown(this.theMinecraft.options.sneakKey.keyCode)) {
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
				}

				if(KeyInput.TOGGLE_ENABLE.isKeyPush()) {
					this.enable = !this.enable;
					this.stripCounter.reset();
					this.forceUpdate = true;
				}

				if(this.allowCavemap && KeyInput.TOGGLE_CAVE_MAP.isKeyPush()) {
					this.renderType = (this.renderType + 1) % 2;
					this.stripCounter.reset();
					this.forceUpdate = true;
				}

				if(KeyInput.TOGGLE_WAYPOINTS.isKeyPush()) {
					this.visibleWaypoints = !this.visibleWaypoints;
				}

				if(KeyInput.TOGGLE_LARGE_MAP.isKeyPush()) {
					this.fullmap = !this.fullmap;
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

				if(this.allowEntitiesRadar && KeyInput.TOGGLE_ENTITIES_RADAR.isKeyPush()) {
					this.configEntitiesRadar = !this.configEntitiesRadar;
				}

				if(KeyInput.SET_WAYPOINT.isKeyPushUp()) {
					mc.openScreen(new GuiWaypointEditorScreen(mc, (Waypoint)null));
				}

				if(KeyInput.MENU_KEY.isKeyPush()) {
					mc.openScreen(new GuiOptionScreen());
				}
			} else if(this.fullmap) {
				this.fullmap = false;
				this.forceUpdate = true;
				this.stripCounter.reset();
			}

			float scalef;
			if(this.deathPoint && this.theMinecraft.screen instanceof DeathScreen && !(this.guiScreen instanceof DeathScreen)) {
				String string41 = "Death Point";
				x = MathHelper.floor(this.thePlayer.x);
				x1 = MathHelper.floor(this.thePlayer.y);
				i49 = MathHelper.floor(this.thePlayer.z);
				Random random52 = new Random();
				float f53 = random52.nextFloat();
				scalef = random52.nextFloat();
				float alpha = random52.nextFloat();
				boolean str = false;
				Iterator width = this.wayPts.iterator();

				while(true) {
					if(width.hasNext()) {
						Waypoint _x = (Waypoint)width.next();
						if(_x.type != 1 || _x.x != x || _x.y != x1 || _x.z != i49 || !_x.enable) {
							continue;
						}

						str = true;
					}

					if(!str) {
						this.wayPts.add(new Waypoint(string41, x, x1, i49, true, f53, scalef, alpha, 1));
						this.saveWaypoints();
					}
					break;
				}
			}

			this.guiScreen = this.theMinecraft.screen;
			if(!this.enable || !checkGuiScreen(mc.screen)) {
				return;
			}

			if(this.threading) {
				if(this.thread == null || !this.thread.isAlive()) {
					this.thread = new Thread(this);
					this.thread.setPriority(3 + this.threadPriority);
					this.thread.setDaemon(true);
					this.thread.start();
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
			double d43 = (double)(this.currentTime - this.previousTime) * 1.0E-9D;
			this.zoomVisible = (float)((double)this.zoomVisible - d43);
			if(this.currentZoom != this.targetZoom) {
				double d50 = Math.max(0.0D, Math.min(1.0D, d43 * 4.0D));
				this.currentZoom += (this.targetZoom - this.currentZoom) * d50;
				if(Math.abs(this.currentZoom - this.targetZoom) < 5.0E-4D) {
					this.currentZoom = this.targetZoom;
				}

				this.zoomVisible = 3.0F;
			}

			this.previousTime = this.currentTime;
			if(this.texture.getId() != 0) {
				switch(this.mapPosition) {
				case 0:
					x1 = 37;
					i49 = 37;
					break;
				case 1:
					x1 = 37;
					i49 = this.scHeight - 37;
					scale = this.scaledResolution.scale + 1 >> 1;
					i49 -= scale * ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) / this.scaledResolution.scale;
					break;
				case 2:
				default:
					x1 = this.scWidth - 37;
					i49 = 37;
					break;
				case 3:
					x1 = this.scWidth - 37;
					i49 = this.scHeight - 37;
					scale = this.scaledResolution.scale + 1 >> 1;
					i49 -= scale * ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) / this.scaledResolution.scale;
				}

				if(this.fullmap) {
					this.renderFullMap(x1, i49);
				} else if(this.roundmap) {
					this.renderRoundMap(x1, i49);
				} else {
					this.renderSquareMap(x1, i49);
				}

				int i54 = this.scaledResolution.scale + 1 >> 1;
				scalef = 1.0F / (float)this.scaledResolution.scale * (float)i54;
				GL11.glPushMatrix();
				GL11.glScalef(scalef, scalef, 1.0F);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				int i55 = (int)(this.zoomVisible * 255.0F);
				int _y;
				String string56;
				int i57;
				int i59;
				if(!this.fullmap && i55 > 0) {
					string56 = String.format("%2.2fx", new Object[]{this.currentZoom});
					i57 = mc.textRenderer.getWidth(string56);
					if(i55 > 255) {
						i55 = 255;
					}

					i59 = (int)((float)(x1 + 30) / scalef - (float)i57);
					_y = (int)((float)(i49 + 26) / scalef);
					int _y1 = i55 << 24 | 0xFFFFFF;
					mc.textRenderer.drawWithShadow(string56, i59, _y, _y1);
				}

				if(this.showCoordinate) {
					int i58 = MathHelper.floor(this.thePlayer.x);
					i57 = MathHelper.floor(this.thePlayer.shape.minY);
					i59 = MathHelper.floor(this.thePlayer.z);
					float f60 = (this.fullmap ? (float)this.scWidth * 0.5F : (float)x1) / scalef;
					float f61 = (this.fullmap ? (float)this.scHeight * 0.5F + 16.0F : (float)(i49 + 32)) / scalef;
					String str1 = String.format("%+d, %+d", new Object[]{i58, i59});
					float width1 = (float)mc.textRenderer.getWidth(str1) * 0.5F;
					mc.textRenderer.drawWithShadow(str1, (int)(f60 - width1), (int)(f61 + 2.0F), 0xFFFFFF);
					str1 = Integer.toString(i57);
					width1 = (float)mc.textRenderer.getWidth(str1) * 0.5F;
					mc.textRenderer.drawWithShadow(str1, (int)(f60 - width1), (int)(f61 + 11.0F), 0xFFFFFF);
				}

				if(this.showMenuKey && !this.fullmap) {
					string56 = String.format("Menu: %s key", new Object[]{KeyInput.MENU_KEY.getKeyName()});
					i57 = this.theMinecraft.textRenderer.getWidth(string56);
					i59 = (int)((float)(x1 + 32) / scalef - (float)i57);
					_y = (int)((float)(i49 + 32) / scalef);
					this.theMinecraft.textRenderer.drawWithShadow(string56, i59, _y + (this.showCoordinate ? 20 : 2), -1);
				}

				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
			}
		} catch (MinimapException minimapException34) {
			minimapException34.printStackTrace();
		} finally {
			GL11.glPopClientAttrib();
			GL11.glPopAttrib();
		}

		Thread.yield();
	}

	public void run() {
		if(this.theMinecraft != null) {
			Thread currentThread = Thread.currentThread();

			while(true) {
				while(!this.enable || currentThread != this.thread || !this.threading) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException interruptedException20) {
						return;
					}

					this.lock.lock();

					label194: {
						try {
							this.condition.await();
							break label194;
						} catch (InterruptedException interruptedException21) {
						} finally {
							this.lock.unlock();
						}

						return;
					}

					if(currentThread != this.thread) {
						return;
					}
				}

				try {
					Thread.sleep((long)(updateFrequencys[updateFrequencys.length - this.updateFrequencySetting - 1] * 2));
				} catch (InterruptedException interruptedException19) {
					return;
				}

				this.lock.lock();

				try {
					this.mapCalc(false);
					if(this.isCompleteImage || this.isUpdateImage) {
						this.condition.await();
					}
					continue;
				} catch (InterruptedException interruptedException23) {
				} catch (Exception exception24) {
					continue;
				} finally {
					this.lock.unlock();
				}

				return;
			}
		}
	}

	private void startDrawingQuads() {
		this.tessellator.start();
	}

	private void draw() {
		this.tessellator.end();
	}

	private void addVertexWithUV(double x, double y, double z, double u, double v) {
		this.tessellator.vertex(x, y, z, u, v);
	}

	private void mapCalc(boolean strip) {
		if(this.theWorld != null && this.thePlayer != null) {
			double d;
			if(this.stripCounter.count() == 0) {
				this.posX = MathHelper.floor(this.thePlayer.x);
				this.posY = MathHelper.floor(this.thePlayer.y);
				this.posZ = MathHelper.floor(this.thePlayer.z);
				this.chunkCoordX = this.thePlayer.chunkX;
				this.chunkCoordZ = this.thePlayer.chunkZ;
				this.skylightSubtracted = this.theWorld.ambientDarkness;
				d = Math.toRadians(this.roundmap && !this.fullmap ? (double)(45.0F - this.thePlayer.yaw) : -45.0D);
				this.sin = (float)Math.sin(d);
				this.cos = (float)Math.cos(d);
			}

			if(this.fullmap) {
				this.stripCountMax1 = 289;
				this.stripCountMax2 = 289;
			} else {
				d = Math.ceil(4.0D / this.currentZoom) * 2.0D + 1.0D;
				this.stripCountMax1 = (int)(d * d);
				d = Math.ceil(4.0D / this.targetZoom) * 2.0D + 1.0D;
				this.stripCountMax2 = (int)(d * d);
			}

			if(this.renderType == 1 && this.allowCavemap) {
				if(!this.forceUpdate && strip) {
					this.caveCalcStrip();
				} else {
					this.caveCalc();
				}
			} else if(!this.forceUpdate && strip) {
				this.surfaceCalcStrip();
			} else {
				this.surfaceCalc();
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

	private void surfaceCalc() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			WorldChunk chunk = this.chunkCache.get(this.theWorld, this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.surfaceCalc(chunk);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalcStrip() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for(int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			WorldChunk chunk = this.chunkCache.get(this.theWorld, this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.surfaceCalc(chunk);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalc(WorldChunk chunk) {
		if(chunk != null && !(chunk instanceof EmptyChunk)) {
			int offsetX = 128 + chunk.chunkX * 16 - this.posX;
			int offsetZ = 128 + chunk.chunkZ * 16 - this.posZ;
			boolean slime = this.showSlimeChunk && this.currentDimension == 0 && this.chunkCache.isSlimeSpawn(chunk.chunkX, chunk.chunkZ);
			PixelColor pixel = new PixelColor(this.transparency);
			WorldChunk chunkMinusX = null;
			WorldChunk chunkPlusX = null;
			WorldChunk chunkMinusZ = null;
			WorldChunk chunkPlusZ = null;
			WorldChunk cmx = null;
			WorldChunk cpx = null;
			WorldChunk cmz = null;
			WorldChunk cpz = null;
			if(this.undulate) {
				chunkMinusZ = this.getChunk(chunk.world, chunk.chunkX, chunk.chunkZ - 1);
				chunkPlusZ = this.getChunk(chunk.world, chunk.chunkX, chunk.chunkZ + 1);
				chunkMinusX = this.getChunk(chunk.world, chunk.chunkX - 1, chunk.chunkZ);
				chunkPlusX = this.getChunk(chunk.world, chunk.chunkX + 1, chunk.chunkZ);
			}

			for(int z = 0; z < 16; ++z) {
				int zCoord = offsetZ + z;
				if(zCoord >= 0) {
					if(zCoord >= 256) {
						break;
					}

					if(this.undulate) {
						cmz = z == 0 ? chunkMinusZ : chunk;
						cpz = z == 15 ? chunkPlusZ : chunk;
					}

					for(int x = 0; x < 16; ++x) {
						int xCoord = offsetX + x;
						if(xCoord >= 0) {
							if(xCoord >= 256) {
								break;
							}

							pixel.clear();
							int height = !this.omitHeightCalc && !this.heightmap && !this.undulate ? 127 : Math.min(127, chunk.getHeight(x, z));
							int y = this.omitHeightCalc ? height : 127;
							this.surfaceCalc(chunk, x, y, z, pixel, (TintType)null);
							float factor;
							if(this.heightmap) {
								factor = this.undulate ? 0.25F : 0.6F;
								double red = (double)(height - this.posY);
								float blue = (float)Math.log10(Math.abs(red) * 0.125D + 1.0D) * factor;
								if(red >= 0.0D) {
									pixel.red += blue * (1.0F - pixel.red);
									pixel.green += blue * (1.0F - pixel.green);
									pixel.blue += blue * (1.0F - pixel.blue);
								} else {
									pixel.red -= Math.abs(blue) * pixel.red;
									pixel.green -= Math.abs(blue) * pixel.green;
									pixel.blue -= Math.abs(blue) * pixel.blue;
								}
							}

							factor = 1.0F;
							if(this.undulate) {
								cmx = x == 0 ? chunkMinusX : chunk;
								cpx = x == 15 ? chunkPlusX : chunk;
								int i25 = cmx.getHeight(x - 1 & 15, z);
								int green = cpx.getHeight(x + 1 & 15, z);
								int i28 = cmz.getHeight(x, z - 1 & 15);
								int pz = cpz.getHeight(x, z + 1 & 15);
								factor += Math.max(-4.0F, Math.min(3.0F, (float)(i25 - green) * this.sin + (float)(i28 - pz) * this.cos)) * 0.14142136F * 0.8F;
							}

							if(slime) {
								pixel.red = (float)((double)pixel.red * 1.2D);
								pixel.green = (float)((double)pixel.green * 0.5D);
								pixel.blue = (float)((double)pixel.blue * 0.5D);
							}

							byte b26 = ftob(pixel.red * factor);
							byte b27 = ftob(pixel.green * factor);
							byte b29 = ftob(pixel.blue * factor);
							if(this.transparency) {
								this.texture.setRGBA(xCoord, zCoord, b26, b27, b29, ftob(pixel.alpha));
							} else {
								this.texture.setRGB(xCoord, zCoord, b26, b27, b29);
							}
						}
					}
				}
			}

		}
	}

	private static final byte ftob(float f) {
		return (byte)Math.max(0, Math.min(255, (int)(f * 255.0F)));
	}

	private void surfaceCalc(WorldChunk chunk, int x, int y, int z, PixelColor pixel, TintType tintType) {
		int blockID = chunk.getBlockAt(x, y, z);
		if(blockID == 0 || this.hideSnow && blockID == 78) {
			if(y > 0) {
				this.surfaceCalc(chunk, x, y - 1, z, pixel, (TintType)null);
			}

		} else {
			int metadata = BlockColor.useMetadata(blockID) ? chunk.getBlockMetadataAt(x, y, z) : 0;
			BlockColor color = BlockColor.getBlockColor(blockID, metadata);
			if(this.transparency) {
				if(color.alpha < 1.0F && y > 0) {
					this.surfaceCalc(chunk, x, y - 1, z, pixel, color.tintType);
					if(color.alpha == 0.0F) {
						return;
					}
				}
			} else if(color.alpha == 0.0F && y > 0) {
				this.surfaceCalc(chunk, x, y - 1, z, pixel, color.tintType);
				return;
			}

			int lightValue;
			switch(this.lightmap) {
			case 1:
				lightValue = y < 127 ? chunk.getLightAt(x, y + 1, z, 0) : 15;
				break;
			case 2:
				lightValue = y < 127 ? chunk.getLightAt(x, y + 1, z, 11) : 4;
				break;
			case 3:
				lightValue = 15;
				break;
			default:
				this.lightmap = 0;
			case 0:
				lightValue = y < 127 ? chunk.getLightAt(x, y + 1, z, this.skylightSubtracted) : 15 - this.skylightSubtracted;
			}

			float lightBrightness = this.lightBrightnessTable[lightValue];
			if(this.environmentColor) {
				int level;
				switch(SyntheticClass_1.$SwitchMap$reifnsk$minimap$TintType[color.tintType.ordinal()]) {
				case 1:
					level = Environment.getEnvironment(chunk, x, z).getGrassColor();
					pixel.composite(color.alpha, level, lightBrightness * 0.6F);
					return;
				case 2:
					level = Environment.getEnvironment(chunk, x, z).getFoliageColor();
					pixel.composite(color.alpha, level, lightBrightness * 0.5F);
					return;
				case 3:
					level = Environment.getEnvironment(chunk, x, z).getFoliageColorPine();
					pixel.composite(color.alpha, level, lightBrightness * 0.5F);
					return;
				case 4:
					level = Environment.getEnvironment(chunk, x, z).getFoliageColorBirch();
					pixel.composite(color.alpha, level, lightBrightness * 0.5F);
					return;
				}
			}

			if(color.tintType != TintType.WATER || tintType != TintType.WATER) {
				if(color.tintType != TintType.GLASS || tintType != TintType.GLASS) {
					if(color.tintType == TintType.REDSTONE) {
						float level1 = (float)metadata * 0.06666667F;
						float r = metadata == 0 ? 0.3F : level1 * 0.6F + 0.4F;
						float g = Math.max(0.0F, level1 * level1 * 0.7F - 0.5F);
						float b = 0.0F;
						float a = color.alpha;
						pixel.composite(a, r, g, b, lightBrightness);
					} else {
						pixel.composite(color.alpha, color.red, color.green, color.blue, lightBrightness);
					}
				}
			}
		}
	}

	private void caveCalc() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while(this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			WorldChunk chunk = this.chunkCache.get(this.theWorld, this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.caveCalc(chunk);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalcStrip() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for(int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			WorldChunk chunk = this.chunkCache.get(this.theWorld, this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.caveCalc(chunk);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalc(WorldChunk chunk) {
		if(chunk != null && !(chunk instanceof EmptyChunk)) {
			int offsetX = 128 + chunk.chunkX * 16 - this.posX;
			int offsetZ = 128 + chunk.chunkZ * 16 - this.posZ;

			for(int z = 0; z < 16; ++z) {
				int zCoord = offsetZ + z;
				if(zCoord >= 0) {
					if(zCoord >= 256) {
						break;
					}

					for(int x = 0; x < 16; ++x) {
						int xCoord = offsetX + x;
						if(xCoord >= 0) {
							if(xCoord >= 256) {
								break;
							}

							float f;
							f = 0.0F;
							int y;
							int _y;
							label135:
							switch(this.getDimension()) {
							case -1:
								y = 0;

								while(true) {
									if(y >= temp.length) {
										break label135;
									}

									_y = this.posY - y;
									if(_y >= 0 && _y < 128 && chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}

									_y = this.posY + y + 1;
									if(_y >= 0 && _y < 128 && chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}

									++y;
								}
							case 0:
								for(y = 0; y < temp.length; ++y) {
									_y = this.posY - y;
									if(_y >= 128 || _y >= 0 && chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}

									_y = this.posY + y + 1;
									if(_y >= 128 || _y >= 0 && chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}
								}
							case 1:
							case 2:
							default:
								break;
							case 3:
								for(y = 0; y < temp.length; ++y) {
									_y = this.posY - y;
									if(_y < 0 || _y >= 128 || chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}

									_y = this.posY + y + 1;
									if(_y < 0 || _y >= 128 || chunk.getBlockAt(x, _y, z) == 0 && chunk.getLightAt(x, _y, z, 12) != 0) {
										f += temp[y];
									}
								}
							}

							f = 0.8F - f;
							this.texture.setRGB(xCoord, zCoord, ftob(0.0F), ftob(f), ftob(0.0F));
						}
					}
				}
			}

		}
	}

	private void renderRoundMap(int x, int y) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glColorMask(false, false, false, false);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, 0.0F);
		GL11.glRotatef(90.0F - this.thePlayer.yaw, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
		this.texture("/reifnsk/minimap/roundmap_mask.png");
		this.drawCenteringRectangle((double)x, (double)y, 1.01D, 64.0D, 64.0D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glColorMask(true, true, true, true);
		double a = 0.25D / this.currentZoom;
		double slideX = (this.thePlayer.x - (double)this.lastX) * 1.0D / 256D;
		double slideY = (this.thePlayer.z - (double)this.lastZ) * 1.0D / 256D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapTransparency ? 0.7F : 1.0F);
		this.texture.bind();
		this.startDrawingQuads();
		this.addVertexWithUV((double)(x - 32), (double)(y + 32), 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
		this.addVertexWithUV((double)(x + 32), (double)(y + 32), 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV((double)(x + 32), (double)(y - 32), 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV((double)(x - 32), (double)(y - 32), 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		double distance;
		if(this.visibleEntitiesRadar) {
			List s = this.theWorld.globalEntities;
			synchronized(s) {
				Iterator c = s.iterator();

				while(c.hasNext()) {
					Entity entity = (Entity)c.next();
					int wayZ = this.getEntityColor(entity);
					if(wayZ != 0) {
						double wayX = this.thePlayer.x - entity.x;
						distance = this.thePlayer.z - entity.z;
						float locate1 = (float)Math.toDegrees(Math.atan2(wayX, distance));
						double distance1 = Math.sqrt(wayX * wayX + distance * distance) * this.currentZoom * 0.5D;

						try {
							GL11.glPushMatrix();
							if(distance1 < 29.0D) {
								float r = (float)(wayZ >> 16 & 255) * 0.003921569F;
								float g = (float)(wayZ >> 8 & 255) * 0.003921569F;
								float b = (float)(wayZ & 255) * 0.003921569F;
								float alpha = (float)Math.max((double)0.2F, 1.0D - Math.abs(this.thePlayer.y - entity.y) * 0.04D);
								GL11.glColor4f(r, g, b, alpha);
								GL11.glTranslatef((float)x, (float)y, 0.0F);
								GL11.glRotatef(-locate1 - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(0.0D, -distance1, 0.0D);
								GL11.glRotatef(-(-locate1 - this.thePlayer.yaw + 180.0F), 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(0.0D, distance1, 0.0D);
								GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
								GL11.glTranslated(0.0D, -distance1, 0.0D);
								if(this.configEntityDirection) {
									GL11.glTranslatef((float)x, (float)y, 0.0F);
									GL11.glRotatef(entity.yaw - this.thePlayer.yaw, 0.0F, 0.0F, 1.0F);
									GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
									this.texture("%blur%/reifnsk/minimap/entity2.png");
									this.drawCenteringRectangle((double)x, (double)y, 1.0D, 8.0D, 8.0D);
								} else {
									this.texture("%blur%/reifnsk/minimap/entity.png");
									this.drawCenteringRectangle((double)x, (double)y, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapTransparency ? 0.7F : 1.0F);
		this.texture("%blur%/reifnsk/minimap/roundmap.png");
		this.drawCenteringRectangle((double)x, (double)y, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		double c1;
		if(this.visibleWaypoints) {
			Iterator s1 = this.wayPts.iterator();

			while(s1.hasNext()) {
				Waypoint pt = (Waypoint)s1.next();
				if(pt.enable) {
					c1 = this.thePlayer.x - (double)pt.x - 0.5D;
					double wayZ1 = this.thePlayer.z - (double)pt.z - 0.5D;
					float locate = (float)Math.toDegrees(Math.atan2(c1, wayZ1));
					distance = Math.sqrt(c1 * c1 + wayZ1 * wayZ1) * this.currentZoom * 0.5D;

					try {
						GL11.glPushMatrix();
						if(distance < 31.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float)Math.min(1.0D, Math.max(0.4D, (distance - 1.0D) * 0.5D)));
							this.texture(Waypoint.FILE[pt.type]);
							GL11.glTranslatef((float)x, (float)y, 0.0F);
							GL11.glRotatef(-locate - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -distance, 0.0D);
							GL11.glRotatef(-(-locate - this.thePlayer.yaw + 180.0F), 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, distance, 0.0D);
							GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
							GL11.glTranslated(0.0D, -distance, 0.0D);
							this.drawCenteringRectangle((double)x, (double)y, 1.0D, 8.0D, 8.0D);
						} else {
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							this.texture(Waypoint.MARKER[pt.type]);
							GL11.glTranslatef((float)x, (float)y, 0.0F);
							GL11.glRotatef(-locate - this.thePlayer.yaw + 180.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
							GL11.glTranslated(0.0D, -34.0D, 0.0D);
							this.drawCenteringRectangle((double)x, (double)y, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		double s2 = Math.sin(Math.toRadians((double)this.thePlayer.yaw)) * 28.0D;
		c1 = Math.cos(Math.toRadians((double)this.thePlayer.yaw)) * 28.0D;
		this.texture("%blur%/reifnsk/minimap/n.png");
		this.drawCenteringRectangle((double)x + c1, (double)y - s2, 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/w.png");
		this.drawCenteringRectangle((double)x - s2, (double)y - c1, 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/s.png");
		this.drawCenteringRectangle((double)x - c1, (double)y + s2, 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/e.png");
		this.drawCenteringRectangle((double)x + s2, (double)y + c1, 1.0D, 8.0D, 8.0D);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void renderSquareMap(int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glColorMask(false, false, false, false);
		this.texture("/reifnsk/minimap/squaremap_mask.png");
		this.drawCenteringRectangle((double)x, (double)y, 1.001D, 64.0D, 64.0D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glColorMask(true, true, true, true);
		double a = 0.25D / this.currentZoom;
		double slideX = (this.thePlayer.x - (double)this.lastX) * 1.0D / 256D;
		double slideY = (this.thePlayer.z - (double)this.lastZ) * 1.0D / 256D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapTransparency ? 0.7F : 1.0F);
		this.texture.bind();
		this.startDrawingQuads();
		this.addVertexWithUV((double)(x - 32), (double)(y + 32), 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
		this.addVertexWithUV((double)(x + 32), (double)(y + 32), 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV((double)(x + 32), (double)(y - 32), 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV((double)(x - 32), (double)(y - 32), 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		double d;
		double t;
		if(this.visibleEntitiesRadar) {
			List exception = this.theWorld.globalEntities;
			synchronized(exception) {
				Iterator wayX = exception.iterator();

				while(wayX.hasNext()) {
					Entity entity = (Entity)wayX.next();
					int wayZ = this.getEntityColor(entity);
					if(wayZ != 0) {
						double wayX1 = this.thePlayer.x - entity.x;
						d = this.thePlayer.z - entity.z;
						wayX1 = wayX1 * this.currentZoom * 0.5D;
						d = d * this.currentZoom * 0.5D;
						t = Math.max(Math.abs(wayX1), Math.abs(d));

						try {
							GL11.glPushMatrix();
							if(t < 31.0D) {
								float hypot = (float)(wayZ >> 16 & 255) * 0.003921569F;
								float g = (float)(wayZ >> 8 & 255) * 0.003921569F;
								float b = (float)(wayZ & 255) * 0.003921569F;
								float alpha = (float)Math.max((double)0.2F, 1.0D - Math.abs(this.thePlayer.y - entity.y) * 0.04D);
								GL11.glColor4f(hypot, g, b, alpha);
								if(this.configEntityDirection) {
									GL11.glTranslated((double)x + d, (double)y - wayX1, 0.0D);
									GL11.glRotatef(entity.yaw - 90.0F, 0.0F, 0.0F, 1.0F);
									GL11.glTranslated((double)(-x) - d, (double)(-y) + wayX1, 0.0D);
									this.texture("%blur%/reifnsk/minimap/entity2.png");
									this.drawCenteringRectangle((double)x + d, (double)y - wayX1, 1.0D, 8.0D, 8.0D);
								} else {
									this.texture("%blur%/reifnsk/minimap/entity.png");
									this.drawCenteringRectangle((double)x + d, (double)y - wayX1, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapTransparency ? 0.7F : 1.0F);
		this.texture("%blur%/reifnsk/minimap/squaremap.png");
		this.drawCenteringRectangle((double)x, (double)y, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(this.visibleWaypoints) {
			Iterator exception1 = this.wayPts.iterator();

			while(exception1.hasNext()) {
				Waypoint pt = (Waypoint)exception1.next();
				if(pt.enable) {
					double wayX2 = this.thePlayer.x - (double)pt.x - 0.5D;
					double wayZ1 = this.thePlayer.z - (double)pt.z - 0.5D;
					wayX2 = wayX2 * this.currentZoom * 0.5D;
					wayZ1 = wayZ1 * this.currentZoom * 0.5D;
					float locate = (float)Math.toDegrees(Math.atan2(wayX2, wayZ1));
					d = Math.max(Math.abs(wayX2), Math.abs(wayZ1));

					try {
						GL11.glPushMatrix();
						if(d < 31.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float)Math.min(1.0D, Math.max(0.4D, (d - 1.0D) * 0.5D)));
							this.texture(Waypoint.FILE[pt.type]);
							this.drawCenteringRectangle((double)x + wayZ1, (double)y - wayX2, 1.0D, 8.0D, 8.0D);
						} else {
							t = 34.0D / d;
							wayX2 *= t;
							wayZ1 *= t;
							double hypot1 = Math.sqrt(wayX2 * wayX2 + wayZ1 * wayZ1);
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							this.texture(Waypoint.MARKER[pt.type]);
							GL11.glTranslatef((float)x, (float)y, 0.0F);
							GL11.glRotatef(-locate + 90.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
							GL11.glTranslated(0.0D, -hypot1, 0.0D);
							this.drawCenteringRectangle((double)x, (double)y, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		this.texture("%blur%/reifnsk/minimap/n.png");
		this.drawCenteringRectangle((double)x, (double)(y - 28), 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/s.png");
		this.drawCenteringRectangle((double)x, (double)(y + 28), 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/w.png");
		this.drawCenteringRectangle((double)(x - 28), (double)y, 1.0D, 8.0D, 8.0D);
		this.texture("%blur%/reifnsk/minimap/e.png");
		this.drawCenteringRectangle((double)(x + 28), (double)y, 1.0D, 8.0D, 8.0D);

		try {
			GL11.glPushMatrix();
			this.texture("%blur%/reifnsk/minimap/mmarrow.png");
			GL11.glTranslated((double)x, (double)y, 0.0D);
			GL11.glRotatef(this.thePlayer.yaw - 90.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslated((double)(-x), (double)(-y), 0.0D);
			this.drawCenteringRectangle((double)x, (double)y, 1.0D, 4.0D, 4.0D);
		} catch (Exception exception43) {
		} finally {
			GL11.glPopMatrix();
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void renderFullMap(int x, int y) {
		double centerX = (double)this.scWidth * 0.5D;
		double centerY = (double)this.scHeight * 0.5D;
		double slideX = (this.thePlayer.x - (double)this.lastX) * 1.0D / 256D;
		double slideY = (this.thePlayer.z - (double)this.lastZ) * 1.0D / 256D;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.texture.bind();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapTransparency ? 0.7F : 1.0F);
		this.startDrawingQuads();
		this.addVertexWithUV(centerX - 120.0D, centerY + 120.0D, 1.0D, 0.96875D + slideX, 0.96875D + slideY);
		this.addVertexWithUV(centerX + 120.0D, centerY + 120.0D, 1.0D, 0.96875D + slideX, 8.0D / 256D + slideY);
		this.addVertexWithUV(centerX + 120.0D, centerY - 120.0D, 1.0D, 8.0D / 256D + slideX, 8.0D / 256D + slideY);
		this.addVertexWithUV(centerX - 120.0D, centerY - 120.0D, 1.0D, 8.0D / 256D + slideX, 0.96875D + slideY);
		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		double d;
		double t;
		if(this.visibleEntitiesRadar) {
			List i$ = this.theWorld.globalEntities;
			synchronized(i$) {
				Iterator wayX = i$.iterator();

				while(wayX.hasNext()) {
					Entity entity = (Entity)wayX.next();
					int wayZ = this.getEntityColor(entity);
					if(wayZ != 0) {
						double wayX1 = this.thePlayer.x - entity.x;
						d = this.thePlayer.z - entity.z;
						t = Math.max(Math.abs(wayX1), Math.abs(d));

						try {
							GL11.glPushMatrix();
							if(t < 114.0D) {
								float hypot = (float)(wayZ >> 16 & 255) * 0.003921569F;
								float x1 = (float)(wayZ >> 8 & 255) * 0.003921569F;
								float x2 = (float)(wayZ & 255) * 0.003921569F;
								float y1 = (float)Math.max((double)0.2F, 1.0D - Math.abs(this.thePlayer.y - entity.y) * 0.04D);
								GL11.glColor4f(hypot, x1, x2, y1);
								if(this.configEntityDirection) {
									GL11.glTranslated(centerX + d, centerY - wayX1, 0.0D);
									GL11.glRotatef(entity.yaw - 90.0F, 0.0F, 0.0F, 1.0F);
									GL11.glTranslated(-centerX - d, -centerY + wayX1, 0.0D);
									this.texture("%blur%/reifnsk/minimap/entity2.png");
									this.drawCenteringRectangle(centerX + d, centerY - wayX1, 1.0D, 8.0D, 8.0D);
								} else {
									this.texture("%blur%/reifnsk/minimap/entity.png");
									this.drawCenteringRectangle(centerX + d, centerY - wayX1, 1.0D, 8.0D, 8.0D);
								}
							}
						} finally {
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.texture("%blur%/reifnsk/minimap/n.png");
		this.drawCenteringRectangle(centerX, centerY - 104.0D, 1.0D, 16.0D, 16.0D);
		this.texture("%blur%/reifnsk/minimap/s.png");
		this.drawCenteringRectangle(centerX, centerY + 104.0D, 1.0D, 16.0D, 16.0D);
		this.texture("%blur%/reifnsk/minimap/w.png");
		this.drawCenteringRectangle(centerX - 104.0D, centerY, 1.0D, 16.0D, 16.0D);
		this.texture("%blur%/reifnsk/minimap/e.png");
		this.drawCenteringRectangle(centerX + 104.0D, centerY, 1.0D, 16.0D, 16.0D);

		try {
			GL11.glPushMatrix();
			this.texture("%blur%/reifnsk/minimap/mmarrow.png");
			GL11.glTranslated(centerX, centerY, 0.0D);
			GL11.glRotatef(this.thePlayer.yaw - 90.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslated(-centerX, -centerY, 0.0D);
			this.drawCenteringRectangle(centerX, centerY, 1.0D, 8.0D, 8.0D);
		} catch (Exception exception46) {
		} finally {
			GL11.glPopMatrix();
		}

		if(this.visibleWaypoints) {
			Iterator i$1 = this.wayPts.iterator();

			while(i$1.hasNext()) {
				Waypoint pt = (Waypoint)i$1.next();
				if(pt.enable) {
					double wayX2 = this.thePlayer.x - (double)pt.x - 0.5D;
					double wayZ1 = this.thePlayer.z - (double)pt.z - 0.5D;
					float locate = (float)Math.toDegrees(Math.atan2(wayX2, wayZ1));
					d = Math.max(Math.abs(wayX2), Math.abs(wayZ1));

					try {
						GL11.glPushMatrix();
						if(d < 114.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float)Math.min(1.0D, Math.max(0.4D, (d - 1.0D) * 0.5D)));
							this.texture(Waypoint.FILE[pt.type]);
							this.drawCenteringRectangle(centerX + wayZ1, centerY - wayX2, 1.0D, 8.0D, 8.0D);
							if(KeyInput.TOGGLE_ZOOM.isKeyDown() && pt.name != null && !pt.name.isEmpty()) {
								GL11.glDisable(GL11.GL_TEXTURE_2D);
								GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.627451F);
								int t1 = this.theMinecraft.textRenderer.getWidth(pt.name);
								int _x = (int)(centerX + wayZ1);
								int hypot1 = (int)(centerY - wayX2);
								int x11 = _x - (t1 >> 1);
								int x21 = x11 + t1;
								int y11 = hypot1 - 15;
								int y2 = hypot1 - 5;
								this.tessellator.start();
								this.tessellator.vertex((double)(x11 - 1), (double)y2, 1.0D);
								this.tessellator.vertex((double)(x21 + 1), (double)y2, 1.0D);
								this.tessellator.vertex((double)(x21 + 1), (double)y11, 1.0D);
								this.tessellator.vertex((double)(x11 - 1), (double)y11, 1.0D);
								this.tessellator.end();
								GL11.glEnable(GL11.GL_TEXTURE_2D);
								this.theMinecraft.textRenderer.drawWithShadow(pt.name, x11, y11 + 1, pt.type == 0 ? -1 : -65536);
							}
						} else {
							t = 117.0D / d;
							wayX2 *= t;
							wayZ1 *= t;
							double hypot2 = Math.sqrt(wayX2 * wayX2 + wayZ1 * wayZ1);
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							this.texture(Waypoint.MARKER[pt.type]);
							GL11.glTranslated(centerX, centerY, 0.0D);
							GL11.glRotatef(-locate + 90.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(-centerX, -centerY, 0.0D);
							GL11.glTranslated(0.0D, -hypot2, 0.0D);
							this.drawCenteringRectangle(centerX, centerY, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void texture(String texture) {
		this.theMinecraft.textureManager.bind(this.theMinecraft.textureManager.load(texture));
	}

	public void setOption(EnumOption option, EnumOptionValue value) {
		this.lock.lock();

		try {
			switch(SyntheticClass_1.$SwitchMap$reifnsk$minimap$EnumOption[option.ordinal()]) {
			case 1:
				this.enable = EnumOptionValue.bool(value);
				break;
			case 2:
				this.showMenuKey = EnumOptionValue.bool(value);
				break;
			case 3:
				this.roundmap = value == EnumOptionValue.ROUND;
				break;
			case 4:
				this.mapPosition = Math.max(0, option.getValue(value));
				break;
			case 5:
				this.mapTransparency = EnumOptionValue.bool(value);
				break;
			case 6:
				this.filtering = EnumOptionValue.bool(value);
				break;
			case 7:
				this.showCoordinate = EnumOptionValue.bool(value);
				break;
			case 8:
				this.updateFrequencySetting = Math.max(0, option.getValue(value));
				break;
			case 9:
				this.threading = EnumOptionValue.bool(value);
				break;
			case 10:
				this.threadPriority = Math.max(0, option.getValue(value));
				if(this.thread != null && this.thread.isAlive()) {
					this.thread.setPriority(3 + this.threadPriority);
				}
				break;
			case 11:
				this.lightmap = Math.max(0, option.getValue(value));
				break;
			case 12:
				this.undulate = EnumOptionValue.bool(value);
				break;
			case 13:
				this.heightmap = EnumOptionValue.bool(value);
				break;
			case 14:
				this.transparency = EnumOptionValue.bool(value);
				break;
			case 15:
				this.environmentColor = EnumOptionValue.bool(value);
				break;
			case 16:
				this.omitHeightCalc = EnumOptionValue.bool(value);
				break;
			case 17:
				this.hideSnow = EnumOptionValue.bool(value);
				break;
			case 18:
				this.showSlimeChunk = EnumOptionValue.bool(value);
				break;
			case 19:
				this.renderType = Math.max(0, option.getValue(value));
				break;
			case 20:
				this.configEntitiesRadar = EnumOptionValue.bool(value);
				break;
			case 21:
				this.theMinecraft.openScreen(new GuiOptionScreen(1));
				break;
			case 22:
				this.theMinecraft.openScreen(new GuiOptionScreen(2));
				break;
			case 23:
				this.theMinecraft.openScreen(new GuiOptionScreen(4));
				break;
			case 24:
				this.theMinecraft.openScreen(new GuiOptionScreen(3));
				break;
			case 25:
				try {
					Desktop.getDesktop().browse(new URI("http://www.minecraftforum.net/index.php?showtopic=482147"));
				} catch (Exception exception9) {
					exception9.printStackTrace();
				}
				break;
			case 26:
				try {
					Desktop.getDesktop().browse(new URI("http://forum.minecraftuser.jp/viewtopic.php?f=13&t=153"));
				} catch (Exception exception8) {
					exception8.printStackTrace();
				}
				break;
			case 27:
				this.deathPoint = EnumOptionValue.bool(value);
				break;
			case 28:
				this.configEntityPlayer = EnumOptionValue.bool(value);
				break;
			case 29:
				this.configEntityAnimal = EnumOptionValue.bool(value);
				break;
			case 30:
				this.configEntityMob = EnumOptionValue.bool(value);
				break;
			case 31:
				this.configEntitySlime = EnumOptionValue.bool(value);
				break;
			case 32:
				this.configEntitySquid = EnumOptionValue.bool(value);
				break;
			case 33:
				this.configEntityDirection = EnumOptionValue.bool(value);
				break;
			case 34:
				this.defaultZoom = Math.max(0, option.getValue(value));
			}

			this.forceUpdate = true;
			this.stripCounter.reset();
			if(this.threading) {
				this.mapCalc(false);
				if(this.isCompleteImage) {
					this.texture.register();
				}
			}
		} finally {
			this.lock.unlock();
		}

	}

	public EnumOptionValue getOption(EnumOption option) {
		switch(SyntheticClass_1.$SwitchMap$reifnsk$minimap$EnumOption[option.ordinal()]) {
		case 1:
			return EnumOptionValue.bool(this.enable);
		case 2:
			return EnumOptionValue.bool(this.showMenuKey);
		case 3:
			return this.roundmap ? EnumOptionValue.ROUND : EnumOptionValue.SQUARE;
		case 4:
			return option.getValue(this.mapPosition);
		case 5:
			return EnumOptionValue.bool(this.mapTransparency);
		case 6:
			return EnumOptionValue.bool(this.filtering);
		case 7:
			return EnumOptionValue.bool(this.showCoordinate);
		case 8:
			return option.getValue(this.updateFrequencySetting);
		case 9:
			return EnumOptionValue.bool(this.threading);
		case 10:
			return option.getValue(this.threadPriority);
		case 11:
			return option.getValue(this.lightmap);
		case 12:
			return EnumOptionValue.bool(this.undulate);
		case 13:
			return EnumOptionValue.bool(this.heightmap);
		case 14:
			return EnumOptionValue.bool(this.transparency);
		case 15:
			return EnumOptionValue.bool(this.environmentColor);
		case 16:
			return EnumOptionValue.bool(this.omitHeightCalc);
		case 17:
			return EnumOptionValue.bool(this.hideSnow);
		case 18:
			return EnumOptionValue.bool(this.showSlimeChunk);
		case 19:
			return option.getValue(this.renderType);
		case 20:
			return EnumOptionValue.bool(this.configEntitiesRadar);
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		default:
			return option.getValue(0);
		case 27:
			return EnumOptionValue.bool(this.deathPoint);
		case 28:
			return EnumOptionValue.bool(this.configEntityPlayer);
		case 29:
			return EnumOptionValue.bool(this.configEntityAnimal);
		case 30:
			return EnumOptionValue.bool(this.configEntityMob);
		case 31:
			return EnumOptionValue.bool(this.configEntitySlime);
		case 32:
			return EnumOptionValue.bool(this.configEntitySquid);
		case 33:
			return EnumOptionValue.bool(this.configEntityDirection);
		case 34:
			return option.getValue(this.defaultZoom);
		}
	}

	void saveOptions() {
		File file = new File(directory, "option.txt");

		try {
			PrintWriter e = new PrintWriter(file);
			EnumOption[] arr$ = EnumOption.values();
			int len$ = arr$.length;

			for(int i$ = 0; i$ < len$; ++i$) {
				EnumOption option = arr$[i$];
				if(this.getOption(option) != EnumOptionValue.SUB_OPTION && this.getOption(option) != EnumOptionValue.VERSION && this.getOption(option) != EnumOptionValue.AUTHER) {
					e.printf("%s: %s%n", new Object[]{capitalize(option.toString()), capitalize(this.getOption(option).toString())});
				}
			}

			e.flush();
			e.close();
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	private void loadOptions() {
		File file = new File(directory, "option.txt");
		if(file.exists()) {
			boolean error = false;

			try {
				Scanner e = new Scanner(file);

				while(e.hasNextLine()) {
					try {
						String[] e1 = e.nextLine().split(":");
						this.setOption(EnumOption.valueOf(toUpperCase(e1[0].trim())), EnumOptionValue.valueOf(toUpperCase(e1[1].trim())));
					} catch (Exception exception5) {
						System.err.println(exception5.getMessage());
						error = true;
					}
				}

				e.close();
			} catch (Exception exception6) {
				exception6.printStackTrace();
			}

			if(error) {
				this.saveOptions();
			}

			this.flagZoom = this.defaultZoom;
		}
	}

	public ArrayList getWaypoints() {
		return this.wayPts;
	}

	void saveWaypoints() {
		File waypointFile = new File(directory, this.currentLevelName + ".DIM" + this.waypointDimension + ".points");
		if(waypointFile.isDirectory()) {
			this.chatInfo("\u00a7E[Rei\'s Minimap] Error Saving Waypoints");
		} else {
			try {
				PrintWriter e = new PrintWriter(waypointFile);
				Iterator i$ = this.wayPts.iterator();

				while(i$.hasNext()) {
					Waypoint pt = (Waypoint)i$.next();
					e.println(pt);
				}

				e.flush();
				e.close();
			} catch (Exception exception5) {
				this.chatInfo("\u00a7E[Rei\'s Minimap] Error Saving Waypoints");
			}

		}
	}

	void loadWaypoints() {
		this.wayPts = null;
		this.wayPtsMap.clear();
		Pattern pattern = Pattern.compile(Pattern.quote(this.currentLevelName) + "\\.DIM(-?[0-9])\\.points");
		int load = 0;
		int dim = 0;
		String[] arr$ = directory.list();
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			String file = arr$[i$];
			Matcher m = pattern.matcher(file);
			if(m.matches()) {
				++dim;
				int dimension = Integer.parseInt(m.group(1));
				ArrayList list = new ArrayList();
				Scanner in = null;

				try {
					in = new Scanner(new File(directory, file));

					while(in.hasNextLine()) {
						Waypoint e = Waypoint.load(in.nextLine());
						if(e != null) {
							list.add(e);
							++load;
						}
					}
				} catch (Exception exception16) {
				} finally {
					if(in != null) {
						in.close();
					}

				}

				this.wayPtsMap.put(dimension, list);
				if(dimension == this.currentDimension) {
					this.wayPts = list;
				}
			}
		}

		if(this.wayPts == null) {
			this.wayPts = new ArrayList();
		}

		if(load != 0) {
			this.chatInfo("\u00a7E[Rei\'s Minimap] " + load + " Waypoints loaded for " + this.currentLevelName);
		}

	}

	private void chatInfo(String s) {
		this.ingameGUI.addChatMessage(s);
	}

	private int getDimension() {
		return this.theMinecraft.player.dimensionId;
	}

	float[] generateLightBrightnessTable(float f) {
		float[] result = new float[16];

		for(int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float)i / 15.0F;
			result[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}

		return result;
	}

	float[] generateLightBrightnessTable2(float f) {
		float[] result = new float[16];

		for(int i = 0; i <= 15; ++i) {
			float f1 = (float)i / 15.0F;
			result[i] = f1 * (1.0F - f) + f;
		}

		return result;
	}

	float[] generateLightBrightnessTable3(float f1, float f2) {
		float[] a1 = this.generateLightBrightnessTable(f1);
		float[] a2 = this.generateLightBrightnessTable(f2);
		float[] result = new float[16];

		for(int i = 0; i <= 15; ++i) {
			result[i] = (a1[i] + a2[i]) * 0.5F;
		}

		return result;
	}

	private WorldChunk getChunk(World world, int x, int z) {
		boolean b = Math.abs(this.chunkCoordX - x) <= 8 && Math.abs(this.chunkCoordZ - z) <= 8;
		Object chunk = b ? this.chunkCache.get(world, x, z) : EMPTY_CHUNK;
		return (WorldChunk)(chunk != null ? chunk : EMPTY_CHUNK);
	}

	private void drawCenteringRectangle(double centerX, double centerY, double z, double w, double h) {
		w *= 0.5D;
		h *= 0.5D;
		this.startDrawingQuads();
		this.addVertexWithUV(centerX - w, centerY + h, z, 0.0D, 1.0D);
		this.addVertexWithUV(centerX + w, centerY + h, z, 1.0D, 1.0D);
		this.addVertexWithUV(centerX + w, centerY - h, z, 1.0D, 0.0D);
		this.addVertexWithUV(centerX - w, centerY - h, z, 0.0D, 0.0D);
		this.draw();
	}

	public static String capitalize(String src) {
		if(src == null) {
			return null;
		} else {
			boolean title = true;
			char[] cs = src.toCharArray();
			int i = 0;

			for(int j = cs.length; i < j; ++i) {
				char c = cs[i];
				if(c == 95) {
					c = 32;
				}

				cs[i] = title ? Character.toTitleCase(c) : Character.toLowerCase(c);
				title = Character.isWhitespace(c);
			}

			return new String(cs);
		}
	}

	public static String toUpperCase(String src) {
		return src == null ? null : src.replace(' ', '_').toUpperCase();
	}

	private static boolean checkGuiScreen(Screen gui) {
		return gui == null || gui instanceof GuiScreenInterface || gui instanceof ChatScreen || gui instanceof DeathScreen;
	}

	private static Map createObfuscatorFieldMap() {
		HashMap map = new HashMap();
		if("Beta 1.7.3".equals("Beta 1.7.3")) {
			map.put("chatMessageList", "e");
			map.put("worldInfo", "x");
			map.put("levelName", "j");
			map.put("sendQueue", "bN");
			map.put("netManager", "e");
			map.put("remoteSocketAddress", "i");
			map.put("dimension", "m");
		} else if("Beta 1.7.3".equals("Beta 1.6.6")) {
			map.put("chatMessageList", "e");
			map.put("worldInfo", "x");
			map.put("levelName", "j");
			map.put("sendQueue", "bM");
			map.put("netManager", "e");
			map.put("remoteSocketAddress", "i");
			map.put("dimension", "m");
		} else if("Beta 1.7.3".equals("Beta 1.5_01")) {
			map.put("chatMessageList", "e");
			map.put("worldInfo", "s");
			map.put("levelName", "j");
			map.put("sendQueue", "bJ");
			map.put("netManager", "d");
			map.put("remoteSocketAddress", "g");
			map.put("dimension", "p");
		}

		return Collections.unmodifiableMap(map);
	}

	private static Object getField(Object obj, String name) {
		String obfName = (String)obfascatorFieldMap.get(name);
		if(obj != null && name != null && obfName != null) {
			Class clazz = obj instanceof Class ? (Class)obj : obj.getClass();
			Object result = getField(clazz, obj, obfName);
			return result != null ? result : getField(clazz, obj, name);
		} else {
			return null;
		}
	}

	private static Object getFields(Object obj, String... names) {
		String[] arr$ = names;
		int len$ = names.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			String name = arr$[i$];
			obj = getField(obj, name);
		}

		return obj;
	}

	private static Object getField(Class clazz, Object obj, String name) {
		while(clazz != null) {
			try {
				Field e = clazz.getDeclaredField(name);
				e.setAccessible(true);
				return e.get(obj);
			} catch (Exception exception4) {
				clazz = clazz.getSuperclass();
			}
		}

		return null;
	}

	private int getEntityColor(Entity entity) {
		return entity == this.thePlayer ? 0 : (entity instanceof PlayerEntity ? (this.visibleEntityPlayer ? -16711681 : 0) : (entity instanceof SquidEntity ? (this.visibleEntitySquid ? -16760704 : 0) : (entity instanceof AnimalEntity ? (this.visibleEntityAnimal ? -1 : 0) : (entity instanceof SlimeEntity ? (this.visibleEntitySlime ? -10444704 : 0) : (entity instanceof HostileEntity ? (this.visibleEntityMob ? -65536 : 0) : 0)))));
	}

	static {
		float f = 0.0F;

		int i;
		for(i = 0; i < temp.length; ++i) {
			temp[i] = (float)(1.0D / Math.sqrt((double)(i + 1)));
			f += temp[i];
		}

		f = 0.3F / f;

		for(i = 0; i < temp.length; ++i) {
			temp[i] *= f;
		}

		f = 0.0F;

		for(i = 0; i < 10; ++i) {
			f += temp[i];
		}

		obfascatorFieldMap = createObfuscatorFieldMap();
	}

	static class SyntheticClass_1 {
		static final int[] $SwitchMap$reifnsk$minimap$TintType;
		static final int[] $SwitchMap$reifnsk$minimap$EnumOption = new int[EnumOption.values().length];

		static {
			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.MINIMAP.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError38) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.SHOW_MENU_KEY.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError37) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.MAP_SHAPE.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError36) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.MAP_POSITION.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError35) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.MAP_TRANSPARENCY.ordinal()] = 5;
			} catch (NoSuchFieldError noSuchFieldError34) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.FILTERING.ordinal()] = 6;
			} catch (NoSuchFieldError noSuchFieldError33) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.SHOW_COORDINATES.ordinal()] = 7;
			} catch (NoSuchFieldError noSuchFieldError32) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.UPDATE_FREQUENCY.ordinal()] = 8;
			} catch (NoSuchFieldError noSuchFieldError31) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.THREADING.ordinal()] = 9;
			} catch (NoSuchFieldError noSuchFieldError30) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.THREAD_PRIORITY.ordinal()] = 10;
			} catch (NoSuchFieldError noSuchFieldError29) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.LIGHTING.ordinal()] = 11;
			} catch (NoSuchFieldError noSuchFieldError28) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.TERRAIN_UNDULATE.ordinal()] = 12;
			} catch (NoSuchFieldError noSuchFieldError27) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.TERRAIN_DEPTH.ordinal()] = 13;
			} catch (NoSuchFieldError noSuchFieldError26) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.TRANSPARENCY.ordinal()] = 14;
			} catch (NoSuchFieldError noSuchFieldError25) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENVIRONMENT_COLOR.ordinal()] = 15;
			} catch (NoSuchFieldError noSuchFieldError24) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.OMIT_HEIGHT_CALC.ordinal()] = 16;
			} catch (NoSuchFieldError noSuchFieldError23) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.HIDE_SNOW.ordinal()] = 17;
			} catch (NoSuchFieldError noSuchFieldError22) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.SHOW_SLIME_CHUNK.ordinal()] = 18;
			} catch (NoSuchFieldError noSuchFieldError21) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.RENDER_TYPE.ordinal()] = 19;
			} catch (NoSuchFieldError noSuchFieldError20) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITIES_RADAR.ordinal()] = 20;
			} catch (NoSuchFieldError noSuchFieldError19) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.MINIMAP_OPTION.ordinal()] = 21;
			} catch (NoSuchFieldError noSuchFieldError18) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.SURFACE_MAP_OPTION.ordinal()] = 22;
			} catch (NoSuchFieldError noSuchFieldError17) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ABOUT_MINIMAP.ordinal()] = 23;
			} catch (NoSuchFieldError noSuchFieldError16) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITIES_RADAR_OPTION.ordinal()] = 24;
			} catch (NoSuchFieldError noSuchFieldError15) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENG_FORUM.ordinal()] = 25;
			} catch (NoSuchFieldError noSuchFieldError14) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.JP_FORUM.ordinal()] = 26;
			} catch (NoSuchFieldError noSuchFieldError13) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.DEATH_POINT.ordinal()] = 27;
			} catch (NoSuchFieldError noSuchFieldError12) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_PLAYER.ordinal()] = 28;
			} catch (NoSuchFieldError noSuchFieldError11) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_ANIMAL.ordinal()] = 29;
			} catch (NoSuchFieldError noSuchFieldError10) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_MOB.ordinal()] = 30;
			} catch (NoSuchFieldError noSuchFieldError9) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_SLIME.ordinal()] = 31;
			} catch (NoSuchFieldError noSuchFieldError8) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_SQUID.ordinal()] = 32;
			} catch (NoSuchFieldError noSuchFieldError7) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.ENTITY_DIRECTION.ordinal()] = 33;
			} catch (NoSuchFieldError noSuchFieldError6) {
			}

			try {
				$SwitchMap$reifnsk$minimap$EnumOption[EnumOption.DEFAULT_ZOOM.ordinal()] = 34;
			} catch (NoSuchFieldError noSuchFieldError5) {
			}

			$SwitchMap$reifnsk$minimap$TintType = new int[TintType.values().length];

			try {
				$SwitchMap$reifnsk$minimap$TintType[TintType.GRASS.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError4) {
			}

			try {
				$SwitchMap$reifnsk$minimap$TintType[TintType.FOLIAGE.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$reifnsk$minimap$TintType[TintType.PINE.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$reifnsk$minimap$TintType[TintType.BIRCH.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

		}
	}
}
