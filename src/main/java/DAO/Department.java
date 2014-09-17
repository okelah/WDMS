package DAO;

public enum Department {

	 		 HumanResources(1),
			 LogisticAndSupply(2),
			 ITSupport(3),
			 SalesAndPromotion(4),
			 ResearchAndDevelopement(5),
			 Finance(6),
			 Management(7);
	 
	private final int departmentId;
	
	Department(int departmentId){
		this.departmentId = departmentId;
	}
	
	public int getDepartmentId(){
		return departmentId;
	}
}
