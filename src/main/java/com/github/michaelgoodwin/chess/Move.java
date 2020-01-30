/*
 * Copyright (c) 2020, Michael Goodwin <https://github.com/MichaelGoodwin>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.michaelgoodwin.chess;

import com.github.michaelgoodwin.chess.pieces.Knight;
import com.github.michaelgoodwin.chess.pieces.Piece;
import com.github.michaelgoodwin.chess.pieces.Rook;
import java.awt.Point;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Move
{
	private static final String CAPTURE_NOTATION = "x";
	private static final String CHECK_NOTATION = "+";
	private static final String MATE_NOTATION = "#";
	private static final String CASTLE_NOTATION = "0-0";
	private static final String LONG_CASTLE_NOTATION = "0-0-0";

	private final Player player;
	private final Piece movedPiece;
	private final Point startingPoint;
	private final Point endingPoint;
	private final Piece capturedPiece;
	private final Piece[][] board; // Need a snapshot of the board to calculate notation

	public static String getColumnLetter(final int colIndex)
	{
		return String.valueOf((char) ('A' + colIndex));
	}

	/**
	 * Converts the move to Algebraic Chess Notation.
	 * When two (or more) identical pieces can move to the same square the annotation would be identical
	 * These cases need to follow one of the below notations in descending order of importance
	 * 1) The starting column (if they differ); or
	 * 2) The starting row (if the files are the same but the ranks differ); or
	 * 3) Both the column and the row (if neither alone is sufficient to identify the piece, usually only when pawns have promoted)
	 * @return the move in algebraic chess notation
	 */
	private String toChessNotation()
	{
		String notation = movedPiece.getChessNotationPrefix();

		final int rowDiff = startingPoint.y - endingPoint.y;
		final int colDiff = startingPoint.x - endingPoint.x;

		// Rooks can only ever attack on the same column or the same row so case 3 will never effect them
		if (movedPiece instanceof Rook)
		{
			if (rowDiff != 0)
			{
				// Since we already know this is a valid move we only need to check for a rook on the other side of the
				// End point relative to the starting point since that's the only other valid move locations
				boolean movingDown = startingPoint.y > endingPoint.y;

				// Go from 0->end if the moved piece is above the captured piece and from end->GameBoard.SIZE if below
				int start = movingDown ? 0 : endingPoint.y + 1;
				int end = movingDown ? endingPoint.y : GameBoard.SIZE;
				for (int i = start; i < end; i++)
				{
					Piece p = board[startingPoint.x][i];
					if (!(p instanceof Rook))
					{
						continue;
					}

					if (p.canMoveToPoint(endingPoint, board))
					{
						notation += String.valueOf(startingPoint.y);
						break;
					}
				}
			}
		}

		// If multiple pieces of the same type can legally move to the same endingPoint they would have the same notation
		// For this specific case we need to include the column letter number (Ndx**) which is the x axis
		// Knights on the same column who can legally move to the same endingPoint would still have the notation (Ndx**)
		// For this specific case we need to use the row number instead (N3x**)
		// When there are Knights on the same row & column we need to combine the notation (Nd3x**)
		else if (movedPiece instanceof Knight)
		{

		}

		if (capturedPiece != null)
		{
			notation += CAPTURE_NOTATION;
			// TODO: Check for both Rooks/Knights being able to capture the same piece, if so append current column letter here as well
			if (false)
			{
				notation += getColumnLetter(startingPoint.x);
			}
		}

		// TODO: Add Check/Checkmate/Castling notations

		return notation;
	}
}
