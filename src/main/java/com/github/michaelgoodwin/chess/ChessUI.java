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
package com.github.michaelgoodwin.chess;

import com.github.michaelgoodwin.chess.ui.ChessGame;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class ChessUI
{
	private static final BufferedImage ICON = ImageUtil.getResourceStreamFromClass(ChessUI.class, "icon.png");

	private JFrame frame = new JFrame();
	private JPanel container = new JPanel();

	void init() throws Exception
	{
		SwingUtilities.invokeAndWait(() ->
		{
			frame.setTitle(Chess.GAME_NAME);
			frame.setIconImage(ICON);
			frame.setResizable(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
			container.add(new ChessGame());

			frame.add(container);
		});
	}

	void show()
	{
		SwingUtilities.invokeLater(() ->
		{
			// Layout frame
			frame.revalidate();
			frame.pack();
			frame.setLocationRelativeTo(null); // Center
			frame.setVisible(true);
			frame.setMinimumSize(frame.getSize());
			frame.toFront();
			frame.requestFocus();
		});
	}
}
