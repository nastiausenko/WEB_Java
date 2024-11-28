package dev.usenkonastia.api.featuretoggle.aspect;

import dev.usenkonastia.api.featuretoggle.FeatureToggleService;
import dev.usenkonastia.api.featuretoggle.annotation.FeatureToggle;
import dev.usenkonastia.api.featuretoggle.exception.FeatureToggleNotEnabledException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Before("@annotation(featureToggle)")
    public void checkFeatureToggle(FeatureToggle featureToggle) {
        String featureName = featureToggle.value().getFeatureName();
        if (!featureToggleService.isEnabled(featureName)) {
            log.warn("Feature toggle {} is not enabled!", featureName);
            throw new FeatureToggleNotEnabledException(featureName);
        }
    }
}
