package reifnsk.minimap;

import java.awt.Point;

class StripCounter {
	private int count;
	private Point[] points;

	StripCounter(int i1) {
		this.points = new Point[i1];
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;
		this.points[0] = new Point(i2, i3);

		for(int i7 = 1; i7 < i1; ++i7) {
			switch(i4) {
			case 0:
				--i3;
				break;
			case 1:
				++i2;
				break;
			case 2:
				++i3;
				break;
			case 3:
				--i2;
			}

			++i5;
			if(i5 > i6) {
				i4 = i4 + 1 & 3;
				i5 = 0;
				if(i4 == 0 || i4 == 2) {
					++i6;
				}
			}

			this.points[i7] = new Point(i2, i3);
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
