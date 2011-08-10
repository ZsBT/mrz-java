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
    }
}
