package down3.biz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import down3.model.DnFileInf;

import up7.DbHelper;

/**
 * 从数据表中加载已完成的数据，包含文件和文件夹
 * @author Administrator
 *
 */
public class CompleteReader 
{
	public String all(Integer uid)
	{
		List<DnFileInf> files = new ArrayList<DnFileInf>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(" f_idSign");//1
        sb.append(",f_nameLoc");//2
        sb.append(",f_lenLoc");//3
        sb.append(",f_sizeLoc");//4
        sb.append(",f_fdTask");//5
        sb.append(",f_pathSvr");//6
        //
        sb.append(" from up7_files");
        //
        sb.append(" where f_uid=? and f_complete=1 and f_fdChild=0");

        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sb.toString());
        try {
			cmd.setInt(1, (uid));
        ResultSet r = db.ExecuteDataSet(cmd);

        while (r.next())
        {
        	cmp_file fi = new cmp_file();
        	fi.idSign = r.getString(1);//与up7_files表对应
        	fi.nameLoc = r.getString(2);
        	fi.lenSvr = r.getLong(3);
        	fi.pathSvr = r.getString(6);
        	fi.sizeSvr = r.getString(4);
        	fi.fdTask = r.getBoolean(5);    
        	fi.signSvr = UUID.randomUUID().toString();//服务端生成，唯一标识
        	files.add(fi);
        }
        r.close();
		} catch (SQLException e) {
			System.out.println("加载文件列表错误，从up7_files中读取数据错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	Gson g = new Gson();
    	return g.toJson(files);
	}
}
