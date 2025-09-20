package reifnsk.minimap.render;

import java.awt.Point;

public class StripCounter {
	private int count;
	private Point[] points;

	public StripCounter(int num) {
		this.points = new Point[num];
		int x = 0;
		int y = 0;
		int a = 0;
		int b = 0;
		int c = 0;
		this.points[0] = new Point(x, y);

		for(int i = 1; i < num; ++i) {
			switch(a) {
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

			++b;
			if(b > c) {
				a = a + 1 & 3;
				b = 0;
				if(a == 0 || a == 2) {
					++c;
				}
			}

			this.points[i] = new Point(x, y);
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
