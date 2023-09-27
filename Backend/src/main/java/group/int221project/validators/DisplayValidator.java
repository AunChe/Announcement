package group.int221project.validators;


import group.int221project.entities.Announce;
import group.int221project.entities.AnnouncementDisplay;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DisplayValidator implements ConstraintValidator<ValidDisplay, Announce> {
    @Override
    public boolean isValid(Announce announce, ConstraintValidatorContext context) {
        if (announce.getAnnouncementDisplay() != null) {
            if (announce.getAnnouncementDisplay().equals("Y") || announce.getAnnouncementDisplay().equals("N")) {
                return true;
            }

            addConstraintViolation("must be either 'Y' or 'N'", "announcementDisplay", context);
        }

        if (announce.getAnnouncementDisplay() == null) {
            announce.setAnnouncementDisplay(("N"));
            return true;
        }

        return false;
    }

    public void addConstraintViolation(String messageTemplate, String field, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}