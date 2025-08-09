package reifnsk.minimap;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

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

	public GuiScrollbar(int id, int x, int y, int w, int h) {
		super(id, x, y, w, h, "");
	}

	public void render(Minecraft mc, int i, int j) {
		if(this.value > this.max - this.extent) {
			this.value = this.max - this.extent;
		}

		if(this.value < this.min) {
			this.value = this.min;
		}

		if(this.orientation == 0) {
			this.drawVertical(mc, i, j);
		} else if(this.orientation == 1) {
			this.drawHorizontal(mc, i, j);
		}

	}

	private void drawVertical(Minecraft mc, int mx, int my) {
		if(this.dragging != 0) {
			this.mouseDragged(mc, mx, my);
		}

		double centerX = (double)this.x + (double)this.width * 0.5D;
		int top = this.y;
		int bottom = this.y + this.height;
		BufferBuilder tesse = BufferBuilder.INSTANCE;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean bx = (double)mx >= centerX - 4.0D && (double)mx <= centerX + 4.0D;
		if(!bx || my < top || my > top + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tesse.start();
		tesse.vertex(centerX, (double)top, 0.0D);
		tesse.vertex(centerX, (double)top, 0.0D);
		tesse.vertex(centerX - 4.0D, (double)(top + 8), 0.0D);
		tesse.vertex(centerX + 4.0D, (double)(top + 8), 0.0D);
		tesse.end();
		double minY;
		double maxY;
		if(this.min < this.max - this.extent) {
			minY = (double)(this.height - 20);
			maxY = (double)(this.extent / (this.max - this.min));
			if(maxY * minY < 3.0D) {
				maxY = 3.0D / minY;
			}

			double minY1 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - maxY);
			double maxY1 = minY1 + maxY;
			minY1 = (double)top + minY1 * minY + 10.0D;
			maxY1 = (double)top + maxY1 * minY + 10.0D;
			if(this.dragging != 5 && (!bx || (double)my < minY1 || (double)my > maxY1 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tesse.start();
			tesse.vertex(centerX + 4.0D, minY1, 0.0D);
			tesse.vertex(centerX - 4.0D, minY1, 0.0D);
			tesse.vertex(centerX - 4.0D, maxY1, 0.0D);
			tesse.vertex(centerX + 4.0D, maxY1, 0.0D);
			tesse.end();
		} else {
			minY = (double)(top + 10);
			maxY = (double)(bottom - 10);
			if(this.dragging != 5 && (!bx || (double)my < minY || (double)my > maxY || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tesse.start();
			tesse.vertex(centerX + 4.0D, minY, 0.0D);
			tesse.vertex(centerX - 4.0D, minY, 0.0D);
			tesse.vertex(centerX - 4.0D, maxY, 0.0D);
			tesse.vertex(centerX + 4.0D, maxY, 0.0D);
			tesse.end();
		}

		if(!bx || my < bottom - 8 || my > bottom || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tesse.start();
		tesse.vertex(centerX, (double)bottom, 0.0D);
		tesse.vertex(centerX, (double)bottom, 0.0D);
		tesse.vertex(centerX + 4.0D, (double)(bottom - 8), 0.0D);
		tesse.vertex(centerX - 4.0D, (double)(bottom - 8), 0.0D);
		tesse.end();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawHorizontal(Minecraft mc, int mx, int my) {
		if(this.dragging != 0) {
			this.mouseDragged(mc, mx, my);
		}

		double centerY = (double)this.y + (double)this.height * 0.5D;
		int left = this.x;
		int right = this.x + this.width;
		BufferBuilder tesse = BufferBuilder.INSTANCE;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean by = (double)my >= centerY - 4.0D && (double)my <= centerY + 4.0D;
		if(!by || mx < left || mx > left + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tesse.start();
		tesse.vertex((double)left, centerY, 0.0D);
		tesse.vertex((double)left, centerY, 0.0D);
		tesse.vertex((double)(left + 8), centerY + 4.0D, 0.0D);
		tesse.vertex((double)(left + 8), centerY - 4.0D, 0.0D);
		tesse.end();
		double minX;
		double maxX;
		if(this.min < this.max - this.extent) {
			minX = (double)(this.width - 20);
			maxX = (double)(this.extent / (this.max - this.min));
			if(maxX * minX < 3.0D) {
				maxX = 3.0D / minX;
			}

			double minX1 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - maxX);
			double maxX1 = minX1 + maxX;
			minX1 = (double)left + minX1 * minX + 10.0D;
			maxX1 = (double)left + maxX1 * minX + 10.0D;
			if(this.dragging != 6 && (!by || (double)mx < minX1 || (double)mx > maxX1 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tesse.start();
			tesse.vertex(minX1, centerY - 4.0D, 0.0D);
			tesse.vertex(minX1, centerY + 4.0D, 0.0D);
			tesse.vertex(maxX1, centerY + 4.0D, 0.0D);
			tesse.vertex(maxX1, centerY - 4.0D, 0.0D);
			tesse.end();
		} else {
			minX = (double)(left + 10);
			maxX = (double)(right - 10);
			if(this.dragging != 6 && (!by || (double)mx < minX || (double)mx > maxX || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tesse.start();
			tesse.vertex(minX, centerY - 4.0D, 0.0D);
			tesse.vertex(minX, centerY + 4.0D, 0.0D);
			tesse.vertex(maxX, centerY + 4.0D, 0.0D);
			tesse.vertex(maxX, centerY - 4.0D, 0.0D);
			tesse.end();
		}

		if(!by || mx < right - 8 || mx > right || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tesse.start();
		tesse.vertex((double)right, centerY, 0.0D);
		tesse.vertex((double)right, centerY, 0.0D);
		tesse.vertex((double)(right - 8), centerY - 4.0D, 0.0D);
		tesse.vertex((double)(right - 8), centerY + 4.0D, 0.0D);
		tesse.end();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean isMouseOver(Minecraft mc, int mx, int my) {
		return super.isMouseOver(mc, mx, my) ? (this.orientation == 0 ? this.mousePressedVertical(mc, mx, my) : (this.orientation == 1 ? this.mousePressedHorizontal(mc, mx, my) : false)) : false;
	}

	private boolean mousePressedVertical(Minecraft mc, int mx, int my) {
		double centerX = (double)this.x + (double)this.width * 0.5D;
		int top = this.y;
		int bottom = this.y + this.height;
		if((double)mx >= centerX - 4.0D && (double)mx <= centerX + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(my < top || my > top + 8 || this.dragging != 0 && this.dragging != 1) {
					if(my < bottom - 8 || my > bottom || this.dragging != 0 && this.dragging != 2) {
						double boxsize = (double)(this.height - 20);
						double barsize = (double)(this.extent / (this.max - this.min));
						if(barsize * boxsize < 3.0D) {
							barsize = 3.0D / boxsize;
						}

						double minY = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - barsize);
						double maxY = minY + barsize;
						minY = (double)top + minY * boxsize + 10.0D;
						maxY = (double)top + maxY * boxsize + 10.0D;
						if((double)my >= minY || this.dragging != 0 && this.dragging != 3) {
							if((double)my > maxY && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 5;
									this.draggingPos = my;
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

	private boolean mousePressedHorizontal(Minecraft mc, int mx, int my) {
		double centerY = (double)this.y + (double)this.height * 0.5D;
		int left = this.x;
		int right = this.x + this.width;
		if((double)my >= centerY - 4.0D && (double)my <= centerY + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(mx < left || mx > left + 8 || this.dragging != 0 && this.dragging != 1) {
					if(mx < right - 8 || mx > right || this.dragging != 0 && this.dragging != 2) {
						double boxsize = (double)(this.width - 20);
						double barsize = (double)(this.extent / (this.max - this.min));
						if(barsize * boxsize < 3.0D) {
							barsize = 3.0D / boxsize;
						}

						double minX = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - barsize);
						double maxX = minX + barsize;
						minX = (double)left + minX * boxsize + 10.0D;
						maxX = (double)left + maxX * boxsize + 10.0D;
						if((double)mx >= minX || this.dragging != 0 && this.dragging != 3) {
							if((double)mx > maxX && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 6;
									this.draggingPos = mx;
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

	protected void mouseDragged(Minecraft minecraft, int mx, int my) {
		float time;
		float barsize;
		float newValue;
		if(this.dragging == 5) {
			time = (float)(this.height - 20);
			barsize = this.extent / (this.max - this.min);
			if(barsize * time < 3.0F) {
				barsize = 3.0F / time;
			}

			newValue = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - barsize) * (float)(my - this.draggingPos) / time;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, newValue));
		}

		if(this.dragging == 6) {
			time = (float)(this.width - 20);
			barsize = this.extent / (this.max - this.min);
			if(barsize * time < 3.0F) {
				barsize = 3.0F / time;
			}

			newValue = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - barsize) * (float)(mx - this.draggingPos) / time;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, newValue));
		}

		long time1 = System.nanoTime();
		if(this.draggingTimer < time1) {
			this.isMouseOver(minecraft, mx, my);
			this.draggingTimer = time1 + this.repeatInterval;
		}

	}

	public void mouseReleased(int i, int j) {
		this.dragging = 0;
	}

	public void setValue(float value) {
		if(value < this.min) {
			value = this.min;
		}

		if(value > this.max - this.extent) {
			value = this.max - this.extent;
		}

		this.value = value;
	}

	public float getValue() {
		return this.value;
	}

	public void setMaximum(float max) {
		if(this.min > max) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.max = max;
			this.value = Math.min(this.value, this.max);
		}
	}

	public float getMaximum() {
		return this.max;
	}

	public void setMinimum(float min) {
		if(min > this.max) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.min = min;
			this.value = Math.max(this.value, this.min);
		}
	}

	public float getMinimum() {
		return this.min;
	}

	public void setVisibleAmount(float extent) {
		if(this.max - this.min < extent) {
			throw new IllegalArgumentException("max - min < extent");
		} else {
			this.extent = Math.min(this.max - this.min, extent);
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
}
