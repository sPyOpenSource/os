/*
 * $Id$
 *
 * Copyright (C) 2003-2015 JNode.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; If not, write to the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
 
package org.jnode.util;

public class BinaryScaleFactor implements ScaleFactor {
    public static String[] values = {
        "",
        "K",
        "M",
        "G",
        //new BinaryScaleFactor(1024l * 1024l * 1024l * 1024l, "T"),
        //new BinaryScaleFactor(1024l * 1024l * 1024l * 1024l * 1024l, "P"),
        //new BinaryScaleFactor(1024l * 1024l * 1024l * 1024l * 1024l * 1024l, "E")
    };
    public static int[] keys = {1, 1024, 1024 * 1024, 1024 * 1024 * 1024};
    //these units have too big multipliers to fit in a long
    // (aka they are greater than 2^64) :
    //Z(1024l*1024l*1024l*1024l*1024l*1024l*1024l, "Z"),
    //Y(1024l*1024l*1024l*1024l*1024l*1024l*1024l*1024l, "Y");

    //public static final BinaryScaleFactor MIN = new BinaryScaleFactor(keys[0], values[0]);
    //public static final BinaryScaleFactor MAX = new BinaryScaleFactor(keys[-1], values[-1]);

    private final int multiplier;
    private final String unit;

    public BinaryScaleFactor(int multiplier, String unit) {
        this.multiplier = multiplier;
        this.unit = unit;
    }

    @Override
    public long getMultiplier() {
        return multiplier;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return multiplier + ", " + unit;
    }

    /**
     * Convert the given value to a size string like 64K
     *
     * @param value      the size to convert
     * @param nbDecimals number of significant figures to display after dot. use Integer.MAX_VALUE for all.
     * @return the text for the size
     */
    public static String apply(final long value, final int nbDecimals) {
        long v = value;
        BinaryScaleFactor unit = null;
        for (int u = 0; u < values.length; u++) {
            if ((v < 1024) && (v >= 0)) {
                unit = new BinaryScaleFactor(keys[u], values[u]);
                break;
            }

            v = v >>> 10;
        }
        unit = (unit == null) ? new BinaryScaleFactor(keys[-1], values[-1]) : unit;
        float dv = ((float) value) / unit.getMultiplier();
        return NumberUtils.toString(dv, nbDecimals) + ' ' + unit.getUnit();
    }
}
