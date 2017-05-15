package up7;
import java.sql.*;

/*
 * 原型
*/
public class DBFolder {

	public DBFolder()
	{
	}

    /// <summary>
    /// 子文件上传完毕
    /// </summary>
    /// <param name="fd_idSvr"></param>
	public  static void child_complete(int fd_idSvr)
    {
        String sql = "update up7_folders set fd_filesComplete=fd_filesComplete+1 where fd_id=?";
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sql);
		try {
			cmd.setInt(1, fd_idSvr);
		} catch (SQLException e) {e.printStackTrace();}        
        db.ExecuteNonQuery(cmd);
    }
}