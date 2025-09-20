package reifnsk.minimap.optionscreen.screen;

import java.util.ArrayList;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import reifnsk.minimap.optionscreen.element.GuiOptionButton;
import reifnsk.minimap.optionscreen.enums.EnumOption;
import reifnsk.minimap.main.ReiMinimap;
import reifnsk.minimap.optionscreen.element.GuiSimpleButton;

public class GuiOptionScreen extends Screen implements GuiScreenInterface {
	public static final int minimapMenu = 0;
	public static final int optionMinimap = 1;
	public static final int optionSurfaceMap = 2;
	public static final int optionEntitiesRadar = 3;
	public static final int aboutMinimap = 4;
	private static final String[] TITLE_STRING = new String[]{"Rei\'s Minimap " + ReiMinimap.version, "Minimap Options", "SurfaceMap Options", "Entities Radar Options", "About Rei\'s Minimap"};
	private int page;
	private ArrayList buttonList = new ArrayList();
	private GuiSimpleButton exitMenu;
	private GuiSimpleButton waypoint;
	private GuiSimpleButton keyconfig;
	private int top;
	private int left;
	private int right;
	private int bottom;
	private int centerX;
	private int centerY;
	private Screen parentScreen;

	public GuiOptionScreen() {
	}

	public GuiOptionScreen(int page) {
		this.page = page;
	}

	public GuiOptionScreen(Screen screen, int page) {
		if(screen instanceof GuiScreenInterface) {
			parentScreen = ((GuiScreenInterface) screen).nonInterfaceParent();
		} else {
			parentScreen = screen;
		}
		this.page = page;
	}

	public void init() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		this.buttons.clear();
		this.buttonList.clear();
		EnumOption[] i = EnumOption.values();
		int button = i.length;

		for(int i$ = 0; i$ < button; ++i$) {
			EnumOption eo = i[i$];
			if(eo.getPage() == this.page && (!isMultiplayer() || (eo != EnumOption.RENDER_TYPE || ReiMinimap.instance.getAllowCavemap()) && (eo != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()))) {
				GuiOptionButton button1 = new GuiOptionButton(this.minecraft.textRenderer, eo);
				button1.setValue(ReiMinimap.instance.getOption(eo));
				this.buttons.add(button1);
				this.buttonList.add(button1);
			}
		}

		this.left = this.width - GuiOptionButton.getWidth() >> 1;
		this.top = this.height - this.buttonList.size() * 10 >> 1;
		this.right = this.width + GuiOptionButton.getWidth() >> 1;
		this.bottom = this.height + this.buttonList.size() * 10 >> 1;

		for(int i6 = 0; i6 < this.buttonList.size(); ++i6) {
			GuiOptionButton guiOptionButton7 = (GuiOptionButton)this.buttonList.get(i6);
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

	public void render(int i, int j, float f) {
		if(minecraft.world == null) renderBackground();
		String title = TITLE_STRING[this.page];
		int titleWidth = this.textRenderer.getWidth(title);
		int optionLeft = this.width - titleWidth >> 1;
		int optionRight = this.width + titleWidth >> 1;
		this.fill(optionLeft - 2, this.top - 22, optionRight + 2, this.top - 8, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, title, this.centerX, this.top - 19, -1);
		this.fill(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.render(i, j, f);
	}

	protected void buttonClicked(ButtonWidget guibutton) {
		if(guibutton instanceof GuiOptionButton) {
			GuiOptionButton gob = (GuiOptionButton)guibutton;
			ReiMinimap.instance.setOption(gob.getOption(), gob.getValue(), parentScreen);
			ReiMinimap.instance.saveOptions();
		}

		if(guibutton instanceof GuiSimpleButton) {
			if(guibutton == this.exitMenu) {
				this.minecraft.setScreen(this.page == 0 ? parentScreen : new GuiOptionScreen(parentScreen, 0));
			}

			if(guibutton == this.waypoint) {
				this.minecraft.setScreen(new GuiWaypointScreen(parentScreen));
			}

			if(guibutton == this.keyconfig) {
				this.minecraft.setScreen(new GuiKeyConfigScreen(parentScreen));
			}
		}

	}

	public boolean isMultiplayer() {
		return this.minecraft.world == null ? false : this.minecraft.world.isRemote;
	}

	@Override
	public Screen nonInterfaceParent() {
		return parentScreen;
	}
}
