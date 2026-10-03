/*
 * Copyright (C) 2026 xuyi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package AI.util;

/**
 *
 * @author xuyi
 */
public class Base64 {

    // The standard 64-character lookup table
    private static final char[] ALPHABET = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        // Output size is always a multiple of 4
        StringBuilder result = new StringBuilder(((data.length + 2) / 3) * 4);
        int i = 0;

        // Process main data in 3-byte blocks
        while (i < data.length - 2) {
            // Mask with 0xFF to prevent Java's sign extension from messing up the math
            int b1 = data[i++] & 0xFF;
            int b2 = data[i++] & 0xFF;
            int b3 = data[i++] & 0xFF;

            // Combine into a 24-bit block
            int combined = (b1 << 16) | (b2 << 8) | b3;

            // Split into 4 six-bit chunks and map to alphabet
            result.append(ALPHABET[(combined >> 18) & 0x3F]);
            result.append(ALPHABET[(combined >> 12) & 0x3F]);
            result.append(ALPHABET[(combined >> 6) & 0x3F]);
            result.append(ALPHABET[combined & 0x3F]);
        }

        // Handle remaining padding blocks (if data length isn't a multiple of 3)
        int remaining = data.length - i;
        if (remaining == 1) {
            int b1 = data[i] & 0xFF;
            result.append(ALPHABET[(b1 >> 2) & 0x3F]);
            result.append(ALPHABET[(b1 << 4) & 0x3F]);
            result.append("==");
        } else if (remaining == 2) {
            int b1 = data[i++] & 0xFF;
            int b2 = data[i] & 0xFF;
            int combined = (b1 << 8) | b2;
            result.append(ALPHABET[(combined >> 10) & 0x3F]);
            result.append(ALPHABET[(combined >> 4) & 0x3F]);
            result.append(ALPHABET[(combined << 2) & 0x3F]);
            result.append("=");
        }

        return result.toString();
    }
}
