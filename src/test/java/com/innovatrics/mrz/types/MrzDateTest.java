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
package com.innovatrics.mrz.types;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Martin Vysny
 */
public class MrzDateTest {

    @Test
    public void testCompareTo() {
        assertEquals(0, new MrzDate(0, 1, 1).compareTo(new MrzDate(0, 1, 1)));
        assertEquals(0, new MrzDate(55, 4, 31).compareTo(new MrzDate(55, 4, 31)));
        assertTrue(new MrzDate(55, 4, 31).compareTo(new MrzDate(55, 4, 30)) > 0);
        assertTrue(new MrzDate(55, 4, 31).compareTo(new MrzDate(54, 4, 31)) > 0);
        assertTrue(new MrzDate(55, 4, 31).compareTo(new MrzDate(55, 3, 31)) > 0);
        assertTrue(new MrzDate(55, 4, 30).compareTo(new MrzDate(55, 4, 31)) < 0);
        assertTrue(new MrzDate(55, 3, 31).compareTo(new MrzDate(55, 4, 31)) < 0);
        assertTrue(new MrzDate(54, 4, 31).compareTo(new MrzDate(55, 4, 31)) < 0);
    }

    @Test
    public void testEquals() {
        assertEquals(new MrzDate(0, 1, 1), new MrzDate(0, 1, 1));
        assertEquals(new MrzDate(55, 4, 31), new MrzDate(55, 4, 31));
        assertFalse(new MrzDate(55, 4, 31).equals(new MrzDate(55, 4, 30)));
        assertFalse(new MrzDate(55, 4, 31).equals(new MrzDate(54, 4, 31)));
        assertFalse(new MrzDate(55, 4, 31).equals(new MrzDate(55, 3, 31)));
        assertFalse(new MrzDate(55, 4, 30).equals(new MrzDate(55, 4, 31)));
        assertFalse(new MrzDate(55, 3, 31).equals(new MrzDate(55, 4, 31)));
        assertFalse(new MrzDate(54, 4, 31).equals(new MrzDate(55, 4, 31)));
    }

    @Test
    public void testToMrz() {
        assertEquals("550431", new MrzDate(55, 4, 31).toMrz());
        assertEquals("081201", new MrzDate(8, 12, 1).toMrz());
        assertEquals("880941", new MrzDate(88, 9, 41).toMrz());
        assertEquals("BB1201", new MrzDate(-1, 12, 1, "BB1201").toMrz());
    }

    @Test
    public void testInvalidDate() {
        MrzDate date = new MrzDate(88, 9, 41);
        assertEquals(88, date.year);
        assertEquals(9, date.month);
        assertEquals(41, date.day);
        assertEquals(false, date.isDateValid());
    }

    @Test
    public void testValidDate() {
        MrzDate date = new MrzDate(88, 9, 30);
        assertEquals(88, date.year);
        assertEquals(9, date.month);
        assertEquals(30, date.day);
        assertEquals(true, date.isDateValid());
    }
}
