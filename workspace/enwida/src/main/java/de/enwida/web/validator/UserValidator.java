package de.enwida.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.utils.Constants;

@Component
public class UserValidator implements Validator {
    
    @Autowired
    private UserService userService;

	public boolean supports(Class clazz) {
		return clazz.equals(User.class);
	}
	
	public void validate(Object target, Errors errors) {
		User user = (User)target;


		if(user.getUserName().isEmpty())
		{
			errors.rejectValue("userName", "de.enwida.field.empty");
		}
		else if (!user.getUserName().matches(Constants.EMAIL_REGULAR_EXPRESSION))
		{
			errors.rejectValue("userName", "de.enwida.email.invalid");
		}
		
		else if(userService.usernameAvailablility(user.getUserName()))
        {
            errors.rejectValue("userName", "de.enwida.email.inuse");
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
