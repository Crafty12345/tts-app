package me.crafty;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class StateStorage {

    public ArrayList<VoiceData> voicesData = new ArrayList<VoiceData>();
    public boolean textSynthesised;
    public int voicePitch = 0;
    public double voiceSpeed = 1.0;
    private String currentDir;

    public StateStorage (){
        currentDir = Paths.get("").toAbsolutePath().toString();
    }

    public ArrayList<VoiceData> GetWavenet(){
        if(voicesData.isEmpty()){
            System.out.println("Error retrieving Wavenet voices: voices are not intialised yet.");
            return null;
        }
        return voicesData.stream().filter(voice -> voice.getType().equalsIgnoreCase("Wavenet")).collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<VoiceData> GetStandard(){
        if(voicesData.isEmpty()){
            System.out.println("Error retrieving Wavenet voices: voices are not intialised yet.");
            return null;
        }
        return voicesData.stream().filter(voice -> voice.getType().equalsIgnoreCase("Wavenet")).collect(Collectors.toCollection(ArrayList::new));
    }


    public void LoadJSONData(String voiceType)
    {
        String filename = "src/main/resources/voices.json";
        System.out.println("Current directory: " + currentDir);
        JSONParser parser = new JSONParser();
        try{
            JSONArray arr = (JSONArray) parser.parse(new FileReader(filename));
            for (Object obj : arr){
                JSONObject voice = (JSONObject) obj;
                String name = voice.get("name").toString();
                String gender = voice.get("ssmlGender").toString();
                int sampleRate = ( (Number) voice.get("naturalSampleRateHertz")).intValue();
                String type = voice.get("type").toString();
                String englishName = voice.get("englishName").toString();
                String langCode = voice.get("languageCode").toString();
                voicesData.add(new VoiceData(
                        langCode,
                        name,
                        gender,
                        sampleRate,
                        type,
                        englishName
                        ));

            }

            Collections.sort(voicesData);

            if(voiceType.equalsIgnoreCase("Wavenet")){
                voicesData = GetWavenet();
            } else if (voiceType.equalsIgnoreCase("Standard")) {
                voicesData = GetStandard();
            }
        }
        catch (FileNotFoundException ex){
            System.out.println("File not found: '" + filename + "'.");
        }
        catch (ParseException ex){
            System.out.println("An error occured when parsing JSON:\n" + ex.getStackTrace());
        }
        catch (IOException ex){
            System.out.println("An IO Exception occured when loading JSON:\n" + ex.getStackTrace());
        }
    }

    public String getCurrentDir(){return currentDir;}
}
