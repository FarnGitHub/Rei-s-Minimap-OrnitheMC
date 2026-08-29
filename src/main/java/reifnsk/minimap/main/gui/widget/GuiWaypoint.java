package reifnsk.minimap.main.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import reifnsk.minimap.main.waypoint.Waypoint;
import reifnsk.minimap.main.gui.screen.GuiWaypointEditorScreen;
import reifnsk.minimap.main.gui.screen.GuiWaypointScreen;

public class GuiWaypoint extends ButtonWidget {
	private static final int[] COLOR1 = new int[]{-1, -65536};
	private static final int[] COLOR2 = new int[]{-4144960, -4194304};
	private static final int COLOR_SIZE = 9;
	private static final int BUTTON_SIZE = 30;
	private static final int ADD_SPACE = 2;
	static final int SIZE = 45;
	private GuiWaypointScreen gws;
	private Waypoint waypoint;
	private int number;
	private String name;
	private int top;
	private int bottom;
	private int left;
	private int right;
	private int ctop;
	private int cbottom;
	private int cleft;
	private int cright;
	private int btop;
	private int bbottom;
	private int bleft;
	private int bright;
	private long clickTime = System.nanoTime();

	public GuiWaypoint(int i1, GuiWaypointScreen guiWaypointScreen2) {
		super(i1, 0, 0, 0, 0, (String)null);
		this.gws = guiWaypointScreen2;
	}

	public void setWaypoint(int i1, Waypoint waypoint2) {
		this.number = i1;
		this.waypoint = waypoint2;
		this.name = null;
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		if(this.waypoint != null) {
			TextRenderer fontRenderer4 = minecraft1.textRenderer;
			if(this.name == null) {
				for(this.name = this.number + ") " + this.waypoint.name; fontRenderer4.getWidth(this.name) > 160; this.name = this.name.substring(0, this.name.length() - 1)) {
				}
			}

			boolean z5 = this.mouseIn(i2, i3);
			this.drawTextWithShadow(fontRenderer4, this.name, this.x + 1, this.y + 1, (z5 ? COLOR1 : COLOR2)[this.waypoint.type]);
			boolean z6 = z5 && i2 < this.cleft;
			int i7 = (int)(this.waypoint.red * 255.0F) & 255;
			int i8 = (int)(this.waypoint.green * 255.0F) & 255;
			int i9 = (int)(this.waypoint.blue * 255.0F) & 255;
			int i10 = 0xFF000000 | i7 << 16 | i8 << 8 | i9;
			this.fill(this.cleft, this.ctop, this.cright, this.cbottom, i10);
			z5 = this.buttonIn(i2, i3);
			String string11 = this.gws.getRemoveMode() ? (this.gws.isRemove(this.waypoint) ? "X" : "KEEP") : (this.waypoint.enable ? "ON" : "OFF");
			i10 = z5 ? -2130706433 : (string11 == "X" ? -1593901056 : (string11 == "KEEP" ? -1610547456 : (this.waypoint.enable ? -1610547456 : -1593901056)));
			this.fill(this.bleft, this.btop, this.bright, this.bbottom, i10);
			this.drawCenteredTextWithShadow(minecraft1.textRenderer, string11, this.bleft + this.bright >> 1, this.btop + 1, -1);
			if(z6) {
				String string12 = String.format("X:%d, Y:%d, Z:%d", new Object[]{this.waypoint.x, this.waypoint.y, this.waypoint.z});
				int i13 = fontRenderer4.getWidth(string12);
				int i14 = i2 - i13 / 2 - 1;
				int i15 = i14 + i13 + 2;
				this.fill(i14, i3 - 11, i15, i3 - 1, -1610612736);
				this.drawCenteredTextWithShadow(fontRenderer4, string12, i2, i3 - 10, -1);
			}

		}
	}

	public boolean isMouseOver(Minecraft minecraft1, int i2, int i3) {
		if(this.waypoint == null) {
			return false;
		} else {
			if(this.mouseIn(i2, i3)) {
				if(this.colorIn(i2, i3)) {
					this.waypoint.red = (float)Math.random();
					this.waypoint.green = (float)Math.random();
					this.waypoint.blue = (float)Math.random();
					this.gws.updateWaypoint(this.waypoint);
					return true;
				}

				if(this.buttonIn(i2, i3)) {
					if(this.gws.getRemoveMode()) {
						this.gws.removeWaypoint(this.waypoint);
					} else {
						this.waypoint.enable = !this.waypoint.enable;
						this.gws.updateWaypoint(this.waypoint);
					}

					return true;
				}

				long j4 = System.nanoTime();
				if(!this.gws.getRemoveMode() && j4 < this.clickTime + 300000000L) {
					minecraft1.setScreen(new GuiWaypointEditorScreen(this.gws, this.waypoint));
					return true;
				}

				this.clickTime = j4;
			}

			return false;
		}
	}

	public void bounds(int i1, int i2, int i3, int i4) {
		this.x = i1;
		this.y = i2;
		this.width = i3;
		this.height = i4;
		this.top = i2;
		this.bottom = i2 + i4;
		this.left = i1;
		this.right = i1 + i3;
		this.ctop = this.top;
		this.cbottom = this.bottom;
		this.cright = this.right - 2 - 30 - 2;
		this.cleft = this.cright - 9;
		this.btop = this.top;
		this.bbottom = this.bottom;
		this.bright = this.right - 2;
		this.bleft = this.bright - 30;
	}

	private boolean mouseIn(int i1, int i2) {
		return i2 >= this.top && i2 < this.bottom && i1 >= this.left && i1 < this.right;
	}

	private boolean colorIn(int i1, int i2) {
		return i2 >= this.ctop && i2 < this.cbottom && i1 >= this.cleft && i1 < this.cright;
	}

	private boolean buttonIn(int i1, int i2) {
		return i2 >= this.btop && i2 < this.bbottom && i1 >= this.bleft && i1 < this.bright;
	}
}
