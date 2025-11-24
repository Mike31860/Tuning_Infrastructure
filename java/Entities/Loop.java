package Entities;

import java.util.ArrayList;

public class Loop {

    private String loopId;
    private ArrayList<Technique> listTechniques;
    private boolean visited; 
    


    public Loop(String loopId, ArrayList<Technique> listTechniques) {
        this.loopId = loopId;
        this.listTechniques = listTechniques;
        this.visited = false;
    }

    public Loop(String loopId) {
        this.loopId = loopId;
        this.visited = false;
    }


    public String getLoopId() {
        return loopId;
    }


    public void setLoopId(String loopId) {
        this.loopId = loopId;
    }


    public ArrayList<Technique> getListTechniques() {
        return listTechniques;
    }


    public void setListTechniques(ArrayList<Technique> listTechniques) {
        this.listTechniques = listTechniques;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    
    

    

    
    
}
