package reifnsk.minimap;

public enum EnumOption {
	MINIMAP("Rei\'s Minimap", 0, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	RENDER_TYPE("Render Type", 0, new EnumOptionValue[]{EnumOptionValue.SURFACE, EnumOptionValue.CAVE}),
	DEATH_POINT("Death Point", 0, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MINIMAP_OPTION("Minimap Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	SURFACE_MAP_OPTION("SurfaceMap Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	ENTITIES_RADAR_OPTION("EntitiesRadar Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	ABOUT_MINIMAP("About Minimap", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	MAP_SHAPE("Map Shape", 1, new EnumOptionValue[]{EnumOptionValue.SQUARE, EnumOptionValue.ROUND}),
	MAP_POSITION("Map Position", 1, new EnumOptionValue[]{EnumOptionValue.UPPER_LEFT, EnumOptionValue.LOWER_LEFT, EnumOptionValue.UPPER_RIGHT, EnumOptionValue.LOWER_RIGHT}),
	MAP_TRANSPARENCY("Map Transparency", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	FILTERING("Filtering", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_COORDINATES("Show Coordinates", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_MENU_KEY("Show MenuKey", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	DEFAULT_ZOOM("Default Zoom", 1, new EnumOptionValue[]{EnumOptionValue.X0_5, EnumOptionValue.X1_0, EnumOptionValue.X1_5, EnumOptionValue.X2_0, EnumOptionValue.X4_0, EnumOptionValue.X8_0}),
	UPDATE_FREQUENCY("Update Frequency", 1, new EnumOptionValue[]{EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH}),
	THREADING("Threading", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	THREAD_PRIORITY("Thread Priority", 1, new EnumOptionValue[]{EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH}),
	LIGHTING("Lighting", 2, new EnumOptionValue[]{EnumOptionValue.DYNAMIC, EnumOptionValue.DAY_TIME, EnumOptionValue.NIGHT_TIME, EnumOptionValue.DISABLE}),
	TERRAIN_UNDULATE("Terrain Undulate", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	TERRAIN_DEPTH("Terrain Depth", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	TRANSPARENCY("Transparency", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENVIRONMENT_COLOR("Environment Color", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	OMIT_HEIGHT_CALC("Omit Height Calc", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	HIDE_SNOW("Hide Snow", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_SLIME_CHUNK("Show Slime Chunk", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITIES_RADAR("Entities Radar", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_PLAYER("Player", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_ANIMAL("Animal", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_MOB("Mob", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_SLIME("Slime", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_SQUID("Squid", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_DIRECTION("Show Direction", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ABOUT_VERSION("Version", 4, new EnumOptionValue[]{EnumOptionValue.VERSION}),
	ABOUT_AUTHER("Auther", 4, new EnumOptionValue[]{EnumOptionValue.AUTHER}),
	ENG_FORUM("Forum (en)", 4, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	JP_FORUM("Forum (jp)", 4, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION});

	public static final int maxPage;
	private String name;
	private EnumOptionValue[] values;
	private int page;

	private EnumOption(String name, int page, EnumOptionValue... values) {
		this.name = name;
		this.page = page;
		this.values = values;
	}

	public String getText() {
		return this.name;
	}

	public int getPage() {
		return this.page;
	}

	public int getValueNum() {
		return this.values.length;
	}

	public EnumOptionValue getValue(int i) {
		return i >= 0 && i < this.values.length ? this.values[i] : this.values[0];
	}

	public int getValue(EnumOptionValue v) {
		for(int i = 0; i < this.values.length; ++i) {
			if(this.values[i] == v) {
				return i;
			}
		}

		return -1;
	}

	static {
		int max = 0;
		EnumOption[] arr$ = values();
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			EnumOption eo = arr$[i$];
			if(max < eo.page) {
				max = eo.page;
			}
		}

		maxPage = max;
	}
}
