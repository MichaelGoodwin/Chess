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
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends Piece
{
	// If a pawn hasn't moved it can move forward two tiles instead of just 1
	private boolean hasMoved = false;

	public Pawn(Team team)
	{
		super(team);
	}

	@Override
	public boolean canMoveToPoint(Point point, Piece[][] board)
	{
		// A pawn can only move towards the enemies side of the board on the Y axis unless it is capturing another piece.
		// If it is capturing another piece it must move forward and sideways 1 square exactly (forward diagonally, D5->C6/E6)

		// A pawn can "en passant capture" an enemy pawn that has moved two tiles on it's last turn.
		// White's pawn on d5 can capture the black pawn on c5 if the last move was c7->c5 (blacks pawn)
		// To capture the black pawn on c6 white will move to c6 from d5
		return true;
	}
}
