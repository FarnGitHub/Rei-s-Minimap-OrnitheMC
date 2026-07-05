package reifnsk.minimap.main.render;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.client.util.GlAllocationUtils;
import org.lwjgl.opengl.GL11;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class GLTextureBufferedImage extends BufferedImage {
	private static final ByteBuffer buffer = GlAllocationUtils.allocateByteBuffer(262144);
	private static final HashMap<Integer, GLTextureBufferedImage> registerImage = new HashMap<>();
	private static final Lock lock = new ReentrantLock();
	public byte[] data;
	private int register;
	private boolean magFiltering;
	private boolean minFiltering;
	private boolean clampTexture;

	private GLTextureBufferedImage(ColorModel colorModel1, WritableRaster writableRaster2, boolean z3, Hashtable hashtable4) {
		super(colorModel1, writableRaster2, z3, hashtable4);
		this.data = ((DataBufferByte)writableRaster2.getDataBuffer()).getData();
	}

	public static GLTextureBufferedImage create(int x, int y) {
		ColorSpace colorSpace2 = ColorSpace.getInstance(1000);
		ComponentColorModel componentColorModel5 = new ComponentColorModel(colorSpace2, new int[]{8, 8, 8, 8}, true, false, 3, 0);
		WritableRaster writableRaster6 = Raster.createInterleavedRaster(0, x, y, x * 4, 4, new int[]{0, 1, 2, 3}, null);
		return new GLTextureBufferedImage(componentColorModel5, writableRaster6, false, null);
	}

	public static GLTextureBufferedImage create(BufferedImage image) {
		GLTextureBufferedImage glTex = create(image.getWidth(), image.getHeight());
		Graphics graphics2 = glTex.getGraphics();
		graphics2.drawImage(image, 0, 0, null);
		graphics2.dispose();
		return glTex;
	}

	public int register() {
		lock.lock();

		int retVal;
		try {
			if(this.register == 0) {
				this.register = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				int clamp = this.clampTexture ? 10496 : 10497;
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp);
				buffer.clear();
				buffer.put(this.data);
				buffer.flip();
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.getWidth(), this.getHeight(), 0, 6408, 5121, buffer);
				registerImage.put(this.register, this);
				retVal = this.register;
				return retVal;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			int clamp = this.clampTexture ? 10496 : 10497;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp);
			buffer.clear();
			buffer.put(this.data);
			buffer.flip();
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, this.getWidth(), this.getHeight(), 6408, 5121, buffer);
			retVal = this.register;
		} finally {
			lock.unlock();
		}

		return retVal;
	}

	public boolean bind() {
		lock.lock();

		try {
			if(this.register == 0) {
				return false;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
		} finally {
			lock.unlock();
		}

		return true;
	}

	public void unregister() {
		lock.lock();

		try {
			if(this.register != 0) {
				GL11.glDeleteTextures(this.register);
				this.register = 0;
				registerImage.remove(this.register);
			}
		} finally {
			lock.unlock();
		}

	}

	public static void unregister(int glId) {
		lock.lock();

		try {
			GLTextureBufferedImage glTexBuffer = registerImage.get(glId);
			if(glTexBuffer != null) {
				glTexBuffer.unregister();
			}
		} finally {
			lock.unlock();
		}

	}

	public void setMagFilter(boolean val) {
		this.magFiltering = val;
	}

	public void setMinFilter(boolean val) {
		this.minFiltering = val;
	}

	public int getId() {
		return this.register;
	}

	public boolean getMagFilter() {
		return this.magFiltering;
	}

	public boolean getMinFilter() {
		return this.minFiltering;
	}

	public void setClampTexture(boolean val) {
		this.clampTexture = val;
	}

	public boolean isClampTexture() {
		return this.clampTexture;
	}

	public void setRGBA(int x, int y, byte r, byte g, byte b, byte a) {
		int coord = (y * this.getWidth() + x) * 4;
		this.data[coord++] = r;
		this.data[coord++] = g;
		this.data[coord++] = b;
		this.data[coord] = a;
	}

	public void setRGB(int x, int y, byte r, byte g, byte b) {
		int coord = (y * this.getWidth() + x) * 4;
		this.data[coord++] = r;
		this.data[coord++] = g;
		this.data[coord++] = b;
		this.data[coord] = -1;
	}

	public void setRGB(int x, int y, int argb) {
		int coord = (y * this.getWidth() + x) * 4;
		this.data[coord++] = (byte)(argb >> 16);
		this.data[coord++] = (byte)(argb >> 8);
		this.data[coord++] = (byte)(argb);
		this.data[coord] = (byte)(argb >> 24);
	}

	public static void createTexture(int[] colors1, int x, int y, int texture, boolean z4, boolean z5) {
		byte[] colors2 = new byte[x * y * 4];
		int index = 0;
		int size = colors1.length;

		for(int coord = 0; index < size; ++index) {
			int argb = colors1[index];
			colors2[coord++] = (byte)(argb >> 16);
			colors2[coord++] = (byte)(argb >> 8);
			colors2[coord++] = (byte)(argb);
			colors2[coord++] = (byte)(argb >> 24);
		}

		createTexture(colors2, x, y, texture, z4, z5);
	}

	public static void createTexture(byte[] colors, int x, int y, int texture, boolean linear, boolean clamp) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		buffer.clear();
		buffer.put(colors);
		buffer.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, x, y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}
}
