/**
 * Java parser for the MRZ records, as specified by the ICAO organization.
 * Copyright (C) 2011 Innovatrics s.r.o.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.innovatrics.mrz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Demo Swing application, demonstrates simple MRZ parsing.
 * @author Martin Vysny
 */
public class Demo {

    private static int toPos(int col, int row, String text) {
        int currentRow = 0;
        int currentCol = 0;
        int pos = 0;
        while (text.length() > pos) {
            if (row == currentRow && currentCol == col) {
                return pos;
            }
            if (text.charAt(pos) == '\n') {
                currentRow++;
                currentCol = 0;
            } else {
                currentCol++;
            }
            pos++;
        }
        return -1;
    }

    /**
     * MRZ demo.
     * @param args 
     */
    public static void main(String[] args) {
        final JFrame frame = new JFrame("MRZDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JTextArea mrz = new JTextArea(5, 44);
        final JButton parse = new JButton("Parse");
        parse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String m = mrz.getText();
                try {
                    final MrzRecord record = MrzParser.parse(m);
                    JOptionPane.showMessageDialog(frame, "Parse successfull: " + record);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Parse failed: " + ex);
                    if (ex instanceof MrzParseException) {
                        final MrzParseException mpe = (MrzParseException) ex;
                        final MrzRange r = mpe.range;
                        mrz.select(toPos(r.column, r.row, m), toPos(r.columnTo, r.row, m));
                    }
                }
            }
        });
        frame.getContentPane().add(mrz, BorderLayout.CENTER);
        frame.getContentPane().add(parse, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
