package group.int221project.validators;

import group.int221project.entities.Announce;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class PublishAndCloseDateValidator implements ConstraintValidator<ValidPublishAndCloseDate, Announce> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public boolean isValid(Announce announces, ConstraintValidatorContext context) {
        //closeDate must be a future date
        if (announces.getCloseDate() != null) {
            if (announces.getCloseDate().length() == 24) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a future date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 20) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_3);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a future date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 16) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_2);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a future date", "closeDate", context);
                    return false;
                }
            }
        }

        //publishDate must be before closeDate
        if (announces.getPublishDate() != null && announces.getCloseDate() != null) {
            if (announces.getPublishDate().length() == 24 && announces.getCloseDate().length() == 24) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER);
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER);
                if (publishDate.toInstant(ZoneOffset.UTC).isAfter(closeDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be before close date", "publishDate", context);
                    return false;
                }
            } else if (announces.getPublishDate().length() == 20 && announces.getCloseDate().length() == 20) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_3);
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_3);
                if (publishDate.toInstant(ZoneOffset.UTC).isAfter(closeDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be before close date", "publishDate", context);
                    return false;
                }
            } else if (announces.getPublishDate().length() == 16 && announces.getCloseDate().length() == 16) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_2);
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_2);
                if (publishDate.toInstant(ZoneOffset.UTC).isAfter(closeDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be before close date", "publishDate", context);
                    return false;
                }
            }
        }

        //publishDate must be a date in the present or in the future
        if (announces.getPublishDate() != null) {
            if (announces.getPublishDate().length() == 24) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER);
                if (publishDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a date in the present or in the future", "publishDate", context);
                    return false;
                }
            } else if (announces.getPublishDate().length() == 20) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_3);
                if (publishDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a date in the present or in the future", "publishDate", context);
                    return false;
                }
            } else if (announces.getPublishDate().length() == 16) {
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_2);
                if (publishDate.toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
                    addConstraintViolation("must be a date in the present or in the future", "publishDate", context);
                    return false;
                }
            }
        }

        //closeDate must be later than publish date (must not equal or before publish date)
        if (announces.getCloseDate() != null && announces.getPublishDate() != null) {
            if (announces.getCloseDate().length() == 24 && announces.getPublishDate().length() == 24) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 20 && announces.getPublishDate().length() == 20) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_3);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_3);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 16 && announces.getPublishDate().length() == 16) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_2);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_2);
                if (closeDate.toInstant(ZoneOffset.UTC).isBefore(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            }
        }

        //close date and publish date must not be the same date and time
        if (announces.getCloseDate() != null && announces.getPublishDate() != null) {
            if (announces.getCloseDate().length() == 24 && announces.getPublishDate().length() == 24) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER);
                if (closeDate.toInstant(ZoneOffset.UTC).equals(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 20 && announces.getPublishDate().length() == 20) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_3);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_3);
                if (closeDate.toInstant(ZoneOffset.UTC).equals(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            } else if (announces.getCloseDate().length() == 16 && announces.getPublishDate().length() == 16) {
                LocalDateTime closeDate = LocalDateTime.parse(announces.getCloseDate(), DATE_TIME_FORMATTER_2);
                LocalDateTime publishDate = LocalDateTime.parse(announces.getPublishDate(), DATE_TIME_FORMATTER_2);
                if (closeDate.toInstant(ZoneOffset.UTC).equals(publishDate.toInstant(ZoneOffset.UTC))) {
                    addConstraintViolation("must be later than publish date", "closeDate", context);
                    return false;
                }
            }
        }

        return true;
    }

    // add error message and field name
    public void addConstraintViolation(String messageTemplate, String field, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}

