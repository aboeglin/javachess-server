package com.javachess;

import com.javachess.utils.FP;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board {
	private final Piece[] pieces;

	private static Function<Stream<String>, Stream<Piece>> colsToPieces(final Color c) {
		return FP.map(x -> c == Color.WHITE ? Pawn.of(x, "2", Color.WHITE)
				: Pawn.of(x, "7", Color.BLACK));
	}

	private static Stream<String> getCols() {
		return Stream.of("a", "b", "c", "d", "e", "f", "g", "h");
	}

	private static Stream<Piece> generatePawns() {
		return FP.concat(colsToPieces(Color.WHITE).apply(getCols()),
				colsToPieces(Color.BLACK).apply(getCols()));
	}

	private static Stream<Piece> generateRooks() {
		return Stream.of(Rook.of("a", "1", Color.WHITE), Rook.of("h", "1", Color.WHITE),
				Rook.of("a", "8", Color.BLACK), Rook.of("h", "8", Color.BLACK));
	}

	private static Stream<Piece> generateBishops() {
		return Stream.of(Bishop.of("b", "1", Color.WHITE), Bishop.of("g", "1", Color.WHITE),
				Bishop.of("g", "8", Color.BLACK), Bishop.of("b", "8", Color.BLACK));
	}

	private Board() {
		this.pieces = FP.pipe(FP.concat(generatePawns()), FP.concat(generateRooks()),
				FP.concat(generateBishops())).apply(Stream.of()).toArray(Piece[]::new);
	}

	public Stream<Piece> getPieces() {
		return Stream.of(this.pieces);
	}

	public static Optional<Piece> getPieceAt(final String x, final String y,
			final Board b) {
		return FP.find(p -> p.getX() == x && p.getY() == y, b.getPieces());
	}

	public static Board create() {
		return new Board();
	}
}
