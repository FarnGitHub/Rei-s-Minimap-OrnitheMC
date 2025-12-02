package reifnsk.minimap;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.client.util.GlAllocationUtils;
import org.lwjgl.opengl.GL11;

public class GLTextureBufferedImage extends BufferedImage {
	private static final ByteBuffer buffer = GlAllocationUtils.allocateByteBuffer(262144);
	private static final HashMap registerImage = new HashMap();
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

	public static GLTextureBufferedImage create(int i0, int i1) {
		ColorSpace colorSpace2 = ColorSpace.getInstance(1000);
		int[] i3 = new int[]{8, 8, 8, 8};
		int[] i4 = new int[]{0, 1, 2, 3};
		ComponentColorModel componentColorModel5 = new ComponentColorModel(colorSpace2, i3, true, false, 3, 0);
		WritableRaster writableRaster6 = Raster.createInterleavedRaster(0, i0, i1, i0 * 4, 4, i4, (Point)null);
		return new GLTextureBufferedImage(componentColorModel5, writableRaster6, false, (Hashtable)null);
	}

	public static GLTextureBufferedImage create(BufferedImage bufferedImage0) {
		GLTextureBufferedImage gLTextureBufferedImage1 = create(bufferedImage0.getWidth(), bufferedImage0.getHeight());
		Graphics graphics2 = gLTextureBufferedImage1.getGraphics();
		graphics2.drawImage(bufferedImage0, 0, 0, (ImageObserver)null);
		graphics2.dispose();
		return gLTextureBufferedImage1;
	}

	public int register() {
		lock.lock();

		int i3;
		try {
			int i1;
			if(this.register == 0) {
				this.register = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				i1 = this.clampTexture ? 10496 : 10497;
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, i1);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, i1);
				buffer.clear();
				buffer.put(this.data);
				buffer.flip();
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.getWidth(), this.getHeight(), 0, 6408, 5121, buffer);
				registerImage.put(this.register, this);
				i3 = this.register;
				return i3;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			i1 = this.clampTexture ? 10496 : 10497;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, i1);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, i1);
			buffer.clear();
			buffer.put(this.data);
			buffer.flip();
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, this.getWidth(), this.getHeight(), 6408, 5121, buffer);
			i3 = this.register;
		} finally {
			lock.unlock();
		}

		return i3;
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
				return;
			}
		} finally {
			lock.unlock();
		}

	}

	public static void unregister(int i0) {
		lock.lock();

		try {
			GLTextureBufferedImage gLTextureBufferedImage1 = (GLTextureBufferedImage)registerImage.get(i0);
			if(gLTextureBufferedImage1 != null) {
				gLTextureBufferedImage1.unregister();
			}
		} finally {
			lock.unlock();
		}

	}

	public void setMagFilter(boolean z1) {
		this.magFiltering = z1;
	}

	public void setMinFilter(boolean z1) {
		this.minFiltering = z1;
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

	public void setClampTexture(boolean z1) {
		this.clampTexture = z1;
	}

	public boolean isClampTexture() {
		return this.clampTexture;
	}

	public void setRGBA(int i1, int i2, byte b3, byte b4, byte b5, byte b6) {
		int i7 = (i2 * this.getWidth() + i1) * 4;
		this.data[i7++] = b3;
		this.data[i7++] = b4;
		this.data[i7++] = b5;
		this.data[i7] = b6;
	}

	public void setRGB(int i1, int i2, byte b3, byte b4, byte b5) {
		int i6 = (i2 * this.getWidth() + i1) * 4;
		this.data[i6++] = b3;
		this.data[i6++] = b4;
		this.data[i6++] = b5;
		this.data[i6] = -1;
	}

	public void setRGB(int i1, int i2, int i3) {
		int i4 = (i2 * this.getWidth() + i1) * 4;
		this.data[i4++] = (byte)(i3 >> 16);
		this.data[i4++] = (byte)(i3 >> 8);
		this.data[i4++] = (byte)(i3 >> 0);
		this.data[i4] = (byte)(i3 >> 24);
	}

	public static void createTexture(int[] i0, int i1, int i2, int i3, boolean z4, boolean z5) {
		byte[] b6 = new byte[i1 * i2 * 4];
		int i7 = 0;
		int i8 = i0.length;

		for(int i9 = 0; i7 < i8; ++i7) {
			int i10 = i0[i7];
			b6[i9++] = (byte)(i10 >> 16);
			b6[i9++] = (byte)(i10 >> 8);
			b6[i9++] = (byte)(i10 >> 0);
			b6[i9++] = (byte)(i10 >> 24);
		}

		createTexture(b6, i1, i2, i3, z4, z5);
	}

	public static void createTexture(byte[] b0, int i1, int i2, int i3, boolean z4, boolean z5) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i3);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, z4 ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, z4 ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, z5 ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, z5 ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		buffer.clear();
		buffer.put(b0);
		buffer.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i1, i2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}
}
