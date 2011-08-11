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
package com.innovatrics.mrz.records;
import com.innovatrics.mrz.MrzParser;
import com.innovatrics.mrz.types.MrzDate;
import com.innovatrics.mrz.types.MrzDocumentCode;
import com.innovatrics.mrz.types.MrzSex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@link SlovakId2_34}.
 * @author Martin Vysny
 */
public class SlovakId2_34Test {
    @Test
    public void testSlovakId234Parsing() {
        final SlovakId2_34 r = (SlovakId2_34) MrzParser.parse("I<SVKNOVAK<<JAN<<<<<<<<<<<<<<<<<<<\n123456<AA5SVK8110251M1801020749313");
        assertEquals(MrzDocumentCode.TypeI, r.code);
        assertEquals('I', r.code1);
        assertEquals('<', r.code2);
        assertEquals("SVK", r.issuingCountry);
        assertEquals("SVK", r.nationality);
        assertEquals("749313", r.optional);
        assertEquals("123456 AA", r.documentNumber);
        assertEquals(new MrzDate(18, 1, 2), r.expirationDate);
        assertEquals(new MrzDate(81, 10, 25), r.dateOfBirth);
        assertEquals(MrzSex.Male, r.sex);
        assertEquals("NOVAK", r.surname);
        assertEquals("JAN", r.givenNames);
    }
}
