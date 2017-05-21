package up7;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import up7.biz.folder.fd_file_redis;
import up7.model.FileInf;
import up7.model.FolderInf;
import up7.model.xdb_files;
import net.sf.json.JSONArray;
import com.google.gson.Gson;

/*
 * 原型
*/
public class DBFile {

	public DBFile()
	{
	}

	/**
	 * 根据文件ID获取文件信息
	 * @param f_id
	 * @param inf
	 * @return
	 */
	public boolean find(int f_id,xdb_files inf)
	{
		boolean ret = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(" f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_pathRel");
		sb.append(",f_md5");
		sb.append(",f_lenLoc");
		sb.append(",f_sizeLoc");
		sb.append(",f_pos");
		sb.append(",f_lenSvr");
		sb.append(",f_perSvr");
		sb.append(",f_complete");
		sb.append(",f_time");
		sb.append(",f_deleted");
		sb.append(" from up7_files where f_id=? limit 0,1");
		
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setInt(1, f_id);
			ResultSet r = db.ExecuteDataSet(cmd);

			if (r.next())
			{
				inf.idSvr 			= f_id;
				inf.uid 			= r.getInt(1);
				inf.nameLoc 		= r.getString(2);
				inf.nameSvr 		= r.getString(3);
				inf.pathLoc 		= r.getString(4);
				inf.pathSvr 		= r.getString(5);
				inf.pathRel 		= r.getString(6);
				inf.md5 			= r.getString(7);
				inf.lenLoc 			= r.getLong(8);
				inf.sizeLoc 		= r.getString(9);
	            inf.FilePos 		= r.getLong(10);
	            inf.lenSvr 			= r.getLong(11);
				inf.perSvr 			= r.getString(12);
				inf.complete 		= r.getBoolean(13);
				inf.PostedTime 		= r.getDate(14);
				inf.deleted			= r.getBoolean(15);
				ret = true;
			}
			cmd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	/// <summary>
	/// 增加一条数据，并返回新增数据的ID
	/// 在ajax_create_fid.aspx中调用
	/// 文件名称，本地路径，远程路径，相对路径都使用原始字符串。
	/// d:\soft\QQ2012.exe
	/// </summary>
	public void Add(xdb_files model)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into up7_files(");
		sb.append(" f_idSign");
		sb.append(",f_sizeLoc");
		sb.append(",f_pos");
		sb.append(",f_lenSvr");
		sb.append(",f_perSvr");
		sb.append(",f_complete");
		sb.append(",f_deleted");
		sb.append(",f_fdChild");
		sb.append(",f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_pathRel");
		sb.append(",f_md5");
		sb.append(",f_lenLoc");
		sb.append(",f_sign");
		
		sb.append(") values (");
		
		sb.append(" ?");//sb.append("@f_idSign");
		sb.append(",?");//sb.append(",@f_sizeLoc");
		sb.append(",?");//sb.append(",@f_pos");
		sb.append(",?");//sb.append(",@f_lenSvr");
		sb.append(",?");//sb.append(",@f_perSvr");
		sb.append(",?");//sb.append(",@f_complete");
		sb.append(",?");//sb.append(",@f_deleted");
		sb.append(",?");//sb.append(",@f_fdChild");
		sb.append(",?");//sb.append(",@f_uid");
		sb.append(",?");//sb.append(",@f_nameLoc");
		sb.append(",?");//sb.append(",@f_nameSvr");
		sb.append(",?");//sb.append(",@f_pathLoc");
		sb.append(",?");//sb.append(",@f_pathSvr");
		sb.append(",?");//sb.append(",@f_pathRel");
		sb.append(",?");//sb.append(",@f_md5");
		sb.append(",?");//sb.append(",@f_lenLoc");
		sb.append(",?");//sb.append(",@f_sign");
		sb.append(") ");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		
		try {
			cmd.setString(1, model.idSign);
			cmd.setString(2, model.sizeLoc);
			cmd.setLong(3, model.FilePos);
			cmd.setLong(4, model.lenSvr);
			cmd.setString(5, model.perSvr);
			cmd.setBoolean(6, model.complete);
			//cmd.setDate(6, (java.sql.Date) model.PostedTime);
			cmd.setBoolean(7, false);
			cmd.setBoolean(8, model.f_fdChild);
			cmd.setInt(9, model.uid);
			cmd.setString(10, model.nameLoc);
			cmd.setString(11, model.nameSvr);
			cmd.setString(12, model.pathLoc);
			cmd.setString(13, model.pathSvr);
			cmd.setString(14, model.pathRel);
			cmd.setString(15, model.md5);
			cmd.setLong(16, model.lenLoc);
			cmd.setString(17, model.sign);
		} catch (SQLException e) {
			System.out.println("添加文件信息错误，数据库错误");
			e.printStackTrace();
		}

		db.ExecuteNonQuery(cmd);
	}
	
	/**
	 * 将文件缓存信息添加到数据库，
	 * @param inf
	 */
	public void addComplete(xdb_files inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into up7_files(");
		sb.append(" f_idSign");//1		
		sb.append(",f_uid");//2
		sb.append(",f_nameLoc");//3
		sb.append(",f_nameSvr");//4
		sb.append(",f_pathLoc");//5
		sb.append(",f_pathSvr");//6		
		sb.append(",f_lenLoc");//7
		sb.append(",f_lenSvr");//8
		sb.append(",f_perSvr");//
		sb.append(",f_sizeLoc");//9
		sb.append(",f_complete");//
		sb.append(",f_blockCount");//10
		sb.append(",f_blockSize");//11
		sb.append(",f_blockPath");//12
		
		sb.append(") values(");
				
		sb.append(" ?");//sb.append("@f_idSign");
		sb.append(",?");//sb.append(",@f_uid");
		sb.append(",?");//sb.append(",@f_nameLoc");
		sb.append(",?");//sb.append(",@f_nameSvr");
		sb.append(",?");//sb.append(",@f_pathLoc");
		sb.append(",?");//sb.append(",@f_pathSvr");
		sb.append(",?");//sb.append(",@f_lenLoc");
		sb.append(",?");//sb.append(",@f_lenSvr");
		sb.append(",'100%'");//sb.append(",@f_perSvr");
		sb.append(",?");//sb.append(",@f_sizeLoc");
		sb.append(",1");//sb.append(",@f_complete");
		sb.append(",?");//sb.append(",@f_blockCount");
		sb.append(",?");//sb.append(",@f_blockSize");
		sb.append(",?");//sb.append(",@f_blockPath");
		sb.append(")");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setString(1, inf.idSign);
			cmd.setInt(2, inf.uid);
			cmd.setString(3, inf.nameLoc);
			cmd.setString(4, inf.nameLoc);
			cmd.setString(5, inf.pathLoc);
			cmd.setString(6, inf.pathSvr);
			cmd.setLong(7, inf.lenLoc);
			cmd.setLong(8, inf.lenLoc);
			cmd.setString(9, inf.lenLoc>1024 ? inf.sizeLoc : PathTool.getDataSize(inf.lenLoc));
			cmd.setInt(10, inf.blockCount);
			cmd.setInt(11,inf.blockSize);
			cmd.setString(12,inf.blockPath);
			cmd.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 清空文件表，文件夹表数据。
	 */
	static public void Clear()
	{
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("delete from up7_files;");
		db.ExecuteNonQuery("delete from up7_folders;");
	}

	/**
	 * @param f_uid
	 * @param f_id
	 */
	static public void Complete(int f_uid, int f_id)
	{
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand("update up7_files set f_perSvr='100%' ,f_complete=1 where f_uid=? and f_fdID=?;");
		try {
			cmd.setInt(1, f_uid);
			cmd.setInt(2, f_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		db.ExecuteNonQuery(cmd);
	}
	
	/**
	 * @param f_uid
	 * @param f_id
	 */
	static public void fd_complete(String f_id, String fd_id, String uid)
	{
		DbHelper db = new DbHelper();
		Connection con = db.GetCon();
		
		try {
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			stmt.addBatch("update up7_files set f_perSvr='100%',f_lenSvr=f_lenLoc,f_complete=1 where f_id=" + f_id);
			stmt.addBatch("update up7_folders set fd_complete=1 where fd_id=" + fd_id + " and fd_uid=" + uid);
			stmt.addBatch("update up7_files set f_perSvr='100%',f_lenSvr=f_lenLoc,f_complete=1 where f_pidRoot=" + fd_id);//更新所有子文件状态，设为已完成
			stmt.executeBatch();
			con.commit();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
    public boolean fd_fileProcess(int uid, int f_id, long f_pos, long lenSvr, String perSvr, int fd_idSvr, long fd_lenSvr,String fd_perSvr,boolean complete)
    {
    	this.f_process(uid, f_id, f_pos, lenSvr, perSvr,complete);
    	this.fd_process(uid, fd_idSvr, fd_lenSvr,fd_perSvr);
    	return true;
    }
    
    public boolean fd_process(int uid,int fd_idSvr,long fd_lenSvr,String perSvr)
    {
        String sql = "call fd_process(?,?,?,?)";
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommandStored(sql);     

		try 
		{
			cmd.setInt(1, uid);
			cmd.setInt(2, fd_idSvr);
			cmd.setLong(3, fd_lenSvr);
			cmd.setString(4, perSvr);
		} catch (SQLException e) {e.printStackTrace();}

		db.ExecuteNonQuery(cmd);
		return true;
	}

	/// <summary>
	/// 更新上传进度
	/// </summary>
	///<param name="f_uid">用户ID</param>
	///<param name="f_id">文件ID</param>
	///<param name="f_pos">文件位置，大小可能超过2G，所以需要使用long保存</param>
	///<param name="f_lenSvr">已上传长度，文件大小可能超过2G，所以需要使用long保存</param>
	///<param name="f_perSvr">已上传百分比</param>
	public boolean f_process(int f_uid,int f_id,long f_pos,long f_lenSvr,String f_perSvr,boolean cmp)
	{
		//String sql = "update up7_files set f_pos=?,f_lenSvr=?,f_perSvr=? where f_uid=? and f_id=?";
		String sql = "call f_process(?,?,?,?,?,?)";//change(2015-03-23):使用存储过程
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommandStored(sql);
		
		try 
		{
			cmd.setLong(1, f_pos);
			cmd.setLong(2, f_lenSvr);
			cmd.setString(3, f_perSvr);
			cmd.setInt(4, f_uid);
			cmd.setInt(5, f_id);
			cmd.setBoolean(6, cmp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.ExecuteNonQuery(cmd);
		return true;
	}

	/// <summary>
	/// 上传完成。将所有相同MD5文件进度都设为100%
	/// </summary>
	public void complete(int uid,int idSvr)
	{
		String sql = "update up7_files set f_lenSvr=f_lenLoc,f_perSvr='100%',f_complete=1 where f_id=? and f_uid=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);
		
		try 
		{
			cmd.setInt(1, idSvr);
			cmd.setInt(2, uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);//在部分环境中测试发现执行后没有效果。
	}
	
	/// <summary>
	/// 上传完成。将所有相同MD5文件进度都设为100%
	/// </summary>
	public void complete(int uid,String idSign)
	{
		String sql = "update up7_files set f_lenSvr=f_lenLoc,f_perSvr='100%',f_complete=1 where f_idSign=? and f_uid=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);
		
		try
		{
			cmd.setString(1,idSign);
			cmd.setInt(2, uid);
		} catch(SQLException e){
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);//在部分环境中测试发现执行后没有效果。f_complete仍然为0
	}

	/// <summary>
	/// 检查相同MD5文件是否有已经上传完的文件
	/// </summary>
	/// <param name="md5"></param>
	public boolean HasCompleteFile(String md5)
	{
		//为空
		if (md5 == null) return false;
		if(md5.isEmpty()) return false;

		String sql = "select f_id from up7_files where f_complete=1 and f_md5=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {
			cmd.setString(1, md5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ret = db.Execute(cmd);

		return ret;
	}

	/// <summary>
	/// 删除一条数据，并不真正删除，只更新删除标识。
	/// </summary>
	/// <param name="f_uid"></param>
	/// <param name="f_id"></param>
	public void Delete(int f_uid,int f_id)
	{
		String sql = "update up7_files set f_deleted=1 where f_uid=? and f_id=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {
			cmd.setInt(1, f_uid);
			cmd.setInt(2, f_id);
			db.ExecuteNonQuery(cmd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void remove(String idSign)
	{
		String sql = "update up7_files set f_deleted=1 where f_idSign=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {			
			cmd.setString(1, idSign);
			db.ExecuteNonQuery(cmd);
		} catch (SQLException e) {
			System.out.println("更新数据库文件信息失败，");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// <summary>
	/// 根据根文件夹ID获取未上传完成的文件列表，并转换成JSON格式。
	/// 说明：
	///		1.此函数会自动对文件路径进行转码
	/// </summary>
	/// <param name="fidRoot"></param>
	/// <returns></returns>
	static public String GetUnCompletes(int fidRoot) throws UnsupportedEncodingException
	{
		StringBuilder sql = new StringBuilder("select ");
		sql.append("f_nameLoc");
		sql.append(",f_pathLoc");
		sql.append(",f_lenLoc");
		sql.append(",f_sizeLoc");
		sql.append(",f_md5");
		sql.append(",f_pidRoot");
		sql.append(",f_pid");
		sql.append(" from up7_files where f_pidRoot=?;");
		ArrayList<FileInf> arrFiles = new ArrayList<FileInf>();

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql.toString());
		try 
		{
			cmd.setInt(1, fidRoot);
			ResultSet r = db.ExecuteDataSet(cmd);
			while (r.next())
			{
				FileInf fi = new FileInf();
				fi.nameLoc = r.getString(0);
				fi.pathLoc = r.getString(1);
				fi.pathLoc = URLEncoder.encode(fi.pathLoc,"UTF-8");
				fi.pathLoc = fi.pathLoc.replace("+", "%20");
				fi.lenLoc = r.getLong(2);
				fi.sizeLoc = r.getString(3);
				fi.md5 = db.GetStringSafe(r.getString(4),"");			
				fi.pidRoot = r.getInt(5);
				fi.pidSvr = r.getInt(6);
				arrFiles.add( fi );
			}
			r.close();
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    JSONArray json = JSONArray.fromObject( arrFiles );
		return json.toString();
	}

    /// <summary>
    /// 获取未上传完的文件列表
    /// </summary>
    /// <param name="fidRoot"></param>
    /// <param name="files"></param>
	static public void GetUnCompletes(int fidRoot,ArrayList<FileInf> files)
	{
		StringBuilder sql = new StringBuilder("select ");
        sql.append("f_id");
        sql.append(",f_nameLoc");
		sql.append(",f_pathLoc");
		sql.append(",f_lenLoc");
		sql.append(",f_sizeLoc");
		sql.append(",f_md5");
		sql.append(",f_pidRoot");
        sql.append(",f_pid");
        sql.append(",f_lenSvr");
        sql.append(",f_pathSvr");//fix(2015-03-18):续传文件时服务器会创建重复文件项信息
		sql.append(" from up7_files where f_pidRoot=? and f_complete=False;");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql.toString());
		try 
		{
			cmd.setInt(1, fidRoot);
			ResultSet r = db.ExecuteDataSet(cmd);
			while (r.next())
			{
				FileInf fi = new FileInf();
	            fi.idSvr = r.getInt(1);
				fi.nameLoc = r.getString(2);
				fi.pathLoc = r.getString(3);
				fi.lenLoc = r.getLong(4);
				fi.sizeLoc = r.getString(5);
				fi.md5 = db.GetStringSafe(r.getString(6),"");
				fi.pidRoot = r.getInt(7);
				fi.pidSvr = r.getInt(8);
	            fi.lenSvr = r.getLong(9);
	            fi.pathSvr = r.getString(10);//fix(2015-03-18):修复续传文件时服务器会创建重复文件信息的问题。
				files.add(fi);
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}