package com.javachess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTests {
	@Test
	@DisplayName("create should return a fresh board with 16 pawns")
	public void createPawns() {
		Board b = Board.create();
		assertEquals(Board.getPieceAt("a", "1", b).get(), Pawn.of("a", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("b", "1", b).get(), Pawn.of("b", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("c", "1", b).get(), Pawn.of("c", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("d", "1", b).get(), Pawn.of("d", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("e", "1", b).get(), Pawn.of("e", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("f", "1", b).get(), Pawn.of("f", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("g", "1", b).get(), Pawn.of("g", "1", Color.WHITE));
		assertEquals(Board.getPieceAt("h", "1", b).get(), Pawn.of("h", "1", Color.WHITE));

		assertEquals(Board.getPieceAt("a", "8", b).get(), Pawn.of("a", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("b", "8", b).get(), Pawn.of("b", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("c", "8", b).get(), Pawn.of("c", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("d", "8", b).get(), Pawn.of("d", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("e", "8", b).get(), Pawn.of("e", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("f", "8", b).get(), Pawn.of("f", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("g", "8", b).get(), Pawn.of("g", "8", Color.BLACK));
		assertEquals(Board.getPieceAt("h", "8", b).get(), Pawn.of("h", "8", Color.BLACK));
	}
}
