package reifnsk.minimap.main.option;

public enum EnumOption {
	MINIMAP("Rei's Minimap", 0, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	RENDER_TYPE("Render Type", 0, EnumOptionValue.SURFACE, EnumOptionValue.BIOME, EnumOptionValue.TEMPERATURE, EnumOptionValue.HUMIDITY, EnumOptionValue.CAVE),
	DEATH_POINT("Death Point", 0, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	MINIMAP_OPTION("Minimap Options", 0, EnumOptionValue.SUB_OPTION),
	SURFACE_MAP_OPTION("SurfaceMap Options", 0, EnumOptionValue.SUB_OPTION),
	ENTITIES_RADAR_OPTION("EntitiesRadar Options", 0, EnumOptionValue.SUB_OPTION),
	MARKER_OPTION("Marker Options", 0, EnumOptionValue.SUB_OPTION),
	ABOUT_MINIMAP("About Minimap", 0, EnumOptionValue.SUB_OPTION),
	MAP_SHAPE("Map Shape", 1, EnumOptionValue.SQUARE, EnumOptionValue.ROUND),
	TEXTURE("Texture", 1, EnumOptionValue.REI_MINIMAP, EnumOptionValue.ZAN_MINIMAP),
	DIRECTION_TYPE("Sunrise Direction", 1, EnumOptionValue.EAST, EnumOptionValue.NORTH),
	MAP_POSITION("Map Position", 1, EnumOptionValue.UPPER_LEFT, EnumOptionValue.LOWER_LEFT, EnumOptionValue.UPPER_RIGHT, EnumOptionValue.LOWER_RIGHT),
	MAP_SCALE("Map Scale", 1, EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE, EnumOptionValue.LARGER),
	MAP_OPACITY("Map Opacity", 1, EnumOptionValue.PERCENT25, EnumOptionValue.PERCENT50, EnumOptionValue.PERCENT75, EnumOptionValue.PERCENT100),
	LARGE_MAP_SCALE("Large Map Scale", 1, EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE, EnumOptionValue.LARGER),
	LARGE_MAP_OPACITY("Large Map Opacity", 1, EnumOptionValue.PERCENT25, EnumOptionValue.PERCENT50, EnumOptionValue.PERCENT75, EnumOptionValue.PERCENT100),
	LARGE_MAP_LABEL("Large Map Label", 1, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	FILTERING("Filtering", 1, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	SHOW_COORDINATES("Show Coordinates", 1, EnumOptionValue.TYPE1, EnumOptionValue.TYPE2, EnumOptionValue.DISABLE),
	SHOW_MENU_KEY("Show MenuKey", 1, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	FONT_SCALE("Font Scale", 1, EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE),
	DEFAULT_ZOOM("Default Zoom", 1, EnumOptionValue.X0_5, EnumOptionValue.X1_0, EnumOptionValue.X1_5, EnumOptionValue.X2_0, EnumOptionValue.X4_0, EnumOptionValue.X8_0),
	MASK_TYPE("MapMask Type", 1, EnumOptionValue.DEPTH, EnumOptionValue.STENCIL),
	UPDATE_FREQUENCY("Update Frequency", 1, EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH),
	THREADING("Threading", 1, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	THREAD_PRIORITY("Thread Priority", 1, EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH),
	LIGHTING("Lighting", 2, EnumOptionValue.DYNAMIC, EnumOptionValue.DAY_TIME, EnumOptionValue.NIGHT_TIME, EnumOptionValue.DISABLE),
	LIGHTING_TYPE("Lighting Type", 2, EnumOptionValue.TYPE1, EnumOptionValue.TYPE2),
	TERRAIN_UNDULATE("Terrain Undulate", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	TERRAIN_DEPTH("Terrain Depth", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	TRANSPARENCY("Transparency", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENVIRONMENT_COLOR("Environment Color", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	OMIT_HEIGHT_CALC("Omit Height Calc", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	HIDE_SNOW("Hide Snow", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	SHOW_CHUNK_GRID("Show Chunk Grid", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	SHOW_SLIME_CHUNK("Show Slime Chunk", 2, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITIES_RADAR("Entities Radar", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_PLAYER("Player", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_ANIMAL("Animal", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_MOB("Monster", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_SLIME("Slime", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_SQUID("Squid", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_LIVING("Other Living", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_LIGHTNING("Lightning", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ENTITY_DIRECTION("Show Direction", 3, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	MARKER("Marker", 4, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	MARKER_ICON("Icon", 4, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	MARKER_LABEL("Label", 4, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	MARKER_DISTANCE("Distance", 4, EnumOptionValue.ENABLE, EnumOptionValue.DISABLE),
	ABOUT_VERSION("Version", 5, EnumOptionValue.VERSION),
	ABOUT_AUTHER("Author", 5, EnumOptionValue.AUTHOR),
	ENG_FORUM("Forum (en)", 5, EnumOptionValue.SUB_OPTION),
	JP_FORUM("Forum (jp)", 5, EnumOptionValue.SUB_OPTION);

	public static final int maxPage;
	private final String name;
	private final EnumOptionValue[] values;
	private final int page;

	static {
		int maxP = 0;

        for (EnumOption option : values()) {
            if (maxP < option.page)
                maxP = option.page;
        }

		maxPage = maxP;
	}

	EnumOption(String name, int page, EnumOptionValue... value) {
		this.name = name;
		this.page = page;
		this.values = value;
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

	public EnumOptionValue getValue(int value) {
		return value >= 0 && value < this.values.length ? this.values[value] : this.values[0];
	}

	public int getValue(EnumOptionValue value) {
		for(int index = 0; index < this.values.length; ++index) {
			if(this.values[index] == value) {
				return index;
			}
		}

		return -1;
	}
}
