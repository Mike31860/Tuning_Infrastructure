package Entities;

public class Loop_reader {

    private String procedureName;
    private String loopId;
    private String originalLoopOrder;
    private String desiredLoopOrder;

    public Loop_reader(String loopId, String procedureName,  String originalLoopOrder, String desiredLoopOrder) {
        this.loopId = loopId;
        this.procedureName = procedureName;
        this.originalLoopOrder = originalLoopOrder;
        this.desiredLoopOrder = desiredLoopOrder;
    }
    public String getLoopId() {
        return loopId;
    }
    public String getProcedureName() {
        return procedureName;
    }
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }
    public void setLoopId(String loopId) {
        this.loopId = loopId;
    }
    public String getOriginalLoopOrder() {
        return originalLoopOrder;
    }
    public void setOriginalLoopOrder(String originalLoopOrder) {
        this.originalLoopOrder = originalLoopOrder;
    }
    public String getDesiredLoopOrder() {
        return desiredLoopOrder;
    }
    
}
