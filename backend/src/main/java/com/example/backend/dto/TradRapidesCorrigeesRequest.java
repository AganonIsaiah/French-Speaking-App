package com.example.backend.dto;

public class TradRapidesCorrigeesRequest {
    private String originalFrench;
    private String translatedEnglish;

    public String getOriginalFrench() {
        return originalFrench;
    }

    public void setOriginalFrench(String originalFrench) {
        this.originalFrench = originalFrench;
    }

    public String getTranslatedEnglish() {
        return translatedEnglish;
    }

    public void setTranslatedEnglish(String translatedEnglish) {
        this.translatedEnglish = translatedEnglish;
    }
}
