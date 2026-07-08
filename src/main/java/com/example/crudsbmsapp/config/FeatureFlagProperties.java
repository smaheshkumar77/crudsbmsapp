package com.example.crudsbmsapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class FeatureFlagProperties {
    private boolean featureFlag;

    public boolean isFeatureFlag() {
        return featureFlag;
    }

    public void setFeatureFlag(boolean featureFlag) {
        this.featureFlag = featureFlag;
    }
}
