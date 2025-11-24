package Entities;

import java.util.ArrayList;

public class Procedure {

    private String procedureId;
  
    private ArrayList<Section> sections;

    public Procedure(String procedureId, ArrayList<Section> sections) {
        this.procedureId = procedureId;
        this.sections = sections;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

 
  
    
    
    
}
