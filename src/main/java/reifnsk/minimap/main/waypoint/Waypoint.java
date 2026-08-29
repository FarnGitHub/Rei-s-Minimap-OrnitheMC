package reifnsk.minimap.main.waypoint;

import reifnsk.minimap.main.render.GLTexture;

public class Waypoint {
	public static final GLTexture[] FILE = new GLTexture[]{GLTexture.WAYPOINT1, GLTexture.WAYPOINT2};
	public static final GLTexture[] MARKER = new GLTexture[]{GLTexture.MARKER1, GLTexture.MARKER2};
	public String name;
	public int x;
	public int y;
	public int z;
	public boolean enable;
	public float red;
	public float green;
	public float blue;
	public int type;

	public Waypoint(String name, int x, int y, int z, boolean enabled, float r, float g, float b) {
		this.name = name == null ? "" : name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.enable = enabled;
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public Waypoint(String name, int x, int y, int z, boolean enabled, float r, float g, float b, int type) {
		this.name = name == null ? "" : name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.enable = enabled;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.type = Math.max(0, type <= 1 ? type : 0);
	}

	public Waypoint(Waypoint wp) {
		this.set(wp);
	}

	public void set(Waypoint wp) {
		this.name = wp.name;
		this.x = wp.x;
		this.y = wp.y;
		this.z = wp.z;
		this.enable = wp.enable;
		this.red = wp.red;
		this.green = wp.green;
		this.blue = wp.blue;
		this.type = Math.max(0, wp.type <= 1 ? wp.type : 0);
	}

	public static Waypoint load(String lines) {
		try {
			String[] sLines = lines.split(":");
			String name = sLines[0];
			int x = Integer.parseInt(sLines[1]);
			int y = Integer.parseInt(sLines[2]);
			int z = Integer.parseInt(sLines[3]);
			boolean enable = Boolean.parseBoolean(sLines[4]);
			int rgb = Integer.parseInt(sLines[5], 16);
			float r = (float)(rgb >> 16 & 255) / 255.0F;
			float g = (float)(rgb >> 8 & 255) / 255.0F;
			float b = (float)(rgb & 255) / 255.0F;
			int type = sLines.length >= 7 ? Integer.parseInt(sLines[6]) : 0;
			return new Waypoint(name, x, y, z, enable, r, g, b, type);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String toString() {
		int r = (int)(this.red * 255.0F) & 255;
		int g = (int)(this.green * 255.0F) & 255;
		int b = (int)(this.blue * 255.0F) & 255;
		int rgb = r << 16 | g << 8 | b;
		return String.format(this.type == 0 ? "%s:%d:%d:%d:%s:%06X" : "%s:%d:%d:%d:%s:%06X:%d", this.name, this.x, this.y, this.z, this.enable, rgb, this.type);
	}
}
