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
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public abstract class Piece
{
	private final Team team;
	private Point location;
	private final BufferedImage whiteIcon;
	private final BufferedImage blackIcon;

	/**
	 * Checks if the piece can move to the desired point on the board
	 * @param point target location
	 * @param gameBoard current game board
	 * @return can it move there
	 */
	public abstract boolean canMoveToPoint(final Point point, final GameBoard gameBoard);

	/**
	 * Calculates all possible moves from the specified point on the board
	 * @param point current location
	 * @param gameBoard current game board
	 * @return Set of {@link Point}s
	 */
	public abstract Set<Point> getPossibleMoves(final Point point, final GameBoard gameBoard);

	/**
	 * Gets the icon that show be displayed for the piece, accounting for team color.
	 * @return Piece icon
	 */
	public BufferedImage getIcon()
	{
		return getTeam().equals(Team.WHITE) ? whiteIcon : blackIcon;
	}

	/**
	 * Calculates all possible moves from the specified point on the board by applying the given offsets
	 * Continues applying the offset until it reaches the edge of the board, an occupied friendly tile, or would capture an enemy unit
	 * @param point current location
	 * @param gameBoard current game board
	 * @param offsets the offsets to be applied
	 * @return Set of {@link Point}s
	 */
	public Set<Point> getPossibleMovesFromOffsets(final Point point, final GameBoard gameBoard, final int[][] offsets)
	{
		final Piece[][] board = gameBoard.getBoard();

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

		return points;
	}

	/**
	 * Checks if the piece can reach the target point, target point should be in a straight line
	 * Does not check if the piece is pinned
	 * @param point destination tile
	 * @param gameBoard current game board
	 * @return false if pieces between or on your target point
	 */
	public boolean canReachDestination(final Point point, final GameBoard gameBoard)
	{
		final Piece[][] board = gameBoard.getBoard();

		final int xDiff = getLocation().x - point.x;
		final int yDiff = getLocation().y - point.y;

		final boolean oneDirection = yDiff == 0 || xDiff == 0;
		final boolean diagonalMovement = Math.abs(yDiff) == Math.abs(xDiff);

		// Pieces that aren't in a straight line, like the knight, should override this method
		if (!oneDirection && !diagonalMovement)
		{
			return false;
		}

		final int[] offset = getMovementOffset(point);

		Point p = getLocation();
		while (p != null)
		{
			// Reached the target location
			if (p.equals(point))
			{
				p = null;
				return true;
			}

			p = new Point(p.x + offset[0], p.y + offset[1]);
			if (p.x > GameBoard.SIZE || p.y > GameBoard.SIZE)
			{
				// Out of bounds
				p = null;
				continue;
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

		return false;
	}

	/**
	 * Gets the chess notation prefix for this piece
	 * @return notation prefix
	 */
	public String getChessNotationPrefix()
	{
		return "";
	}

	/**
	 * Returns the offset required to get to the target position iteratively
	 * @param target destination tile
	 * @return {xOffset,yOffset}
	 */
	public int[] getMovementOffset(final Point target)
	{
		final int xDiff = getLocation().x - target.x;
		final int yDiff = getLocation().y - target.y;

		return new int[] {Integer.compare(xDiff, 0), Integer.compare(yDiff, 0)};
	}
}
