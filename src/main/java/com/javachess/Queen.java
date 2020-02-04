package com.javachess;

public class Queen extends Piece {
	private Queen(String x, String y, Color c) {
		this.x = x;
		this.y = y;
		this.color = c;
	}

	public static Queen of(String x, String y, Color c) {
		return new Queen(x, y, c);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof Queen;
	}
}
