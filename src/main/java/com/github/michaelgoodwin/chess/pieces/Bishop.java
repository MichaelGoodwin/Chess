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
import java.util.Arrays;
import java.util.Set;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Bishop extends Piece
{
	private static final BufferedImage WHITE_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "wb.png");
	private static final BufferedImage BLACK_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "bb.png");

	public Bishop(Team team)
	{
		super(team, WHITE_ICON, BLACK_ICON);
	}

	@Override
	public boolean canMoveToPoint(Point point, GameBoard gameBoard)
	{
		// Must move to a new point
		if (getLocation().equals(point))
		{
			return false;
		}

		// A bishop can only move diagonally
		int xDiff = point.x - getLocation().x;
		int yDiff = point.y - getLocation().y;

		// Not moving diagonally
		if (Math.abs(xDiff) != Math.abs(yDiff))
		{
			return false;
		}

		if (isPinned(gameBoard))
		{
			// Pinned pieces can only move in the direction of the pin
			if (Arrays.compare(getMovementOffset(getLocation(), point), getKingPinOffset(gameBoard)) != 0)
			{
				return false;
			}
		}

		// Check if path to target location is clear
		if (!canReachDestination(point, gameBoard))
		{
			return false;
		}

		return true;
	}

	@Override
	public Set<Point> getPossibleMoves(Point point, GameBoard board)
	{
		final int[][] offsets = {
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
		return "B";
	}
}
