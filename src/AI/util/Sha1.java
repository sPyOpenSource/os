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

import java.nio.charset.StandardCharsets;

/**
 *
 * @author xuyi
 */
public class Sha1 {
    // Helper: Left rotate a 32-bit integer by a specific number of bits
    private static int leftRotate(int value, int bits) {
        return (value << bits) | (value >>> (32 - bits));
    }

    public static String hash(String message) {
        // Convert string input into binary bytes
        byte[] msgBytes = message.getBytes();//StandardCharsets.UTF_8);
        long originalLengthBits = (long) msgBytes.length * 8;

        // --- STEP 1: PADDING ---
        // Calculate total padded length (must be a multiple of 64 bytes / 512 bits)
        int paddedLength = msgBytes.length + 1 + 8; // msg + 0x80 byte + 8-byte length descriptor
        while (paddedLength % 64 != 0) {
            paddedLength++;
        }

        byte[] buffer = new byte[paddedLength];
        System.arraycopy(msgBytes, 0, buffer, 0, msgBytes.length);
        buffer[msgBytes.length] = (byte) 0x80; // Append the '1' bit (0x80 byte)

        // Append original length in bits as a 64-bit big-endian integer at the very end
        buffer[paddedLength - 8] = (byte) (originalLengthBits >>> 56);
        buffer[paddedLength - 7] = (byte) (originalLengthBits >>> 48);
        buffer[paddedLength - 6] = (byte) (originalLengthBits >>> 40);
        buffer[paddedLength - 5] = (byte) (originalLengthBits >>> 32);
        buffer[paddedLength - 4] = (byte) (originalLengthBits >>> 24);
        buffer[paddedLength - 3] = (byte) (originalLengthBits >>> 16);
        buffer[paddedLength - 2] = (byte) (originalLengthBits >>> 8);
        buffer[paddedLength - 1] = (byte) (originalLengthBits);

        // --- STEP 2: INITIAL STATE ---
        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;

        // --- STEP 3: PROCESS EACH 512-BIT CHUNK ---
        int[] w = new int[80];
        for (int chunkOffset = 0; chunkOffset < paddedLength; chunkOffset += 64) {

            // Populate initial 16 words from the current chunk
            for (int i = 0; i < 16; i++) {
                int base = chunkOffset + (i * 4);
                w[i] = ((buffer[base]     & 0xFF) << 24) |
                       ((buffer[base + 1] & 0xFF) << 16) |
                       ((buffer[base + 2] & 0xFF) << 8)  |
                       (buffer[base + 3]  & 0xFF);
            }

            // Extend the 16 words into 80 words
            for (int i = 16; i < 80; i++) {
                w[i] = leftRotate(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
            }

            // Initialize hash value for this chunk
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;

            // --- STEP 4: THE 80 ROUND LOOP ---
            for (int i = 0; i < 80; i++) {
                int f, k;

                if (i < 20) {
                    f = (b & c) | (~b & d);
                    k = 0x5A827999;
                } else if (i < 40) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                } else if (i < 60) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                } else {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }

                // SHA-1 Round mixing formula (Java integer overflow naturally wraps correctly)
                int temp = leftRotate(a, 5) + f + e + k + w[i];
                e = d;
                d = c;
                c = leftRotate(b, 30);
                b = a;
                a = temp;
            }

            // Add this chunk's results to the accumulated total
            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
        }

        // --- STEP 5: FORMAT OUTPUT ---
        // Format each integer into an 8-character hex string padded with zeros
        return String.format("%08x%08x%08x%08x%08x", h0, h1, h2, h3, h4);
    }
    
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) Integer.parseInt(
                hex.substring(i, i + 2), 16
            );
        }

        return result;
    }
}
