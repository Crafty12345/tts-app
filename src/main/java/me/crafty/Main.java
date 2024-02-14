package me.crafty;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.tarsos.dsp.example.TarsosDSPExampleRunner;
import be.tarsos.dsp.example.unverified.PitchShiftingExample;
import be.tarsos.dsp.example.TarsosDSPExampleStarter;
public class Main {
    public static Position screenSize = new Position(600,600);
    public static EventHandler eventHandler;
    public static StateStorage stateStorage;

    public static Position GetCenterPosition(Position objSize){
        Position screenCenter = new Position(screenSize.x / 2, screenSize.y / 2);
        return new Position(screenCenter.x - (objSize.x / 2), screenCenter.y - (objSize.y / 2));
    }

    public static void main(String[] args)
    {

        stateStorage = new StateStorage();
        stateStorage.LoadJSONData("Standard");
        eventHandler = new EventHandler();
        String currentDir = stateStorage.getCurrentDir();
        //TimeStretch.main(new String[] {currentDir + "\\src\\main\\resources\\out.wav",currentDir + "\\src\\main\\resources\\test.wav","2.0"});

        JFrame frame = new JFrame();
        int leftMargin = 10;
        int topMargin = 10;
        TextArea taTextEntry = new TextArea("",0,0,TextArea.SCROLLBARS_NONE);
        taTextEntry.setFont(new Font("Arial", Font.PLAIN,20));
        taTextEntry.setBounds(leftMargin,topMargin,screenSize.x - 40, 200);

        JComboBox cbLanguageList = new JComboBox(new voicesCBModel(stateStorage.voicesData));

        JLabel lblVoice = new JLabel("Select a voice");
        lblVoice.setFont(new Font("Arial", Font.BOLD, 18));

        cbLanguageList.setMaximumRowCount(8);
        lblVoice.setBounds(leftMargin + 40, topMargin + taTextEntry.getHeight(), 200, 50);
        cbLanguageList.setBounds(leftMargin, topMargin + taTextEntry.getHeight() + 40, 200, 50);

        JSlider slPitchSlider = new JSlider(-500,500);
        slPitchSlider.setBounds((int)(cbLanguageList.getBounds().getMaxX()), cbLanguageList.getY(),200,50);
        JLabel lblPitch = new JLabel("Select pitch");
        lblPitch.setBounds(((int)lblVoice.getBounds().getMaxX()) + 20, topMargin + taTextEntry.getHeight(), 200, 50);
        lblPitch.setFont(new Font("Arial", Font.BOLD, 18));

        JSlider slSpeedSlider = new JSlider(25,175);
        JLabel lblSpeed = new JLabel("Select speed");
        lblSpeed.setBounds(((int)cbLanguageList.getBounds().getMinX()) + 20, (int)cbLanguageList.getBounds().getMaxY() + 20, 200, 50);
        lblSpeed.setFont(new Font("Arial", Font.BOLD, 18));
        slSpeedSlider.setBounds((int)(cbLanguageList.getBounds().getMinX()),(int) lblSpeed.getBounds().getMaxY(),200,50);

        JButton btnPlayAudio = new JButton("Play Audio");
        btnPlayAudio.setBounds(250, 450, 100, 100);
        btnPlayAudio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VoiceData selectedVoice = stateStorage.voicesData.get(cbLanguageList.getSelectedIndex());
                String selectedVoiceName = selectedVoice.getName();
                String selectedVoiceLanguage = selectedVoice.getLanguageCode();
                String selectedVoiceGender = selectedVoice.getGender();
                if(!stateStorage.textSynthesised){
                    eventHandler.SynthesiseAudio(e, taTextEntry.getText(),selectedVoiceLanguage,selectedVoiceName,selectedVoiceGender);
                }
                eventHandler.adjustPitch();
                eventHandler.adjustSpeed();
                eventHandler.PlayAudio();
            }
        });

        taTextEntry.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                stateStorage.textSynthesised = false;
            }
        });
        slPitchSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int val = source.getValue();
                stateStorage.voicePitch = val;
            }
        });
        slSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                double val = source.getValue() / 100.0;
                stateStorage.voiceSpeed = val;
            }
        });



        frame.add(taTextEntry);
        frame.add(lblVoice);
        frame.add(cbLanguageList);
        frame.add(lblPitch);
        frame.add(slPitchSlider);
        frame.add(lblSpeed);
        frame.add(slSpeedSlider);
        frame.add(btnPlayAudio);

        frame.setSize(screenSize.x, screenSize.y);
        frame.setLayout(null);
        frame.setVisible(true);
    }

}