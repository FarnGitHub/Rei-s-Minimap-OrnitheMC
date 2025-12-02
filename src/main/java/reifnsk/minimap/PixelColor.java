package reifnsk.minimap;

public class PixelColor {
	static final float d = 0.003921569F;
	public final boolean alphaComposite;
	public float red;
	public float green;
	public float blue;
	public float alpha;

	public PixelColor() {
		this(true);
	}

	public PixelColor(boolean z1) {
		this.alphaComposite = z1;
	}

	public void clear() {
		this.red = this.green = this.blue = this.alpha = 0.0F;
	}

	public void composite(int i1) {
		this.composite(i1, 1.0F);
	}

	public void composite(int i1, float f2) {
		if(this.alphaComposite) {
			float f3 = (float)(i1 >> 24 & 255) * 0.003921569F;
			float f4 = (float)(i1 >> 16 & 255) * 0.003921569F * f2;
			float f5 = (float)(i1 >> 8 & 255) * 0.003921569F * f2;
			float f6 = (float)(i1 >> 0 & 255) * 0.003921569F * f2;
			this.red += (f4 - this.red) * f3;
			this.green += (f5 - this.green) * f3;
			this.blue += (f6 - this.blue) * f3;
			this.alpha += (1.0F - this.alpha) * f3;
		} else {
			this.alpha = (float)(i1 >> 24 & 255) * 0.003921569F;
			this.red = (float)(i1 >> 16 & 255) * 0.003921569F * f2;
			this.green = (float)(i1 >> 8 & 255) * 0.003921569F * f2;
			this.blue = (float)(i1 >> 0 & 255) * 0.003921569F * f2;
		}

	}

	public void composite(float f1, int i2, float f3) {
		if(this.alphaComposite) {
			float f5 = (float)(i2 >> 16 & 255) * 0.003921569F * f3;
			float f6 = (float)(i2 >> 8 & 255) * 0.003921569F * f3;
			float f7 = (float)(i2 >> 0 & 255) * 0.003921569F * f3;
			this.red += (f5 - this.red) * f1;
			this.green += (f6 - this.green) * f1;
			this.blue += (f7 - this.blue) * f1;
			this.alpha += (1.0F - this.alpha) * f1;
		} else {
			this.alpha = (float)(i2 >> 24 & 255) * 0.003921569F;
			this.red = (float)(i2 >> 16 & 255) * 0.003921569F * f3;
			this.green = (float)(i2 >> 8 & 255) * 0.003921569F * f3;
			this.blue = (float)(i2 >> 0 & 255) * 0.003921569F * f3;
		}

	}

	public void composite(float f1, int i2, float f3, float f4, float f5) {
		if(this.alphaComposite) {
			float f7 = (float)(i2 >> 16 & 255) * 0.003921569F * f3;
			float f8 = (float)(i2 >> 8 & 255) * 0.003921569F * f4;
			float f9 = (float)(i2 >> 0 & 255) * 0.003921569F * f5;
			this.red += (f7 - this.red) * f1;
			this.green += (f8 - this.green) * f1;
			this.blue += (f9 - this.blue) * f1;
			this.alpha += (1.0F - this.alpha) * f1;
		} else {
			this.alpha = (float)(i2 >> 24 & 255) * 0.003921569F;
			this.red = (float)(i2 >> 16 & 255) * 0.003921569F * f3;
			this.green = (float)(i2 >> 8 & 255) * 0.003921569F * f4;
			this.blue = (float)(i2 >> 0 & 255) * 0.003921569F * f5;
		}

	}

	public void composite(float f1, float f2, float f3, float f4) {
		if(this.alphaComposite) {
			this.red += (f2 - this.red) * f1;
			this.green += (f3 - this.green) * f1;
			this.blue += (f4 - this.blue) * f1;
			this.alpha += (1.0F - this.alpha) * f1;
		} else {
			this.alpha = f1;
			this.red = f2;
			this.green = f3;
			this.blue = f4;
		}

	}

	public void composite(float f1, float f2, float f3, float f4, float f5) {
		if(this.alphaComposite) {
			this.red += (f2 * f5 - this.red) * f1;
			this.green += (f3 * f5 - this.green) * f1;
			this.blue += (f4 * f5 - this.blue) * f1;
			this.alpha += (1.0F - this.alpha) * f1;
		} else {
			this.alpha = f1;
			this.red = f2 * f5;
			this.green = f3 * f5;
			this.blue = f4 * f5;
		}

	}
}
