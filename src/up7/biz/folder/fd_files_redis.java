package up7.biz.folder;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import up7.model.xdb_files;

/*
 * key：文件夹GUID-files
 * 添加：
 * fd_files_redis fs = new fd_files_redis();
 * fs.idSign = "";//设置文件夹GUID
 * fs.add(file-guid);
 * 
 * 读取：
 * fd_files_redis fs = new fd_files_redis();
 * fs.idSign = "";
 * List<String> ids = fs.all();
 * for(String id : ids)
 * {}
 * */
public class fd_files_redis 
{
	public String idSign;
	Jedis con=null;
	
	public fd_files_redis(Jedis c,String idSign){this.con = c;this.idSign = idSign;}
	
	String getKey()
	{
		String key = idSign+"-files";
		return key;
	}
	
	public void del()
	{
		this.con.del(this.getKey());
	}
	
	public void add(String fSign)
	{	
		this.con.lpush(this.getKey(), fSign);	
	}
	
	public void add(List<xdb_files> fs)
	{
		String key = this.getKey();		
		for(xdb_files f : fs)
		{
			this.con.lpush(key, f.idSign);
		}
	}
	
	public Set<String> all()
	{
		Set<String> ids = this.con.smembers(this.getKey());		
		return ids;		
	}
}
