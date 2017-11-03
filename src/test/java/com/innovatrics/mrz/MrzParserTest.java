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

import com.innovatrics.mrz.types.MrzDate;
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
        assertEquals('3', MrzParser.computeCheckDigitChar("520727"));
        assertEquals('2', MrzParser.computeCheckDigitChar("D231458907<<<<<<<<<<<<<<<34071279507122<<<<<<<<<<"));
    }

    @Test
    public void testValidCheckDigit(){

        String CzechPassport = "P<CZESPECIMEN<<VZOR<<<<<<<<<<<<<<<<<<<<<<<<<\n99003853<1CZE1101018M1207046110101111<<<<<94";
        assertEquals( true, MrzParser.parse(CzechPassport).validDateOfBirth );
        assertEquals( true, MrzParser.parse(CzechPassport).validExpirationDate );
        assertEquals( true, MrzParser.parse(CzechPassport).validDocumentNumber );
        assertEquals( true, MrzParser.parse(CzechPassport).validComposite );

        String GermanPassport =  "P<D<<MUSTERMANN<<ERIKA<<<<<<<<<<<<<<<<<<<<<<\nC01X01R741D<<6408125F2010315<<<<<<<<<<<<<<<9";
        assertEquals( true, MrzParser.parse(GermanPassport).validDateOfBirth );
        assertEquals( true, MrzParser.parse(GermanPassport).validExpirationDate );
        assertEquals( true, MrzParser.parse(GermanPassport).validDocumentNumber );
        assertEquals( false, MrzParser.parse(GermanPassport).validComposite ); // yes, this specimen has intentationally wrong check digit
    }

    @Test
    public void testDateParsing() {
        assertEquals(new MrzDate(34, 7, 12), new MrzParser("CIUTOD231458907A123X5328434D23\n3407127M9507122UTO<<<<<<<<<<<6\nSTEVENSON<<PETER<<<<<<<<<<<<<<\n").parseDate(new MrzRange(0,6,1)));
        assertEquals(new MrzDate(95, 12, 1), new MrzParser("CIUTOD231458907A123X5328434D23\n3407127M9512012UTO<<<<<<<<<<<6\nSTEVENSON<<PETER<<<<<<<<<<<<<<\n").parseDate(new MrzRange(8,14,1)));
    }

    @Test
    public void testToMrz() {
        // \u010d = č
        assertEquals("CACACA<<<<<", MrzParser.toMrz("\u010da\u010da\u010da", 11));
        assertEquals("HERBERT<<FRANK<<<", MrzParser.toMrz("Herbert  Frank", 17));
        assertEquals("PAT<<MAT", MrzParser.toMrz("Pat, Mat", 8));
        assertEquals("FOO<", MrzParser.toMrz("foo bar baz", 4));
        assertEquals("<<<<<<<<", MrzParser.toMrz("*$()&/\\", 8));
        assertEquals("AEAEIJIJ", MrzParser.toMrz("\u00C4\u00E4\u0132\u0133", 8));
        assertEquals("OEOE", MrzParser.toMrz("\u00D6\u00F6", 4));
        assertEquals("DART", MrzParser.toMrz("D’Artagnan", 4));
        assertEquals("DART", MrzParser.toMrz("D'Artagnan", 4));
    }
    
    @Test
    public void testNameToMrz() {
        assertEquals("HERBERT<<FRANK<<<", MrzParser.nameToMrz("Herbert", "Frank", 17));
        assertEquals("ERIKSSON<<ANNA<MARIA<<<", MrzParser.nameToMrz("Eriksson", "Anna, Maria", 23));
        // test name truncating
        assertEquals("PAPANDROPOULOUS<<JONATHOON<ALEC", MrzParser.nameToMrz("Papandropoulous", "Jonathoon Alec", 31));
        assertEquals("NILAVADHANANANDA<<CHAYAPA<DEJ<K", MrzParser.nameToMrz("Nilavadhanananda", "Chayapa Dejthamrong Krasuang", 31));
        assertEquals("NILAVADHANANANDA<<ARNPOL<PETC<C", MrzParser.nameToMrz("NILAVADHANANANDA", "ARNPOL PETCH CHARONGUANG", 31));
        assertEquals("BENNELONG<WOOLOOMOOLOO<W<W<<D<P", MrzParser.nameToMrz("BENNELONG WOOLOOMOOLOO WARRANDYTE WARNAMBOOL", "DINGO POTOROO", 31));
    }

    @Test
    public void testValidDates() {
        String validBirthDateMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBR8809117F2007162<<<<<<<<<<<<<<08";
        MrzRecord record = MrzParser.parse(validBirthDateMrz);
        assertEquals(true, record.dateOfBirth.isDateValid());
        assertEquals(true, record.expirationDate.isDateValid());
        assertEquals(true, record.validDateOfBirth);
        assertEquals(true, record.validExpirationDate);
    }

    @Test
    public void testMrzInvalidBirthDate() {
        String invalidBirthDateMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBR8809417F2007162<<<<<<<<<<<<<<08";
        MrzRecord record = MrzParser.parse(invalidBirthDateMrz);
        assertEquals(false, record.dateOfBirth.isDateValid());
        assertEquals(false, record.validDateOfBirth);
    }

    @Test
    public void testMrzInvalidExpiryDate() {
        String invalidExpiryDateMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBR8809117F2007462<<<<<<<<<<<<<<08";
        MrzRecord record = MrzParser.parse(invalidExpiryDateMrz);
        assertEquals(false, record.expirationDate.isDateValid());
        assertEquals(false, record.validExpirationDate);
    }

    @Test
    public void testUnparseableDates() {
        String unparseableDatesMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBRBB09117F2ZZ7162<<<<<<<<<<<<<<08";
        MrzRecord record = MrzParser.parse(unparseableDatesMrz);
        assertNotNull(record.dateOfBirth);
        assertEquals(-1, record.dateOfBirth.year);
        assertEquals(9, record.dateOfBirth.month);
        assertEquals(11, record.dateOfBirth.day);
        assertEquals(false, record.dateOfBirth.isDateValid());
        assertEquals(false, record.validDateOfBirth);

        assertNotNull(record.expirationDate);
        assertEquals(-1, record.expirationDate.year);
        assertEquals(-1, record.expirationDate.month);
        assertEquals(16, record.expirationDate.day);
        assertEquals(false, record.expirationDate.isDateValid());
        assertEquals(false, record.validExpirationDate);
    }

    @Test
    public void testRawDate() {
        String validBirthDateMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBR8809117F2007162<<<<<<<<<<<<<<08";
        MrzRecord record = MrzParser.parse(validBirthDateMrz);
        assertEquals("880911", record.dateOfBirth.toMrz());

        String invalidBirthDateMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBR8809417F2007162<<<<<<<<<<<<<<08";
        record = MrzParser.parse(invalidBirthDateMrz);
        assertEquals("880941", record.dateOfBirth.toMrz());

        String unparseableDatesMrz = "P<GBRUK<SPECIMEN<<ANGELA<ZOE<<<<<<<<<<<<<<<<\n9250764733GBRBB09117F2007162<<<<<<<<<<<<<<08";
        record = MrzParser.parse(unparseableDatesMrz);
        assertEquals("BB0911", record.dateOfBirth.toMrz());
    }
}
