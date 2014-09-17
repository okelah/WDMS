package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

public class DBWrapper {
	
	private static Connection connection;
	private static DataSource dataSource;
	private static final String SYSTEMLOG_DESTINATION_DIR_PATH ="c:/tmp/syslogs/";
	
	
	public DBWrapper() {

	setDataSource(dataSource);
	
	}

	
	
	public void setDataSource(DataSource dataSource) {
		DBWrapper.dataSource = dataSource;
	}

	
	/*
	 * For Select Queries. 
	 */
	
	
	public HashMap<String,ArrayList<String>>  executeQuery(String query,ArrayList<String> queryParams) throws SQLException
	{
		connection = dataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		System.out.println("Query is :"+query);

		for(int i=0;i<queryParams.size();i++)
		{
		statement.setString(i+1,queryParams.get(i));
		System.out.println("Param :"+i+":    "+queryParams.get(i));
		}
		ResultSet rs = statement.executeQuery();
		ResultSetMetaData metaDeta=rs.getMetaData();
		
		HashMap<String,ArrayList<String>> queryResult=new HashMap<String, ArrayList<String>>();
		ArrayList<String> valueList;
		
		while(rs.next())
		{
			for(int i=1;i<=metaDeta.getColumnCount();i++)
			{
				String columnName=metaDeta.getColumnName(i);
				String columnValue=rs.getString(columnName);
				
				if(queryResult.containsKey(columnName))
				{
					valueList=queryResult.get(columnName);
					valueList.add(columnValue);
				}
				else
				{
					valueList=new ArrayList<String>();
					valueList.add(columnValue);
					
				}
				queryResult.put(columnName, valueList);
			}
		}
		connection.close();
		return queryResult;
	}
	
	/*
	 * For Update and Insert
	 */
	
	public int   executeUpdate(String query,ArrayList<String> queryParams) throws SQLException
	{
		connection = dataSource.getConnection();
		System.out.println("Query is :"+query);

		PreparedStatement statement = connection.prepareStatement(query);

		for(int i=0;i<queryParams.size();i++)
		{
		statement.setString(i+1,queryParams.get(i));
		System.out.println("Param :"+i+":    "+queryParams.get(i));
		}
		int rowCount=statement.executeUpdate();
		System.out.println("Number Of Rows Affected:"+rowCount);		
		connection.close();
		return rowCount;
	}
	
	public void getSystemLog(String filename) throws SQLException
	{
		connection = dataSource.getConnection();
		Statement stmt = connection.createStatement();
	    filename = SYSTEMLOG_DESTINATION_DIR_PATH+filename;
	    System.out.println("xxxxxxxxxx      "+  filename);
	    String tablename = "systemlog";
	  
	    stmt.executeQuery("SELECT * INTO OUTFILE \"" + filename + "\" FROM " + tablename);
	}

}
