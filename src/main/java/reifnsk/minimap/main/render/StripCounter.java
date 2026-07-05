package reifnsk.minimap.main.render;

import java.awt.Point;

public class StripCounter {
	private int count;
	private final Point[] points;

	public StripCounter(int size) {
		this.points = new Point[size];
		int x = 0;
		int y = 0;
		int direction = 0;
		int down = 0;
		int up = 0;
		this.points[0] = new Point(x, y);

		for(int index = 1; index < size; ++index) {
			switch(direction) {
				case 0:
					--y;
					break;
				case 1:
					++x;
					break;
				case 2:
					++y;
					break;
				case 3:
					--x;
			}

			++down;
			if(down > up) {
				direction = direction + 1 & 3;
				down = 0;
				if(direction == 0 || direction == 2) {
					++up;
				}
			}

			this.points[index] = new Point(x, y);
		}

	}

	public Point next() {
		return this.points[this.count++];
	}

	public int count() {
		return this.count;
	}

	public void reset() {
		this.count = 0;
	}
}
