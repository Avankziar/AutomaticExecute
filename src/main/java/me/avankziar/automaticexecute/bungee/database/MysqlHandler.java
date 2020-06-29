package main.java.me.avankziar.automaticexecute.bungee.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;

public class MysqlHandler
{
	private AutomaticExecute plugin;
	public String tableNameI;
	
	public MysqlHandler(AutomaticExecute plugin)
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().get().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean existItem(String itemname) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `item_name` FROM `" + tableNameI + "` WHERE `item_name` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, itemname);
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return true;
		        }
		    } catch (SQLException e) 
			{
				  AutomaticExecute.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
		
	/*public boolean createItem(ItemStack item) 
	{
		String name = "";
		if(item.getItemMeta() != null && item.getItemMeta().hasDisplayName())
		{
			name = item.getItemMeta().getDisplayName();
		} else
		{
			name = item.getType().toString();
		}
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + tableNameI 
						+ "`(`player_uuid`, `player_name`) " 
						+ "VALUES(?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, name);
		        preparedStatement.setString(2, plugin.getUtility().);
		        
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  AutomaticMessage.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}*/
	
	public boolean updateDataI(Object object, Object whereobject, String setcolumn, String wherecolumn) 
	{
		PreparedStatement preparedUpdateStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + tableNameI 
						+ "` " + "SET `" + setcolumn + "` = ?" + " WHERE `"+wherecolumn+"` = ?";
				preparedUpdateStatement = conn.prepareStatement(data);
				preparedUpdateStatement.setObject(1, object);
				preparedUpdateStatement.setObject(1, whereobject);
				
				preparedUpdateStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				AutomaticExecute.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedUpdateStatement != null) {
						preparedUpdateStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	public Object getDataI(Object object, String selectcolumn, String wherecolumn)
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `" + selectcolumn + "` FROM `" + tableNameI + "` WHERE `" + wherecolumn + "` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setObject(1, object);
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return result.getObject(selectcolumn);
		        }
		    } catch (SQLException e) 
			{
				  AutomaticExecute.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	public void deleteDataI(Object object, String wherecolumn)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + tableNameI + "` WHERE `" + wherecolumn + "` = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setObject(1, object);
			preparedStatement.execute();
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			try {
				if (preparedStatement != null) 
				{
					preparedStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}