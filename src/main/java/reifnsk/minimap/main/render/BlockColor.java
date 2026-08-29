package reifnsk.minimap.main.render;

public final class BlockColor {
	private final int argb;
	public final TintType tintType;
	public final float alpha;
	public final float red;
	public final float green;
	public final float blue;

	BlockColor(int argb, TintType tint) {
		if(tint == null) {
			tint = TintType.NONE;
		}

		float a = (float)(argb >> 24 & 255) * 0.003921569F;
		float r = (float)(argb >> 16 & 255) * 0.003921569F;
		float g = (float)(argb >> 8 & 255) * 0.003921569F;
		float b = (float)(argb & 255) * 0.003921569F;
		this.alpha = a;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.argb = argb;
		this.tintType = tint;
	}

	public String toString() {
		return String.format("%08X:%s", this.argb, this.tintType);
	}

	public int hashCode() {
		return this.argb;
	}

	public boolean equals(Object obj) {
		return obj instanceof BlockColor && this.equals((BlockColor)obj);
	}

	public boolean equals(BlockColor color) {
		return this.argb == color.argb && this.tintType == color.tintType;
	}
}
