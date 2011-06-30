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
import com.innovatrics.mrz.MrzRange;
import com.innovatrics.mrz.MrzRecord;
import com.innovatrics.mrz.types.MrzFormat;

/**
 * MRTD TD1 format: A three line long, 30 characters per line format.
 * @author Martin Vysny
 */
public class MrtdTd1 extends MrzRecord {

    public MrtdTd1() {
        super(MrzFormat.MTRD_TD1);
    }
    /**
     * Optional data at the discretion
    of the issuing State. May contain
    an extended document number
    as per 6.7, note (j).
     */
    public String optional;
    /**
     * optional (for U.S. passport holders, 21-29 may be corresponding passport number)
     */
    public String optional2;

    @Override
    public void fromMrz(String mrz) {
        super.fromMrz(mrz);
        final MrzParser p = new MrzParser(mrz);
        documentNumber = p.parseString(new MrzRange(5, 14, 0));
        p.checkDigit(14, 0, new MrzRange(5, 14, 0), "document number");
        optional = p.parseString(new MrzRange(15, 30, 0));
        dateOfBirth = p.parseDate(new MrzRange(0, 6, 1));
        p.checkDigit(6, 1, new MrzRange(0, 6, 1), "date of birth");
        sex = p.parseSex(7, 1);
        expirationDate = p.parseDate(new MrzRange(8, 14, 1));
        p.checkDigit(14, 1, new MrzRange(8, 14, 1), "expiration date");
        nationality = p.parseString(new MrzRange(15, 18, 1));
        optional2 = p.parseString(new MrzRange(18, 29, 1));
        p.checkDigit(29, 1, p.rawValue(new MrzRange(5, 30, 0), new MrzRange(0, 7, 1), new MrzRange(8, 15, 1), new MrzRange(18, 29, 1)), "mrz");
        setName(p.parseName(new MrzRange(0, 30, 2)));
    }

    @Override
    public String toString() {
        return "MRTD-TD1{" + super.toString() + ", optional=" + optional + ", optional2=" + optional2 + '}';
    }
}
