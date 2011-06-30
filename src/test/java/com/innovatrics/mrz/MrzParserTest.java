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

import com.innovatrics.mrz.records.Unknown2_34;
import com.innovatrics.mrz.records.MrtdTd1;
import com.innovatrics.mrz.records.MrtdTd2;
import com.innovatrics.mrz.types.MrzDate;
import com.innovatrics.mrz.types.MrzDocumentCode;
import com.innovatrics.mrz.types.MrzSex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the parser.
 * @author Martin Vysny
 */
public class MrzParserTest {

    /**
     * Test of computeCheckDigit method, of class MrzRecord.
     */
    @Test
    public void testComputeCheckDigit() {
        assertEquals(3, MrzParser.computeCheckDigit("520727"));
        assertEquals(2, MrzParser.computeCheckDigit("D231458907<<<<<<<<<<<<<<<34071279507122<<<<<<<<<<"));
    }

    @Test
    public void testDateParsing() {
        assertEquals("050607", new MrzDate(5, 6, 7).toMrz());
        assertEquals("420712", new MrzDate(42, 7, 12).toMrz());
    }

    @Test
    public void testTd1Parsing() {
        final MrtdTd1 r = (MrtdTd1) MrzParser.parse("CIUTOD231458907A123X5328434D23\n3407127M9507122UTO<<<<<<<<<<<6\nSTEVENSON<<PETER<<<<<<<<<<<<<<\n");
        assertEquals(MrzDocumentCode.TypeC, r.code);
        assertEquals('C', r.code1);
        assertEquals('I', r.code2);
        assertEquals("UTO", r.issuingCountry);
        assertEquals("D23145890", r.documentNumber);
        assertEquals("A123X5328434D23", r.optional);
        assertEquals("", r.optional2);
        assertEquals(new MrzDate(95, 7, 12), r.expirationDate);
        assertEquals(new MrzDate(34, 7, 12), r.dateOfBirth);
        assertEquals(MrzSex.Male, r.sex);
        assertEquals("STEVENSON", r.surname);
        assertEquals("PETER", r.givenNames);
    }

    @Test
    public void testTd2Parsing() {
        final MrtdTd2 r = (MrtdTd2) MrzParser.parse("I<UTOSTEVENSON<<PETER<<<<<<<<<<<<<<<\nD231458907UTO3407127M9507122<<<<<<<2");
        assertEquals(MrzDocumentCode.TypeI, r.code);
        assertEquals('I', r.code1);
        assertEquals('<', r.code2);
        assertEquals("UTO", r.issuingCountry);
        assertEquals("UTO", r.nationality);
        assertEquals("", r.optional);
        assertEquals("D23145890", r.documentNumber);
        assertEquals(new MrzDate(95, 7, 12), r.expirationDate);
        assertEquals(new MrzDate(34, 7, 12), r.dateOfBirth);
        assertEquals(MrzSex.Male, r.sex);
        assertEquals("STEVENSON", r.surname);
        assertEquals("PETER", r.givenNames);
    }

    @Test
    public void testUnknown234Parsing() {
        final Unknown2_34 r = (Unknown2_34) MrzParser.parse("I<SVKNOVAK<<JAN<<<<<<<<<<<<<<<<<<<\n123456<AA5SVK8110251M1801020749313");
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
