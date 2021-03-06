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
import com.github.michaelgoodwin.chess.Team;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class Piece
{
	private final Team team;
	private Point location;

	/**
	 * Checks if the piece can move to the desired point on the board
	 * @param point target location
	 * @param board current game board
	 * @return can it move there
	 */
	public abstract boolean canMoveToPoint(final Point point, final Piece[][] board);

	/**
	 * Calculates all possible moves from the specified point on the board
	 * @param point current location
	 * @param board current game board
	 * @return Set of {@link Point}s
	 */
	public abstract Set<Point> getPossibleMoves(final Point point, final Piece[][] board);

	/**
	 * Calculates all possible moves from the specified point on the board by applying the given offsets
	 * Continues applying the offset until it reaches the edge of the board, an occupied friendly tile, or would capture an enemy unit
	 * @param point current location
	 * @param board current game board
	 * @param offsets the offsets to be applied
	 * @return Set of {@link Point}s
	 */
	public Set<Point> getPossibleMovesFromOffsets(final Point point, final Piece[][] board, final int[][] offsets)
	{
		final Set<Point> points = new HashSet<>();

		for (int[] o : offsets)
		{
			Point p = point;
			for (int i = 0; i < GameBoard.SIZE; i++)
			{
				p = new Point(p.x + o[0], p.y + o[1]);
				if (p.x > GameBoard.SIZE || p.y > GameBoard.SIZE)
				{
					// Out of bounds
					break;
				}

				Piece piece = board[p.x][p.y];
				if (piece == null)
				{
					points.add(p);
				}
				else
				{
					if (!piece.getTeam().equals(getTeam()))
					{
						points.add(p);
					}
					// Can't go through pieces so can't go any further in this direction
					break;
				}
			}
		}

		// TODO: Account for being pinned
		return points;
	}

	/**
	 * Checks if the piece can reach the target point, target point should be in a straight line
	 * Will return false if there are pieces between or on your target point.
	 * @param point
	 * @param board
	 * @return
	 */
	public boolean canReachDestination(final Point point, final Piece[][] board)
	{
		final int xDiff = getLocation().x - point.x;
		final int yDiff = getLocation().y - point.y;

		int[] offset;
		// Moving diagonally?
		if (Math.abs(xDiff) == Math.abs(yDiff))
		{
			offset = new int[]{xDiff > 0 ? 1 : -1, yDiff > 0 ? 1 : -1};
		}
		else
		{
			offset = new int[]{0, 0};
			if (xDiff != 0)
			{
				offset[0] = xDiff > 0 ? 1 : -1;
			}
			else
			{
				offset[1] = yDiff > 0 ? 1 : -1;
			}
		}

		Point p = getLocation();
		while (p != null)
		{
			// Reached the target location
			if (p.equals(point))
			{
				p = null;
				continue;
			}

			p = new Point(p.x + offset[0], p.y + offset[1]);
			if (p.x > GameBoard.SIZE || p.y > GameBoard.SIZE)
			{
				// Out of bounds
				return false;
			}

			final Piece piece = board[p.x][p.y];
			if (piece != null)
			{
				// We only care if its occupied by the enemy team and its the ending point
				if (piece.getTeam().equals(getTeam()) || !p.equals(point))
				{
					return false;
				}
				p = null;
			}
		}


		//TODO: Check for pinned
		return true;
	}

	/**
	 * Gets the chess notation prefix for this piece
	 * @return notation prefix
	 */
	public String getChessNotationPrefix()
	{
		return "";
	}
}
