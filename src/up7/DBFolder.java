package up7;
import java.sql.*;

/*
 * 原型
*/
public class DBFolder {

	public DBFolder()
	{
	}

	public void remove(String idSign,Integer uid)
	{
		String sql = "{call fd_remove(?,?)}";
		DbHelper db = new DbHelper();
		try 
		{
			CallableStatement cmd = db.GetCommandStored(sql);
			cmd.setString(1, idSign);
			cmd.setInt(2, uid);
			cmd.execute();
			cmd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}