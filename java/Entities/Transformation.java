package Entities;

public class Transformation {
	
	private String name;
	private double relativeImprovement;
	private double executionTime;
	
	
	public Transformation(String name, double relativeImprovement, double executionTime) {
		super();
		this.name = name;
		this.relativeImprovement = relativeImprovement;
		this.executionTime = executionTime;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRelativeImprovement() {
		return relativeImprovement;
	}
	public void setRelativeImprovement(double relativeImprovement) {
		this.relativeImprovement = relativeImprovement;
	}

	public double getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(double executionTime) {
		this.executionTime = executionTime;
	}
	@Override
	public String toString() {
		return this.name.equals("loop-interchange")&&this.getRelativeImprovement()==0?"": this.name.equals("loop-interchange")&&this.getRelativeImprovement()==1?"-"+ name: "-" + name + "=" + relativeImprovement+" ";
	}


}
