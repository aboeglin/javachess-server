package com.javachess;

import com.javachess.utils.FP;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board {
	private final Piece[] pieces;

	private static Function<Stream<String>, Stream<Piece>> colsToPieces(final Color c) {
		return FP.map(FP.ifElse(__ -> c == Color.WHITE ? Boolean.TRUE : Boolean.FALSE, x -> Pawn.of(x, "1", Color.WHITE),
				x -> Pawn.of(x, "8", Color.BLACK)));
	}

	private static Stream<String> getCols() {
		return Stream.of("a", "b", "c", "d", "e", "f", "g", "h");
	}

	private static Stream<Piece> generatePawns() {
		return FP.concat(colsToPieces(Color.WHITE).apply(getCols()), colsToPieces(Color.BLACK).apply(getCols()));
	}

	private Board() {
		this.pieces = generatePawns().toArray(Piece[]::new);
	}

	public Stream<Piece> getPieces() {
		return Stream.of(this.pieces);
	}

	public static Optional<Piece> getPieceAt(final String x, final String y, final Board b) {
		return FP.find(p -> p.getX() == x && p.getY() == y, b.getPieces());
		// return b.getPieces().(p -> p.getX() == x && p.getY() == y);
	}

	public static Board create() {
		return new Board();
	}
}
