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

import com.innovatrics.mrz.MrzParseException;
import com.innovatrics.mrz.MrzRange;

/**
 * Lists all supported MRZ record types (a.k.a. document codes).
 * @author Martin Vysny
 */
public enum MrzDocumentCode {

    /**
     * A passport, P or IP.
     */
    Passport,
    /**
     * General I type (besides IP).
     */
    TypeI,
    /**
     * General A type (besides AC).
     */
    TypeA,
    /**
     * Crew member (AC).
     */
    CrewMember,
    /**
     * General type C.
     */
    TypeC, 
    /**
     * Type V (Visa).
     */
    TypeV,
    /**
     *
     */
    Migrant;

    public static MrzDocumentCode parse(String mrz) {
        final String code = mrz.substring(0, 2);
        if (code.equals("IV")) {
            throw new MrzParseException("IV document code is not allowed", mrz, new MrzRange(0, 2, 0), null);
        }
        if (code.charAt(0) == 'P' || code.equals("IP")) {
            return Passport;
        }
        if (code.equals("AC")) {
            return CrewMember;
        }
        if (code.charAt(0) == 'I') {
            return TypeI;
        }
        if (code.charAt(0) == 'A') {
            return TypeA;
        }
        if (code.charAt(0) == 'C') {
            return TypeC;
        }
        if (code.charAt(0) == 'V') {
            return TypeV;
        }
        if (code.equals("ME")) {
            return Migrant;
        }
        throw new MrzParseException("Invalid document code: " + code, mrz, new MrzRange(0, 2, 0), null);
    }
}
