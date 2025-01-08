package it.airdesk.airdesk_app.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.service.FacilityService;

@Component
public class FacilityValidator implements Validator{

	@Autowired
	private FacilityService facilityService;

	private static final Logger logger = LoggerFactory.getLogger(FacilityValidator.class);
	
	@Override
	public void validate(Object o, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.facilityService.alreadyExists((Facility)o)) {
				logger.debug("e' un duplicato");
				errors.reject("facility.duplicato");
			}
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Facility.class.equals(clazz);
	}

}
