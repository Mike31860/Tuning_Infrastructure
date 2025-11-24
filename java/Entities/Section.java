package Entities;

import java.util.ArrayList;

public class Section {

    private String sectionId;
    private Double executionTime;
    private ArrayList<Technique> techniques;
    private boolean finish;


    public Section(String sectionId, Double executionTime) {
        this.sectionId = sectionId;
        this.executionTime = executionTime;
    }
    

    public Section(String sectionId, ArrayList<Technique> techniques) {
        this.sectionId = sectionId;
        this.techniques = techniques;
        this.finish = false;
    }


    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Double executionTime) {
        this.executionTime = executionTime;
    }


    public ArrayList<Technique> getTechniques() {
        return techniques;
    }


    public void setTechniques(ArrayList<Technique> techniques) {
        this.techniques = techniques;
    }


    public boolean isFinish() {
        return finish;
    }


    public void setFinish(boolean fineshed) {
        this.finish = fineshed;
    }



    
    
}
