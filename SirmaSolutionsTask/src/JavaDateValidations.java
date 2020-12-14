import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class JavaDateValidations {
	public static void main(String[] args) {
		String dateFormat = "MM-dd-yyyy";
		String dateString = "05-26-2020";

		// DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
		// .withResolverStyle(ResolverStyle.STRICT);

		// LocalDate parsedLocalDate = validateAndParseDateJava8(dateString,
		// dateFormatter);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
				.append(DateTimeFormatter.ofPattern("EEEE MMMM d")).toFormatter();

		MonthDay monthDay = MonthDay.parse(dateString, formatter);
		LocalDate parsedDate = monthDay.atYear(2017); // or whatever year you want it at
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/d/yyyy");

		String formattedStringDate = parsedDate.format(formatter2);
		System.out.println(formattedStringDate); // For "TUESDAY JULY 25" input, it gives the output 07/25/2017

	}

	// Java 8 - Use DateTimeFormatter (thread-safe)
	public static LocalDate validateAndParseDateJava8(String dateStr, DateTimeFormatter dateFormatter) {
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateStr, dateFormatter);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}