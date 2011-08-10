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
import com.innovatrics.mrz.types.MrzFormat;
import com.innovatrics.mrz.types.MrzSex;
import java.text.Normalizer;

/**
 * Parses the MRZ records.
 * <p/>
 * All parse methods throws {@link MrzParseException} unless stated otherwise.
 * @author Martin Vysny
 */
public class MrzParser {

    /**
     * The MRZ record, not null.
     */
    public final String mrz;
    /**
     * The MRZ record separated into rows.
     */
    public final String[] rows;
    /**
     * MRZ record format.
     */
    public final MrzFormat format;

    /**
     * Creates new parser which parses given MRZ record.
     * @param mrz the MRZ record, not null.
     */
    public MrzParser(String mrz) {
        this.mrz = mrz;
        this.rows = mrz.split("\n");
        this.format = MrzFormat.get(mrz);
    }

    /**
     * Parses the MRZ name in form of SURNAME&lt;&lt;FIRSTNAME&lt;
     * @param range the range
     * @return array of [surname, first_name].
     */
    public String[] parseName(MrzRange range) {
        checkValidCharacters(range);
        String str = rawValue(range);
        while (str.endsWith("<")) {
            str = str.substring(0, str.length() - 1);
        }
        final String[] names = str.split("<<");
        final String surname = parseString(new MrzRange(range.column, range.column + names[0].length(), range.row));
        final String givenNames = parseString(new MrzRange(range.column + names[0].length() + 2, range.column + str.length(), range.row));
        return new String[]{surname, givenNames};
    }

    /**
     * Returns a raw MRZ value from given range. If multiple ranges are specified, the value is concatenated.
     * @param range the ranges, not null.
     * @return raw value, never null, may be empty.
     */
    public String rawValue(MrzRange... range) {
        final StringBuilder sb = new StringBuilder();
        for (MrzRange r : range) {
            sb.append(rows[r.row].substring(r.column, r.columnTo));
        }
        return sb.toString();
    }

    /**
     * Checks that given range contains valid characters.
     * @param range the range to check.
     */
    public void checkValidCharacters(MrzRange range) {
        final String str = rawValue(range);
        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (c != '<' && (c < '0' || c > '9') && (c < 'A' || c > 'Z')) {
                throw new MrzParseException("Invalid character in MRZ record: " + c, mrz, new MrzRange(range.column + i, range.column + i + 1, range.row), format);
            }
        }
    }

    /**
     * Parses a string in given range. &lt;&lt; are replaced with ", ", &lt; is replaced by space.
     * @param range the range
     * @return parsed string.
     */
    public String parseString(MrzRange range) {
        checkValidCharacters(range);
        String str = rawValue(range);
        while (str.endsWith("<")) {
            str = str.substring(0, str.length() - 1);
        }
        return str.replace("<<", ", ").replace('<', ' ');
    }

    /**
     * Verifies the check digit.
     * @param col the 0-based column of the check digit.
     * @param row the 0-based column of the check digit.
     * @param strRange the range for which the check digit is computed.
     * @param fieldName (optional) field name. Used only when {@link MrzParseException} is thrown.
     */
    public void checkDigit(int col, int row, MrzRange strRange, String fieldName) {
        checkDigit(col, row, rawValue(strRange), fieldName);
    }

    /**
     * Verifies the check digit.
     * @param col the 0-based column of the check digit.
     * @param row the 0-based column of the check digit.
     * @param str the raw MRZ substring.
     * @param fieldName (optional) field name. Used only when {@link MrzParseException} is thrown.
     */
    public void checkDigit(int col, int row, String str, String fieldName) {
        final char digit = (char) (computeCheckDigit(str) + '0');
        final char checkDigit = rows[row].charAt(col);
        if (digit != checkDigit || (checkDigit != '<' && checkDigit != '0' && digit == '0')) {
            throw new MrzParseException("Check digit verification failed for " + fieldName + ": expected " + digit + " but got " + checkDigit, mrz, new MrzRange(col, col + 1, row), format);
        }
    }

    /**
     * Parses MRZ date.
     * @param range the range containing the date, in the YYMMDD format. The range must be 6 characters long.
     * @return parsed date
     * @throws IllegalArgumentException if the range is not 6 characters long.
     */
    public MrzDate parseDate(MrzRange range) {
        if (range.length() != 6) {
            throw new IllegalArgumentException("Parameter range: invalid value " + range + ": must be 6 characters long");
        }
        MrzRange r = null;
        try {
            r = new MrzRange(range.column, range.column + 2, range.row);
            final int year = Integer.parseInt(rawValue(r));
            if (year < 0 || year > 99) {
                throw new MrzParseException("Failed to parse MRZ date: invalid year value " + year + ": must be 0..99", mrz, r, format);
            }
            r = new MrzRange(range.column + 2, range.column + 4, range.row);
            final int month = Integer.parseInt(rawValue(r));
            if (month < 1 || month > 12) {
                throw new MrzParseException("Failed to parse MRZ date: invalid month value " + month + ": must be 1..12", mrz, r, format);
            }
            r = new MrzRange(range.column + 4, range.column + 6, range.row);
            final int day = Integer.parseInt(rawValue(r));
            if (day < 1 || day > 31) {
                throw new MrzParseException("Failed to parse MRZ date: invalid day value " + day + ": must be 1..31", mrz, r, format);
            }
            return new MrzDate(year, month, day);
        } catch (NumberFormatException ex) {
            throw new MrzParseException("Failed to parse MRZ date " + rawValue(range) + ": " + ex, mrz, r, format);
        }
    }

    /**
     * Parses the "sex" value from given column/row.
     * @param col the 0-based column
     * @param row the 0-based row
     * @return sex, never null.
     */
    public MrzSex parseSex(int col, int row) {
        return MrzSex.fromMrz(rows[row].charAt(col));
    }
    private static final int[] MRZ_WEIGHTS = new int[]{7, 3, 1};

    private static int getCharacterValue(char c) {
        if (c == '<') {
            return 0;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        throw new RuntimeException("Invalid character in MRZ record: " + c);
    }

    /**
     * Computes MRZ check digit for given string of characters.
     * @param str the string
     * @return check digit in range of 0..9, inclusive. See <a href="http://www2.icao.int/en/MRTD/Downloads/Doc%209303/Doc%209303%20English/Doc%209303%20Part%203%20Vol%201.pdf">MRTD documentation</a> part 15 for details.
     */
    public static int computeCheckDigit(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += getCharacterValue(str.charAt(i)) * MRZ_WEIGHTS[i % MRZ_WEIGHTS.length];
        }
        return result % 10;
    }

    /**
     * Computes MRZ check digit for given string of characters.
     * @param str the string
     * @return check digit in range of 0..9, inclusive. See <a href="http://www2.icao.int/en/MRTD/Downloads/Doc%209303/Doc%209303%20English/Doc%209303%20Part%203%20Vol%201.pdf">MRTD documentation</a> part 15 for details.
     */
    public static char computeCheckDigitChar(String str) {
        return (char) ('0' + computeCheckDigit(str));
    }

    /**
     * Factory method, which parses the MRZ and returns appropriate record class.
     * @param mrz MRZ to parse.
     * @return record class.
     */
    public static MrzRecord parse(String mrz) {
        final MrzRecord result = MrzFormat.get(mrz).newRecord();
        result.fromMrz(mrz);
        return result;
    }

    public static String toMrz(String string, int length) {
        if (string == null) {
            string = "";
        }
        string = deaccent(string).toUpperCase();
        string = string.replaceAll("[ \n\t\f\r,]", "<");
        if (string.length() > length) {
            string = string.substring(0, length);
        }
        final StringBuilder sb = new StringBuilder(string);
        while (sb.length() < length) {
            sb.append('<');
        }
        return sb.toString();
    }

    private static String deaccent(String str) {
        String n = Normalizer.normalize(str, Normalizer.Form.NFD);
        return n.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
}
