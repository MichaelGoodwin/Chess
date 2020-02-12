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
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends Piece
{
	private static final BufferedImage WHITE_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "wp.png");
	private static final BufferedImage BLACK_ICON = ImageUtil.getResourceStreamFromClass(Piece.class, "bp.png");

	private static final int[][] WHITE_ATTACK_OFFSETS = {
		{1, 1}, // Up & Right
		{-1, 1} // Up & Left
	};
	private static final int[][] BLACK_ATTACK_OFFSETS = {
		{1, -1}, // Down & Right
		{-1, -1} // Down & Left
	};
	private static final int[] WHITE_MOVE_OFFSET = {0, 1};
	private static final int[] BLACK_MOVE_OFFSET = {0, -1};

	@Setter
	private boolean hasMoved = false;
	@Setter
	// TODO: Have this be tracked by the game
	private boolean canPassant = false;

	public Pawn(Team team)
	{
		super(team, WHITE_ICON, BLACK_ICON);
	}

	@Override
	public boolean canMoveToPoint(Point point, GameBoard gameBoard)
	{
		final Piece[][] board = gameBoard.getBoard();

		int[] forwardOffset;
		int[][] attackOffsets;
		switch (getTeam())
		{
			case WHITE:
				forwardOffset = WHITE_MOVE_OFFSET;
				attackOffsets = WHITE_ATTACK_OFFSETS;
				break;
			case BLACK:
				forwardOffset = BLACK_MOVE_OFFSET;
				attackOffsets = BLACK_ATTACK_OFFSETS;
				break;
			default:
				return false;
		}

		final int[] offset = {point.x - getLocation().x, point.y - getLocation().y};

		// A pawn can only move towards the enemies side of the board on the Y axis unless it is capturing another piece.
		// If it is capturing another piece it must move diagonally (D5->C6/E6 for white or D5->C4/E4 for black)
		// If a pawn hasn't moved yet it can move 2 tiles granted the tiles are open

		// If they moved more than 1 tile left/right or 2 tiles up/down its an impossible move.
		if (Math.abs(offset[0]) > 1 || Math.abs(offset[1]) > 2)
		{
			return false;
		}

		final boolean isAttacking = Arrays.equals(offset, attackOffsets[0]) || Arrays.equals(offset, attackOffsets[1]);
		final boolean isMovingForward = Arrays.equals(offset, forwardOffset);
		final boolean isJumpingForward = Math.abs(offset[1]) == 2;

		// Must be one of these movement methods
		if (!isAttacking && !isMovingForward && !isJumpingForward)
		{
			return false;
		}

		if (isJumpingForward)
		{
			// Can only move two squares on first move
			if (hasMoved || offset[0] != 0)
			{
				return false;
			}

			// Check that the tile we are attempting to skip is empty
			final Point forwardPoint = new Point(getLocation().x + forwardOffset[0], getLocation().y + forwardOffset[1]);
			final Piece forwardPiece = board[forwardPoint.x][forwardPoint.y];
			if (forwardPiece != null)
			{
				return false;
			}
			// Continue below to check target location
		}

		Piece targetPiece = board[point.x][point.y];
		if (targetPiece == null)
		{
			if (isAttacking)
			{
				if (canPassant)
				{
					// Check for target behind target tile
					final Point passantTarget = new Point(point.x, point.y - forwardOffset[1]);
					targetPiece = board[passantTarget.x][passantTarget.y];
					// can only en-passant capture pawns, instanceof will return false for nulls as well
					if (!(targetPiece instanceof Pawn))
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}

		// Don't else statement as the targetPiece can change if passant capturing
		if (targetPiece != null)
		{
			// Pawns can't have a target if moving forward, and it must be an enemy if they are attacking
			if (!isAttacking || targetPiece.getTeam().equals(getTeam()))
			{
				return false;
			}
		}

		// If pinned we need to check we are moving in the pin direction, if we aren't then the move is illegal
		if (isPinned(gameBoard))
		{
			final Point kingPosition = gameBoard.getKingPositions().get(getTeam()).getLocation();
			final int[] kingToMeOffset = getMovementOffset(kingPosition, getLocation());
			// Not moving in the same direction as the pin
			if (Arrays.compare(offset, kingToMeOffset) != 0)
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public Set<Point> getPossibleMoves(Point point, GameBoard gameBoard)
	{
		final Piece[][] board = gameBoard.getBoard();
		final Set<Point> points = new HashSet<>();

		int[] forwardOffset;
		int[][] attackOffsets;
		switch (getTeam())
		{
			case WHITE:
				forwardOffset = WHITE_MOVE_OFFSET;
				attackOffsets = WHITE_ATTACK_OFFSETS;
				break;
			case BLACK:
				forwardOffset = BLACK_MOVE_OFFSET;
				attackOffsets = BLACK_ATTACK_OFFSETS;
				break;
			default:
				return points;
		}

		final boolean pinned = isPinned(gameBoard);

		final Point forward = new Point(point.x + forwardOffset[0], + point.y + forwardOffset[1]);
		if (!pinned && forward.x < GameBoard.SIZE && forward.y < GameBoard.SIZE)
		{
			final Piece forwardPiece = board[forward.x][forward.y];
			if (forwardPiece == null)
			{
				points.add(forward);
			}
		}

		for (int[] o : attackOffsets)
		{
			final Point attackPoint = new Point(point.x + o[0], point.y + o[1]);
			if (attackPoint.x < GameBoard.SIZE && attackPoint.y < GameBoard.SIZE)
			{
				final Piece attackedPiece = board[attackPoint.x][attackPoint.y];
				if (attackedPiece == null)
				{
					// Pinned pieces cant passant
					if (!canPassant || pinned)
					{
						continue;
					}

					// Check for target behind target tile
					final Point passantTarget = new Point(attackPoint.x, attackPoint.y - forwardOffset[1]);
					final Piece targetPiece = board[passantTarget.x][passantTarget.y];

					if (targetPiece instanceof Pawn && !targetPiece.getTeam().equals(getTeam()))
					{
						points.add(attackPoint);
					}
				}
				else if (!attackedPiece.getTeam().equals(getTeam()))
				{
					// If pinned we need to check we are capturing in the pin direction, if we aren't then the move is illegal
					if (pinned)
					{
						final Point kingPosition = gameBoard.getKingPositions().get(getTeam());
						final int[] kingToMeOffset = getMovementOffset(kingPosition, getLocation());
						if (Arrays.compare(o, kingToMeOffset) != 0)
						{
							continue;
						}
					}

					points.add(attackPoint);
				}
			}
		}

		return points;
	}
}
