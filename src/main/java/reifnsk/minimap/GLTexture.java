package reifnsk.minimap;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class GLTexture {
	private static String DEFAULT_PACK = "/reifnsk/minimap/reitextures/";
	private static String pack = DEFAULT_PACK;
	private static ArrayList list = new ArrayList();
	private static GLTexture missing = new GLTexture("missing.png", true, false);
	static final GLTexture TEMPERATURE = new GLTexture("temperature.png", true, true);
	static final GLTexture HUMIDITY = new GLTexture("humidity.png", true, true);
	static final GLTexture ROUND_MAP = new GLTexture("roundmap.png", true, true);
	static final GLTexture ROUND_MAP_MASK = new GLTexture("roundmap_mask.png", false, true);
	static final GLTexture SQUARE_MAP = new GLTexture("squaremap.png", true, true);
	static final GLTexture SQUARE_MAP_MASK = new GLTexture("squaremap_mask.png", false, true);
	static final GLTexture ENTITY = new GLTexture("entity.png", true, true);
	static final GLTexture ENTITY2 = new GLTexture("entity2.png", true, true);
	static final GLTexture LIGHTNING = new GLTexture("lightning.png", true, true);
	static final GLTexture N = new GLTexture("n.png", true, true);
	static final GLTexture E = new GLTexture("e.png", true, true);
	static final GLTexture W = new GLTexture("w.png", true, true);
	static final GLTexture S = new GLTexture("s.png", true, true);
	static final GLTexture MMARROW = new GLTexture("mmarrow.png", true, true);
	static final GLTexture WAYPOINT1 = new GLTexture("waypoint.png", true, true);
	static final GLTexture WAYPOINT2 = new GLTexture("waypoint2.png", true, true);
	static final GLTexture MARKER1 = new GLTexture("marker.png", true, true);
	static final GLTexture MARKER2 = new GLTexture("marker2.png", true, true);
	private final String fileName;
	private final boolean blur;
	private final boolean clamp;
	private int textureId;

	static void setPack(String string0) {
		if(!string0.equals(pack)) {
			Iterator iterator2 = list.iterator();

			while(iterator2.hasNext()) {
				GLTexture gLTexture1 = (GLTexture)iterator2.next();
				gLTexture1.release();
			}

			pack = string0;
		}
	}

	private GLTexture(String string1, boolean z2, boolean z3) {
		this.fileName = string1;
		this.blur = z2;
		this.clamp = z3;
		list.add(this);
	}

	int[] getData() {
		BufferedImage bufferedImage1 = read(this.fileName);
		int i2 = bufferedImage1.getWidth();
		int i3 = bufferedImage1.getHeight();
		int[] i4 = new int[i2 * i3];
		bufferedImage1.getRGB(0, 0, i2, i3, i4, 0, i2);
		return i4;
	}

	void bind() {
		if(this.textureId == 0) {
			BufferedImage bufferedImage1 = read(this.fileName);
			if(bufferedImage1 == null) {
				this.textureId = this == missing ? -2 : -1;
			} else {
				this.textureId = GL11.glGenTextures();
				int i2 = bufferedImage1.getWidth();
				int i3 = bufferedImage1.getHeight();
				int[] i4 = new int[i2 * i3];
				bufferedImage1.getRGB(0, 0, i2, i3, i4, 0, i2);
				GLTextureBufferedImage.createTexture(i4, i2, i3, this.textureId, this.blur, this.clamp);
			}
		}

		if(this.textureId == -2) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		} else {
			if(this.textureId == -1) {
				missing.bind();
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
		}
	}

	void release() {
		if(this.textureId > 0) {
			GL11.glDeleteTextures(this.textureId);
		}

		this.textureId = 0;
	}

	private static BufferedImage read(String string0) {
		BufferedImage bufferedImage1 = readImage(pack + string0);
		return bufferedImage1 == null ? readImage(DEFAULT_PACK + string0) : bufferedImage1;
	}

	private static BufferedImage readImage(String string0) {
		InputStream inputStream1 = ReiMinimap.instance.theMinecraft.texturePacks.selected.getResource(string0);
		if(inputStream1 == null) {
			return null;
		} else {
			try {
				BufferedImage bufferedImage3 = ImageIO.read(inputStream1);
				return bufferedImage3;
			} catch (Exception exception10) {
			} finally {
				try {
					inputStream1.close();
				} catch (Exception exception9) {
				}

			}

			return null;
		}
	}
}
