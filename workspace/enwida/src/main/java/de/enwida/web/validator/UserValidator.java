package de.enwida.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;

@Component
public class UserValidator implements Validator {

	@Autowired
	private IUserService userService;

	public boolean supports(Class<?> clazz) {
		return clazz.equals(User.class);
	}

	public void validateUser(User user, Errors errors, boolean updated)
			throws Exception {
		if (!updated) {
			// check email is empty
			if (user.getEmail().isEmpty()) {
				errors.rejectValue("email", "de.enwida.field.empty");
			}
			// checking email in correct format
			else if (!user.getEmail().matches(Constants.EMAIL_REGULAR_EXPRESSION)) {
				errors.rejectValue("email", "de.enwida.email.invalid");
			} 
			// checking valid email addresses for Enwida
			else if (!isValidEmailDomain(user.getEmail().toLowerCase())) {
				errors.rejectValue("email", "de.enwida.email.notAllowed");
			} 
			// checking email availability
			else if (userService.emailAvailability(user.getEmail())) {
				errors.rejectValue("email", "de.enwida.email.inuse");
			} 
			// checking user name availability
			else if (!user.getUserName().isEmpty() && userService.userNameAvailability(user.getUserName())) {
				errors.rejectValue("userName", "de.enwida.userName.inuse");
			}
			// checking password field
			if (user.getPassword().isEmpty()) {
				errors.rejectValue("password", "de.enwida.field.empty");
			}
			// checking confirm password field
			if (user.getConfirmPassword().isEmpty()) {
				errors.rejectValue("confirmPassword", "de.enwida.field.empty");
			}
			
			if (!user.getConfirmPassword().isEmpty() && !user.getPassword().isEmpty()) {
				// checking confirm password matches password
				if (!user.getPassword().equals(user.getConfirmPassword())) { 
					errors.rejectValue("password", "de.enwida.password.mismatch");
				} 
				// checking password length
				else if (user.getPassword().length() < 6) { 
					errors.rejectValue("password", "de.enwida.password.length.short");
				}
			}
		} 
		else {

			if (!user.getPassword().isEmpty() && updated) {
				if (!user.getPassword().equals(user.getConfirmPassword())) {
					errors.rejectValue("password", "de.enwida.password.mismatch");
				}
			}
		}
		// check email is empty
		if (user.getFirstName().isEmpty()) {
			errors.rejectValue("firstName", "de.enwida.field.empty");
		} 
		// invalid characters
		else if (!user.getFirstName().matches("[a-zA-Z.? ]*")) {
			errors.rejectValue("firstName", "de.enwida.field.inavlidChar");
		}
		// check email is empty
		if (user.getLastName().isEmpty()) {
			errors.rejectValue("lastName", "de.enwida.field.empty");
		}
		// invalid characters
		else if (!user.getLastName().matches("[a-zA-Z.? ]*")) {
			errors.rejectValue("lastName", "de.enwida.field.inavlidChar");
		}
	}

	public void validate(Object target, Errors errors) {
		User user = (User) target;
		try {
			validateUser(user, errors, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isValidEmailDomain(String email) {
		String domain = email.substring(email.lastIndexOf('@'));

		if (domain.contains("gmail")) {
			return false;
		} else if (domain.contains("hotmail")) {
			return false;
		} else if (domain.contains("live")) {
			return false;
		} else if (domain.contains("ymail")) {
			return false;
		} else if (domain.contains("rocketmail")) {
			return false;
		} else if (domain.contains("facebook")) {
			return false;
		}

		return true;
	}

}
