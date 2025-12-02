package reifnsk.minimap;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiWaypointScreen extends Screen implements GuiScreenInterface {
	static final int MIN_STRING_WIDTH = 64;
	static final int MAX_STRING_WIDTH = 160;
	static final int LIST_SIZE = 9;
	private ReiMinimap rmm = ReiMinimap.instance;
	private List wayPts = this.rmm.getWaypoints();
	private GuiWaypoint[] guiWaypoints = new GuiWaypoint[9];
	private GuiScrollbar scrollbar = new GuiScrollbar(0, 0, 0, 12, 90);
	private GuiSimpleButton backButton;
	private GuiSimpleButton addButton;
	private GuiSimpleButton removeFlagButton;
	private GuiSimpleButton removeApplyButton;
	private GuiSimpleButton removeCancelButton;
	private GuiSimpleButton prevDimension;
	private GuiSimpleButton nextDimension;
	private ConcurrentHashMap deleteObject = new ConcurrentHashMap();
	private int scroll;
	private boolean removeMode;
	private int maxStringWidth;
	private Screen parent;

	public GuiWaypointScreen(Screen guiScreen1) {
		this.parent = guiScreen1;

		for(int i2 = 0; i2 < 9; ++i2) {
			this.guiWaypoints[i2] = new GuiWaypoint(i2, this);
		}

	}

	public void init() {
		this.buttons.clear();
		Keyboard.enableRepeatEvents(true);
		GuiWaypoint[] guiWaypoint4 = this.guiWaypoints;
		int i3 = this.guiWaypoints.length;

		int i2;
		for(i2 = 0; i2 < i3; ++i2) {
			GuiWaypoint guiWaypoint1 = guiWaypoint4[i2];
			this.buttons.add(guiWaypoint1);
		}

		this.buttons.add(this.scrollbar);
		this.updateWaypoints();
		int i5 = this.width / 2;
		i2 = this.height + 90 >> 1;
		this.backButton = new GuiSimpleButton(0, i5 - 65, i2 + 7, 40, 14, this.parent == null ? "Close" : "Back");
		this.buttons.add(this.backButton);
		this.addButton = new GuiSimpleButton(0, i5 - 20, i2 + 7, 40, 14, "Add");
		this.buttons.add(this.addButton);
		this.removeFlagButton = new GuiSimpleButton(0, i5 + 25, i2 + 7, 40, 14, "Remove");
		this.buttons.add(this.removeFlagButton);
		this.removeApplyButton = new GuiSimpleButton(0, i5 - 65, i2 + 7, 60, 14, "Remove");
		this.buttons.add(this.removeApplyButton);
		this.removeCancelButton = new GuiSimpleButton(0, i5 + 5, i2 + 7, 60, 14, "Cancel");
		this.buttons.add(this.removeCancelButton);
		this.prevDimension = new GuiSimpleButton(0, 0, 0, 14, 14, "<");
		this.buttons.add(this.prevDimension);
		this.nextDimension = new GuiSimpleButton(0, 0, 0, 14, 14, ">");
		this.buttons.add(this.nextDimension);
		this.setRemoveMode(this.removeMode);
	}

	public void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void render(int i1, int i2, float f3) {
		this.backButton.active = this.backButton.visible = !this.removeMode;
		this.addButton.active = this.addButton.visible = !this.removeMode;
		this.removeFlagButton.active = this.removeFlagButton.visible = !this.removeMode;
		this.removeApplyButton.active = this.removeApplyButton.visible = this.removeMode;
		this.removeCancelButton.active = this.removeCancelButton.visible = this.removeMode;
		this.addButton.active = this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension();
		int i4 = Math.min(160, this.maxStringWidth) + 16;
		int i5 = this.height - 90 >> 1;
		int i6 = this.height + 90 >> 1;
		int i7 = this.width - i4 - 45 - 10 >> 1;
		int i8 = this.width + i4 + 45 + 10 >> 1;
		this.fill(i7 - 2, i5 - 2, i8 + 2, i6 + 2, -1610612736);
		String string9 = String.format("Waypoints [%s]", new Object[]{ReiMinimap.instance.getDimensionName(ReiMinimap.instance.getWaypointDimension())});
		int i10 = this.textRenderer.getWidth(string9);
		int i11 = this.width - i10 >> 1;
		int i12 = this.width + i10 >> 1;
		this.prevDimension.x = i11 - 18;
		this.prevDimension.y = i5 - 22;
		this.nextDimension.x = i12 + 4;
		this.nextDimension.y = i5 - 22;
		super.render(i1, i2, f3);
		this.fill(i11 - 2, i5 - 22, i12 + 2, i5 - 8, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, string9, this.width / 2, i5 - 19, -1);
	}

	public void tick() {
		int i1 = (int)this.scrollbar.getValue();
		if(this.scroll != i1) {
			this.scroll = i1;
			this.setWaypoints();
		}

	}

	protected void keyPressed(char c1, int i2) {
		super.keyPressed(c1, i2);
		switch(i2) {
		case 199:
			this.scrollbar.setValue(this.scrollbar.getMinimum());
			break;
		case 200:
			this.scrollbar.unitDecrement();
			break;
		case 201:
			this.scrollbar.blockDecrement();
		case 202:
		case 203:
		case 204:
		case 205:
		case 206:
		default:
			break;
		case 207:
			this.scrollbar.setValue(this.scrollbar.getMaximum());
			break;
		case 208:
			this.scrollbar.unitIncrement();
			break;
		case 209:
			this.scrollbar.blockIncrement();
		}

	}

	public void onMouseEvent() {
		super.onMouseEvent();
		int i1 = Mouse.getDWheel();
		if(i1 != 0) {
			i1 = i1 < 0 ? 3 : -3;
			this.scrollbar.setValue(this.scrollbar.getValue() + (float)i1);
		}

	}

	protected void buttonClicked(ButtonWidget guiButton1) {
		if(guiButton1 == this.backButton) {
			this.minecraft.setScreen(this.parent);
		}

		if(guiButton1 == this.removeFlagButton) {
			this.setRemoveMode(true);
		}

		if(guiButton1 == this.removeCancelButton) {
			this.setRemoveMode(false);
		}

		if(guiButton1 == this.removeApplyButton) {
			boolean z2 = false;

			Waypoint waypoint3;
			for(Iterator iterator4 = this.deleteObject.keySet().iterator(); iterator4.hasNext(); z2 |= this.wayPts.remove(waypoint3)) {
				waypoint3 = (Waypoint)iterator4.next();
			}

			if(z2) {
				this.rmm.saveWaypoints();
				this.updateWaypoints();
			}

			this.setRemoveMode(false);
		}

		if(guiButton1 == this.addButton && this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension()) {
			this.minecraft.setScreen(new GuiWaypointEditorScreen(this, (Waypoint)null));
		}

		if(guiButton1 == this.prevDimension) {
			this.setRemoveMode(false);
			this.rmm.prevDimension();
			this.wayPts = this.rmm.getWaypoints();
			this.updateWaypoints();
		}

		if(guiButton1 == this.nextDimension) {
			this.setRemoveMode(false);
			this.rmm.nextDimension();
			this.wayPts = this.rmm.getWaypoints();
			this.updateWaypoints();
		}

	}

	void setRemoveMode(boolean z1) {
		this.removeMode = z1;
		this.deleteObject.clear();
	}

	boolean getRemoveMode() {
		return this.removeMode;
	}

	boolean isRemove(Waypoint waypoint1) {
		return this.deleteObject.containsKey(waypoint1);
	}

	void addWaypoint(Waypoint waypoint1) {
		if(!this.wayPts.contains(waypoint1)) {
			this.wayPts.add(waypoint1);
			this.rmm.saveWaypoints();
			this.updateWaypoints();
			this.scrollbar.setValue(this.scrollbar.getMaximum());
		}

	}

	void removeWaypoint(Waypoint waypoint1) {
		if(this.removeMode) {
			if(this.deleteObject.remove(waypoint1) == null) {
				this.deleteObject.put(waypoint1, waypoint1);
			}
		} else if(this.wayPts.remove(waypoint1)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

	}

	void updateWaypoint(Waypoint waypoint1) {
		if(this.wayPts.contains(waypoint1)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

	}

	private void updateWaypoints() {
		this.maxStringWidth = 64;
		int i1 = 0;

		for(int i2 = this.wayPts.size(); i1 < i2; ++i1) {
			Waypoint waypoint3 = (Waypoint)this.wayPts.get(i1);
			this.maxStringWidth = Math.max(this.maxStringWidth, this.textRenderer.getWidth(i1 + 1 + ") " + waypoint3.name));
		}

		this.scrollbar.setMinimum(0.0F);
		this.scrollbar.setMaximum((float)this.wayPts.size());
		this.scrollbar.setVisibleAmount((float)Math.min(9, this.wayPts.size()));
		this.scroll = (int)this.scrollbar.getValue();
		this.updateGui();
		this.setWaypoints();
	}

	private void updateGui() {
		int i1 = Math.min(160, this.maxStringWidth) + 16;
		int i2 = this.height - 90 - 4 >> 1;
		int i3 = this.width - i1 - 45 - 12 >> 1;
		int i4 = this.width + i1 + 45 + 12 >> 1;

		for(int i5 = 0; i5 < 9; ++i5) {
			this.guiWaypoints[i5].bounds(i3 + 2, i2 + 2 + 10 * i5, i1 + 45, 9);
		}

		this.scrollbar.x = i4 - 12;
		this.scrollbar.y = i2 + 2;
	}

	private void setWaypoints() {
		for(int i1 = 0; i1 < 9; ++i1) {
			int i2 = i1 + this.scroll;
			this.guiWaypoints[i1].setWaypoint(i2 + 1, i2 < this.wayPts.size() ? (Waypoint)this.wayPts.get(i2) : null);
		}

	}

	Minecraft getMinecraft() {
		return this.minecraft;
	}
}
