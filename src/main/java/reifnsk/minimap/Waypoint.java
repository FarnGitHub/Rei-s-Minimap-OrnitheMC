package reifnsk.minimap;

public class Waypoint {
	private static final int MAX_TYPE_VALUE = 1;
	public static final int NORMAL = 0;
	public static final int DEATH_POINT = 1;
	static final GLTexture[] FILE = new GLTexture[]{GLTexture.WAYPOINT1, GLTexture.WAYPOINT2};
	static final GLTexture[] MARKER = new GLTexture[]{GLTexture.MARKER1, GLTexture.MARKER2};
	public String name;
	public int x;
	public int y;
	public int z;
	public boolean enable;
	public float red;
	public float green;
	public float blue;
	public int type;

	Waypoint(String string1, int i2, int i3, int i4, boolean z5, float f6, float f7, float f8) {
		this.name = string1 == null ? "" : string1;
		this.x = i2;
		this.y = i3;
		this.z = i4;
		this.enable = z5;
		this.red = f6;
		this.green = f7;
		this.blue = f8;
	}

	Waypoint(String string1, int i2, int i3, int i4, boolean z5, float f6, float f7, float f8, int i9) {
		this.name = string1 == null ? "" : string1;
		this.x = i2;
		this.y = i3;
		this.z = i4;
		this.enable = z5;
		this.red = f6;
		this.green = f7;
		this.blue = f8;
		this.type = Math.max(0, i9 <= 1 ? i9 : 0);
	}

	Waypoint(Waypoint waypoint1) {
		this.set(waypoint1);
	}

	void set(Waypoint waypoint1) {
		this.name = waypoint1.name;
		this.x = waypoint1.x;
		this.y = waypoint1.y;
		this.z = waypoint1.z;
		this.enable = waypoint1.enable;
		this.red = waypoint1.red;
		this.green = waypoint1.green;
		this.blue = waypoint1.blue;
		this.type = Math.max(0, waypoint1.type <= 1 ? waypoint1.type : 0);
	}

	static Waypoint load(String string0) {
		try {
			String[] string1 = string0.split(":");
			String string2 = string1[0];
			int i3 = Integer.parseInt(string1[1]);
			int i4 = Integer.parseInt(string1[2]);
			int i5 = Integer.parseInt(string1[3]);
			boolean z6 = Boolean.parseBoolean(string1[4]);
			int i7 = Integer.parseInt(string1[5], 16);
			float f8 = (float)(i7 >> 16 & 255) / 255.0F;
			float f9 = (float)(i7 >> 8 & 255) / 255.0F;
			float f10 = (float)(i7 >> 0 & 255) / 255.0F;
			int i11 = string1.length >= 7 ? Integer.parseInt(string1[6]) : 0;
			return new Waypoint(string2, i3, i4, i5, z6, f8, f9, f10, i11);
		} catch (RuntimeException runtimeException12) {
			runtimeException12.printStackTrace();
			return null;
		}
	}

	public String toString() {
		int i1 = (int)(this.red * 255.0F) & 255;
		int i2 = (int)(this.green * 255.0F) & 255;
		int i3 = (int)(this.blue * 255.0F) & 255;
		int i4 = i1 << 16 | i2 << 8 | i3;
		return String.format(this.type == 0 ? "%s:%d:%d:%d:%s:%06X" : "%s:%d:%d:%d:%s:%06X:%d", new Object[]{this.name, this.x, this.y, this.z, this.enable, i4, this.type});
	}
}
