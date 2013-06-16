package de.enwida.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.enwida.web.dto.UserDTO;
import de.enwida.web.model.User;
import de.enwida.web.utils.Constants;

public class UserValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(UserDTO.class);
	}

	public void validate(Object target, Errors errors) {
		UserDTO user = (UserDTO)target;

		if(user.getUserName().isEmpty())
		{
			errors.rejectValue("userName", "de.enwida.field.empty");
		}
		else if (!user.getUserName().matches(Constants.EMAIL_REGULAR_EXPRESSION))
		{
			errors.rejectValue("userName", "de.enwida.email.inavlid");
		}
		
		// Checking password field
		if(user.getPassword().isEmpty())
		{
			errors.rejectValue("password", "de.enwida.field.empty");
		}
		
		// Checking confirm password field
		if(user.getConfirmPassword().isEmpty())
		{
			errors.rejectValue("confirmPassword", "de.enwida.field.empty");
		}
		
		if(!user.getConfirmPassword().isEmpty() && !user.getPassword().isEmpty())
		{
			if(!user.getPassword().equals(user.getConfirmPassword()))
			{
				errors.rejectValue("password", "de.enwida.password.mismatch");
			}
		}
		
		if(user.getFirstName().isEmpty())
		{
			errors.rejectValue("firstName", "de.enwida.field.empty");
		}
		else if(!user.getFirstName().matches("[a-zA-Z.? ]*"))
		{
			errors.rejectValue("firstName", "de.enwida.field.inavlidChar");
		}
		if(user.getLastName().isEmpty())
		{
			errors.rejectValue("lastName", "de.enwida.field.empty");
		}
		else if(!user.getLastName().matches("[a-zA-Z.? ]*"))
		{
			errors.rejectValue("lastName", "de.enwida.field.inavlidChar");
		}
		
	}

}
