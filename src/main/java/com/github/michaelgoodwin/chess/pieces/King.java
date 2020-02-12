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
package com.github.michaelgoodwin.chess.pieces;

import com.github.michaelgoodwin.chess.GameBoard;
import com.github.michaelgoodwin.chess.ImageUtil;
import com.github.michaelgoodwin.chess.Team;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class King extends Piece
{
	private static final BufferedImage WHITE_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "wk.png");
	private static final BufferedImage BLACK_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "bk.png");

	// TODO: track and allow for castling
	public boolean canCastle = false;

	public King(Team team)
	{
		super(team, WHITE_ICON, BLACK_ICON);
	}

	@Override
	public boolean canMoveToPoint(Point point, GameBoard gameBoard)
	{
		final Piece[][] board = gameBoard.getBoard();
		// Must move to a new point
		if (getLocation().equals(point))
		{
			return false;
		}

		// A king can move one tile in any direction
		final int xDiff = Math.abs(point.x - getLocation().x);
		final int yDiff = Math.abs(point.y - getLocation().y);

		// Moved more than possible
		if (xDiff > 1 || yDiff > 1)
		{
			return false;
		}

		final Piece targetPiece = board[point.x][point.y];
		if (targetPiece != null)
		{
			if (targetPiece.getTeam().equals(getTeam()))
			{
				return false;
			}
		}

		// TODO: Check if we'd be in check by moving here (or captured piece would be protected)
		return true;
	}

	// Similar to Piece::getPossibleMovesFromOffset but can only move 1 tile and can't move into check
	@Override
	public Set<Point> getPossibleMoves(Point point, GameBoard gameBoard)
	{
		final Piece[][] board = gameBoard.getBoard();
		final int[][] offsets = {
			{0, 1}, // Up
			{0, -1}, // Down
			{-1, 0}, // Left
			{1, 0}, // Right

			{1, 1}, // Up & Right
			{-1, 1}, // Up & Left
			{1, -1}, // Down & Right
			{-1, -1} // Down & Left
		};

		final Set<Point> points = new HashSet<>();
		for (int[] o : offsets)
		{
			final Point p = new Point(point.x + o[0], point.y + o[1]);
			if (!isPointValid(p))
			{
				// Out of bounds
				continue;
			}

			final Piece piece = board[p.x][p.y];
			if (piece == null || !piece.getTeam().equals(getTeam()))
			{
				// TODO: Check if this tile is being attacked by another unit (would put us in check)
				points.add(p);
			}
		}

		return points;
	}

	@Override
	public String getChessNotationPrefix()
	{
		return "K";
	}
}

