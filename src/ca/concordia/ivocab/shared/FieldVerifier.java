package ca.concordia.ivocab.shared;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldVerifier {
	private static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static boolean isValidLogin(String name) {
		boolean b= name.matches(EMAIL_PATTERN);
		System.out.println("the email patter is valid or not "+b);
		return b;
	}
	
	public static boolean isIdenticalPassword(String value1, String value2)
	{
		return value1.equals(value2);
	}
}
