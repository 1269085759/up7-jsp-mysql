package down3.biz;

import com.google.gson.Gson;

import down3.model.DnFolderInf;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import up7.DBFile;
import up7.DbHelper;
import up7.model.FileInf;
import up7.model.FolderInf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DnFolder 
{
    public DnFolder()
    { }
    
    public static int Add(DnFolderInf inf)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into down3_folders(");		
		sb.append(" fd_name");
		sb.append(",fd_uid");
		sb.append(",fd_mac");
		sb.append(",fd_pathLoc");
		sb.append(",fd_id_old");
		
		sb.append(") values(");		
		
		sb.append(" ?");//sb.append("@fd_name");
		sb.append(",?");//sb.append(",@fd_uid");
		sb.append(",?");//sb.append(",@fd_mac");
		sb.append(",?");//sb.append(",@fd_pathLoc");
		sb.append(",?");//sb.append(",@fd_id_old");
		sb.append(")");

		DbHelper db = new DbHelper();
		//PreparedStatement cmd = db.GetCommand(sb.toString(),"fd_id");
		PreparedStatement cmd = db.GetCommandPK(sb.toString());
		try {
			cmd.setString(1, inf.nameLoc);
			cmd.setInt(2, inf.uid);
			cmd.setString(3, inf.mac);
			cmd.setString(4, inf.pathLoc);
			cmd.setInt(5, inf.fdID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		int fid = (int)db.ExecuteGenKey(cmd);		
		
		return fid;    	
    }
    
    public static void Clear()
    {
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("truncate table down3_folders");
		db.ExecuteNonQuery("truncate table down3_files");
    }
    
    public static void Del(String idF,String idFD,String uid,String mac)
    {
        String sql = "delete from down3_folders where fd_id=? and fd_mac=? and fd_uid=?";
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sql);
        try
        {
			cmd.setInt(1, Integer.parseInt(idFD) );
			cmd.setString(2, mac);
			cmd.setString(3, uid);
			db.ExecuteNonQuery(cmd);
			
			//删除down3_files
			sql = "delete from down3_files where f_id=? and f_mac=? and f_uid=?";
			cmd = db.GetCommand(sql);
			cmd.setInt(1, Integer.parseInt(idF));
			cmd.setString(2, mac);
			cmd.setString(3, uid);
			db.ExecuteNonQuery(cmd);
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        }
    }
    
    public static void Update(String fid,String uid,String mac,String percent)
    {
        String sql = "update down3_folders set fd_percent=? where fd_id=? and fd_uid=? and fd_mac=?";
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sql);
        try
        {
			cmd.setString(1, percent );
			cmd.setInt(2, Integer.parseInt(fid) );
			cmd.setInt(3, Integer.parseInt(uid) );
			cmd.setString(4, mac );
			db.ExecuteNonQuery(cmd);
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        }    	
    }
}