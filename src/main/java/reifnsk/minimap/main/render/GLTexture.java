package reifnsk.minimap.main.render;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GLTexture {
	private static final String DEFAULT_PACK = "/reifnsk/minimap/reitextures/";
	private static String pack = DEFAULT_PACK;
	private static final ArrayList<GLTexture> list = new ArrayList<>();
	private static final GLTexture missing = new GLTexture("missing.png", true, false);
	public static final GLTexture TEMPERATURE = new GLTexture("temperature.png", true, true);
	public static final GLTexture HUMIDITY = new GLTexture("humidity.png", true, true);
	public static final GLTexture ROUND_MAP = new GLTexture("roundmap.png", true, true);
	public static final GLTexture ROUND_MAP_MASK = new GLTexture("roundmap_mask.png", false, true);
	public static final GLTexture SQUARE_MAP = new GLTexture("squaremap.png", true, true);
	public static final GLTexture SQUARE_MAP_MASK = new GLTexture("squaremap_mask.png", false, true);
	public static final GLTexture ENTITY = new GLTexture("entity.png", true, true);
	public static final GLTexture ENTITY2 = new GLTexture("entity2.png", true, true);
	public static final GLTexture LIGHTNING = new GLTexture("lightning.png", true, true);
	public static final GLTexture N = new GLTexture("n.png", true, true);
	public static final GLTexture E = new GLTexture("e.png", true, true);
	public static final GLTexture W = new GLTexture("w.png", true, true);
	public static final GLTexture S = new GLTexture("s.png", true, true);
	public static final GLTexture MMARROW = new GLTexture("mmarrow.png", true, true);
	public static final GLTexture WAYPOINT1 = new GLTexture("waypoint.png", true, true);
	public static final GLTexture WAYPOINT2 = new GLTexture("waypoint2.png", true, true);
	public static final GLTexture MARKER1 = new GLTexture("marker.png", true, true);
	public static final GLTexture MARKER2 = new GLTexture("marker2.png", true, true);
	private final String fileName;
	private final boolean blur;
	private final boolean clamp;
	private int textureId;

	public static void setPack(String str) {
		if(!str.equals(pack)) {
            for (GLTexture gLTexture1 : list) {
                gLTexture1.release();
            }
			pack = str;
		}
	}

	private GLTexture(String string1, boolean z2, boolean z3) {
		this.fileName = string1;
		this.blur = z2;
		this.clamp = z3;
		list.add(this);
	}

	public int[] getData() {
		BufferedImage bufferedImage1 = read(this.fileName);
		int i2 = bufferedImage1.getWidth();
		int i3 = bufferedImage1.getHeight();
		int[] i4 = new int[i2 * i3];
		bufferedImage1.getRGB(0, 0, i2, i3, i4, 0, i2);
		return i4;
	}

	public void bind() {
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

	public void release() {
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
		try (InputStream strean = Minecraft.INSTANCE.texturePacks.selected.getResource(string0)) {
			return ImageIO.read(strean);
		} catch (Exception ignored) {
		}
        return null;
    }
}
