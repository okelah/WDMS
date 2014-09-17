package utils;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;

public class validation {


	public static boolean validateFilenameGUID(String GUID) {
		boolean valid = false;
		String extension=null;
		String docId=null;

		if(GUID.length()>36)
		{
			int index=GUID.lastIndexOf(".");
			docId=GUID.substring(0,index);
			extension=GUID.substring(index+1);
		}

		// check length and structure (36 chars and hex)
		if ((GUID.matches("(\\d|[abcdef]){8}-(\\d|[abcdef]){4}-(\\d|[abcdef]){4}-(\\d|[abcdef]){4}-(\\d|[abcdef]){12}") && extension == null) ||
				(docId.matches("(\\d|[abcdef]){8}-(\\d|[abcdef]){4}-(\\d|[abcdef]){4}-(\\d|[abcdef]){4}-(\\d|[abcdef]){12}") && extension.equals("e")))
		{
			valid = true;
		}

		return valid;
	}

	public static boolean validateNumber(String number, int maxlength) {
		boolean valid = false;
		// check length and structure (36 chars and hex)
		if (number.matches("\\d+"))
		{
			//TODO: call DAO function to check that entry is valid.
			valid = true;
		}
		return valid;
	}

	public static boolean validateAlpha(String alpha, int maxlength) {
		boolean valid = false;
		// check length and structure (36 chars and hex)
		if (alpha.matches("[a-zA-Z]+"))
		{
			//TODO: call DAO function to check that entry is valid.
			valid = true;
		}
		return valid;
	}

	public static boolean validateAlphanumeric(String alphanum, int maxlength) {
		boolean valid = false;
		// check length and structure (36 chars and hex)
		if (alphanum.matches("[a-zA-Z0-9]+"))
		{
			//TODO: call DAO function to check that entry is valid.
			valid = true;
		}
		return valid;
	}

	public static boolean validateEmail(String email) {
		try{
			InternetAddress  internetAddress=new InternetAddress(email);
			internetAddress.validate();
		}catch (AddressException e) {
			return false;
		}
		return true;
	}

	public static boolean validatePassword(String pass) {
		List<Rule> ruleList=new ArrayList<Rule>();

		ruleList.add(new LengthRule(8,16));
		ruleList.add(new WhitespaceRule());

		CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
		charRule.getRules().add(new DigitCharacterRule(1));
		charRule.getRules().add(new NonAlphanumericCharacterRule(1));
		charRule.getRules().add(new UppercaseCharacterRule(1));
		charRule.getRules().add(new LowercaseCharacterRule(1));
		charRule.setNumberOfCharacteristics(4);


		ruleList.add(charRule);

		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(pass));

		RuleResult result = validator.validate(passwordData);
		return result.isValid();
	}
}
