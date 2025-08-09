package reifnsk.minimap;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

public enum KeyInput {
	MENU_KEY(50),
	TOGGLE_ENABLE(0),
	TOGGLE_ZOOM(44),
	TOGGLE_LARGE_MAP(45),
	TOGGLE_WAYPOINTS(0),
	TOGGLE_CAVE_MAP(0),
	TOGGLE_ENTITIES_RADAR(0),
	SET_WAYPOINT(46),
	ZOOM_IN(0),
	ZOOM_OUT(0);

	private static File configFile = new File(ReiMinimap.directory, "keyconfig.txt");
	private final int defaultKeyIndex;
	private String label;
	private int keyIndex;
	private boolean keyDown;
	private boolean oldKeyDown;

	private KeyInput(int keyIndex) {
		this.defaultKeyIndex = keyIndex;
		this.keyIndex = keyIndex;
		this.label = ReiMinimap.capitalize(this.name());
	}

	private KeyInput(String label, int keyIndex) {
		this.label = label;
		this.defaultKeyIndex = keyIndex;
		this.keyIndex = keyIndex;
	}

	public void setKey(int keyIndex) {
		if(keyIndex == 1) {
			keyIndex = 0;
		}

		if(keyIndex != 0 || this != MENU_KEY) {
			if(keyIndex != 0) {
				KeyInput[] arr$ = values();
				int len$ = arr$.length;

				for(int i$ = 0; i$ < len$; ++i$) {
					KeyInput temp = arr$[i$];
					if(temp.keyIndex == keyIndex) {
						if(temp == MENU_KEY && this.keyIndex == 0) {
							return;
						}

						temp.keyIndex = this.keyIndex;
						temp.keyDown = false;
						temp.oldKeyDown = false;
						break;
					}
				}
			}

			this.keyIndex = keyIndex;
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
		return keyName == null ? String.format("#%02X", new Object[]{this.keyIndex}) : ReiMinimap.capitalize(keyName);
	}

	public void setKey(String keyName) {
		int key = Keyboard.getKeyIndex(keyName);
		if(keyName.startsWith("#")) {
			try {
				key = Integer.parseInt(keyName.substring(1), 16);
			} catch (Exception exception4) {
			}
		}

		this.setKey(key);
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
		KeyInput[] arr$ = values();
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			KeyInput input = arr$[i$];
			input.oldKeyDown = input.keyDown;
			input.keyDown = input.keyIndex != 0 && Keyboard.isKeyDown(input.keyIndex);
		}

	}

	public static boolean saveKeyConfig() {
		PrintWriter out = null;

		boolean len$;
		try {
			out = new PrintWriter(configFile);
			KeyInput[] e = values();
			int i10 = e.length;

			for(int i$ = 0; i$ < i10; ++i$) {
				KeyInput key = e[i$];
				out.println(key.toString());
			}

			return true;
		} catch (Exception exception8) {
			len$ = false;
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}

		}

		return len$;
	}

	public static void loadKeyConfig() {
		Scanner in = null;

		try {
			in = new Scanner(configFile);

			while(in.hasNextLine()) {
				try {
					String[] e = in.nextLine().split(":");
					valueOf(ReiMinimap.toUpperCase(e[0].trim())).setKey(ReiMinimap.toUpperCase(e[1].trim()));
				} catch (Exception exception6) {
				}
			}
		} catch (Exception exception7) {
		} finally {
			if(in != null) {
				in.close();
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

	static {
		loadKeyConfig();
	}
}
