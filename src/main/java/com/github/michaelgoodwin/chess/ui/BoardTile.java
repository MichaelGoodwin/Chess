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

import com.github.michaelgoodwin.chess.ImageUtil;
import com.github.michaelgoodwin.chess.pieces.Piece;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.Getter;

public class BoardTile extends JPanel
{
	private static final Dimension MINUMUM_SIZE = new Dimension(40, 40);

	@Getter
	// Used for displaying text in the top-left corner of the grid square (number)
	private final JLabel topLabel = new JLabel();

	@Getter
	// Used for displaying text in the bottom-right corner of the grid square (letter)
	private final JLabel bottomLabel = new JLabel();

	private Piece piece;
	private BufferedImage resizedIcon;

	public BoardTile()
	{
		setLayout(new GridLayout(2, 1, 0, 0));
		setLayout(new GridBagLayout());
		setPreferredSize(MINUMUM_SIZE);

		final GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;

		topLabel.setVerticalAlignment(JLabel.TOP);
		topLabel.setHorizontalAlignment(JLabel.LEFT);
		topLabel.setBorder(new EmptyBorder(1, 0, 0, 0));
		add(topLabel, c);
		c.gridy++;

		bottomLabel.setVerticalAlignment(JLabel.BOTTOM);
		bottomLabel.setHorizontalAlignment(JLabel.RIGHT);
		bottomLabel.setBorder(new EmptyBorder(0, 0, 0, 1));
		add(bottomLabel, c);
	}

	public void setPiece(final Piece p)
	{
		piece = p;
		resizedIcon = null;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (piece == null)
		{
			return;
		}

		final Graphics2D g2d = (Graphics2D) g;
		if (resizedIcon == null || (getWidth() != resizedIcon.getWidth() || getHeight() != resizedIcon.getHeight()))
		{
			resizedIcon = ImageUtil.resizeImage(piece.getIcon(), getWidth(), getHeight());
		}
		g2d.drawImage(resizedIcon, 0, 0, null);
	}
}
