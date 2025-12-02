package reifnsk.minimap;

public enum EnumOption {
	MINIMAP("Rei\'s Minimap", 0, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	RENDER_TYPE("Render Type", 0, new EnumOptionValue[]{EnumOptionValue.SURFACE, EnumOptionValue.BIOME, EnumOptionValue.TEMPERATURE, EnumOptionValue.HUMIDITY, EnumOptionValue.CAVE}),
	DEATH_POINT("Death Point", 0, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MINIMAP_OPTION("Minimap Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	SURFACE_MAP_OPTION("SurfaceMap Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	ENTITIES_RADAR_OPTION("EntitiesRadar Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	MARKER_OPTION("Marker Options", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	ABOUT_MINIMAP("About Minimap", 0, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	MAP_SHAPE("Map Shape", 1, new EnumOptionValue[]{EnumOptionValue.SQUARE, EnumOptionValue.ROUND}),
	TEXTURE("Texture", 1, new EnumOptionValue[]{EnumOptionValue.REI_MINIMAP, EnumOptionValue.ZAN_MINIMAP}),
	DIRECTION_TYPE("Sunrise Direction", 1, new EnumOptionValue[]{EnumOptionValue.EAST, EnumOptionValue.NORTH}),
	MAP_POSITION("Map Position", 1, new EnumOptionValue[]{EnumOptionValue.UPPER_LEFT, EnumOptionValue.LOWER_LEFT, EnumOptionValue.UPPER_RIGHT, EnumOptionValue.LOWER_RIGHT}),
	MAP_SCALE("Map Scale", 1, new EnumOptionValue[]{EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE, EnumOptionValue.LARGER}),
	MAP_OPACITY("Map Opacity", 1, new EnumOptionValue[]{EnumOptionValue.PERCENT25, EnumOptionValue.PERCENT50, EnumOptionValue.PERCENT75, EnumOptionValue.PERCENT100}),
	LARGE_MAP_SCALE("Large Map Scale", 1, new EnumOptionValue[]{EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE, EnumOptionValue.LARGER}),
	LARGE_MAP_OPACITY("Large Map Opacity", 1, new EnumOptionValue[]{EnumOptionValue.PERCENT25, EnumOptionValue.PERCENT50, EnumOptionValue.PERCENT75, EnumOptionValue.PERCENT100}),
	LARGE_MAP_LABEL("Large Map Label", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	FILTERING("Filtering", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_COORDINATES("Show Coordinates", 1, new EnumOptionValue[]{EnumOptionValue.TYPE1, EnumOptionValue.TYPE2, EnumOptionValue.DISABLE}),
	SHOW_MENU_KEY("Show MenuKey", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	FONT_SCALE("Font Scale", 1, new EnumOptionValue[]{EnumOptionValue.GUI_SCALE, EnumOptionValue.AUTO, EnumOptionValue.SMALL, EnumOptionValue.NORMAL, EnumOptionValue.LARGE}),
	DEFAULT_ZOOM("Default Zoom", 1, new EnumOptionValue[]{EnumOptionValue.X0_5, EnumOptionValue.X1_0, EnumOptionValue.X1_5, EnumOptionValue.X2_0, EnumOptionValue.X4_0, EnumOptionValue.X8_0}),
	MASK_TYPE("MapMask Type", 1, new EnumOptionValue[]{EnumOptionValue.DEPTH, EnumOptionValue.STENCIL}),
	UPDATE_FREQUENCY("Update Frequency", 1, new EnumOptionValue[]{EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH}),
	THREADING("Threading", 1, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	THREAD_PRIORITY("Thread Priority", 1, new EnumOptionValue[]{EnumOptionValue.VERY_LOW, EnumOptionValue.LOW, EnumOptionValue.MIDDLE, EnumOptionValue.HIGH, EnumOptionValue.VERY_HIGH}),
	LIGHTING("Lighting", 2, new EnumOptionValue[]{EnumOptionValue.DYNAMIC, EnumOptionValue.DAY_TIME, EnumOptionValue.NIGHT_TIME, EnumOptionValue.DISABLE}),
	LIGHTING_TYPE("Lighting Type", 2, new EnumOptionValue[]{EnumOptionValue.TYPE1, EnumOptionValue.TYPE2}),
	TERRAIN_UNDULATE("Terrain Undulate", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	TERRAIN_DEPTH("Terrain Depth", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	TRANSPARENCY("Transparency", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENVIRONMENT_COLOR("Environment Color", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	OMIT_HEIGHT_CALC("Omit Height Calc", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	HIDE_SNOW("Hide Snow", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_CHUNK_GRID("Show Chunk Grid", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	SHOW_SLIME_CHUNK("Show Slime Chunk", 2, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITIES_RADAR("Entities Radar", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_PLAYER("Player", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_ANIMAL("Animal", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_MOB("Monster", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_SLIME("Slime", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_SQUID("Squid", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_LIVING("Other Living", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_LIGHTNING("Lightning", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ENTITY_DIRECTION("Show Direction", 3, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MARKER("Marker", 4, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MARKER_ICON("Icon", 4, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MARKER_LABEL("Label", 4, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	MARKER_DISTANCE("Distance", 4, new EnumOptionValue[]{EnumOptionValue.ENABLE, EnumOptionValue.DISABLE}),
	ABOUT_VERSION("Version", 5, new EnumOptionValue[]{EnumOptionValue.VERSION}),
	ABOUT_AUTHER("Author", 5, new EnumOptionValue[]{EnumOptionValue.AUTHOR}),
	ENG_FORUM("Forum (en)", 5, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION}),
	JP_FORUM("Forum (jp)", 5, new EnumOptionValue[]{EnumOptionValue.SUB_OPTION});

	public static final int maxPage;
	private String name;
	private EnumOptionValue[] values;
	private int page;

	static {
		int i0 = 0;
		EnumOption[] enumOption4;
		int i3 = (enumOption4 = values()).length;

		for(int i2 = 0; i2 < i3; ++i2) {
			EnumOption enumOption1 = enumOption4[i2];
			if(i0 < enumOption1.page) {
				i0 = enumOption1.page;
			}
		}

		maxPage = i0;
	}

	private EnumOption(String string3, int i4, EnumOptionValue... enumOptionValue5) {
		this.name = string3;
		this.page = i4;
		this.values = enumOptionValue5;
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

	public EnumOptionValue getValue(int i1) {
		return i1 >= 0 && i1 < this.values.length ? this.values[i1] : this.values[0];
	}

	public int getValue(EnumOptionValue enumOptionValue1) {
		for(int i2 = 0; i2 < this.values.length; ++i2) {
			if(this.values[i2] == enumOptionValue1) {
				return i2;
			}
		}

		return -1;
	}
}
