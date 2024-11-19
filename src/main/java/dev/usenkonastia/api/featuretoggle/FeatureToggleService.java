package dev.usenkonastia.api.featuretoggle;

import dev.usenkonastia.api.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {
    private final FeatureToggleProperties featureToggles;

    public FeatureToggleService(FeatureToggleProperties featureToggles) {
        this.featureToggles = featureToggles;
    }

    public boolean isEnabled(String featureName) {
        return featureToggles.check(featureName);
    }

    public void enable(String featureName) {
        featureToggles.getToggles().put(featureName, true);
    }

    public void disable(String featureName) {
        featureToggles.getToggles().put(featureName, false);
    }
}
