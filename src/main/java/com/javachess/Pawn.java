package com.javachess;

public class Pawn extends Piece {
	private Pawn(String x, String y, Color c) {
		this.x = x;
		this.y = y;
		this.color = c;
	}

	public static Pawn of(String x, String y, Color c) {
		return new Pawn(x, y, c);
	}

	@Override
	public boolean canMoveTo(String x, String y, Board b) {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof Pawn;
	}
}
