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
public class Knight extends Piece
{
	public Knight(Team team)
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

		// A knight moves in an L shape, which means 2 in either X/Y and 1 in either X/Y;
		final int xDiff = Math.abs(getLocation().x - point.x);
		final int yDiff = Math.abs(getLocation().y - point.y);

		final boolean validShape = (xDiff == 2 && yDiff == 1) || (xDiff == 1 && yDiff == 2);
		if (!validShape)
		{
			return false;
		}

		// TODO: Check if piece occupies the point and if we can capture it. Check if king would be in check after moving (pinned piece)
		return true;
	}

	@Override
	public String getChessNotationPrefix()
	{
		return "N";
	}
}
