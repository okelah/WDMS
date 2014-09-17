package DAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

public class Logger {

	private static DataSource dataSource;
	
	public Logger(){
		DBWrapper dbw = new DBWrapper();
		dbw.setDataSource(dataSource);
	}
	
	public Logger(DataSource data)
	{
		dataSource = data;
		DBWrapper dbw = new DBWrapper();
		dbw.setDataSource(dataSource);
	}
	
	public static void storeLog(String comment, int Category, int userId)
			throws SQLException {

		ArrayList<String> queryParams = new ArrayList<String>();
		queryParams.add(comment);
		queryParams.add(Integer.toString(Category));
		queryParams.add(Integer.toString(userId));

		DBWrapper obj = new DBWrapper();
		int r = obj.executeUpdate(DBQueries.INSERT_LOG, queryParams);
		if (r > 0) {
			System.out.println("Logging is Done");
		}

		else
			System.out.println("Problem is Logging");

	}

	public static void main(String[] args) throws SQLException {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyyHH:mm:ss");
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);

		// storeLog("dengey","bomb",123);

	}

	public void setDataSource(DataSource data) 
	{
		dataSource = data;
	}

}
