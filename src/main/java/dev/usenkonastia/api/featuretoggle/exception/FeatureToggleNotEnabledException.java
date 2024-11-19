package dev.usenkonastia.api.featuretoggle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FeatureToggleNotEnabledException extends RuntimeException {
    public FeatureToggleNotEnabledException(String featureName) {
        super(String.format("Feature " + featureName + " is not enabled!"));
    }
}
