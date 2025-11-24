package Entities;

public class Technique {
	
	public static final String INLINING_EXPANTION="mode=1:depth:0:pragma=0:foronly=1:complement=0:functions=";
	
	private String name;
	private String value;
	private String loopTinline;
	private boolean visited; 
	private String originalLoopOrder;
	private String desiredLoopOrder;

	
	public Technique(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		this.visited = false;
	}
	public Technique(String name, String value, String originalLoopOrder, String desiredLoopOrder) {
		super();
		this.name = name;
		this.value = value;
		this.originalLoopOrder = originalLoopOrder;
		this.desiredLoopOrder = desiredLoopOrder;
		this.visited = false;
	}
	
	public Technique(String name, String value, String loopTinline) {
		super();
		this.name = name;
		this.value = value;
		this.loopTinline = loopTinline;
		this.visited = false;
	}
	
	public Technique() {
		super();
	
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public void setDesiredLoopOrder(String desiredLoopOrder) {
		this.desiredLoopOrder = desiredLoopOrder;
	}

	@Override
	public String toString() {
		String technique ="";
		if(this.name.equals("tinline")&&this.getValue()=="0")
		{
			technique="";
		}
		else if(this.name.contains("tinline")&&this.getValue()=="1")
		{
			technique= name+ "=" + INLINING_EXPANTION+this.loopTinline;
		}

		else {
			technique=  name + "=" + value;
		}
		
		return technique;
			
		//return this.name.equals("loop_interchange")&&this.getValue()=="0"?"": this.name.equals("loop_interchange")&&this.getValue()=="1"?"-"+ name: "-" + name + "=" + value+" ";
	}

	public static String getInliningExpantion() {
		return INLINING_EXPANTION;
	}

	public String getLoopTinline() {
		return loopTinline;
	}

	public void setLoopTinline(String loopTinline) {
		this.loopTinline = loopTinline;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}
	
}