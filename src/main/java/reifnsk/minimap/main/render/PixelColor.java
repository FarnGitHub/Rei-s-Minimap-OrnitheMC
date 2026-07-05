package reifnsk.minimap.main.render;

@SuppressWarnings("unused")
public class PixelColor {
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

	public void composite(int argb, float alpha) {
		if(this.alphaComposite) {
			float a = (float)(argb >> 24 & 255) * 0.003921569F;
			float r = (float)(argb >> 16 & 255) * 0.003921569F * alpha;
			float g = (float)(argb >> 8 & 255) * 0.003921569F * alpha;
			float b = (float)(argb & 255) * 0.003921569F * alpha;
			this.red += (r - this.red) * a;
			this.green += (g - this.green) * a;
			this.blue += (b - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = (float)(argb >> 24 & 255) * 0.003921569F;
			this.red = (float)(argb >> 16 & 255) * 0.003921569F * alpha;
			this.green = (float)(argb >> 8 & 255) * 0.003921569F * alpha;
			this.blue = (float)(argb & 255) * 0.003921569F * alpha;
		}

	}

	public void composite(float alpha, int rgb, float brightness) {
		if(this.alphaComposite) {
			float r = (float)(rgb >> 16 & 255) * 0.003921569F * brightness;
			float g = (float)(rgb >> 8 & 255) * 0.003921569F * brightness;
			float b = (float)(rgb & 255) * 0.003921569F * brightness;
			this.red += (r - this.red) * alpha;
			this.green += (g - this.green) * alpha;
			this.blue += (b - this.blue) * alpha;
			this.alpha += (1.0F - this.alpha) * alpha;
		} else {
			this.alpha = (float)(rgb >> 24 & 255) * 0.003921569F;
			this.red = (float)(rgb >> 16 & 255) * 0.003921569F * brightness;
			this.green = (float)(rgb >> 8 & 255) * 0.003921569F * brightness;
			this.blue = (float)(rgb & 255) * 0.003921569F * brightness;
		}

	}

	public void composite(float brightness, int rgb, float b1, float b2, float b3) {
		if(this.alphaComposite) {
			float r = (float)(rgb >> 16 & 255) * 0.003921569F * b1;
			float g = (float)(rgb >> 8 & 255) * 0.003921569F * b2;
			float b = (float)(rgb & 255) * 0.003921569F * b3;
			this.red += (r - this.red) * brightness;
			this.green += (g - this.green) * brightness;
			this.blue += (b - this.blue) * brightness;
			this.alpha += (1.0F - this.alpha) * brightness;
		} else {
			this.alpha = (float)(rgb >> 24 & 255) * 0.003921569F;
			this.red = (float)(rgb >> 16 & 255) * 0.003921569F * b1;
			this.green = (float)(rgb >> 8 & 255) * 0.003921569F * b2;
			this.blue = (float)(rgb & 255) * 0.003921569F * b3;
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

	public void composite(float a, float r, float g, float b, float brightness) {
		if(this.alphaComposite) {
			this.red += (r * brightness - this.red) * a;
			this.green += (g * brightness - this.green) * a;
			this.blue += (b * brightness - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = a;
			this.red = r * brightness;
			this.green = g * brightness;
			this.blue = b * brightness;
		}

	}
}
