package reifnsk.minimap.main.gui.screen;

import java.util.List;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import reifnsk.minimap.main.ReiMinimap;
import reifnsk.minimap.main.waypoint.Waypoint;
import reifnsk.minimap.main.gui.widget.GuiScrollbar;
import reifnsk.minimap.main.gui.widget.GuiSimpleButton;
import reifnsk.minimap.main.gui.widget.GuiTextField;

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

	public GuiWaypointEditorScreen(Minecraft minecraft1, Waypoint waypoint2) {
		this.waypoint = waypoint2;
		this.waypointBackup = waypoint2 == null ? null : new Waypoint(waypoint2);
		String string3;
		int i4;
		int i5;
		int i6;
		if(waypoint2 == null) {
			string3 = "";
			ClientPlayerEntity entityPlayerSP7 = minecraft1.player;
			i4 = MathHelper.floor(entityPlayerSP7.x);
			i5 = MathHelper.floor(entityPlayerSP7.y);
			i6 = MathHelper.floor(entityPlayerSP7.z);
		} else {
			string3 = waypoint2.name;
			i4 = waypoint2.x;
			i5 = waypoint2.y;
			i6 = waypoint2.z;
		}

		this.nameTextField = new GuiTextField(string3);
		this.nameTextField.setInputType(0);
		this.nameTextField.active();
		this.xCoordTextField = new GuiTextField(Integer.toString(i4));
		this.xCoordTextField.setInputType(1);
		this.yCoordTextField = new GuiTextField(Integer.toString(i5));
		this.yCoordTextField.setInputType(2);
		this.zCoordTextField = new GuiTextField(Integer.toString(i6));
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
			GuiScrollbar guiScrollbar8 = new GuiScrollbar(0, 0, 0, 118, 10);
			guiScrollbar8.setMinimum(0.0F);
			guiScrollbar8.setMaximum(255.0F);
			guiScrollbar8.setVisibleAmount(0.0F);
			guiScrollbar8.setBlockIncrement(10.0F);
			guiScrollbar8.orientation = 1;
			this.rgb[i9] = guiScrollbar8;
		}

		this.rgb[0].setValue((float)(waypoint2 == null ? Math.random() : (double)waypoint2.red) * 255.0F);
		this.rgb[1].setValue((float)(waypoint2 == null ? Math.random() : (double)waypoint2.green) * 255.0F);
		this.rgb[2].setValue((float)(waypoint2 == null ? Math.random() : (double)waypoint2.blue) * 255.0F);
	}

	public GuiWaypointEditorScreen(GuiWaypointScreen guiWaypointScreen1, Waypoint waypoint2) {
		this(guiWaypointScreen1.getMinecraft(), waypoint2);
		this.parrent = guiWaypointScreen1;
	}

	public void init() {
		Keyboard.enableRepeatEvents(true);

		for(int i1 = 0; i1 < 3; ++i1) {
			this.rgb[i1].x = this.width - 150 >> 1;
			this.rgb[i1].y = this.height / 2 + 20 + i1 * 10;
			this.buttons.add(this.rgb[i1]);
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

	public void render(int i1, int i2, float f3) {
		int i4 = MathHelper.floor(this.minecraft.player.x);
		int i5 = MathHelper.floor(this.minecraft.player.y);
		int i6 = MathHelper.floor(this.minecraft.player.z);
		this.xCoordTextField.setNorm(i4);
		this.yCoordTextField.setNorm(i5);
		this.zCoordTextField.setNorm(i6);
		String string7 = "Waypoint Edit";
		int i8 = this.textRenderer.getWidth(string7);
		int i9 = this.width - i8 >> 1;
		int i10 = this.width + i8 >> 1;
		this.fill(i9 - 2, this.height / 2 - 71, i10 + 2, this.height / 2 - 57, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, string7, this.width / 2, this.height / 2 - 68, -1);
		String string11 = Integer.toString(i4).equals(this.xCoordTextField.text) ? "xCoord: (Current)" : "xCoord:";
		this.drawTextWithShadow(this.textRenderer, string11, (this.width - 150) / 2 + 1, this.height / 2 - 19, -1);
		string11 = Integer.toString(i5).equals(this.yCoordTextField.text) ? "yCoord: (Current)" : "yCoord:";
		this.drawTextWithShadow(this.textRenderer, string11, (this.width - 150) / 2 + 1, this.height / 2 - 9, -1);
		string11 = Integer.toString(i6).equals(this.zCoordTextField.text) ? "zCoord: (Current)" : "zCoord:";
		this.drawTextWithShadow(this.textRenderer, string11, (this.width - 150) / 2 + 1, this.height / 2 + 1, -1);
		this.fill((this.width - 150) / 2 - 2, this.height / 2 - 50, (this.width + 150) / 2 + 2, this.height / 2 + 52, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, "Waypoint Name", this.width >> 1, this.height / 2 - 49, -1);
		this.drawCenteredTextWithShadow(this.textRenderer, "Coordinate", this.width >> 1, this.height / 2 - 29, -1);
		this.drawCenteredTextWithShadow(this.textRenderer, "Color", this.width >> 1, this.height / 2 + 11, -1);
		if(this.waypoint != null) {
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
		}

		int i12 = (int)this.rgb[0].getValue() & 255;
		int i13 = (int)this.rgb[1].getValue() & 255;
		int i14 = (int)this.rgb[2].getValue() & 255;
		int i15 = 0xFF000000 | i12 << 16 | i13 << 8 | i14;
		this.drawCenteredTextWithShadow(this.textRenderer, String.format("R:%03d", new Object[]{i12}), this.width / 2 - 15, this.height / 2 + 21, -2139062144);
		this.drawCenteredTextWithShadow(this.textRenderer, String.format("G:%03d", new Object[]{i13}), this.width / 2 - 15, this.height / 2 + 31, -2139062144);
		this.drawCenteredTextWithShadow(this.textRenderer, String.format("B:%03d", new Object[]{i14}), this.width / 2 - 15, this.height / 2 + 41, -2139062144);
		this.fill(this.width + 90 >> 1, this.height / 2 + 20, this.width + 150 >> 1, this.height / 2 + 50, i15);
		super.render(i1, i2, f3);
	}

	protected void keyPressed(char c1, int i2) {
		if(i2 == 1) {
			this.cancel();
		} else if(i2 == 28 && GuiTextField.getActive() == this.zCoordTextField) {
			this.zCoordTextField.norm();
			this.accept();
		} else {
			GuiTextField.activeRender(this.minecraft, c1, i2);
		}
	}

	private void cancel() {
		if(this.waypoint != null) {
			this.waypoint.set(this.waypointBackup);
		}

		this.minecraft.setScreen(this.parrent);
	}

	private void accept() {
		if(this.waypoint != null) {
			this.waypoint.name = this.nameTextField.text;
			this.waypoint.x = parseInt(this.xCoordTextField.text);
			this.waypoint.y = parseInt(this.yCoordTextField.text);
			this.waypoint.z = parseInt(this.zCoordTextField.text);
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
			this.parrent.updateWaypoint(this.waypoint);
		} else {
			String string1 = this.nameTextField.text;
			int i2 = parseInt(this.xCoordTextField.text);
			int i3 = parseInt(this.yCoordTextField.text);
			int i4 = parseInt(this.zCoordTextField.text);
			float f5 = this.rgb[0].getValue() / 255.0F;
			float f6 = this.rgb[1].getValue() / 255.0F;
			float f7 = this.rgb[2].getValue() / 255.0F;
			this.waypoint = new Waypoint(string1, i2, i3, i4, true, f5, f6, f7);
			if(this.parrent == null) {
				ReiMinimap reiMinimap = ReiMinimap.instance;
				reiMinimap.getWaypoints().add(this.waypoint);
				reiMinimap.saveWaypoints();
			} else {
				this.parrent.addWaypoint(this.waypoint);
			}
		}

		this.minecraft.setScreen(this.parrent);
	}

	private static int parseInt(String string0) {
		try {
			return Integer.parseInt(string0);
		} catch (Exception exception1) {
			return 0;
		}
	}

	protected void buttonClicked(ButtonWidget guiButton1) {
		if(guiButton1 == this.okButton) {
			this.accept();
		} else if(guiButton1 == this.cancelButton) {
			this.cancel();
		}
	}
}
