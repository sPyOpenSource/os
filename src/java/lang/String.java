package java.lang;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class String implements CharSequence
{

    static int lastIndexOf(char[] value, int i, int count, char[] toCharArray, int i0, int length, int fromIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  // private static Hashtable stringPool = new Hashtable();

    static int indexOf(char[] value, int i, int count, char[] toCharArray, int i0, int length, int fromIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static String format(String format, Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private char[] value;
    
    /** Cache the hash code for the string */
    private int hash; // Default to 0
    
    public String()
    {
	value = new char[0];
    }

    public String(char[] newValue, int offset, int count)
    {
	value = new char[count];
	for(int i = 0; i < count; i++) value[i] = newValue[offset + i];
    }

    public String(byte bytes[], int offset, int length) {
	this(bytes, 0, offset, length);
    }

    public String(char[] newValue)
    {
	this(newValue, 0, newValue.length);
    }

    public String(byte[] newValue, int hibyte, int offset, int count)
    {
	int k = (hibyte & 0xFF) << 8;
	value = new char[count];
	for (int i = 0; i < count; i++)
	    value[i] = (char)((newValue[offset + i] & 0xFF) + k);
    }

    public String(byte[] newValue, int hibyte)
    {
	this(newValue, hibyte, 0, newValue.length);
    }

    public String(byte[] newValue)
    {
	this(newValue, 0, 0, newValue.length);
    }
    public String(byte[] newValue, String enc) {
	this(newValue);
    }

    public String(String newValue)
    {
	int n = newValue.length();
	value = new char[n];
	newValue.getChars(0, n, value, 0);
        this.hash = newValue.hash;
    }

    public String(StringBuffer newValue)
    {
	int n = newValue.length();
	value = new char[n];
	newValue.getChars(0, n, value, 0);
    }

    public String(byte bytes[], int offset, int length, String charsetName)
    {
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        checkBounds(bytes, offset, length);
        this.value = new char[]{'i','n','d','e','x','.','h','t','m','l'};//StringCoding.decode(charsetName, bytes, offset, length);
    }

    @Override
    public String toString()
    {
	return this;
    }

    @Override
    public int hashCode()
    {
	int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    @Override
    public int length()
    {
	return value.length;
    }

    @Override
    public char charAt(int index)
    {
	return value[index];
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
    {
	if ((srcBegin < 0)
	    || (srcEnd > value.length)
	    || (srcEnd < srcBegin)
	    || (dst.length < (srcEnd - srcBegin + dstBegin)))
	    throw new StringIndexOutOfBoundsException();
	charArrayCopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    public byte[] getBytes() {
	int len = this.length();
	byte[] buf = new byte[len];
	getBytes(0, len, buf, 0);
	return buf;
    }

    public byte[] getBytes(String enc) throws UnsupportedEncodingException{
	return getBytes();
    }

    public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin)
    {
	if (srcBegin < 0 || srcEnd > value.length || srcEnd < srcBegin)
	    throw new StringIndexOutOfBoundsException();
	int count = srcEnd - srcBegin;
	for (int i = 0; i < count; i++)
	    dst[dstBegin + i] = (byte) value[srcBegin + i];
    }

    public char[] toCharArray()
    {
	int len = value.length;
	char[] result = new char[len];
        System.arraycopy(value, 0, result, 0, len);
	return result;
    }

    public String substring(int beginIndex)
    {
	if (beginIndex < 0 || beginIndex > value.length)
	    throw new StringIndexOutOfBoundsException();
	return new String(value, beginIndex, value.length - beginIndex);
    }

    public String substring(int beginIndex, int endIndex)
    {
	if (beginIndex < 0 || endIndex > value.length || endIndex < beginIndex)
	    throw new StringIndexOutOfBoundsException();
	return new String(value, beginIndex, endIndex - beginIndex);
    }

    @Override
    public boolean equals(Object obj)
    {
	if (obj == null)
	    return false;
	if (!(obj instanceof String))
	    return false;

	String str = (String) obj;

	if (str.length() != this.value.length)
	    return false;

	for (int i = 0; i < value.length; i++)
	    if (str.value[i] != this.value[i])
		return false;

	return true;
    }

    public boolean equalsIgnoreCase(String str)
    {
	if (str == null)
	    return false;

	if (str.length() != value.length)
	    return false;

	for (int i = 0; i < value.length; i++)
	    {
		char c1 = str.charAt(i);
		char c2 = value[i];
		if (c1 == c2)
		    continue;
		if (Character.toUpperCase(c1) == Character.toUpperCase(c2))
		    continue;
		if (Character.toLowerCase(c1) == Character.toLowerCase(c2))
		    continue;
		return false;
	    }

	return true;
    }

    public int compareTo(String str)
    {
	int n = Math.min(value.length, str.length());

	for (int i = 0; i < n; i++)
	    {
		if (value[i] > str.charAt(i))
		    return 1;
		if (value[i] < str.charAt(i))
		    return -1;
	    }

	if (value.length > n)
	    return 1;
	if (str.length() > n)
	    return -1;

	return 0;
    }

    public boolean regionMatches(boolean ignoreCase, int offset, String str, int strOffset, int len)
    {
      if (offset < 0 || offset + len > value.length)
	return false;
      if (strOffset < 0 || strOffset + len > str.length())
	return false;
      if (ignoreCase) {
	for (int i = 0; i < len; i++) {
	  char c1 = str.value[strOffset + i];
	  char c2 = value[offset + i];
	  if (c1 == c2)
	    continue;
	  if (Character.toUpperCase(c1) == Character.toUpperCase(c2))
	    continue;
	  if (Character.toLowerCase(c1) == Character.toLowerCase(c2))
	    continue;
	  return false;
	}
	return true;
      }      
      return regionMatches(offset,str,strOffset,len);
    }

    public boolean regionMatches(int offset, String str, int strOffset, int len)
    {
      if (offset < 0 || offset + len > value.length)
	return false;
      if (strOffset < 0 || strOffset + len > str.length())
	return false;
      for (int i = 0; i < len; i++) {
	if (value[offset + i] != str.value[strOffset + i]) {
	  return false;
	}
      }
      return true;
    }

    public boolean startsWith(String prefix)
    {
	int n = prefix.length();
	return regionMatches(0, prefix, 0, n);
    }

    public boolean startsWith(String prefix, int offset)
    {
	int n = prefix.length();
	return regionMatches(offset, prefix, 0, n);
    }

    public boolean endsWith(String suffix)
    {
	int n = suffix.length();
	return regionMatches(value.length - n, suffix, 0, n);
    }

    public String concat(String str)
    {
	int n = str.length();
	if (n == 0)
	    return this;

	int m = value.length;
	char[] buff = new char[m + n];
	getChars(0, m, buff, 0);
	str.getChars(0, n, buff, m);

	return new String(buff);
    }

    public String replace(char oldChar, char newChar)
    {
	char[] buff = new char[value.length];
	boolean found = false;

	for (int i = 0; i < value.length; i++)
	    {
		char c = value[i];
		if (c == oldChar)
		    {
			found = true;
			buff[i] = newChar;
		    }
		else
		    buff[i] = c;
	    }

	return found ? new String(buff) : this;
    }

    public String toLowerCase()
    {
	char[] buff = new char[value.length];
	for (int i = 0; i < value.length; i++)
	    buff[i] = Character.toLowerCase(value[i]);
	return new String(buff);
    }

    public String toUpperCase()
    {
	char[] buff = new char[value.length];
	for (int i = 0; i < value.length; i++)
	    buff[i] = Character.toUpperCase(value[i]);
	return new String(buff);
    }

    public String trim()
    {
	int i, j;

	for (i = 0; i < value.length; i++)
	    if (value[i] > '\u0020')
		break;

	for (j = value.length - 1; j >= i; j--)
	    if (value[j] > '\u0020')
		break;

	int n = j - i + 1;
	char[] buff = new char[n];
	charArrayCopy(value, i, buff, 0, n);

	return new String(buff);
    }

    public int indexOf(int c, int fromIndex)
    {
	for (int i = fromIndex; i < value.length; i++)
	    if (value[i] == c)
		return i;
	return -1;
    }

    public int indexOf(int c)
    {
	return indexOf(c, 0);
    }

    public int indexOf(String str, int fromIndex)
    {
	int n = value.length - (str.length() - 1);
	for (int i = fromIndex; i < n; i++)
	    if (startsWith(str, i))
		return i;
	return -1;
    }

    public int indexOf(String str)
    {
	return indexOf(str, 0);
    }

    public int lastIndexOf(int c, int fromIndex)
    {
	for (int i = value.length - 1; i >= fromIndex; i--)
	    if (value[i] == c)
		return i;
	return -1;
    }

    public int lastIndexOf(int c)
    {
	return lastIndexOf(c, 0);
    }

    public int lastIndexOf(String str, int fromIndex)
    {
	for (int i = value.length - 1; i >= fromIndex; i--)
	    if (startsWith(str, i))
		return i;
	return -1;
    }

    public int lastIndexOf(String str)
    {
	return lastIndexOf(str, 0);
    }

    public String intern()
    {
      /*	String str = (String) stringPool.get(this);
	if (str != null)
	    return str;
	stringPool.put(this, this);
	*/
	return this;
    }

    public static String copyValueOf(char[] newValue)
    {
	return new String(newValue);
    }

    public static String copyValueOf(char[] newValue, int offset, int count)
    {
	return new String(newValue, offset, count);
    }

    public static String valueOf(boolean b)
    {
	return b ? "true" : "false";
    }

    public static String valueOf(char c)
    {
	char[] buf = new char[1];
	buf[0] = c;
	return new String(buf);
    }

    public static String valueOf(char[] newValue)
    {
	return new String(newValue);
    }

    public static String valueOf(char[] newValue, int offset, int count)
    {
	return new String(newValue, offset, count);
    }

    
    public static String valueOf(double d)
    {
	//return Double.toString(d);
	throw new Error();
    }

    public static String valueOf(float f)
    {
	//return Float.toString(f);
	throw new Error();
    }
    

    public static String valueOf(int i)
    {
	return Integer.toString(i);
    }

    public static String valueOf(long l)
    {
	return Long.toString(l);
    }

    public static String valueOf(Object obj)
    {
	return (obj == null) ? "null" : obj.toString();
    }

    private static void charArrayCopy(char[] src, int srcOffset, char[] dst, int dstOffset, int count) {        
	for(int i = 0; i < count; i++)
	    dst[dstOffset + i] = src[srcOffset + i];
    }

    void getChars(char[] value, int start) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String[] split(String regex, int limit){
        /* fastpath if the regex is a
         (1)one-char String and this character is not one of the
            RegEx's meta characters ".$|()[{^?*+\\", or
         (2)two-char String and the first char is the backslash and
            the second is not the ascii digit or ascii letter.
         */
        char ch = 0;
        if (((regex.value.length == 1 &&
             ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1) ||
             (regex.length() == 2 &&
              regex.charAt(0) == '\\' &&
              (((ch = regex.charAt(1))-'0')|('9'-ch)) < 0 &&
              ((ch-'a')|('z'-ch)) < 0 &&
              ((ch-'A')|('Z'-ch)) < 0)) &&
            (ch < Character.MIN_HIGH_SURROGATE ||
             ch > Character.MAX_LOW_SURROGATE))
        {
            int off = 0;
            int next = 0;
            boolean limited = limit > 0;
            ArrayList<String> list = new ArrayList<>();
            while ((next = indexOf(ch, off)) != -1) {
                if (!limited || list.size() < limit - 1) {
                    list.add(substring(off, next));
                    off = next + 1;
                } else {    // last one
                    //assert (list.size() == limit - 1);
                    list.add(substring(off, value.length));
                    off = value.length;
                    break;
                }
            }
            // If no match was found, return this
            if (off == 0)
                return new String[]{this};

            // Add remaining segment
            if (!limited || list.size() < limit)
                list.add(substring(off, value.length));

            // Construct result
            int resultSize = list.size();
            if (limit == 0) {
                while (resultSize > 0 && list.get(resultSize - 1).length() == 0) {
                    resultSize--;
                }
            }
            String[] result = new String[resultSize];
            for(int i = 0; i < list.size(); i++){
                result[i] = list.get(i);
            }
            return result;
        }
        return null;//Pattern.compile(regex).split(this, limit);
    }
    
    public String[] split(String regex) {
        return split(regex, 0);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static void checkBounds(byte[] bytes, int offset, int length) {
        if (length < 0)
            throw new StringIndexOutOfBoundsException(length);
        if (offset < 0)
            throw new StringIndexOutOfBoundsException(offset);
        if (offset > bytes.length - length)
            throw new StringIndexOutOfBoundsException(offset + length);
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
