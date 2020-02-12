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
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
public class Rook extends Piece
{
	private static final BufferedImage WHITE_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "wr.png");
	private static final BufferedImage BLACK_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "br.png");

	@Setter
	private boolean hasMoved = false;

	public Rook(Team team)
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

		// A Rook can move an unlimited number of tiles on one axis only
		final int xDiff = point.x - getLocation().x;
		final int yDiff = point.y - getLocation().y;

		final boolean oneDirection = yDiff == 0 || xDiff == 0;
		if (!oneDirection)
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

		if (!canReachDestination(point, gameBoard))
		{
			return false;
		}

		// TODO: Check for pinned
		return true;
	}

	@Override
	public Set<Point> getPossibleMoves(Point point, GameBoard board)
	{
		final int[][] offsets = {
			{0, 1}, // Up
			{0, -1}, // Down
			{-1, 0}, // Left
			{1, 0} // Right
		};

		return getPossibleMovesFromOffsets(point, board, offsets);
	}

	@Override
	public String getChessNotationPrefix()
	{
		return "R";
	}
}
