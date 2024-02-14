package me.crafty;

import javax.swing.*;
import java.util.ArrayList;

public class voicesCBModel extends AbstractListModel implements ComboBoxModel {


    private final int defaultIndex = 25;

    public voicesCBModel(ArrayList<VoiceData> voicesData) {
        this.voicesData = voicesData;
    }

    private ArrayList<VoiceData> voicesData = new ArrayList<VoiceData>();
    String selection = null;
    VoiceData selectedVoice = null;

    public String getElementAt(int index){
        return voicesData.get(index).displayName;
    }

    public int getSize(){
        return voicesData.size();
    }

    public void setSelectedItem(Object item){
        selection = item.toString();
    }
    public String getSelectedItem(){
        if (selection == null){
            selection = voicesData.get(defaultIndex).displayName;
        }
        return selection;
    }
}
