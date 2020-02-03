package com.javachess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RookTests {
	@Test
	@DisplayName("equals should return true if the piece has the same color and the same position")
	public void equalsTrue() {
		Piece p1 = Rook.of("a", "1", Color.WHITE);
		Piece p2 = Rook.of("a", "1", Color.WHITE);
		assertEquals(true, p1.equals(p2));
	}

	@Test
	@DisplayName("equals should return false if the piece has the same color and the same position")
	public void equalsFalse() {
		Piece p1 = Rook.of("a", "1", Color.BLACK);
		Piece p2 = Rook.of("a", "1", Color.WHITE);
		assertEquals(false, p1.equals(p2));
	}
}
