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

import com.github.michaelgoodwin.chess.Game;
import com.github.michaelgoodwin.chess.Player;
import com.github.michaelgoodwin.chess.Team;
import com.github.michaelgoodwin.chess.User;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.Duration;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessGame extends JPanel
{
	public ChessGame()
	{
		super();

		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;

		final Player white = new Player(new User("Michael Goodwin"), Team.WHITE);
		final Player black = new Player(new User("Michael Goodwin"), Team.BLACK);
		final Game g = new Game(null, null, Duration.ofSeconds(120), Duration.ZERO);
		g.newGame();

		add(new PlayerBanner(), c);
		c.gridy++;
		c.weighty = 1;
		add(new ChessBoard(g.getBoard()), c);
		c.gridy++;
		c.weighty = 0;
		add(new PlayerBanner(), c);
		c.gridy++;

		c.gridheight = c.gridy;
		c.gridy = 0;
		c.gridx++;
		c.weightx = 0;
		add(new JLabel("Moves and other stuff goes over here btw"), c);
	}
}
