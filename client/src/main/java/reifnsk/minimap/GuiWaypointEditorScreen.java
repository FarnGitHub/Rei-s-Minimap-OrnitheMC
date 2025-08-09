package reifnsk.minimap;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.InputPlayerEntity;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class GuiWaypointEditorScreen extends Screen implements GuiScreenInterface {
	private GuiWaypointScreen parrent;
	private Waypoint waypoint;
	private Waypoint waypointBackup;
	private GuiTextField nameTextField;
	private GuiTextField xCoordTextField;
	private GuiTextField yCoordTextField;
	private GuiTextField zCoordTextField;
	private GuiScrollbar[] rgb;
	private GuiSimpleButton okButton;
	private GuiSimpleButton cancelButton;

	public GuiWaypointEditorScreen(Minecraft mc, Waypoint waypoint) {
		this.waypoint = waypoint;
		this.waypointBackup = waypoint == null ? null : new Waypoint(waypoint);
		String name;
		int x;
		int y;
		int z;
		if(waypoint == null) {
			name = "";
			InputPlayerEntity i = mc.player;
			x = net.minecraft.util.math.MathHelper.floor(i.x);
			y = net.minecraft.util.math.MathHelper.floor(i.y);
			z = net.minecraft.util.math.MathHelper.floor(i.z);
		} else {
			name = waypoint.name;
			x = waypoint.x;
			y = waypoint.y;
			z = waypoint.z;
		}

		this.nameTextField = new GuiTextField(name);
		this.nameTextField.setInputType(0);
		this.nameTextField.active();
		this.xCoordTextField = new GuiTextField(Integer.toString(x));
		this.xCoordTextField.setInputType(1);
		this.yCoordTextField = new GuiTextField(Integer.toString(y));
		this.yCoordTextField.setInputType(2);
		this.zCoordTextField = new GuiTextField(Integer.toString(z));
		this.zCoordTextField.setInputType(1);
		this.nameTextField.setNext(this.xCoordTextField);
		this.nameTextField.setPrev(this.zCoordTextField);
		this.xCoordTextField.setNext(this.yCoordTextField);
		this.xCoordTextField.setPrev(this.nameTextField);
		this.yCoordTextField.setNext(this.zCoordTextField);
		this.yCoordTextField.setPrev(this.xCoordTextField);
		this.zCoordTextField.setNext(this.nameTextField);
		this.zCoordTextField.setPrev(this.yCoordTextField);
		this.rgb = new GuiScrollbar[3];

		for(int i9 = 0; i9 < 3; ++i9) {
			GuiScrollbar gs = new GuiScrollbar(0, 0, 0, 118, 10);
			gs.setMinimum(0.0F);
			gs.setMaximum(255.0F);
			gs.setVisibleAmount(0.0F);
			gs.orientation = 1;
			this.rgb[i9] = gs;
		}

		this.rgb[0].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.red) * 255.0F);
		this.rgb[1].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.green) * 255.0F);
		this.rgb[2].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.blue) * 255.0F);
	}

	public GuiWaypointEditorScreen(GuiWaypointScreen parrent, Waypoint waypoint) {
		this(parrent.getMinecraft(), waypoint);
		this.parrent = parrent;
	}

	public void init() {
		Keyboard.enableRepeatEvents(true);

		for(int i = 0; i < 3; ++i) {
			this.rgb[i].x = this.width - 150 >> 1;
			this.rgb[i].y = this.height / 2 + 20 + i * 10;
			this.buttons.add(this.rgb[i]);
		}

		this.nameTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 40, 150, 9);
		this.xCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 20, 150, 9);
		this.yCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 10, 150, 9);
		this.zCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2, 150, 9);
		this.buttons.add(this.nameTextField);
		this.buttons.add(this.xCoordTextField);
		this.buttons.add(this.yCoordTextField);
		this.buttons.add(this.zCoordTextField);
		this.okButton = new GuiSimpleButton(0, this.width / 2 - 65, this.height / 2 + 58, 60, 14, "OK");
		this.cancelButton = new GuiSimpleButton(1, this.width / 2 + 5, this.height / 2 + 58, 60, 14, "Cancel");
		this.buttons.add(this.okButton);
		this.buttons.add(this.cancelButton);
	}

	public void removed() {
		Keyboard.enableRepeatEvents(false);
		super.removed();
	}

	public void render(int mx, int my, float f) {
		int x = MathHelper.floor(this.minecraft.player.x);
		int y = MathHelper.floor(this.minecraft.player.y);
		int z = MathHelper.floor(this.minecraft.player.z);
		String title = "Waypoint Edit";
		int titleWidth = this.textRenderer.getWidth(title);
		int titleLeft = this.width - titleWidth >> 1;
		int titleRight = this.width + titleWidth >> 1;
		this.fill(titleLeft - 2, this.height / 2 - 71, titleRight + 2, this.height / 2 - 57, -1610612736);
		this.drawCenteredString(this.textRenderer, title, this.width / 2, this.height / 2 - 68, -1);
		String temp = Integer.toString(x).equals(this.xCoordTextField.message) ? "xCoord: (Current)" : "xCoord:";
		this.drawString(this.textRenderer, temp, (this.width - 150) / 2 + 1, this.height / 2 - 19, -1);
		temp = Integer.toString(y).equals(this.yCoordTextField.message) ? "yCoord: (Current)" : "yCoord:";
		this.drawString(this.textRenderer, temp, (this.width - 150) / 2 + 1, this.height / 2 - 9, -1);
		temp = Integer.toString(z).equals(this.zCoordTextField.message) ? "zCoord: (Current)" : "zCoord:";
		this.drawString(this.textRenderer, temp, (this.width - 150) / 2 + 1, this.height / 2 + 1, -1);
		this.fill((this.width - 150) / 2 - 2, this.height / 2 - 50, (this.width + 150) / 2 + 2, this.height / 2 + 52, -1610612736);
		this.drawCenteredString(this.textRenderer, "Waypoint Name", this.width >> 1, this.height / 2 - 49, -1);
		this.drawCenteredString(this.textRenderer, "Coordinate", this.width >> 1, this.height / 2 - 29, -1);
		this.drawCenteredString(this.textRenderer, "Color", this.width >> 1, this.height / 2 + 11, -1);
		if(this.waypoint != null) {
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
		}

		int r = (int)this.rgb[0].getValue() & 255;
		int g = (int)this.rgb[1].getValue() & 255;
		int b = (int)this.rgb[2].getValue() & 255;
		int color = 0xFF000000 | r << 16 | g << 8 | b;
		this.drawCenteredString(this.textRenderer, String.format("R:%03d", new Object[]{r}), this.width / 2 - 15, this.height / 2 + 21, -2139062144);
		this.drawCenteredString(this.textRenderer, String.format("G:%03d", new Object[]{g}), this.width / 2 - 15, this.height / 2 + 31, -2139062144);
		this.drawCenteredString(this.textRenderer, String.format("B:%03d", new Object[]{b}), this.width / 2 - 15, this.height / 2 + 41, -2139062144);
		this.fill(this.width + 90 >> 1, this.height / 2 + 20, this.width + 150 >> 1, this.height / 2 + 50, color);
		super.render(mx, my, f);
	}

	protected void keyPressed(char c, int i) {
		if(i == 1) {
			this.cancel();
		} else if(i == 28 && GuiTextField.getActive() == this.zCoordTextField) {
			this.accept();
		} else {
			GuiTextField.keyType(this.minecraft, c, i);
		}
	}

	private void cancel() {
		if(this.waypoint != null) {
			this.waypoint.set(this.waypointBackup);
		}

		this.minecraft.openScreen(this.parrent);
	}

	private void accept() {
		if(this.waypoint != null) {
			this.waypoint.name = this.nameTextField.message;
			this.waypoint.x = parseInt(this.xCoordTextField.message);
			this.waypoint.y = parseInt(this.yCoordTextField.message);
			this.waypoint.z = parseInt(this.zCoordTextField.message);
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
			this.parrent.updateWaypoint(this.waypoint);
		} else {
			String name = this.nameTextField.message;
			int x = parseInt(this.xCoordTextField.message);
			int y = parseInt(this.yCoordTextField.message);
			int z = parseInt(this.zCoordTextField.message);
			float r = this.rgb[0].getValue() / 255.0F;
			float g = this.rgb[1].getValue() / 255.0F;
			float b = this.rgb[2].getValue() / 255.0F;
			this.waypoint = new Waypoint(name, x, y, z, true, r, g, b);
			if(this.parrent == null) {
				ReiMinimap rmm = ReiMinimap.instance;
				ArrayList wayPts = rmm.getWaypoints();
				wayPts.add(this.waypoint);
				rmm.saveWaypoints();
			} else {
				this.parrent.addWaypoint(this.waypoint);
			}
		}

		this.minecraft.openScreen(this.parrent);
	}

	private static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception exception2) {
			return 0;
		}
	}

	protected void buttonClicked(ButtonWidget guibutton) {
		if(guibutton == this.okButton) {
			this.accept();
		} else if(guibutton == this.cancelButton) {
			this.cancel();
		}
	}
}
