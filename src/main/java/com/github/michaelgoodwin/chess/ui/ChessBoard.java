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

import com.github.michaelgoodwin.chess.GameBoard;
import com.github.michaelgoodwin.chess.Move;
import com.github.michaelgoodwin.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class ChessBoard extends JPanel
{
	private static final Color LIGHT_TILE = new Color(181, 136, 99);
	private static final Color DARK_TILE = new Color(240, 217, 181);
	private static final Dimension MINIMUM_BOARD_SIZE = new Dimension(300, 300);

	private Point selectedPoint = null;

	public ChessBoard(final ChessGame game, final GameBoard gameBoard)
	{
		super();

		setLayout(new GridLayout(8, 8));

		final Piece[][] board = gameBoard.getBoard();
		boolean darkFlag = true;
		// Due to the way Java will layout these items we need to start with the last row first
		for (int j = GameBoard.SIZE - 1; j >= 0; j--)
		{
			for (int i = 0; i < GameBoard.SIZE; i++)
			{
				final BoardTile tile = new BoardTile();
				tile.setBackground(darkFlag ? DARK_TILE : LIGHT_TILE);
				tile.setPiece(board[i][j]);

				// Add Row numbers to first column
				if (i == 0)
				{
					tile.getTopLabel().setText(String.valueOf(j + 1));
				}

				// Add column letters to bottom row
				if (j == 0)
				{
					tile.getBottomLabel().setText(Move.getColumnLetter(i).toLowerCase());
				}

				add(tile);
				darkFlag = !darkFlag;
				final Point tileCoord = new Point(i, j);
				tile.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mousePressed(MouseEvent e)
					{
						super.mousePressed(e);
						if (selectedPoint == null)
						{
							final Piece p = board[tileCoord.x][tileCoord.y];
							if (p != null)
							{
								selectedPoint = tileCoord;
							}
						}
						else
						{
							//TODO: Show failure visually
							game.playMove(new Move(selectedPoint, tileCoord, gameBoard));
							selectedPoint = null;
							update(gameBoard);
							revalidate();
							repaint();
						}
					}
				});
			}

			// We need to repeat the last color on the next row to get a checkered pattern
			darkFlag = !darkFlag;
		}

		setMinimumSize(MINIMUM_BOARD_SIZE);

		revalidate();
		repaint();
	}

	void update(GameBoard gameBoard)
	{
		int count = 0;
		final Piece[][] board = gameBoard.getBoard();
		for (int j = GameBoard.SIZE - 1; j >= 0; j--)
		{
			for (int i = 0; i < GameBoard.SIZE; i++)
			{
				Component c = getComponent(count);
				if (c instanceof BoardTile)
				{
					BoardTile tile = ((BoardTile) c);
					tile.setPiece(board[i][j]);
				}
				count++;
			}
		}

		revalidate();
		repaint();
	}
}
