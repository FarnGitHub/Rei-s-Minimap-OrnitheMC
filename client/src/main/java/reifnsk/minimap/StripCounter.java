package reifnsk.minimap;

import java.awt.Point;

class StripCounter {
	private int count;
	private Point[] points;

	StripCounter(int num) {
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

	Point next() {
		return this.points[this.count++];
	}

	int count() {
		return this.count;
	}

	void reset() {
		this.count = 0;
	}
}
