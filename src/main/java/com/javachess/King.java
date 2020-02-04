package com.javachess;

public class King extends Piece {
	private King(String x, String y, Color c) {
		this.x = x;
		this.y = y;
		this.color = c;
	}

	public static King of(String x, String y, Color c) {
		return new King(x, y, c);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof King;
	}
}
