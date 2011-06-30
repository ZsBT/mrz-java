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

/**
 * Holds a MRZ date type.
 * @author Martin Vysny
 */
public class MrzDate {

    /**
     * Year, 00-99.
     */
    public final int year;
    /**
     * Month, 1-12.
     */
    public final int month;
    /**
     * Day, 1-31.
     */
    public final int day;

    public MrzDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        check();
    }

    @Override
    public String toString() {
        return "MrzDate{" + "year=" + year + ", month=" + month + ", day=" + day + '}';
    }

    public String toMrz() {
        return String.format("%02d%02d%02d", year, month, day);
    }

    private void check() {
        if (year < 0 || year > 99) {
            throw new IllegalArgumentException("Parameter year: invalid value " + year + ": must be 0..99");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Parameter month: invalid value " + month + ": must be 1..12");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Parameter day: invalid value " + day + ": must be 1..31");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MrzDate other = (MrzDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.year;
        hash = 11 * hash + this.month;
        hash = 11 * hash + this.day;
        return hash;
    }
}
