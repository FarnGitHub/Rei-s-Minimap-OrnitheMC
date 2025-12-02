package reifnsk.minimap;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

public enum KeyInput {
	MENU_KEY(50),
	TOGGLE_ENABLE(0),
	TOGGLE_RENDER_TYPE(0),
	TOGGLE_ZOOM(44),
	TOGGLE_LARGE_MAP(45),
	TOGGLE_LARGE_MAP_LABEL(0),
	TOGGLE_WAYPOINTS_VISIBLE(0),
	TOGGLE_WAYPOINTS_MARKER(0),
	TOGGLE_WAYPOINTS_DIMENSION(0),
	TOGGLE_ENTITIES_RADAR(0),
	SET_WAYPOINT(46),
	WAYPOINT_LIST(0),
	ZOOM_IN(0),
	ZOOM_OUT(0);

	private static File configFile = new File(ReiMinimap.directory, "keyconfig.txt");
	private final int defaultKeyIndex;
	private String label;
	private int keyIndex;
	private boolean keyDown;
	private boolean oldKeyDown;

	static {
		loadKeyConfig();
		saveKeyConfig();
	}

	private KeyInput(int i3) {
		this.defaultKeyIndex = i3;
		this.keyIndex = i3;
		this.label = ReiMinimap.capitalize(this.name());
	}

	private KeyInput(String string3, int i4) {
		this.label = string3;
		this.defaultKeyIndex = i4;
		this.keyIndex = i4;
	}

	public void setKey(int i1) {
		if(i1 == 1) {
			i1 = 0;
		}

		if(i1 != 0 || this != MENU_KEY) {
			if(i1 != 0) {
				KeyInput[] keyInput5;
				int i4 = (keyInput5 = values()).length;

				for(int i3 = 0; i3 < i4; ++i3) {
					KeyInput keyInput2 = keyInput5[i3];
					if(keyInput2.keyIndex == i1) {
						if(keyInput2 == MENU_KEY && this.keyIndex == 0) {
							return;
						}

						keyInput2.keyIndex = this.keyIndex;
						keyInput2.keyDown = false;
						keyInput2.oldKeyDown = false;
						break;
					}
				}
			}

			this.keyIndex = i1;
			this.keyDown = false;
			this.oldKeyDown = false;
		}
	}

	public int getKey() {
		return this.keyIndex;
	}

	public String label() {
		return this.label;
	}

	public String getKeyName() {
		String string1 = Keyboard.getKeyName(this.keyIndex);
		return string1 == null ? String.format("#%02X", new Object[]{this.keyIndex}) : ReiMinimap.capitalize(string1);
	}

	public void setKey(String string1) {
		int i2 = Keyboard.getKeyIndex(string1);
		if(string1.startsWith("#")) {
			try {
				i2 = Integer.parseInt(string1.substring(1), 16);
			} catch (Exception exception3) {
			}
		}

		this.setKey(i2);
	}

	public boolean isKeyDown() {
		return this.keyDown;
	}

	public boolean isKeyPush() {
		return this.keyDown && !this.oldKeyDown;
	}

	public boolean isKeyPushUp() {
		return !this.keyDown && this.oldKeyDown;
	}

	public static void update() {
		KeyInput[] keyInput3;
		int i2 = (keyInput3 = values()).length;

		for(int i1 = 0; i1 < i2; ++i1) {
			KeyInput keyInput0 = keyInput3[i1];
			keyInput0.oldKeyDown = keyInput0.keyDown;
			keyInput0.keyDown = keyInput0.keyIndex != 0 && Keyboard.isKeyDown(keyInput0.keyIndex);
		}

	}

	public static boolean saveKeyConfig() {
		PrintWriter printWriter0 = null;

		try {
			printWriter0 = new PrintWriter(configFile);
			KeyInput[] keyInput4;
			int i3 = (keyInput4 = values()).length;

			for(int i2 = 0; i2 < i3; ++i2) {
				KeyInput keyInput1 = keyInput4[i2];
				printWriter0.println(keyInput1.toString());
			}

			return true;
		} catch (Exception exception8) {
		} finally {
			if(printWriter0 != null) {
				printWriter0.flush();
				printWriter0.close();
			}

		}

		return false;
	}

	public static void loadKeyConfig() {
		Scanner scanner0 = null;

		try {
			scanner0 = new Scanner(configFile);

			while(scanner0.hasNextLine()) {
				try {
					String[] string1 = scanner0.nextLine().split(":");
					valueOf(ReiMinimap.toUpperCase(string1[0].trim())).setKey(ReiMinimap.toUpperCase(string1[1].trim()));
				} catch (Exception exception6) {
				}
			}
		} catch (Exception exception7) {
		} finally {
			if(scanner0 != null) {
				scanner0.close();
			}

		}

	}

	public void setDefault() {
		this.keyIndex = this.defaultKeyIndex;
	}

	public boolean isDefault() {
		return this.keyIndex == this.defaultKeyIndex;
	}

	public String toString() {
		return ReiMinimap.capitalize(this.name()) + ": " + this.getKeyName();
	}
}
