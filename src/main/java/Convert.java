import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 对象转换类。
 */
public final class Convert implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 将Timestamp转换成Calendar对象
	 * 
	 * @param tstamp
	 *            要转换的Timestamp对象
	 * @return Calendar对象
	 * */
	public static Calendar toCalendar(Timestamp tstamp) {
		Calendar cal = Calendar.getInstance();
		if (tstamp != null) {
			Date date = new Date(0);
			date.setTime(tstamp.getTime());
			cal.setTime(date);
		} else {
			cal = null;
		}
		return cal;
	}

	/**
	 * 判断当前对象是否为空。
	 *
	 * <pre>
	 * Object o = null;
	 * Convert.isNull(o);
	 * </pre>
	 *
	 * @param value
	 *            要判断的对象。
	 * @return true/false。
	 * */
	public static boolean isNull(Object value) {
		if (value == null) {
			return true;
		} else {
			if (value.equals("")) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 将当前的Byte对象转换成布尔值
	 *
	 * <pre>
	 *
	 * </pre>
	 *
	 * <code>
	 *  byte b = 0;<br>
	 *  Convert.toBoolean(b);
	 * </code>
	 *
	 * @param value
	 *            要判断的对象。
	 * @return true/false。
	 * */
	public static boolean toBoolean(Byte value) {
		return value.intValue() != 0;
	}

	public static boolean toBoolean(char value) {
		String s = String.valueOf(value);
		return (s != null) && s.equalsIgnoreCase("true");

	}

	public static boolean toBoolean(long value) {
		return value != 0;
	}

	public static boolean toBoolean(double value) {
		return value != 0;
	}

	public static boolean toBoolean(int value) {
		return value != 0;
	}

	public static boolean toBoolean(String value) {
		if (value == null) {
			return false;
		}
		return (value != null) && value.equalsIgnoreCase("true");
	}

	public static byte toByte(boolean value) {
		if (value)
			return 0;
		else
			return 1;
	}

	public static byte toByte(char value) {
		return Byte.parseByte(String.valueOf(value));
	}

	public static byte toByte(String value) {
		return Byte.parseByte(value);
	}

	public static Date toDate(long value) {
		Date d = null;
		d.setTime(value);
		return d;
	}

	public static Date toDate(int value) {
		Date d = null;
		d.setTime(value);
		return d;
	}

	public static Date toDate(Object value) {
		String o = value.toString();
		return toDate(o);
	}

	public static Date toDate(String value) {
		return toDate(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static Timestamp toTimestamp(String value) {
		long l = 0;
		if (Convert.isNull(value)) {
			return null;
		}
		if (value.length() == 10) {
			l = Convert.toDate(value, "yyyy-MM-dd").getTime();
		} else if (value.length() == 16) {
			l = Convert.toDate(value, "yyyy-MM-dd HH:mm").getTime();
		} else if (value.length() == 19) {
			l = Convert.toDate(value, "yyyy-MM-dd HH:mm:ss").getTime();
		} else {
			l = Convert.toDate(value.substring(0, 10), "yyyy-MM-dd").getTime();
		}

		Timestamp t = new Timestamp(l);
		return t;
	}

	public static Date toDate(String value, String dateFormat) {

		Date d = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
		try {
			d = dateFormatter.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public static long toDecimal(long value) {
		return value;
	}

	public static long toDecimal(int value) {
		return value;
	}

	public static long toDecimal(String value) {
		return Long.parseLong(value);
	}

	public static double toDouble(boolean value) {
		if (value) {
			return 0;
		} else {
			return 1;
		}
	}

	public static double toDouble(byte value) {
		return Double.parseDouble(toString(value));
	}

	public static double toDouble(char value) {
		return Double.parseDouble(toString(value));
	}

	public static double toDouble(Date value) {
		return Double.valueOf(new Long(value.getTime()).toString())
				.doubleValue();
	}

	public static double toDouble(int value) {
		return Double.valueOf(new Integer(value).toString()).doubleValue();
	}

	public static double toDouble(String value) {
		if (Convert.isNull(value)) {
			return Double.NaN;
		} else {
			return Double.parseDouble(value);
		}
	}

	public static int toInteger(String value) {
		return Integer.parseInt(value);
	}

	public static int toInteger(boolean value) {
		if (value) {
			return 0;
		} else {
			return 1;
		}
	}

	public static int toInteger(byte value) {
		return value;
	}

	public static int toInteger(char value) {
		return value;
	}

	public static int toInteger(Date value) {
		return toInteger(value.getTime());
	}

	public static int toInteger(long value) {
		return toInteger(toString(value));
	}

	public static int toInteger(float value) {
		return Float.floatToIntBits(value);
	}

	public static String toString(boolean value) {
		return Boolean.toString(value);
	}

	public static String toString(byte value) {
		return Byte.toString(value);
	}

	public static String toString(char value) {
		return String.valueOf(value);
	}

	public static String toString(char[] value) {
		return String.valueOf(value);
	}

	public static String toString(char[] value, int startIndex, int count) {
		return String.valueOf(value, startIndex, count);
	}

	public static String toString(Date value) {
		return toString(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static String toString(Date value, String dateFormat) {
		if (Convert.isNull(value)) {
			return "";
		} else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
			return dateFormatter.format(value);
		}
	}

	public static String toString(double value) {
		String v = String.valueOf(value);
		// if (v.endsWith(".0")){
		// v = v.substring(0, v.length() - 2);
		// }
		return v;
	}

	public static String toString(float value) {
		return String.valueOf(value);
	}

	public static String toStrng(int value) {
		return String.valueOf(value);
	}

	public static String toString(long value) {
		return String.valueOf(value);
	}

	public static String toString(Object value) {
		return String.valueOf(value);
	}

	public static String toString(Timestamp timestamp) {
		if (Convert.isNull(timestamp)) {
			return "";
		} else {
			String time = timestamp.toString();
			if (time.endsWith("00:00:00.0")) {
				time = time.substring(0, 10);
			} else {
				time = time.substring(0, 16);
			}
			return time;
		}
	}

	public static String toString(double value, int length) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(length); // 最长小数位
		return format.format(value); // s: 3.25
	}

	public static String toString(long value, int length) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(length); // 最长小数位
		return format.format(value); // s: 3.25
	}

	public static String toString(Object value, int length) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(length); // 最长小数位
		return format.format(value); // s: 3.25
	}

	public static Object[][] toArrayList(ResultSet rs, int size, int total) {
		Object o[][] = new Object[size][total];
		try {
			while (rs.next()) {
				for (int i = 1; i <= total; i++) {
					o[rs.getRow() - 1][i] = rs.getObject(i);
				}
			}
		} catch (SQLException e) {
			o = null;
		}
		return o;
	}
	
	/**
	 * 金额格式化
	 * @param s 金额
	 * @param len 小数位数
	 * ###,###.00 格式
	 * add by sumfing 2011-04-25
	 */
	public static String insertComma(String s, int len) {
		if (s == null || s.length() < 1) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("##0.00");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("##0.");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		return formater.format(num);
	}

}
