package com.javachess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceTests {
	@Test
	@DisplayName("getYAsInt should return an integer representation of y")
	public void getYAsInt() {
		Piece p1 = Pawn.of("a", "1", Color.WHITE);
		assertEquals(1, Piece.getYAsInt(p1));
	}

	@Test
	@DisplayName("getXAsInt should return an integer representation of y")
	public void getXAsInt() {
		Piece p1 = Pawn.of("e", "1", Color.WHITE);
		assertEquals(5, Piece.getXAsInt(p1));
	}
}
