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

import com.github.michaelgoodwin.chess.pieces.Bishop;
import com.github.michaelgoodwin.chess.pieces.King;
import com.github.michaelgoodwin.chess.pieces.Knight;
import com.github.michaelgoodwin.chess.pieces.Pawn;
import com.github.michaelgoodwin.chess.pieces.Piece;
import com.github.michaelgoodwin.chess.pieces.Queen;
import com.github.michaelgoodwin.chess.pieces.Rook;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameBoard
{
	public static final int SIZE = 8;
	private static final int WHITE_PAWN_ROW = 1; // 2nd row
	private static final int BLACK_PAWN_ROW = 6; // 7th row
	private static final int PAWN_COUNT = 8;

	private static final int WHITE_BACK_ROW = 0; // 1st row
	private static final int BLACK_BACK_ROW = 7; // 8th row

	public Piece[][] board = new Piece[SIZE][SIZE];
	public Move lastMove;
	@Setter
	public Player activePlayer;

	// Needed for checking for pins
	private Map<Team, Point> kingPositions = new HashMap<>();

	public void newGame()
	{
		board = new Piece[SIZE][SIZE];
		createTeamPieces(Team.WHITE);
		createTeamPieces(Team.BLACK);
		lastMove = null;
		activePlayer = null;
	}

	private void createTeamPieces(Team team)
	{
		int pawnRow, backRow;
		switch (team)
		{
			case WHITE:
				pawnRow = WHITE_PAWN_ROW;
				backRow = WHITE_BACK_ROW;
				break;
			case BLACK:
				pawnRow = BLACK_PAWN_ROW;
				backRow = BLACK_BACK_ROW;
				break;
			default:
				return;
		}

		for (int i = 0; i < PAWN_COUNT; i++)
		{
			final Pawn p = new Pawn(team);
			p.setLocation(new Point(i, pawnRow));
			board[i][pawnRow] = p;
		}

		// TODO: Find a programmatic approach to creating the back rank
		final Piece aRook = new Rook(team);
		aRook.setLocation(new Point(0, backRow));
		board[0][backRow] = aRook;

		final Piece bKnight = new Knight(team);
		bKnight.setLocation(new Point(1, backRow));
		board[1][backRow] = bKnight;

		final Piece cBishop = new Bishop(team);
		cBishop.setLocation(new Point(2, backRow));
		board[2][backRow] = cBishop;

		final Piece queen = new Queen(team);
		queen.setLocation(new Point(3, backRow));
		board[3][backRow] = queen;

		final Piece king = new King(team);
		king.setLocation(new Point(4, backRow));
		board[4][backRow] = king;
		kingPositions.put(team, king.getLocation());

		final Piece fBishop = new Bishop(team);
		fBishop.setLocation(new Point(5, backRow));
		board[5][backRow] = fBishop;

		final Piece gKnight = new Knight(team);
		gKnight.setLocation(new Point(6, backRow));
		board[6][backRow] = gKnight;

		final Piece hRook = new Rook(team);
		hRook.setLocation(new Point(7, backRow));
		board[7][backRow] = hRook;
	}

	public void playMove(Move move)
	{
		// TODO: Add checks and stuff
		final Point start = move.getStartingPoint();
		final Point end = move.getEndingPoint();

		move.getMovedPiece().setLocation(end);
		if (move.getCapturedPiece() != null)
		{
			move.getCapturedPiece().setLocation(new Point(-1, -1));
		}

		board[start.x][start.y] = null;
		board[end.x][end.y] = move.getMovedPiece();

		lastMove = move;
	}
}
