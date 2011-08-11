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
 * Tests {@link MrvA}.
 * @author Martin Vysny
 */
public class MrvATest {
    @Test
    public void testMrvVisaACardParsing() {
        final MrvA r = (MrvA) MrzParser.parse("V<UTOERIKSSON<<ANNA<MARIA<<<<<<<<<<<<<<<<<<<\nL898902C<3UTO6908061F9406236ZE184226B<<<<<<<\n");
        assertEquals(MrzDocumentCode.TypeV, r.code);
        assertEquals('V', r.code1);
        assertEquals('<', r.code2);
        assertEquals("UTO", r.issuingCountry);
        assertEquals("UTO", r.nationality);
        assertEquals("L898902C", r.documentNumber);
        assertEquals(new MrzDate(94, 6, 23), r.expirationDate);
        assertEquals("ZE184226B", r.optional);
        assertEquals(new MrzDate(69, 8, 6), r.dateOfBirth);
        assertEquals(MrzSex.Female, r.sex);
        assertEquals("ERIKSSON", r.surname);
        assertEquals("ANNA MARIA", r.givenNames);
    }

    @Test
    public void testMrvVisaAMrz() {
        final MrvA r = new MrvA();
        r.issuingCountry = "FRA";
        r.nationality = "FRA";
        r.optional = "123456";
        r.documentNumber = "ABCDE1234512";
        r.expirationDate = new MrzDate(18, 1, 2);
        r.dateOfBirth = new MrzDate(81, 10, 25);
        r.sex = MrzSex.Male;
        r.surname = "NOVAK";
        r.givenNames = "JAN";
        assertEquals("V<FRANOVAK<<JAN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\nABCDE12346FRA8110251M1801020123456<<<<<<<<<<\n", r.toMrz());
    }
}
