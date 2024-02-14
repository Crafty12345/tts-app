package me.crafty;

import java.util.Arrays;
import java.util.Collections;

public class VoiceData implements Comparable<VoiceData>
{
    public VoiceData(String languageCode, String name, String gender, int sampleRate, String type, String englishName) {
        this.languageCode = languageCode;
        this.name = name;
        this.gender = gender;
        this.sampleRate = sampleRate;
        this.type = type;
        this.englishName = englishName;
        this.displayName = generateDisplayName();
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public String getType() {
        return type;
    }

    public String getEnglishName() {
        return englishName;
    }
    private String generateDisplayName(){return englishName + " (" + gender + ")";}

    private String name;
    private String languageCode;
    private String gender;
    private int sampleRate;
    private String type;
    private String englishName;

    public String getDisplayName() {
        return displayName;
    }

    String displayName;
    @Override
    public int compareTo(VoiceData v2) {
        return displayName.compareTo(v2.displayName);
    }
}
