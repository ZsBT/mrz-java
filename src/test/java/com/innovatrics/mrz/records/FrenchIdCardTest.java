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
 * Tests {@link FrenchIdCard}.
 * @author Martin Vysny
 */
public class FrenchIdCardTest {
    @Test
    public void testFrenchIdCardParsing() {
        final FrenchIdCard r = (FrenchIdCard) MrzParser.parse("IDFRAPETE<<<<<<<<<<<<<<<<<<<<<952042\n0509952018746NICOLAS<<PAUL<8206152M3\n");
        assertEquals(MrzDocumentCode.TypeI, r.code);
        assertEquals('I', r.code1);
        assertEquals('D', r.code2);
        assertEquals("FRA", r.issuingCountry);
        assertEquals("FRA", r.nationality);
        assertEquals("050995201874", r.documentNumber);
//        assertEquals(new MrzDate(95, 1, 2), r.expirationDate);
        assertEquals("952042", r.optional);
        assertEquals(new MrzDate(82, 6, 15), r.dateOfBirth);
        assertEquals(MrzSex.Male, r.sex);
        assertEquals("PETE", r.surname);
        assertEquals("NICOLAS, PAUL", r.givenNames);
    }

    @Test
    public void testFrenchIdToMrz() {
        final FrenchIdCard r = new FrenchIdCard();
        r.issuingCountry = "FRA";
        r.nationality = "FRA";
        r.optional = "123456";
        r.documentNumber = "ABCDE1234512";
        r.expirationDate = new MrzDate(18, 1, 2);
        r.dateOfBirth = new MrzDate(81, 10, 25);
        r.sex = MrzSex.Male;
        r.surname = "NOVAK";
        r.givenNames = "JAN";
        assertEquals("IDFRANOVAK<<<<<<<<<<<<<<<<<<<<123456\nABCDE12345126JAN<<<<<<<<<<<8110251M<\n", r.toMrz());
    }
}
