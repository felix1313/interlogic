package felix.study.address.util;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.text.DateFormatter;

public class DateUtil {
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern(DATE_FORMAT);

	/**
	 * Returns the given date as a well formatted String. The above defined
	 * {@link DateUtil#DATE_PATTERN} is used.
	 * 
	 * @param date
	 *            the date to be returned as a string
	 * @return formatted string
	 */
	public static String format(LocalDate date) {
		if (date == null)
			return null;
		else
			return DATE_FORMATTER.format(date);
	}

	public static LocalDate parse(String dateString) {
		try {
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	/**
	 * Checks the String whether it is a valid date.
	 * 
	 * @param dateString
	 * @return true if the String is a valid date
	 */
	public static boolean validDate(String dateString) {
		// Try to parse the String.
		return DateUtil.parse(dateString) != null;
	}

}
