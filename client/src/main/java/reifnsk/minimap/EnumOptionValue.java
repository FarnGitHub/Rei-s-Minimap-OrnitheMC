package reifnsk.minimap;

public enum EnumOptionValue {
	ENABLE(-1610547456),
	DISABLE(-1593901056),
	SURFACE(-1610547456),
	CAVE(-1593901056),
	SQUARE(-1610612481),
	ROUND(-1610547456),
	DYNAMIC(-1610547456),
	DAY_TIME(-1610547201),
	NIGHT_TIME(-1593868288),
	VERY_LOW(-1610612481),
	LOW(-1610547201),
	MIDDLE(-1610547456),
	HIGH(-1593868288),
	VERY_HIGH(-1593901056),
	SURFACE_MAP(-1593901056),
	CAVE_MAP(-1593901056),
	SUB_OPTION(-1606401984, "->"),
	UPPER_LEFT(-1610547456),
	LOWER_LEFT(-1610547456),
	UPPER_RIGHT(-1610547456),
	LOWER_RIGHT(-1610547456),
	X0_5(-1610547456, "x0.5"),
	X1_0(-1610547456, "x1.0"),
	X1_5(-1610547456, "x1.5"),
	X2_0(-1610547456, "x2.0"),
	X4_0(-1610547456, "x4.0"),
	X8_0(-1610547456, "x8.0"),
	VERSION(-1610547456, "v1.8"),
	AUTHER(-1610547456, "ReiFNSK");

	public final int color;
	private final String text;

	private EnumOptionValue(int color) {
		this.color = color;
		this.text = capitalize(this.name());
	}

	private EnumOptionValue(int color, String text) {
		this.color = color;
		this.text = text;
	}

	public String text() {
		return this.text;
	}

	public static EnumOptionValue bool(boolean b) {
		return b ? ENABLE : DISABLE;
	}

	public static boolean bool(EnumOptionValue v) {
		return v == ENABLE;
	}

	public static String capitalize(String src) {
		if(src == null) {
			return null;
		} else {
			boolean title = true;
			char[] cs = src.toCharArray();
			int i = 0;

			for(int j = cs.length; i < j; ++i) {
				char c = cs[i];
				if(c == 95) {
					c = 32;
				}

				cs[i] = title ? Character.toTitleCase(c) : Character.toLowerCase(c);
				title = Character.isWhitespace(c);
			}

			return new String(cs);
		}
	}
}
