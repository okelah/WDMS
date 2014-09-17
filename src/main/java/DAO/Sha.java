package DAO;


public class Sha {

	public static String hashCreator(String password){

		//TODO : Make Multiple runs and add Salt.
		String sha="";
		if(password==null)
			return null;
			sha=HashConstructer.getSaltedHash(password);  
		return sha;

	}
	
	public static boolean validatePassword(String inputPassword,String storedPassword)
	{
		
		try {
			
			boolean flag=HashConstructer.check(inputPassword,storedPassword);
			return flag;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
		
		
		
	}
	
	

}
