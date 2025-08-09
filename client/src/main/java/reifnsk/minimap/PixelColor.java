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

	public PixelColor(boolean alphaComposite) {
		this.alphaComposite = alphaComposite;
	}

	public void clear() {
		this.red = this.green = this.blue = this.alpha = 0.0F;
	}

	public void composite(int argb) {
		this.composite(argb, 1.0F);
	}

	public void composite(int argb, float light) {
		if(this.alphaComposite) {
			float a = (float)(argb >> 24 & 255) * 0.003921569F;
			float r = (float)(argb >> 16 & 255) * 0.003921569F * light;
			float g = (float)(argb >> 8 & 255) * 0.003921569F * light;
			float b = (float)(argb >> 0 & 255) * 0.003921569F * light;
			this.red += (r - this.red) * a;
			this.green += (g - this.green) * a;
			this.blue += (b - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = (float)(argb >> 24 & 255) * 0.003921569F;
			this.red = (float)(argb >> 16 & 255) * 0.003921569F * light;
			this.green = (float)(argb >> 8 & 255) * 0.003921569F * light;
			this.blue = (float)(argb >> 0 & 255) * 0.003921569F * light;
		}

	}

	public void composite(float alpha, int rgb, float light) {
		if(this.alphaComposite) {
			float r = (float)(rgb >> 16 & 255) * 0.003921569F * light;
			float g = (float)(rgb >> 8 & 255) * 0.003921569F * light;
			float b = (float)(rgb >> 0 & 255) * 0.003921569F * light;
			this.red += (r - this.red) * alpha;
			this.green += (g - this.green) * alpha;
			this.blue += (b - this.blue) * alpha;
			this.alpha += (1.0F - this.alpha) * alpha;
		} else {
			this.alpha = (float)(rgb >> 24 & 255) * 0.003921569F;
			this.red = (float)(rgb >> 16 & 255) * 0.003921569F * light;
			this.green = (float)(rgb >> 8 & 255) * 0.003921569F * light;
			this.blue = (float)(rgb >> 0 & 255) * 0.003921569F * light;
		}

	}

	public void composite(float a, float r, float g, float b) {
		if(this.alphaComposite) {
			this.red += (r - this.red) * a;
			this.green += (g - this.green) * a;
			this.blue += (b - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = a;
			this.red = r;
			this.green = g;
			this.blue = b;
		}

	}

	public void composite(float a, float r, float g, float b, float light) {
		if(this.alphaComposite) {
			this.red += (r * light - this.red) * a;
			this.green += (g * light - this.green) * a;
			this.blue += (b * light - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = a;
			this.red = r * light;
			this.green = g * light;
			this.blue = b * light;
		}

	}
}
