package reifnsk.minimap.main.option;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import reifnsk.minimap.main.ReiMinimap;

@SuppressWarnings("unused")
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

	private final static File configFile = new File(ReiMinimap.directory, "keyconfig.txt");
	private final int defaultKeyIndex;
	private final String label;
	private int keyIndex;
	private boolean keyDown;
	private boolean oldKeyDown;

	static {
		loadKeyConfig();
		saveKeyConfig();
	}

	KeyInput(int keyId) {
		this.defaultKeyIndex = keyId;
		this.keyIndex = keyId;
		this.label = ReiMinimap.capitalize(this.name());
	}

	KeyInput(String label, int keyId) {
		this.label = label;
		this.defaultKeyIndex = keyId;
		this.keyIndex = keyId;
	}

	public void setKey(int key) {
		if(key == 1) {
			key = 0;
		}

		if(key != 0 || this != MENU_KEY) {
			if(key != 0) {
                for (KeyInput input : values()) {
                    if (input.keyIndex == key) {
                        if (input == MENU_KEY && this.keyIndex == 0) {
                            return;
                        }

                        input.keyIndex = this.keyIndex;
                        input.keyDown = false;
                        input.oldKeyDown = false;
                        break;
                    }
                }
			}

			this.keyIndex = key;
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
		String keyName = Keyboard.getKeyName(this.keyIndex);
		return keyName == null ? String.format("#%02X", this.keyIndex) : ReiMinimap.capitalize(keyName);
	}

	public void setKey(String keyName) {
		int keyId = Keyboard.getKeyIndex(keyName);
		if(keyName.startsWith("#")) {
			try {
				keyId = Integer.parseInt(keyName.substring(1), 16);
			} catch (Exception ignored) {
			}
		}

		this.setKey(keyId);
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
        for (KeyInput input : values()) {
            input.oldKeyDown = input.keyDown;
            input.keyDown = input.keyIndex != 0 && Keyboard.isKeyDown(input.keyIndex);
        }

	}

	public static boolean saveKeyConfig() {
		try(PrintWriter writer = new PrintWriter(configFile)) {
            for (KeyInput keyInput1 : values()) {
                writer.println(keyInput1.toString());
            }
			return true;
		} catch (Exception ignored) {
		}

		return false;
	}

	public static void loadKeyConfig() {

        try (Scanner scanner = new Scanner(configFile)) {
            while (scanner.hasNextLine()) {
                try {
                    String[] lines = scanner.nextLine().split(":");
                    valueOf(ReiMinimap.toUpperCase(lines[0].trim())).setKey(ReiMinimap.toUpperCase(lines[1].trim()));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
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
