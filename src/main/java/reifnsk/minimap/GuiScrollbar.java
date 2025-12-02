package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiScrollbar extends ButtonWidget {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private long repeatStart = 500000000L;
	private long repeatInterval = 40000000L;
	int orientation;
	private float value = 0.0F;
	private float extent = 0.0F;
	private float min = 0.0F;
	private float max = 0.0F;
	private float unitIncrement = 1.0F;
	private float blockIncrement = 9.0F;
	private int draggingPos;
	private float draggingValue;
	private int dragging;
	private long draggingTimer;
	private int minBarSize = 6;

	public GuiScrollbar(int i1, int i2, int i3, int i4, int i5) {
		super(i1, i2, i3, i4, i5, "");
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		if(this.value > this.max - this.extent) {
			this.value = this.max - this.extent;
		}

		if(this.value < this.min) {
			this.value = this.min;
		}

		if(this.orientation == 0) {
			this.drawVertical(minecraft1, i2, i3);
		} else if(this.orientation == 1) {
			this.drawHorizontal(minecraft1, i2, i3);
		}

	}

	private void drawVertical(Minecraft minecraft1, int i2, int i3) {
		if(this.dragging != 0) {
			this.mouseDragged(minecraft1, i2, i3);
		}

		double d4 = (double)this.x + (double)this.width * 0.5D;
		int i6 = this.y;
		int i7 = this.y + this.height;
		Tessellator tessellator8 = Tessellator.INSTANCE;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean z9 = (double)i2 >= d4 - 4.0D && (double)i2 <= d4 + 4.0D;
		if(!z9 || i3 < i6 || i3 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startQuads();
		tessellator8.vertex(d4, (double)i6, 0.0D);
		tessellator8.vertex(d4, (double)i6, 0.0D);
		tessellator8.vertex(d4 - 4.0D, (double)(i6 + 8), 0.0D);
		tessellator8.vertex(d4 + 4.0D, (double)(i6 + 8), 0.0D);
		tessellator8.draw();
		double d10;
		double d12;
		if(this.min < this.max - this.extent) {
			d10 = (double)(this.height - 20);
			d12 = (double)(this.extent / (this.max - this.min));
			if(d12 * d10 < (double)this.minBarSize) {
				d12 = (double)this.minBarSize / d10;
			}

			double d14 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d12);
			double d16 = d14 + d12;
			d14 = (double)i6 + d14 * d10 + 10.0D;
			d16 = (double)i6 + d16 * d10 + 10.0D;
			if(this.dragging != 5 && (!z9 || (double)i3 < d14 || (double)i3 > d16 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startQuads();
			tessellator8.vertex(d4 + 4.0D, d14, 0.0D);
			tessellator8.vertex(d4 - 4.0D, d14, 0.0D);
			tessellator8.vertex(d4 - 4.0D, d16, 0.0D);
			tessellator8.vertex(d4 + 4.0D, d16, 0.0D);
			tessellator8.draw();
		} else {
			d10 = (double)(i6 + 10);
			d12 = (double)(i7 - 10);
			if(this.dragging != 5 && (!z9 || (double)i3 < d10 || (double)i3 > d12 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startQuads();
			tessellator8.vertex(d4 + 4.0D, d10, 0.0D);
			tessellator8.vertex(d4 - 4.0D, d10, 0.0D);
			tessellator8.vertex(d4 - 4.0D, d12, 0.0D);
			tessellator8.vertex(d4 + 4.0D, d12, 0.0D);
			tessellator8.draw();
		}

		if(!z9 || i3 < i7 - 8 || i3 > i7 || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startQuads();
		tessellator8.vertex(d4, (double)i7, 0.0D);
		tessellator8.vertex(d4, (double)i7, 0.0D);
		tessellator8.vertex(d4 + 4.0D, (double)(i7 - 8), 0.0D);
		tessellator8.vertex(d4 - 4.0D, (double)(i7 - 8), 0.0D);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawHorizontal(Minecraft minecraft1, int i2, int i3) {
		if(this.dragging != 0) {
			this.mouseDragged(minecraft1, i2, i3);
		}

		double d4 = (double)this.y + (double)this.height * 0.5D;
		int i6 = this.x;
		int i7 = this.x + this.width;
		Tessellator tessellator8 = Tessellator.INSTANCE;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean z9 = (double)i3 >= d4 - 4.0D && (double)i3 <= d4 + 4.0D;
		if(!z9 || i2 < i6 || i2 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startQuads();
		tessellator8.vertex((double)i6, d4, 0.0D);
		tessellator8.vertex((double)i6, d4, 0.0D);
		tessellator8.vertex((double)(i6 + 8), d4 + 4.0D, 0.0D);
		tessellator8.vertex((double)(i6 + 8), d4 - 4.0D, 0.0D);
		tessellator8.draw();
		double d10;
		double d12;
		if(this.min < this.max - this.extent) {
			d10 = (double)(this.width - 20);
			d12 = (double)(this.extent / (this.max - this.min));
			if(d12 * d10 < (double)this.minBarSize) {
				d12 = (double)this.minBarSize / d10;
			}

			double d14 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d12);
			double d16 = d14 + d12;
			d14 = (double)i6 + d14 * d10 + 10.0D;
			d16 = (double)i6 + d16 * d10 + 10.0D;
			if(this.dragging != 6 && (!z9 || (double)i2 < d14 || (double)i2 > d16 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startQuads();
			tessellator8.vertex(d14, d4 - 4.0D, 0.0D);
			tessellator8.vertex(d14, d4 + 4.0D, 0.0D);
			tessellator8.vertex(d16, d4 + 4.0D, 0.0D);
			tessellator8.vertex(d16, d4 - 4.0D, 0.0D);
			tessellator8.draw();
		} else {
			d10 = (double)(i6 + 10);
			d12 = (double)(i7 - 10);
			if(this.dragging != 6 && (!z9 || (double)i2 < d10 || (double)i2 > d12 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startQuads();
			tessellator8.vertex(d10, d4 - 4.0D, 0.0D);
			tessellator8.vertex(d10, d4 + 4.0D, 0.0D);
			tessellator8.vertex(d12, d4 + 4.0D, 0.0D);
			tessellator8.vertex(d12, d4 - 4.0D, 0.0D);
			tessellator8.draw();
		}

		if(!z9 || i2 < i7 - 8 || i2 > i7 || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startQuads();
		tessellator8.vertex((double)i7, d4, 0.0D);
		tessellator8.vertex((double)i7, d4, 0.0D);
		tessellator8.vertex((double)(i7 - 8), d4 - 4.0D, 0.0D);
		tessellator8.vertex((double)(i7 - 8), d4 + 4.0D, 0.0D);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean isMouseOver(Minecraft minecraft1, int i2, int i3) {
		return super.isMouseOver(minecraft1, i2, i3) ? (this.orientation == 0 ? this.mousePressedVertical(minecraft1, i2, i3) : (this.orientation == 1 ? this.mousePressedHorizontal(minecraft1, i2, i3) : false)) : false;
	}

	private boolean mousePressedVertical(Minecraft minecraft1, int i2, int i3) {
		double d4 = (double)this.x + (double)this.width * 0.5D;
		int i6 = this.y;
		int i7 = this.y + this.height;
		if((double)i2 >= d4 - 4.0D && (double)i2 <= d4 + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(i3 < i6 || i3 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
					if(i3 < i7 - 8 || i3 > i7 || this.dragging != 0 && this.dragging != 2) {
						double d8 = (double)(this.height - 20);
						double d10 = (double)(this.extent / (this.max - this.min));
						if(d10 * d8 < (double)this.minBarSize) {
							d10 = (double)this.minBarSize / d8;
						}

						double d12 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d10);
						double d14 = d12 + d10;
						d12 = (double)i6 + d12 * d8 + 10.0D;
						d14 = (double)i6 + d14 * d8 + 10.0D;
						if((double)i3 >= d12 || this.dragging != 0 && this.dragging != 3) {
							if((double)i3 > d14 && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 5;
									this.draggingPos = i3;
									this.draggingValue = this.value;
								}

								return true;
							}
						} else {
							this.dragging = 3;
							this.blockDecrement();
							return true;
						}
					} else {
						this.dragging = 2;
						this.unitIncrement();
						return true;
					}
				} else {
					this.dragging = 1;
					this.unitDecrement();
					return true;
				}
			}
		} else {
			return false;
		}
	}

	private boolean mousePressedHorizontal(Minecraft minecraft1, int i2, int i3) {
		double d4 = (double)this.y + (double)this.height * 0.5D;
		int i6 = this.x;
		int i7 = this.x + this.width;
		if((double)i3 >= d4 - 4.0D && (double)i3 <= d4 + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(i2 < i6 || i2 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
					if(i2 < i7 - 8 || i2 > i7 || this.dragging != 0 && this.dragging != 2) {
						double d8 = (double)(this.width - 20);
						double d10 = (double)(this.extent / (this.max - this.min));
						if(d10 * d8 < (double)this.minBarSize) {
							d10 = (double)this.minBarSize / d8;
						}

						double d12 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d10);
						double d14 = d12 + d10;
						d12 = (double)i6 + d12 * d8 + 10.0D;
						d14 = (double)i6 + d14 * d8 + 10.0D;
						if((double)i2 >= d12 || this.dragging != 0 && this.dragging != 3) {
							if((double)i2 > d14 && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 6;
									this.draggingPos = i2;
									this.draggingValue = this.value;
								}

								return true;
							}
						} else {
							this.dragging = 3;
							this.blockDecrement();
							return true;
						}
					} else {
						this.dragging = 2;
						this.unitIncrement();
						return true;
					}
				} else {
					this.dragging = 1;
					this.unitDecrement();
					return true;
				}
			}
		} else {
			return false;
		}
	}

	protected void mouseDragged(Minecraft minecraft1, int i2, int i3) {
		float f4;
		float f5;
		float f6;
		if(this.dragging == 5) {
			f4 = (float)(this.height - 20);
			f5 = this.extent / (this.max - this.min);
			if(f5 * f4 < (float)this.minBarSize) {
				f5 = (float)this.minBarSize / f4;
			}

			f6 = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - f5) * (float)(i3 - this.draggingPos) / f4;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, f6));
		}

		if(this.dragging == 6) {
			f4 = (float)(this.width - 20);
			f5 = this.extent / (this.max - this.min);
			if(f5 * f4 < (float)this.minBarSize) {
				f5 = (float)this.minBarSize / f4;
			}

			f6 = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - f5) * (float)(i2 - this.draggingPos) / f4;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, f6));
		}

		long j7 = System.nanoTime();
		if(this.draggingTimer < j7) {
			this.isMouseOver(minecraft1, i2, i3);
			this.draggingTimer = j7 + this.repeatInterval;
		}

	}

	public void mouseReleased(int i1, int i2) {
		this.dragging = 0;
	}

	public void setValue(float f1) {
		if(f1 < this.min) {
			f1 = this.min;
		}

		if(f1 > this.max - this.extent) {
			f1 = this.max - this.extent;
		}

		this.value = f1;
	}

	public float getValue() {
		return this.value;
	}

	public void setMaximum(float f1) {
		if(this.min > f1) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.max = f1;
			this.value = Math.min(this.value, this.max);
		}
	}

	public float getMaximum() {
		return this.max;
	}

	public void setMinimum(float f1) {
		if(f1 > this.max) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.min = f1;
			this.value = Math.max(this.value, this.min);
		}
	}

	public float getMinimum() {
		return this.min;
	}

	public void setVisibleAmount(float f1) {
		if(this.max - this.min < f1) {
			throw new IllegalArgumentException("max - min < extent");
		} else {
			this.extent = Math.min(this.max - this.min, f1);
		}
	}

	public float getVisibleAmount() {
		return this.extent;
	}

	public void unitIncrement() {
		this.value = Math.min(this.max - this.extent, this.value + this.unitIncrement);
	}

	public void unitDecrement() {
		this.value = Math.max(this.min, this.value - this.unitIncrement);
	}

	public void blockIncrement() {
		this.value = Math.min(this.max - this.extent, this.value + this.blockIncrement);
	}

	public void blockDecrement() {
		this.value = Math.max(this.min, this.value - this.blockIncrement);
	}

	public void setMinimumBarSize(int i1) {
		this.minBarSize = i1;
	}

	public int getMinimumBarSize() {
		return this.minBarSize;
	}

	public void setUnitIncrement(float f1) {
		this.unitIncrement = f1;
	}

	public void setBlockIncrement(float f1) {
		this.blockIncrement = f1;
	}

	public float getUnitIncrement() {
		return this.unitIncrement;
	}

	public float getBlockIncrement() {
		return this.blockIncrement;
	}
}
