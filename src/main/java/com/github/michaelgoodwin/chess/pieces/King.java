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
public class King extends Piece
{
	// TODO: track and allow for castling
	public boolean canCastle = false;

	public King(Team team)
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

		// A king can move one tile in any direction
		final int xDiff = Math.abs(getLocation().x - point.x);
		final int yDiff = Math.abs(getLocation().y - point.y);

		// Moved more than possible
		if (xDiff > 1 || yDiff > 1)
		{
			return false;
		}

		// TODO: Check if position is occupied by a piece we can't capture or if we'd be in check by moving here
		return true;
	}

	@Override
	public String getChessNotationPrefix()
	{
		return "K";
	}
}

