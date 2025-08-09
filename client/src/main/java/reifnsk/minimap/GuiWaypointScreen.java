package reifnsk.minimap;

import java.util.ArrayList;

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
	private ArrayList wayPts = ReiMinimap.instance.getWaypoints();
	private GuiWaypoint[] guiWaypoints = new GuiWaypoint[9];
	private GuiScrollbar scrollbar = new GuiScrollbar(0, 0, 0, 12, 90);
	private GuiSimpleButton backButton;
	private GuiSimpleButton addButton;
	private GuiSimpleButton removeButton;
	private GuiSimpleButton cancelButton;
	private int scroll;
	private boolean removeMode;
	private int maxStringWidth;

	public GuiWaypointScreen() {
		for(int i = 0; i < 9; ++i) {
			this.guiWaypoints[i] = new GuiWaypoint(i, this);
		}

	}

	public void init() {
		this.buttons.clear();
		Keyboard.enableRepeatEvents(true);
		GuiWaypoint[] centerX = this.guiWaypoints;
		int bottom = centerX.length;

		for(int i$ = 0; i$ < bottom; ++i$) {
			GuiWaypoint gpt = centerX[i$];
			this.buttons.add(gpt);
		}

		this.buttons.add(this.scrollbar);
		this.updateWaypoints();
		int i5 = this.width / 2;
		bottom = this.height + 90 >> 1;
		this.backButton = new GuiSimpleButton(0, i5 - 65, bottom + 7, 40, 14, "Back");
		this.buttons.add(this.backButton);
		this.addButton = new GuiSimpleButton(0, i5 - 20, bottom + 7, 40, 14, "Add");
		this.buttons.add(this.addButton);
		this.removeButton = new GuiSimpleButton(0, i5 + 25, bottom + 7, 40, 14, "Remove");
		this.buttons.add(this.removeButton);
		this.cancelButton = new GuiSimpleButton(0, i5 - 20, bottom + 7, 40, 14, "Cancel");
		this.buttons.add(this.cancelButton);
		this.setRemoveMode(this.removeMode);
	}

	public void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void render(int mouseX, int mouseY, float f) {
		int gwpWidth = Math.min(160, this.maxStringWidth) + 16;
		int top = this.height - 90 >> 1;
		int bottom = this.height + 90 >> 1;
		int left = this.width - gwpWidth - 45 - 10 >> 1;
		int right = this.width + gwpWidth + 45 + 10 >> 1;
		this.fill(left - 2, top - 2, right + 2, bottom + 2, -1610612736);
		super.render(mouseX, mouseY, f);
		String title = "Waypoints";
		int titleWidth = this.textRenderer.getWidth(title);
		int titleLeft = this.width - titleWidth >> 1;
		int titleRight = this.width + titleWidth >> 1;
		this.fill(titleLeft - 2, top - 22, titleRight + 2, top - 8, -1610612736);
		this.drawCenteredString(this.textRenderer, title, this.width / 2, top - 19, -1);
	}

	public void tick() {
		int temp = (int)this.scrollbar.getValue();
		if(this.scroll != temp) {
			this.scroll = temp;
			this.setWaypoints();
		}

	}

	protected void keyPressed(char c, int i) {
		super.keyPressed(c, i);
		switch(i) {
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

	public void handleInputs() {
		super.handleInputs();
		int i = Mouse.getDWheel();
		if(i != 0) {
			i = i < 0 ? 3 : -3;
			this.scrollbar.setValue(this.scrollbar.getValue() + (float)i);
		}

	}

	protected void buttonClicked(ButtonWidget guibutton) {
		if(guibutton == this.backButton) {
			this.minecraft.openScreen(new GuiOptionScreen());
		}

		if(guibutton == this.removeButton) {
			this.setRemoveMode(true);
		}

		if(guibutton == this.cancelButton) {
			this.setRemoveMode(false);
		}

		if(guibutton == this.addButton) {
			this.minecraft.openScreen(new GuiWaypointEditorScreen(this, (Waypoint)null));
		}

	}

	void setRemoveMode(boolean b) {
		this.backButton.active = this.backButton.visible = !b;
		this.addButton.active = this.addButton.visible = !b;
		this.removeButton.active = this.removeButton.visible = !b;
		this.cancelButton.active = this.cancelButton.visible = b;
		if(this.removeMode != b) {
			this.removeMode = b;
		}

	}

	boolean getRemoveMode() {
		return this.removeMode;
	}

	void addWaypoint(Waypoint wp) {
		if(!this.wayPts.contains(wp)) {
			this.wayPts.add(wp);
			this.rmm.saveWaypoints();
			this.updateWaypoints();
			this.scrollbar.setValue(this.scrollbar.getMaximum());
		}

	}

	void removeWaypoint(Waypoint wp) {
		if(this.wayPts.remove(wp)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

		this.setRemoveMode(false);
	}

	void updateWaypoint(Waypoint wp) {
		if(this.wayPts.contains(wp)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

	}

	private void updateWaypoints() {
		this.maxStringWidth = 64;
		int i = 0;

		for(int j = this.wayPts.size(); i < j; ++i) {
			Waypoint pt = (Waypoint)this.wayPts.get(i);
			this.maxStringWidth = Math.max(this.maxStringWidth, this.textRenderer.getWidth(i + 1 + ") " + pt.name));
		}

		this.scrollbar.setMinimum(0.0F);
		this.scrollbar.setMaximum((float)this.wayPts.size());
		this.scrollbar.setVisibleAmount((float)Math.min(9, this.wayPts.size()));
		this.scroll = (int)this.scrollbar.getValue();
		this.updateGui();
		this.setWaypoints();
	}

	private void updateGui() {
		int gwpWidth = Math.min(160, this.maxStringWidth) + 16;
		int top = this.height - 90 - 4 >> 1;
		int left = this.width - gwpWidth - 45 - 12 >> 1;
		int right = this.width + gwpWidth + 45 + 12 >> 1;

		for(int i = 0; i < 9; ++i) {
			this.guiWaypoints[i].bounds(left + 2, top + 2 + 10 * i, gwpWidth + 45, 9);
		}

		this.scrollbar.x = right - 12;
		this.scrollbar.y = top + 2;
	}

	private void setWaypoints() {
		for(int i = 0; i < 9; ++i) {
			int num = i + this.scroll;
			this.guiWaypoints[i].setWaypoint(num + 1, num < this.wayPts.size() ? (Waypoint)this.wayPts.get(num) : null);
		}

	}

	Minecraft getMinecraft() {
		return this.minecraft;
	}
}
