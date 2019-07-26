package java.lang;

public final class Long extends Number
{
    public static final Class TYPE = Class.getPrimitiveClass("long");
	public static final long MIN_VALUE = 0x8000000000000000L;
	public static final long MAX_VALUE = 0x7FFFFFFFFFFFFFFFL;

    static void getChars(long i, int index, char[] buf) {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = Integer.DigitOnes[r];
            buf[--charPos] = Integer.DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int)i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = Integer.DigitOnes[r];
            buf[--charPos] = Integer.DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;) {
            q2 = (i2 * 52429) >>> (16+3);
            r = i2 - ((q2 << 3) + (q2 << 1));  // r = i2-(q2*10) ...
            buf[--charPos] = Integer.digits[r];
            i2 = q2;
            if (i2 == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    static int stringSize(long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static long lowestOneBit(long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static int numberOfTrailingZeros(long zeroBit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private long value;

    private static String toUnsignedString(long value, int shift)
    {
            if( value == 0 )
                    return "0";

            String result = "";
            int radix = 1 << shift;
            int mask = radix - 1;
            while (value != 0)
            {
                    result = Character.forDigit((int)(value & mask), radix) + result;
                    value >>>= shift;
            }
            return result;
    }

    public static String toString(long value)
    {
            return toString(value, 10);
    }

    public static String toString(long value, int radix)
    {
        if (value == 0)
            return "0";

        boolean negative = false;
        if (value < 0)
        {
            value = -value;
            negative = true;
        }

        String result = "";

        while (value > 0)
        {
            result = Character.forDigit((int)(value % radix), radix) + result;
            value /= radix;
        }

        return negative ? ("-" + result) : result;
    }

    @Override
    public String toString()
    {
            return toString(value);
    }

    public static String toHexString(long i)
    {
            return toUnsignedString(i, 4);
    }

    public static String toOctalString(long i)
    {
            return toUnsignedString(i, 3);
    }

    public static String toBinaryString(long i)
    {
            return toUnsignedString(i, 1);
    }

    public static long parseLong(String s) throws NumberFormatException
    {
            return parseLong(s, 10);
    }

    public static long parseLong(String s, int radix) throws NumberFormatException
    {
            if (s == null || s.equals(""))
                    throw new NumberFormatException();

            if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
                    throw new NumberFormatException();

            int position = 0;
            long result = 0;
            boolean signed = false;

            if (s.charAt(position) == '-')
            {
                    signed = true;
                    position++;
            }

            int digit;

            for (; position < s.length(); position++)
            {
                    digit = Character.digit(s.charAt(position), radix);
                    if (digit < 0)
                            throw new NumberFormatException();
                    result = (result * radix) - digit;
            }

            if (!signed && result < -MAX_VALUE)
                    throw new NumberFormatException();

            return (signed ? result : -result );
    }

    public static Long valueOf(String s) throws NumberFormatException
    {
            return new Long(parseLong(s));
    }

    public static Long valueOf(String s, int radix) throws NumberFormatException
    {
            return new Long(parseLong(s, radix));
    }

	/*public static Long getLong(String nm)
	{
		return getLong(nm, null);
	}

	public static Long getLong(String nm, long val)
	{
		Long result = getLong(nm, null);
		return ((result == null) ? new Long(val) : result);
	}

	public static Long getLong(String nm, Long val)
	{
		String value = System.getProperty(nm);
		if (value == null)
			return val;
		try
		{
			if (value.startsWith("0x"))
				return valueOf(value.substring(2), 16);
			if (value.startsWith("#"))
				return valueOf(value.substring(1), 16);
			if (value.startsWith("0"))
				return valueOf(value.substring(1), 8);
			return valueOf(value, 10);
		}
		catch (NumberFormatException ex)
		{
		}
		return val;
	}*/

    public boolean equals(Object obj)
    {
            if (obj == null)
                    return false;
            if (!(obj instanceof Long))
                    return false;

            Long l = (Long) obj;

            return value == l.longValue();
    }

    public int hashCode()
    {
            return (int)(value ^ (value >>> 32));
    }

    public int intValue()
    {
            return (int) value;
    }

    public long longValue()
    {
            return (long) value;
    }


    public float floatValue()
    {
        /*return (float) value;*/ throw new Error();
    }

    public double doubleValue()
    {
         throw new Error();
         /*return (double) value;*/
    }

    public Long(long value)
    {
            this.value = value;
    }

    public Long(String s) throws NumberFormatException
    {
            this.value = valueOf(s).longValue();
    }
}
