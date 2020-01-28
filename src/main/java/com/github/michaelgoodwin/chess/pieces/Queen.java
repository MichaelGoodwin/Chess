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

import com.github.michaelgoodwin.chess.Team;
import java.awt.Point;
import java.util.Set;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Queen extends Piece
{
	public Queen(Team team)
	{
		super(team);
	}

	@Override
	public boolean canMoveToPoint(Point point, Piece[][] board)
	{
		// Must move to a new point
		if (getLocation().equals(point))
		{
			return false;
		}

		// A queen can move an unlimited number of tiles diagonally(bishop) or in one direction(Rook)
		final int xDiff = getLocation().x - point.x;
		final int yDiff = getLocation().y - point.y;

		final boolean oneDirection = yDiff == 0 || xDiff == 0;
		final boolean diagonalMovement = yDiff == xDiff;

		if (!oneDirection && !diagonalMovement)
		{
			return false;
		}

		// Check if path to target location is clear
		if (!canReachDestination(point, board))
		{
			return false;
		}

		// TODO: Check for king pins
		return true;
	}

	@Override
	public Set<Point> getPossibleMoves(Point point, Piece[][] board)
	{
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

		return getPossibleMovesFromOffsets(point, board, offsets);
	}

	@Override
	public String getChessNotationPrefix()
	{
		return "Q";
	}
}
