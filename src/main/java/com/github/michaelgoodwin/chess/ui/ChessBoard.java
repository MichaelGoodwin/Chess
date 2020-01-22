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
package com.github.michaelgoodwin.chess.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class ChessBoard extends JPanel
{
	private static final Color LIGHT_TILE = new Color(181, 136, 99);
	private static final Color DARK_TILE = new Color(240, 217, 181);
	private static final Dimension MINIMUM_BOARD_SIZE = new Dimension(300, 300);

	public ChessBoard()
	{
		super();

		setLayout(new GridLayout(8, 8));
		boolean darkFlag = false;
		for (int i = 0; i < 64; i++)
		{
			// We need to repeat the last color on the next rows first tile to get a checkered pattern
			if (i % 8 == 0)
			{
				darkFlag = !darkFlag;
			}

			final BoardTile tile = new BoardTile();
			tile.setBackground(darkFlag ? DARK_TILE : LIGHT_TILE);
			add(tile);

			darkFlag = !darkFlag;
		}

		setMinimumSize(MINIMUM_BOARD_SIZE);
		setPreferredSize(MINIMUM_BOARD_SIZE);

		revalidate();
		repaint();
	}
}
