package com.javachess;

public abstract class Piece {
	protected String x;
	protected String y;
	protected Color color;

	public String getX() {
		return this.x;
	}

	public String getY() {
		return this.y;
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Piece) {
			Piece p = (Piece) o;
			return this.x == p.x && this.y == p.y && this.color == p.color;
		} else {
			return false;
		}
	}
}