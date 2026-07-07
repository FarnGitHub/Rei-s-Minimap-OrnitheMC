package reifnsk.minimap.main.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import reifnsk.minimap.main.option.EnumOption;
import reifnsk.minimap.main.ReiMinimap;
import reifnsk.minimap.main.gui.widget.GuiOptionButton;
import reifnsk.minimap.main.gui.widget.GuiSimpleButton;

import java.util.ArrayList;

public class GuiOptionScreen extends Screen implements GuiScreenInterface {
	private static final int LIGHTING_VERSION = 16844800;
	private static final int SUNRISE_DIRECTION = 16844931;
	public static final int minimapMenu = 0;
	public static final int optionMinimap = 1;
	public static final int optionSurfaceMap = 2;
	public static final int optionEntitiesRadar = 3;
	public static final int optionMarker = 4;
	public static final int aboutMinimap = 5;
	private static final String[] TITLE_STRING = new String[]{"Rei's Minimap StationAPI " + ReiMinimap.version, "Minimap Options", "SurfaceMap Options", "Entities Radar Options", "Marker Options", "About Rei's Minimap"};
	private int page;
	private ArrayList buttonListRei = new ArrayList();
	private GuiSimpleButton exitMenu;
	private GuiSimpleButton waypoint;
	private GuiSimpleButton keyconfig;
	private int top;
	private int left;
	private int right;
	private int bottom;
	private int centerX;
	private int centerY;
	private Screen parent;

	public GuiOptionScreen() {
	}

	public GuiOptionScreen(int i1) {
		this.page = i1;
	}

	public GuiOptionScreen(Screen paraSida) {
		this(0);
		parent = paraSida;
	}

	public void init() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		this.buttons.clear();
		this.buttonListRei.clear();
		EnumOption[] enumOption4;
		int i3 = (enumOption4 = EnumOption.values()).length;

		for(int i2 = 0; i2 < i3; ++i2) {
			EnumOption enumOption1 = enumOption4[i2];
			if(enumOption1.getPage() == this.page && ((this.minecraft.world != null && !this.minecraft.world.isRemote) || enumOption1 != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()) && enumOption1 != EnumOption.DIRECTION_TYPE) {
				GuiOptionButton guiOptionButton5 = new GuiOptionButton(this.minecraft.textRenderer, enumOption1);
				guiOptionButton5.setValue(ReiMinimap.instance.getOption(enumOption1));
				this.buttons.add(guiOptionButton5);
				this.buttonListRei.add(guiOptionButton5);
			}
		}

		this.left = this.width - GuiOptionButton.getWidth() >> 1;
		this.top = this.height - this.buttonListRei.size() * 10 >> 1;
		this.right = this.width + GuiOptionButton.getWidth() >> 1;
		this.bottom = this.height + this.buttonListRei.size() * 10 >> 1;

		for(int i6 = 0; i6 < this.buttonListRei.size(); ++i6) {
			GuiOptionButton guiOptionButton7 = (GuiOptionButton)this.buttonListRei.get(i6);
			guiOptionButton7.x = this.left;
			guiOptionButton7.y = this.top + i6 * 10;
		}

		if(this.page == 0) {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 95, this.bottom + 7, 60, 14, "Exit Menu");
			this.buttons.add(this.exitMenu);
			this.waypoint = new GuiSimpleButton(1, this.centerX - 30, this.bottom + 7, 60, 14, "Waypoints");
			this.buttons.add(this.waypoint);
			this.keyconfig = new GuiSimpleButton(2, this.centerX + 35, this.bottom + 7, 60, 14, "Keyconfig");
			this.buttons.add(this.keyconfig);
		} else {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 30, this.bottom + 7, 60, 14, "Back");
			this.buttons.add(this.exitMenu);
		}

	}

	public void render(int i1, int i2, float f3) {
		if(ReiMinimap.instance.theMinecraft.world == null) this.renderBackground();
		String string4 = TITLE_STRING[this.page];
		int i5 = this.textRenderer.getWidth(string4);
		int i6 = this.width - i5 >> 1;
		int i7 = this.width + i5 >> 1;
		this.fill(i6 - 2, this.top - 22, i7 + 2, this.top - 8, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, string4, this.centerX, this.top - 19, -1);
		this.fill(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.render(i1, i2, f3);
	}

	protected void buttonClicked(ButtonWidget button) {
		if(button instanceof GuiOptionButton optionWidget) {
			ReiMinimap.instance.setOption(optionWidget.getOption(), optionWidget.getValue());
			ReiMinimap.instance.saveOptions();
		}

		if(button instanceof GuiSimpleButton) {
			if(button == this.exitMenu) {
				this.minecraft.setScreen(this.page == 0 ? parent : new GuiOptionScreen(0));
			}

			if(button == this.waypoint) {
				this.minecraft.setScreen(new GuiWaypointScreen(this));
			}

			if(button == this.keyconfig) {
				this.minecraft.setScreen(new GuiKeyConfigScreen());
			}
		}

	}
}
