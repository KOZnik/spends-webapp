package pl.koznik.spends.common.control;

import javax.inject.Provider;
import javax.validation.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@javax.ws.rs.ext.Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    private static final Logger LOGGER = Logger.getLogger(ValidationExceptionMapper.class.getName());

    @Context
    private Configuration config;

    @Context
    private Provider<Request> request;

    @Override
    public Response toResponse(final ValidationException exception) {
        if (exception instanceof ConstraintViolationException) {
            LOGGER.log(Level.FINER, "Following ConstraintViolations has been encountered.", exception);
            final ConstraintViolationException cve = (ConstraintViolationException) exception;
            final Response.ResponseBuilder response = Response.status(getStatus(cve));

            // Entity
            final List<Variant> variants = Variant.mediaTypes(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).build();
            final Variant variant = request.get().selectVariant(variants);
            if (variant != null) {
                response.type(variant.getMediaType());
            }
            response.entity(
                    new GenericEntity<>(
                            getEntity(cve.getConstraintViolations()),
                            new GenericType<List<ValidationError>>() {
                            }.getType()
                    )
            );

            return response.build();
        } else {
            LOGGER.log(Level.WARNING, "Unexpected Bean Validation problem.", exception);

            return Response.serverError().entity(exception.getMessage()).build();
        }
    }

    private List<ValidationError> getEntity(final Set<ConstraintViolation<?>> violations) {
        final List<ValidationError> errors = new ArrayList<ValidationError>();

        for (final ConstraintViolation<?> violation : violations) {
            errors.add(new ValidationError(getInvalidValue(violation.getInvalidValue()), violation.getMessage(),
                    violation.getMessageTemplate(), getPath(violation)));
        }

        return errors;
    }

    private String getInvalidValue(final Object invalidValue) {
        if (invalidValue == null) {
            return null;
        }

        if (invalidValue.getClass().isArray()) {
            return Arrays.toString((Object[]) invalidValue);
        }

        return invalidValue.toString();
    }

    private Response.Status getStatus(final ConstraintViolationException exception) {
        return getResponseStatus(exception.getConstraintViolations());
    }

    private Response.Status getResponseStatus(final Set<ConstraintViolation<?>> constraintViolations) {
        final Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();

        if (iterator.hasNext()) {
            return getResponseStatus(iterator.next());
        } else {
            return Response.Status.BAD_REQUEST;
        }
    }

    private Response.Status getResponseStatus(final ConstraintViolation<?> constraintViolation) {
        for (final Path.Node node : constraintViolation.getPropertyPath()) {
            final ElementKind kind = node.getKind();

            if (ElementKind.RETURN_VALUE.equals(kind)) {
                return Response.Status.INTERNAL_SERVER_ERROR;
            }
        }

        return Response.Status.BAD_REQUEST;
    }

    private String getPath(final ConstraintViolation<?> violation) {
        final String leafBeanName = violation.getLeafBean().getClass().getSimpleName();
        final String leafBeanCleanName = (leafBeanName.contains("$")) ? leafBeanName.substring(0,
                leafBeanName.indexOf("$")) : leafBeanName;
        final String propertyPath = violation.getPropertyPath().toString();

        return leafBeanCleanName + (!"".equals(propertyPath) ? '.' + propertyPath : "");
    }

    @XmlRootElement
    public static final class ValidationError {
        private String invalidValue;
        private String message;
        private String messageTemplate;
        private String path;

        public ValidationError() {
        }

        public ValidationError(final String invalidValue, final String message, final String messageTemplate, final String path) {
            this.invalidValue = invalidValue;
            this.message = message;
            this.messageTemplate = messageTemplate;
            this.path = path;
        }

        public String getInvalidValue() {
            return invalidValue;
        }

        public void setInvalidValue(String invalidValue) {
            this.invalidValue = invalidValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageTemplate() {
            return messageTemplate;
        }

        public void setMessageTemplate(String messageTemplate) {
            this.messageTemplate = messageTemplate;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}