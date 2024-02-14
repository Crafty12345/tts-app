package me.crafty;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioGenerator;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.io.*;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.example.unverified.PitchShiftingExample;
import be.tarsos.dsp.example.unverified.TimeStretch;

public class EventHandler implements LineListener
{
    static boolean isPlaybackCompleted;
    final String tempPath = "./src/main/resources/";

    @Override
    public void update(LineEvent lineEvent){
        if (LineEvent.Type.START == lineEvent.getType()){
            System.out.println("Playback has started.");
        } else if (LineEvent.Type.STOP == lineEvent.getType()) {
            isPlaybackCompleted = true;
            System.out.println("Playback completed.");
        }
    }

    private SsmlVoiceGender getSsmlGender(String gender){
        switch (gender.toLowerCase()){
            case "female":
                return SsmlVoiceGender.FEMALE;
            case "male":
                return SsmlVoiceGender.MALE;
            case "neutral":
                return SsmlVoiceGender.NEUTRAL;
            default:
                System.out.println("Warning: '"+ gender +"' is not a recognised gender.");
                return null;
        }
    }

    public void SynthesiseAudio(ActionEvent e, String text, String language, String name, String gender)
    {
        try{
            try (TextToSpeechClient ttsClient = TextToSpeechClient.create()){
                System.out.println("Synthesising audio...");
                SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
                System.out.println(name);
                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(language)
                        .setName(name)
                        .setSsmlGender(getSsmlGender(gender))
                        .build();

                AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build();
                SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input,voice,audioConfig);
                ByteString audioContents = response.getAudioContent();
                try (OutputStream out = new FileOutputStream(tempPath + "out.wav")){
                    out.write(audioContents.toByteArray());
                    System.out.println("Contents written to 'out.wav'");
                }
            }
        }
        catch (IOException ex){
            System.out.println("An error occurred: \n" + ex.toString());
        }

    }

    public void adjustPitch(){
        final String fname = tempPath + "out.wav";
        final String outFname = tempPath + "out2.wav";
        final int pitchAdjustment = Main.stateStorage.voicePitch;
        PitchShiftingExample.main(new String[] {fname,outFname,Integer.toString(pitchAdjustment)});
    }

    public void adjustSpeed(){
        final String fname = tempPath + "out2.wav";
        final String outFname = tempPath + "final.wav";
        final double speedAdjustment = Main.stateStorage.voiceSpeed;
        TimeStretch.main(new String[] {fname,outFname,Double.toString(speedAdjustment)});
    }

    public void PlayAudio(){
        System.out.println("Playing audio...");
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(tempPath + "final.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            audioStream.close();
            System.out.println("Finished playing audio.");

        }
        catch (UnsupportedAudioFileException ex){
            System.out.println("An Error Occured whilst trying to process audio:\n" + ex.getMessage());
        }
        catch (LineUnavailableException ex){
            System.out.println("An Error Occured whilst trying to play audio:\n" + ex.getMessage());
        }
        catch (IOException ex){
            System.out.println("An error occurred whilst trying to play audio:\n" + ex.getMessage());
        }
    }

}