package com.bn.serverinfo;//声明包语句
			
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
		
public class DBUtil{                                       //创建主类
		public static Connection getConnection(){		   //编写与数据库建立连接的方法
			Connection con = null;                         //声明连接
			try{			
				Class.forName("org.gjt.mm.mysql.Driver");	//声明驱动
		con = DriverManager.getConnection("jdbc:mysql://localhost/orderdish?useUnicode=true&characterEncoding=UTF-8","root","");//得到连接（数据库名，编码形式，数据库用户名，数据库密码）
	}catch(Exception e){
		e.printStackTrace();              //捕获异常
	}
	return con;                           //返回连接
}
	//根据员工的姓名得到id
	public static String getWorkeridByname(String string){	
		Connection con = getConnection();//与数据库建立连接
		Statement st = null;//创建接口
		ResultSet rs = null;//结果集
		String str = null;//字符串常量
		try{		
			st = con.createStatement();//创建一个对象来将SQL语句发送到数据库
			String task = "select w_id from WorkerInfo where w_name='"+string+"';";//编写SQL语句
			rs = st.executeQuery(task);//执行SQL语句
		    rs.next();                 //遍历执行 
		    str=rs.getString(1);       //将查询得到的员工ID放入字符串常量
		}
		catch(Exception e){		      //捕获异常，返回空值
			e.printStackTrace();
			return null;
		}
		finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}//结果集
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;                                 //返回员工ID
	}
	//得到计量单位的信息
	public static List<String[]> getCU(){	
		Connection con = getConnection();//与数据库建立连接
	    Statement st = null;//创建接口
	    ResultSet rs = null;//结果集
		List<String[]>ll=new ArrayList<String[]>();//创建存放数据的列表
		try{ 		 
			st=con.createStatement();//创建一个对象来将SQL语句发送到数据库
			String sql="select unit_id,unit_name,unit_remark from CountUnit where unit_delflg='"+0+"'";//编写SQL语句
			rs=st.executeQuery(sql);//执行SQL语句
			while(rs.next()){		  //循环遍历结果集	
				String[] s=new String[3];
				for(int i=0;i<s.length;i++){				
					s[i]=rs.getString(i+1);     //将得到的信息放入字符串数组
				}
				ll.add(s);                      //将字符串数据添加到列表
			}	
		}catch(Exception e){		
			e.printStackTrace();                 //捕获异常
		}
		finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
     ////得到计量单位的最大编号
	public static String getCUMaxNO(){	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{ 		 
			st=con.createStatement();
			String sql="select max(unit_id) from countunit";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){		    
		    	str=String.valueOf(0);
		    }
		    else{		    
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//更新计量单位
	private static Object UPDATECOUNTUNIT=new Object();
	public static String updateCU(String[] getmsg){ 	
		synchronized(UPDATECOUNTUNIT){		        //方法加锁
			Connection con = getConnection();
			Statement st = null;
			String idnum[]=getCountUnitID();
			try{			
				int i;
				st = con.createStatement();
				for( i=0;i<idnum.length;i++){               //若是已有的计量单位，就更改				
					if(getmsg[1].equals(idnum[i])){					
						String sql = "update countunit set unit_name= '"+getmsg[2]+"',unit_remark= '"+getmsg[3]+"' where unit_id="+getmsg[1];
						st.executeUpdate(sql);
						break;
					}
				}
				if(i==idnum.length){	                //若不是已有的，就插入			
				String sql="insert into countunit(unit_id,unit_name,unit_remark) values('"+getmsg[1]+"','"+getmsg[2]+"','"+getmsg[3]+"')";
				st.executeUpdate(sql);
				}
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}		
	}
	//得到计量单位的编号
	private static String[] getCountUnitID(){ 	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    int count=getCount("countunit");
		String[] ll=new String[count];
		try{		  
			st=con.createStatement();
			String sql="select unit_id from countunit";       
			rs=st.executeQuery(sql);
			int k=0;
			System.out.println(rs.getRow());             //打印编号
			while(rs.next()){				
				System.out.println(rs.getString(1));
				ll[k]=rs.getString(1);
				System.out.println(ll[k]+"k="+k);
				k++;				
			}	
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
	//删除计量单位信息
	private static Object DELCOUNTUNIT=new Object();
	public static String delCU(String id) {	
		synchronized(DELCOUNTUNIT){		             //加锁
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql = "update countunit set unit_delflg='"+1+"' where unit_id="+id;
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{		
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//根据计量单位id得到名称
	public static String getCUnameById(String string) {	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{	
			st = con.createStatement();
			String task = "select unit_name from CountUnit where unit_id='"+string+"';";
			rs = st.executeQuery(task);
		    rs.next();
		    str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}	
	//根据员工id得到名称
	public static String getWorkernameById(String string){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
			st = con.createStatement();
			String task = "select w_name from WorkerInfo where w_id='"+string+"';";
			rs = st.executeQuery(task);
		    rs.next();
		    str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}	
	//根据计量单位名称得到id
	public static String getCUidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
			st = con.createStatement();
			String task = "select unit_id from CountUnit where unit_name='"+string+"';";
			rs = st.executeQuery(task);
		    rs.next();
		    str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}	
	//得到菜系信息
	public static List<String[]> getVegeType(){	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		List<String[]>ll=new ArrayList<String[]>();
		try{		  
			st=con.createStatement();
			String sql="select vt_id,vt_name from vegetype where vt_delflg='"+0+"'";
			rs=st.executeQuery(sql);
			while(rs.next()){			
				String[] s=new String[2];
				for(int i=0;i<s.length;i++){				
					s[i]=rs.getString(i+1);
				}
				ll.add(s);
			}	
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
	//得到菜系的最大编号
	public static String getVTMaxNo(){
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{		  
			st=con.createStatement();
			String sql="select max(vt_id) from vegetype";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){
		    	str=String.valueOf(0);
		    }
		    else{
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//更新菜系信息
	private static Object UPDATEVT=new Object();
	public static String updateVT(String[] str){	
		synchronized(UPDATEVT){		
			Connection con = getConnection();
			Statement st = null;
			String idnum[]=getVegetypeID();
			try{			
				int i;
				st = con.createStatement();
				for( i=0;i<idnum.length;i++){	           //若已有菜系，则更改			
					if(str[1].equals(idnum[i])){					
						String sql = "update vegetype set vt_name= '"+str[2]+"' where vt_id="+str[1];
						st.executeUpdate(sql);
						break;
					}
				}
				if(i==idnum.length){		                 //若无此菜系，则插入		
				String sql="insert into vegetype(vt_id,vt_name) values('"+str[1]+"','"+str[2]+"')";
				st.executeUpdate(sql);
				}
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//得到菜系的编号
	private static String[] getVegetypeID(){	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    int count=getCount("vegetype");
		String[] ll=new String[count];
		try{		  
			st=con.createStatement();
			String sql="select vt_id from vegetype";
			rs=st.executeQuery(sql);
			int k=0;
			while(rs.next()){				
				ll[k]=rs.getString(1);
				k++;
			}	
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
	//删除菜系
	private static Object DELVT=new Object();
	public static String delVT(String id){	
		synchronized(DELVT){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql = "update vegetype set vt_delflg='"+1+"' where vt_id="+id;
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}	
    //获得主类别的信息
    public static List<String[]> getMCG(){   
    	Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select mcate_id,mcate_name from" +
			" MainCategory where MainCategory.mcate_delflag='"+0+"'";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[2];
				for(int i=0;i<str.length;i++){				
					str[i]=rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
   //得到主类别的最大编号
    public static String getMCGMaxNo(){    
    	Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{		  
			st=con.createStatement();
			String sql="select max(mcate_id) from MainCategory";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){		   
		    	str=String.valueOf(0);
		    }
		    else{
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
   //添加主类信息（凉菜，热菜，。。。）
    private static Object ADDMCG=new Object();
	public static String addMCG(String getmsg, String getmsg2) {
		synchronized(ADDMCG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql="insert into MainCategory(mcate_id,mcate_name) values('"+getmsg+"','"+getmsg2+"')";
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}	
	//更新主类的信息
	private static Object UPDATEMCG=new Object();
	public static String updateMCG(String string,String string1) {
		synchronized(UPDATEMCG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql = "update MainCategory set mcate_name='"+string1+"' " +
						"where mcate_id="+string;
				System.out.println(sql);
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//删除主类信息
	private static Object DELMCG=new Object();
	public static String delMCG(String id) {
		synchronized(DELMCG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql="update MainCategory set  mcate_delflag='"+1+"' " +
						"where mcate_id = '"+id+"';";
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//得到菜品类别的信息
	public static List<String[]> getCG() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql="select cate_id,cate_name,cate_createtime," +
					"cate_updatetime,mcate_name from Category,MainCategory " +
					"where cate_delflag='"+0+"' and Category.mcate_id = MainCategory.mcate_id";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[5];
				for(int i=0;i<str.length;i++){
					str[i]=rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//得到菜品类别的最大编号
	public static String getCGMaxNo() {
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{ 		 
			st=con.createStatement();
			String sql="select max(cate_id) from Category";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){
		    	str=String.valueOf(0);
		    }
		    else{
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//根据主类的名称得到主类的id
	public static String getMCGidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str;
		try{		
			st = con.createStatement();
			String task = "select mcate_id from MainCategory where mcate_name='"+string+"'";
			rs = st.executeQuery(task);
            rs.next();
            str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//添加菜品类别
	private static Object ADDCG=new Object();
	public static String addCG(String[] getmsg, String mcgid) {
		synchronized(ADDCG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql="insert into Category(cate_id,cate_name,cate_createtime," +
						"cate_updatetime,mcate_id) values('"+getmsg[1]+"','"+getmsg[2]
						+"','"+getmsg[3]+"','"+getmsg[4]+"','"+mcgid+"')";
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//更新菜品类别信息
	private static Object UPDATECG=new Object();
	public static String updateCG(String[] getmsg, String time, String mcgid) {
		synchronized(UPDATECG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				System.out.println("shifoujinru");
				String sql = "update Category set cate_name='"+getmsg[2]+"'," +
			" cate_createtime='"+getmsg[3]+"',cate_updatetime='"+time+"',mcate_id='"+mcgid+"' where cate_id='"+getmsg[1]+"'";
				System.out.println(sql);
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//删除菜品类别
	private static Object DELCG=new Object();
	public static String delCG(String id) {
		synchronized(DELCG){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				System.out.println(id);
				st = con.createStatement();
				String sql="update Category set  cate_delflag='"+1+"' where cate_id = '"+id+"';";
				System.out.println(sql);
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//得到菜品信息
	public static List<String[]> getVege() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			//菜品信息包括：菜品ID，名称，价格，计量单位名称，类别名称，主类名，菜系名，规格名，菜品介绍
			String sql="select distinct vege_id,vege_name,vege_vegeprice," +
			"unit_name,cate_name,mcate_name,vt_name,vs_name,vege_intro " +
			"from vege,countunit,category,vegetype,MainCategory,VegeStandard " +
			"where  vege_useflg='"+1+"' and vege.unit_id =  countunit.unit_id and " +
			"vege.cate_id = category.cate_id  and category.mcate_id = maincategory.mcate_id and vege.vt_id = vegetype.vt_id  " +
			" and vege.vs_id = VegeStandard.vs_id ";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[9];
				for(int i=0;i<str.length;i++){
					str[i]=rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//得到菜品的最大编号
	public static String getVegeMaxNO() {
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{		  
			st=con.createStatement();
			String sql="select max(vege_id) from Vege";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){
		    	str=String.valueOf(0);
		    }
		    else{
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//得到规格的信息
	public static List<String[]> getVS() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql="select * from VegeStandard";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				System.out.println("sql");
				String[] str = new String[2];
				for(int i=0;i<str.length;i++){				
					str[i]=rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//得到规格的最大编号
	public static String getVSMaxNO(){	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{ 		 
			st=con.createStatement();
			String sql="select max(vs_id ) from VegeStandard";
			rs=st.executeQuery(sql);
		    rs.next();
		    if(rs.getString(1)==null){
		    	str=String.valueOf(0);
		    }
		    else{
		    	str=rs.getString(1);
		    }
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//更新规格信息
	private static Object UPDATEVS=new Object();
	public static String updateVS(String[] getmsg) {
		synchronized(UPDATEVS){		
			Connection con = getConnection();
			Statement st = null;
			String idnum[]=getVSID();
			try{			
				int i;
				st = con.createStatement();
				for( i=0;i<idnum.length;i++){
					if(getmsg[1].equals(idnum[i])){
						String sql = "update VegeStandard set vs_name= '"+getmsg[2]+"' where vs_id ="+getmsg[1];
						st.executeUpdate(sql);
						break;
					}
				}
				if(i==idnum.length){
				String sql="insert into VegeStandard values('"+getmsg[1]+"','"+getmsg[2]+"')";
				st.executeUpdate(sql);
				}
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//得到规格的所有编号
	private static String[] getVSID() {
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    int count=getCount("VegeStandard");
	    
		String[] ll=new String[count];
		try{		  
			st=con.createStatement();
			String sql="select vs_id from VegeStandard";
			rs=st.executeQuery(sql);
			int k=0;
			while(rs.next()){				
				ll[k]=rs.getString(1);
				k++;
			}	
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
	//删除规格信息
	private static Object DELVS=new Object();
	public static String delVS(String id) {
		synchronized(DELVS){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql = "delete from VegeStandard where vs_id="+id;
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//根据子类被的名称得到主类别的名称
	public static String getMcgnameByCgname(String getmsg) {
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		String str=new String();
		try{ 		 
			st=con.createStatement();
			String sql="select mcate_name from category,maincategory where cate_name = '"+getmsg+"' " +
					"and category.mcate_id = maincategory.mcate_id";
			rs=st.executeQuery(sql);
		    rs.next();
		    	str=rs.getString(1);
		    	System.out.println(str);
		}
		catch(Exception e){		
			e.printStackTrace();
		}finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//根据规格的名称得到规格的id
	public static String getVSidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str;
		try{		
			st = con.createStatement();
			String task = "select vs_id from VegeStandard where vs_name='"+string+"'";
			rs = st.executeQuery(task);
            rs.next();
            str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//根据类别的名称得到类别的id
	public static String getCGidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str;
		try{		
			st = con.createStatement();
			String task = "select cate_id from category where cate_name='"+string+"'";
			rs = st.executeQuery(task);
            rs.next();
            str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//根据系别的名称得到系别的id
	public static String getVTidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str;
		try{		
			st = con.createStatement();
			String task = "select vt_id from vegetype where vt_name='"+string+"'";
			rs = st.executeQuery(task);
            rs.next();
            str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//根据餐厅的名称得到餐厅的id
	public static String getRoomidByname(String string) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str;
		try{		
			st = con.createStatement();
			String task = "select room_id from Room where room_name='"+string+"'";
			rs = st.executeQuery(task);
            rs.next();
            str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//添加菜品信息
    private static Object ADDVEGE=new Object();
	public static String addVege(String[] getmsg, String cuid,
			 String cgid, String vtid, String ggid) {
		 synchronized(ADDVEGE){
		
 			Connection con = getConnection();
 			Statement st = null;
 			try{
 				st = con.createStatement();
 				String sql = "insert into vege(vege_id,vege_name,vege_vegeprice," +
 				"unit_id,cate_id,vt_id,vs_id,vege_intro)" +
 				" values('"+getmsg[1]+"','"+getmsg[2]+"',"+getmsg[3]+"," +
 				"'"+cuid+"','"+cgid+"','"+vtid+"','"+ggid+"','"+getmsg[8]+"')";
 				System.out.println(sql);
 				st.executeUpdate(sql);
 			}catch(Exception e){			
 				e.printStackTrace() ;
 				return null;
 			}finally{ 			
 				try{st.close();}catch(Exception e) {e.printStackTrace();}
 				try{con.close();}catch(Exception e) {e.printStackTrace();}
 			}
 			return "ok";
 		}
	}
	//删除菜品信息
	private static Object DELVEGE=new Object();
	public static String delVege(String string) {
		synchronized(DELVEGE){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql = "update vege set  vege_useflg='"+0+"' where vege_id="+string;
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//更新菜品信息
	private static Object UPDATEVEGE=new Object();
	public static String updateVege(String[] getmsg, String cuid,
			String cgid, String vtid,  String ggid) {
		synchronized(UPDATEVEGE){		
			Connection con = getConnection();
			Statement st = null;
			String idnum[]=getVegeID();
			try{			
				int i;
				st = con.createStatement();
				for( i=0;i<idnum.length;i++){
					if(getmsg[1].equals(idnum[i])){
				String sql="update vege set vege_name='"+getmsg[2]+"' , vege_vegeprice='"+getmsg[3]+"' , " +
				" unit_id='"+cuid+"' , cate_id='"+cgid+"' , vt_id='"+vtid+"' ," +
						" vs_id='"+ggid+"' , vege_intro='"+getmsg[8]+"' where vege_id = "+getmsg[1];
				System.out.println(sql);
				st.executeUpdate(sql);
				break;
					}
				}
				System.out.println("sucess.....");
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	 //得到菜品所有的编号
	private static String[] getVegeID(){
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    int count=getCount("vege");
			String[] ll=new String[count];
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege";
				rs=st.executeQuery(sql);
				int k=0;
				while(rs.next()){					
					ll[k]=rs.getString(1);
					k++;
				}	
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return ll;
	}
	//根据菜品编号查询大于编号的菜品
	public static List<String> sVegemid(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege where vege_id >= '"+string+"' and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品编号查询小于编号的菜品
	public static List<String> sVegelid(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege where vege_id <= '"+string+"' and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品名称查询菜品
	public static List<String> sVegebyname(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{ 			 
				st=con.createStatement();
				String sql="select vege_id from vege where vege_name='"+string+"' and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品类别查询菜品
	public static List<String> sVegebycg(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{ 			 
				st=con.createStatement();
				String sql="select vege_id from vege,category where cate_name='"+string+"'" +
						" and category.cate_id = vege.cate_id and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品主类查询菜品
	public static List<String> sVegebymcg(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege,maincategory,category where mcate_name='"+string+"' " +
			   "and maincategory.mcate_id = category.mcate_id and category.cate_id = vege.cate_id and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品系别查询菜品
	public static List<String> sVegebyvt(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{ 			 
				st=con.createStatement();
				String sql="select vege_id from vege,vegetype where vegetype.vt_name='"
				+string+"' and vegetype.vt_id = vege.vt_id and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品价格查询大于价格的菜品
	public static List<String> sVegemprice(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege where vege_vegeprice >= " +
						"'"+string+"' and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品价格查询小于菜品的价格
	public static List<String> sVegelprice(String string) {
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege where vege_vegeprice <= '"+string+"' and vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//得到所有菜品的编号
	public static List<String> getVegeid(){	
		 Connection con = getConnection();
		    Statement st = null;
		    ResultSet rs = null;
		    List<String> list=new ArrayList<String>();
			try{			  
				st=con.createStatement();
				String sql="select vege_id from vege where vege_useflg='"+1+"'";
				rs=st.executeQuery(sql);
				while(rs.next()){
					list.add(rs.getString(1));
				}
			}catch(Exception e){			
				e.printStackTrace();
			}
			finally{
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
	}
	//根据菜品id得到菜品的信息
	public static List<String[]> getVegeByid(String[] msgvege){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			for(int k=0;k<msgvege.length;k++){
			String sql="select vege_id" +"vege_name,vege_vegeprice," +
					"vege_intro,unit_name,cate_name,mcate_name,vt_name,vs_name " +
					"from vege,countunit,category,vegetype,MainCategory,VegeStandard" +
					" where  vege_useflg='"+1+"' and vege.vege_id='"+msgvege[k]+"' " +
							"and vege.unit_id =  countunit.unit_id and " +
					"vege.cate_id = category.cate_id  and category.mcate_id = " +
					"maincategory.mcate_id and vege.vt_id = vegetype.vt_id " +
					" and vege.vs_id = VegeStandard.vs_id ";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[18];
				for(int i=0;i<str.length;i++){
					str[i]=rs.getString(i+1);
				}
				list.add(str);
			}
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//获取所有的餐厅信息
	public static List<String[]> getRoom(){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select room_no,room_name from Room where room_delflg='0'";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[3];
			    str[0]=rs.getString(1);
			    str[1]=rs.getString(2);			    
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//获取所有的餐厅名称
		public static List<String[]> getRoomName(){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			List<String[]> list = new ArrayList<String[]>();
			try{			
				st = con.createStatement();
				String task = "select room_name from Room where room_delflg='0'";
				rs = st.executeQuery(task);
				while(rs.next()){				
					String[] str = new String[1];
					str[0]=rs.getString(1);
					list.add(str);
				}
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
		}//删除餐厅
		private static Object DELROOM_LOCK=new Object();
		public static String delRoom(String room_id){		
			synchronized(DELROOM_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{ 				
					String curtime=TypeExchangeUtil.getCurTime();
					st=con.createStatement();
					String sql="update room set room_delflg='1',room_lastedittime=" +
							"'"+curtime+"'"+" where room_id='"+room_id+"'";
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}		
		//根据餐厅名称查id
		public static String getRidbyname(String roomname) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String rid=null;
			try{			
				st = con.createStatement();
				String sql="select room_id from room where room_name='"+roomname+"'";
				rs=st.executeQuery(sql);
				rs.next();
				 rid=rs.getString(1);
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return rid;
		}
		//获取餐厅id的最大值
		public static String getMaxRoomId(){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String maxnum=null;
			try{			
				st=con.createStatement();
				String sql="select max(room_id) from room ";
				rs=st.executeQuery(sql);
			    rs.next();
			    if(rs.getString(1)==null){
			    	maxnum=String.valueOf(0);
			    }
			    else{
			    	maxnum=rs.getString(1);
			    }
			    System.out.println(maxnum);
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return maxnum;
		}
       //添加餐厅		
		private static Object ADDROOM_LOCK=new Object();
		public static String addRoom(String rid,String rname,String rkeywd){		
			synchronized(ADDROOM_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{				 
					st=con.createStatement();
					String curtime=(TypeExchangeUtil.getCurTime());
				String sql="insert into room(room_id,room_no,room_name,room_keywd," +
						"room_createtime,room_lastedittime) values ('"+rid+"'," +
						"'"+rid+"','"+rname+"','"+rkeywd+"','"+curtime+"','"+curtime+"')";							
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}	
         //更新餐厅
		private static Object UPDATEROOM_LOCK=new Object();
		public static String updateRoom(String rid,String rname,String rkeywd){	
			synchronized(UPDATEROOM_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{ 				
					st=con.createStatement();
					String curtime=(TypeExchangeUtil.getCurTime());								
					String sql="update  room set room_no='"+rid+"',room_name='"+rname+"'," +
					"room_keywd='"+rkeywd+"',room_lastedittime='"+curtime+"'where room_id='"+rid+"'";		
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}	
		//根据餐厅查询餐台
				public static List<String[]> getPointByroom(String roomname){ 				
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					List<String[]> list = new ArrayList<String[]>();
					try{					
						st=con.createStatement();
						String roomid=getRidbyname(roomname);
						String task = "select point_id,point_name,ptype_id,point_stopflg ," +
								"point_num,point_state from Point where room_id='"+roomid+"'" +
										"and point_delflg='0'";
						rs = st.executeQuery(task);
						while(rs.next()){						
							String[] str = new String[7];
							str[0]=rs.getString(1);                              //餐台ID
							str[1]=rs.getString(2);                              //餐台名称
							str[2]=rs.getString(3);                              
							str[2]=getPtypename(str[2]);                         //餐台类型
							str[3]=rs.getString(4).equals("0")?"正常":"暂停使用";    //停用标记：是否可用
							str[4]=rs.getInt(5)+"";                              //餐台人数
							str[5]=rs.getString(6).equals("0")?"无人":"有人";       //餐台状态：是否有人
							str[6]=roomname;                                     //餐厅名称
							list.add(str);
						}
					}catch(Exception e){					
						e.printStackTrace();
						return null;
					}finally{					
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return list;
		}
		//根据餐厅类型id查询名称
		public static String getPtypename(String typeid) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String ptypename=null;
			try{			
				st = con.createStatement();
				String sql="select ptype_name from pointtype where ptype_id='"+typeid+"'";
				rs=st.executeQuery(sql);
				rs.next();
				ptypename=rs.getString(1);
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return ptypename;
		}
		//获取餐台id的最大值
		public static String getMaxPointId() {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String maxnum=null;
			try{			
				st = con.createStatement();
				st=con.createStatement();
				String sql="select max(point_id) from point";
				rs=st.executeQuery(sql);
			    rs.next();
			    if(rs.getString(1)==null){
			    	maxnum=String.valueOf(0);
			    }
			    else{
			    	maxnum=rs.getString(1);
			    }
				
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return maxnum;
		}
		//获取所有的餐厅类型的名称
	public static List<String[]> getRoomTypeName() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select ptype_name from pointtype";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[1];
				str[0]=rs.getString(1);
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
		//获取所有的餐厅类型的名称
		public static String getPTypeNameByid (String ptname){
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String ptid=null;
			try{			
				st = con.createStatement();
				String task = "select ptype_id from pointtype where ptype_name='"+ptname+"'";
				rs = st.executeQuery(task);
				rs.next();
				ptid=rs.getString(1);
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return ptid;
		}
	    //添加餐台				
		private static Object ADDPOINT_LOCK=new Object();
		public static String addPoint(String pid,String pname,int pnum,String pkeywd,String ptype,String proom){		
			synchronized(ADDPOINT_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{ 				
					ptype=getPTypeNameByid(ptype);                   //获取所有的餐厅类型的名称
					proom=getRidbyname(proom);                       //根据餐厅名称查id
					st=con.createStatement();
					String sql="insert into point(point_id,point_no,point_name,ptype_id,room_id," +
							"point_num) values ('"+pid+"','"+pid+"','"+pname+"','"+ptype+"','"+proom+"',"
							+pnum+")";
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}
        //删除餐台
		private static Object DELPOINT_LOCK=new Object();
		public static String delPoint(String point_id){		
			synchronized(DELPOINT_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{				
					st=con.createStatement();
					String sql="update  point set point_delflg='1' where point_id='"+point_id+"'";
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}
		//更新餐台信息
		private static Object UPDATEPOINT_LOCK=new Object();
		public static String updatePoint(String pid,String pname,int num,String pkey,String ptype,String istop,String proom){		
			synchronized(UPDATEPOINT_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				try{				 
					st=con.createStatement();

					ptype=getPTypeNameByid(ptype);                     //获取所有的餐厅类型的名称
					proom=getRidbyname(proom);                        //根据餐厅名称查id
					istop=istop.equals("正常")?"0":"1";                 //根据停用标记判断是否停用
					String sql="update  point set point_no='"+pid+"',point_name='"+pname+"'," +
							"point_num="+num+",ptype_id='"+ptype+"',point_stopflg= '"+istop+"'," +
									"room_id='"+proom+"' where point_id='"+pid+"'";
					System.out.println(sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}			
		}
			//更新餐台信息
			private static Object UPDATEPOS_LOCK=new Object();
			public static String updatePos(String pid,String pname,String pbz,String proom){			
				synchronized(UPDATEPOS_LOCK){			
					Connection con = getConnection();
					Statement st = null;
					try{ 					
						st=con.createStatement();
						String updatetime=TypeExchangeUtil.getCurTime();
						proom=getRidbyname(proom);                            //根据餐厅名称查id
						pbz=pbz.equals("暂无")?"":pbz;                         
						String sql="update  pos set pos_name='"+pname+"',pos_bz='"+pbz+
						"',room_id='"+proom+"',pos_lastedittime='"+updatetime+"' where pos_id='"+pid+"'";
						System.out.println(sql);
						st.executeUpdate(sql);
					}catch(Exception e){					
						e.printStackTrace() ;
						return null;
					}finally{					
						try{st.close();}catch(Exception e) {e.printStackTrace();}
						try{con.close();}catch(Exception e) {e.printStackTrace();}
					}
					return "ok";
				}				
			}		
			//查询今日的所有结账的订单信息
			public static List<String[]> getAlAccOrderInfo() {
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						List<String[]> list=new ArrayList<String[]>();
						try{						
							String time[]=TypeExchangeUtil.getCurTime().split(" ");
							st = con.createStatement();
							String sql="select point.point_name,o_id,o_guestnum,o_ODTime,o_workerID,o_totalPri" +
									" from orderinfo,point,settleaccounts " +
									"where orderinfo.point_id=point.point_id " +
											" and orderinfo.o_id =settleaccounts.sa_OrderId " +
											" and sa_createtime like '"+time[0]+"%' order by sa_createtime";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							while(rs.next()){
								String str[]=new String[7];
								str[0]=rs.getString(1);                       //餐厅名称
								str[1]=rs.getString(2);                       //订单号
								str[2]=rs.getInt(3)+"";                       //顾客人数
								str[3]=rs.getString(4);                       //订单时间
								str[4]=rs.getString(5);                       //服务员ID
								str[5]=rs.getFloat(6)+"";                     
								str[6]=rs.getFloat(7)+"";                     //总价格
								list.add(str);
							}
							System.out.println(list.size()+list.get(0)[0]);
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
					}
			//根据查询未结账的订单信息
			public static List<String[]> getOrderInfo(){ 			
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						List<String[]> list=new ArrayList<String[]>();
						try{						
							st = con.createStatement();
							String sql="select point_name, o_id,o_guestnum,o_ODTime,o_workerID,o_totalPri" +
									" from point,orderinfo" +
									" where  orderinfo.point_id=point.point_id " +
									"and  point_state='1' and o_SAFlg='0' and o_id not in(select sa_OrderId from settleaccounts)";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							while(rs.next()){
								String str[]=new String[6];
								str[0]=rs.getString(1);                     //餐台名称
								str[1]=rs.getString(2);                     //订单号
								str[2]=rs.getInt(3)+"";                     //顾客人数
								str[3]=rs.getString(4);                     //订单时间
								str[4]=rs.getString(5);                     //服务员ID
								str[5]=rs.getFloat(6)+"";                   //总价格
								list.add(str);
							}
							System.out.println(list.size());
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
					}
			//根据餐台id查询出已经结账的订单信息
			public static List<String[]> SOrderInfoByPName(String pname){ 			
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						List<String[]> list=new ArrayList<String[]>();
						try{						
							String str[]=new String[6];
							st = con.createStatement();
							String sql="select distinct point.point_name,o_id,o_guestnum,o_ODTime,o_workerID,o_totalPri" +
									" from orderinfo,point " +
									" where orderinfo.point_id=point.point_id and point_name='"+pname+"'";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							while(rs.next()){							
								str[0]=pname;
								str[1]=rs.getString(2);
								str[2]=rs.getInt(3)+"";
								str[3]=rs.getString(4);
								str[4]=rs.getString(5);
								str[5]=rs.getFloat(6)+"";
								list.add(str);
							}
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
			}
			//根据餐台名称查询未结账的订单信息(用于结账)
			public static List<String[]> SAccountInfoByPName(String pname){ 			
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						List<String[]> list=new ArrayList<String[]>();
						try{
							String str[]=new String[5];
							st = con.createStatement();
							String sql="select point.point_id,o_id,o_guestnum,o_workerID,o_totalPri" +
									" from orderinfo,point " +
									"where orderinfo.point_id=point.point_id and point_name='"+pname+"' and o_SAFlg='0' ";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							if(rs.next()){							
								str[0]=pname;                         //餐台名称
								str[1]=rs.getString(2);               //订单ID
								str[2]=rs.getInt(3)+"";               //餐台人数
								str[3]=rs.getString(4);               //服务员ID
								str[4]=rs.getFloat(5)+"";             //总价格
							}
							list.add(str);
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
			}
			//根据餐台id详细订单信息
			 //根据餐台名称查询出结账或者未结账的（人还未离开的）订单详细信息
		  public static List<String[]> SOrderXxByPid(String pname){			    
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						String sql=null;
						List<String[]> list=new ArrayList<String[]>();
						try{						
							String pid=searchPointIdByName(pname);
							st = con.createStatement();
							sql="select o_id from orderinfo where o_SAFlg='0' and point_id='"+pid+"'";
							rs=st.executeQuery(sql);
							rs.next();
							String oid=rs.getString(1);
							sql="select vege_name,odc_vegeQua,odc_vegePri,odc_remark" +
									" from orderdishconfirm ,vege " +
									"where orderdishconfirm.odc_vege_id=vege.vege_id and odc_orderID='"+oid+"' and odc_delFlg='0'";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							while(rs.next()){
								String str[]=new String[5];
								str[0]=rs.getString(1);
								str[1]=pname;
								str[2]=rs.getInt(2)+"";
								str[3]=rs.getFloat(3)+"";	
								System.out.println(str[4]+"ddd");
								System.out.println(str[4]);
								str[4]=(rs.getString(4)+"").equals("null")?"暂无":(rs.getString(4)+"");
								list.add(str);								
							}
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
				}
//==================标准套餐内容=========
   //根据规格id查询名称
     public static String getStname(String id) {
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						String list=null;
						try{						
							st = con.createStatement();							
							String sql="select vs_name from vegestandard where vs_id ='"+id+"'";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							rs.next();
							list=rs.getString(1);						
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return list;
					}     
 	//根据菜品id得到名成和规格
     public static String[] getVegeNameStd(String id) {
						Connection con = getConnection();
						Statement st = null;
						ResultSet rs = null;
						String[] str=new String[2];
						try{						
							st = con.createStatement();							
							String sql="select vege_name,vs_id from vege where vege_id ='"+id+"'";
							System.out.println(sql);
							rs=st.executeQuery(sql);
							while(rs.next()){								
								str[0]=rs.getString(1);
								str[1]=rs.getString(2);
								str[1]=getStname(str[1]);								
							}
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{						
							try {rs.close();} catch (SQLException e) {e.printStackTrace();}
							try {st.close();} catch (SQLException e) {e.printStackTrace();}
							try {con.close();} catch (SQLException e) {e.printStackTrace();}
						}
						return str;
					}
  //获得每个表的长度
 public static int getCount(String tname){ 
	 Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		int num=0;		
		try{ 		 
			st=con.createStatement();
			String sql="select count(*) from "+tname;
			rs=st.executeQuery(sql);
			rs.next();
			num=rs.getInt(1);
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return num;
 }
//---------------------------------------------lzk----begin(PC端)
	   //员工表workerInfo-------------------------------begin
			//获取员工信息
			public static List<String[]> getWorkerInfo(){				
				Connection con = getConnection();
				Statement st = null;
				ResultSet rs = null;
				List<String[]> list = new ArrayList<String[]>();
				try{
					st = con.createStatement();
					String sql = "select w_id,w_name,w_password," +
							"w_sex, " +							
							"role_id from workerinfo where w_delflg='0'";
					rs = st.executeQuery(sql);
					while(rs.next()){    //如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
						String str[] = new String[5];  
						for(int i=0; i<str.length; i++){
							str[i] = rs.getString(i+1);
						}
						list.add(str);
					}
					System.out.println("list.size="+list.size());   //					
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}finally{		 		
		 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
		 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		 		}
				return list;
			}
			//根据员工id,查询某一个员工的信息
			public static String[] getOneOfWorkerInfo(String w_id){				
				Connection con = getConnection();
				Statement st = null;
				ResultSet rs = null;
				String[] resultArr = new String[5];
				try{
					st = con.createStatement();
					String sql = "select w_id,w_name,w_password," +
							"w_sex, " +
							"role_id  from workerinfo where  w_id='"+w_id+"'";
					rs = st.executeQuery(sql);
					if(rs.next()){ //如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false					   
						for(int i=0; i<resultArr.length; i++){						
							resultArr[i] = rs.getString(i+1);
						}
					}					
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}finally{		 		
		 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
		 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		 		}
				return resultArr;
			}
			public static Object DEL_WORKERINFO = new Object();
				public static String delWorkerInfo(String id){					
					synchronized(DEL_WORKERINFO){       //更新操作，需要同步						
						Connection con = getConnection();
						Statement st = null;
						try{						
							st=con.createStatement();
							String sql="update workerinfo set w_delflg='1' where w_id='"+id+"'";
							st.executeUpdate(sql);
							
						}catch(Exception e){						
							e.printStackTrace();
							return null;
						}finally{
							try{ st.close(); }catch(Exception e){e.printStackTrace();}
							try{ con.close();}catch(Exception e){e.printStackTrace();}
						}
						return "ok";
					}					
			}
				private static Object UPDATE_WORKERINFO = new Object();
		 		public static String updateWorkerInfo(String param[]){		 		
		 			synchronized(UPDATE_WORKERINFO){		 			
		 				Connection con = getConnection();
		 				Statement st = null;
		 				try{		 				
		 					st = con.createStatement();
		 					String sql = "update workerinfo set w_name= '"+param[2]+"', " +
		 							"w_password= '"+param[3]+"',w_sex= '"+param[4]+"',"+
		 				            " role_id= '"+param[5]+"' "+
		 							"where w_id='"+param[1]+"'";
		 					System.out.println("sql="+sql);
		 					st.executeUpdate(sql);
		 				}catch(Exception e){		 				
		 					e.printStackTrace() ;
		 					return null;
		 				}finally{		 				
		 					try{st.close();}catch(Exception e) {e.printStackTrace();}
		 					try{con.close();}catch(Exception e) {e.printStackTrace();}
		 				}
		 				return "ok";
		 			}		 			
		 		}
		 		//获取WorkerInfo表中的最大编号
		 		public static String getWorkerMaxNo(){		 			
		 			String maxNo = null;
		 			Connection con = getConnection();
		 			Statement st = null;
		 			ResultSet rs = null;
		 			try{
		 				st = con.createStatement();
		 				String sql = "select max(w_id) from workerInfo";
		 				rs = st.executeQuery(sql);
		 				rs.next();
		 				maxNo = rs.getString(1);
		 				if(maxNo==null){  
		 					maxNo = "0";
		 				}
		 				System.out.println("maxNO="+maxNo);
		 			}catch(Exception e){
		 				e.printStackTrace();
		 				return null;
		 			}finally{
		 				try{ rs.close(); }catch(Exception e){ e.printStackTrace();}
		 				try{ st.close(); }catch(Exception e){ e.printStackTrace();}
		 				try{ con.close(); }catch(Exception e){ e.printStackTrace();}
		 			}
		 			return maxNo;
		 		}		 		
		 		private static Object ADD_WORKERINFO = new Object();
		 		public static String addWorkerInfo(String param[]){		 		
		 			synchronized(ADD_WORKERINFO){		 			
		 				Connection con = getConnection();
		 				Statement st = null;
		 				try{		 				
		 					st = con.createStatement();
		 					String sql="insert into workerinfo(w_id,w_name,w_password," +
		 							"w_sex," +"role_id) " +
		 							"values('"+param[1]+"','"+param[2]+"','"+param[3]+"','"+param[4]+"', '"+param[5]+"' )";
		 					st.executeUpdate(sql);		 				
		 				}catch(Exception e){		 				
		 					e.printStackTrace() ;
		 					return null;
		 				}finally{		 				
		 					try{st.close();}catch(Exception e) {e.printStackTrace();}
		 					try{con.close();}catch(Exception e) {e.printStackTrace();}
		 				}
		 				return "ok";
		 			}		 			
		 		}
			//员工表WorkerInfo-------------------------------------------end
			//角色表Role-------------------------------------begin
				public static List<String[]> getRole(){					
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					List<String[]> list = new ArrayList<String[]>();
					try{
						st = con.createStatement();
						String sql = "select role_id,role_name from role";
						rs = st.executeQuery(sql);
						while(rs.next()){    //如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
							String str[] = new String[2];
							str[0] = rs.getString(1);
							str[1] = rs.getString(2);
							list.add(str);
						}
						System.out.println("list.size="+list.size());   //						
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}finally{			 		
			 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
			 		}
					return list;
				}
				public static String[] getRoleID(){					
					Connection con = getConnection();
				    Statement st = null;
				    ResultSet rs = null;
					String[] ll=new String[100];
					try{					  
						st=con.createStatement();
						String sql="select role_id from role";
						rs=st.executeQuery(sql);
						int k=0;
						while(rs.next()){							
							ll[k]=rs.getString(1);
							k++;
						}	
						for(int i=0;i<ll.length;i++){
							System.out.println("ll["+i+"]="+ll[i]);
						}
					}catch(Exception e){					
						e.printStackTrace();
					}
					finally{
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return ll;					
				}
				//角色表Role-------------------------------------end				
				//权限表Authority-------------------------------begin
				public static List<String[]> getAuthority(){					
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					List<String[]> list = new ArrayList<String[]>();
					try{
						st = con.createStatement();
						String sql = "select authority_id,authority_name from authority";
						rs = st.executeQuery(sql);
						while(rs.next()){    //如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
							String str[] = new String[2];
							str[0] = rs.getString(1);
							str[1] = rs.getString(2);
							list.add(str);
						}
						System.out.println("list.size="+list.size());  
						
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}finally{			 		
			 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
			 		}
					return list;
				}
				public static String[] getAuthorityID(){					
					Connection con = getConnection();
				    Statement st = null;
				    ResultSet rs = null;
					String[] ll=new String[100];
					try{					  
						st=con.createStatement();
						String sql="select authority_id from authority";
						rs=st.executeQuery(sql);
						int k=0;
						while(rs.next()){							
							ll[k]=rs.getString(1);
							k++;
						}	
						for(int i=0;i<ll.length;i++){
							System.out.println("ll["+i+"]="+ll[i]);
						}
					}catch(Exception e){					
						e.printStackTrace();
					}
					finally{
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return ll;					
				}
				//权限表Authority-------------------------------end
				//权限分配表authority_role--------------------------------------begin
				public static List<String[]> getAuthorityRole(){					
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					List<String[]> list = new ArrayList<String[]>();
					try{
						st = con.createStatement();
						String sql = "select role_id,authority_id from authority_role";
						rs = st.executeQuery(sql);
						while(rs.next()){    //如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
							String str[] = new String[2];
							str[0] = rs.getString(1);
							str[1] = rs.getString(2);
							list.add(str);
						}
						System.out.println("list.size="+list.size());   //						
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}finally{			 		
			 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
			 		}
					return list;
				}			
				public static boolean isAuthorityRoleID(String authority_id,String role_id){					
					Connection con = getConnection();
				    Statement st = null;
				    ResultSet rs = null;
					try{					  
						st=con.createStatement();
						String sql="select role_id,authority_id from authority_role " +
								"where authority_id='"+authority_id+"' and role_id='"+role_id+"'";
						rs=st.executeQuery(sql);
						if(rs.next()!=false){
							return true;
						}else{
							return false;
						}
					}catch(Exception e){					
						e.printStackTrace();
					}
					finally{
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return false;					
				}
				//根据餐台名称详细订单信息
public static List<String[]> SOrderACountByPid(String pname){
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql=null;
		List<String[]> list=new ArrayList<String[]>();
		try{		
			String pid=searchPointIdByName(pname);
			st = con.createStatement();
			sql="select o_id from orderinfo where o_SAFlg='0' and point_id='"+pid+"'";
			rs=st.executeQuery(sql);
			rs.next();
			String oid=rs.getString(1);
			sql="select vege_name,odc_vegeQua,odc_vegePri,unit_name,vs_name " +
					" from orderdishconfirm,vege,countunit,vegestandard " +
					"where odc_orderID='"+oid+"' and vege.unit_id=countunit.unit_id" +
					" and orderdishconfirm.odc_vege_id=vege.vege_id and vege.vs_id=vegestandard.vs_id ";
			System.out.println(sql);
			rs=st.executeQuery(sql);
			while(rs.next()){			
				String str[]=new String[4];
				str[0]=rs.getString(1);
				str[1]=rs.getInt(2)+"";
				str[2]=rs.getFloat(3)+"/"+rs.getString(4);
				str[3]=rs.getString(5);
				list.add(str);				
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
}
//把账单存入数据库（把桌子状态置0，并台表中置1不可用）
//更新结算方式（插入）
private static Object INSERTACCOUT=new Object();
public static String insertAccount(String str[]){
/*(0：订单号，1.桌名称2.应收费用3.打折后的费用4.打折率5.实收费用6.找回钱数7.赠券金额8.结算方式名称9.打折名称10.赠券人11抹零的钱数12，是否开发票标记 13.操作人,14.零结账标记
 * 新：0：订单号，1.桌名，2.应收，3.实收，4.找回，5.发票，6.操作人，7.零结账
	String sa_giveBillflg,String orderid,String deskname,String allmoney,
	String dmoney, String givemoney,String backmoney,String sa_name,String disc_name,
	String zqMoney,String ticketScale,String createw_id
) */
		synchronized(INSERTACCOUT){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs=null;
			String sql=null;
		    String maxid="0";
			try{			  
				st = con.createStatement();
				//得到当前时间
				String curtime=TypeExchangeUtil.getCurTime();
				//获取结账最大值
				sql="select max(sa_streamID) from settleaccounts";
				rs=st.executeQuery(sql);
				rs.next();
				if(rs.getString(1)!=null){				
					maxid=rs.getString(1);
				}
				maxid=(Integer.parseInt(maxid)+1)<10?"00"+(Integer.parseInt(maxid)+1):(Integer.parseInt(maxid)+1)<100?"0"+(Integer.parseInt(maxid)+1):(Integer.parseInt(maxid)+1)+"";
				System.out.println();
				//得到折扣id
				float totalfee=Float.parseFloat(str[3]);//所有的钱
				//插入结账记录
				 sql="insert into settleaccounts(sa_streamID,sa_OrderId,sa_createtime,createw_id," +
						"sa_requestReceive,sa_factReceive,sa_ZLMoney," +"sa_giveBillFlg)" +
						"values('"+maxid+"','"+str[1]+"','"+curtime+"','"+str[6]+"','"+
								+totalfee+"',"+Float.parseFloat(str[4])+","+Float.parseFloat(str[5])+",'"+str[7]+"')";
				 System.out.println(sql);
				 st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{rs.close();}catch(Exception e){e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
//=============================后厨端================
//根据菜品名称获取id
public static String getVegeIdByName(String vegename) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			String str=null;
			try{			
				st = con.createStatement();
				String sql="select vege_id from vege where vege_name='"+vegename+"'";
			    System.out.println(sql);
			    rs=st.executeQuery(sql);
		        rs.next();
		        str=rs.getString(1);	
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	} 
//获取菜品部分信息
public static List<String[]> getVegeSomeInfo(){
Connection con = getConnection();
Statement st = null;
ResultSet rs = null;
List<String[]> list = new ArrayList<String[]>();
try{
	st = con.createStatement();
	String task = "select vege_id,vege_name,vs_id,vege_vegeprice from vege where vege_useflg='1'";
rs = st.executeQuery(task);
while(rs.next()){
	String[] str = new String[4];
    str[0]=rs.getString(1);
    str[1]=rs.getString(2);
    str[2]=rs.getString(3);
    str[2]=getStname(str[2]);
    str[3]=""+rs.getFloat(4);
		list.add(str);
	}
}catch(Exception e){
	e.printStackTrace();
	return null;
}finally{
	try {rs.close();} catch (SQLException e) {e.printStackTrace();}
	try {st.close();} catch (SQLException e) {e.printStackTrace();}
	try {con.close();} catch (SQLException e) {e.printStackTrace();}
}
return list;
}
//==============服务员手持端
//判断登陆权限
	public static String pdWaiter(String str){	
	    Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    String name=null;
		try{		 
			st=con.createStatement();
			String sql="select w_password from WorkerInfo where w_id='"+str+"'and w_delflg='0'";
			rs=st.executeQuery(sql);
			rs.next();
			name=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return name;
	}
	//检查员工是否已经登陆
	public static String isHasLogin(String str){	
	    Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    String name=null;
		try{		  
			st=con.createStatement();
			String sql="select is_loginflg from WorkerInfo where w_id='"+str+"'and w_delflg='0'";
			rs=st.executeQuery(sql);
			rs.next();
			name=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return name;
	}
	//更新订单
		private static Object UPDATELOgINFLG=new Object();
		public static String updateLoginFlg(String wid,String flg){		
			synchronized(UPDATELOgINFLG){			
				Connection con = getConnection();
				Statement st = null;
				try{				
					st = con.createStatement();
					String sql = "update workerinfo set is_loginflg='"+flg+"' where w_id='"+wid+"'";
					System.out.println("sql="+sql);
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}
		}
	//根据员工id查询权限名称
	public static List<String[]> getLoginAuth(String w_id){ 	
	    Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
	    List<String[]> list=new ArrayList<String[]>();
		try{		  
			st=con.createStatement();
			String sql="select authority_name from authority where authority_id in" +
					"(select authority_id from authority_role where role_id in" +
					"(select role_id from workerinfo where w_id='"+w_id+"'))";
			rs=st.executeQuery(sql);
			System.out.println(sql);
			while(rs.next()){
				String str[]=new String[1];
				str[0]=rs.getString(1);
				System.out.println(str[0]);
				list.add(str);
			}		
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	///===============================手持端
	public static String getCurDeskState(String tname){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
	    String state = "0";
		try{		
			String pid=searchPointIdByName(tname);
			st=con.createStatement();
			String task = "select point_state from Point where point_id='"+pid+"'and point_delflg='0'";
			rs = st.executeQuery(task);
			if(rs.next()){			
			 state=rs.getString(1); 
			}
			System.out.println(state);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return state;
	}		
	//根据餐厅查询餐台
	public static List<String[]> getClientPointByroom(String roomname,String ptype_id){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st=con.createStatement();
			String roomid=getRidbyname(roomname);
			String task = "select point_id,point_name,point_num,point_state from Point where room_id='"+roomid+"'and point_delflg='0' and ptype_id='"+ptype_id+"'";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[5];
				str[0]=rs.getString(1);
				str[1]=rs.getString(2);
				str[2]=getPtypename(ptype_id);				
				str[3]=rs.getInt(3)+"";
				str[4]=rs.getString(4);				
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//根据订单信息查询所点菜品
	public static List<String[]> getClientOrderById(String orderid){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql=null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st=con.createStatement();			
			sql = "select orderinfo.point_id,orderinfo.o_totalPri,orderdishconfirm.odc_setMealFlg " +
					"from orderinfo,orderdishconfirm where o_id='"+orderid+"'and orderinfo.o_id=orderdishconfirm.odc_orderId";
			rs = st.executeQuery(sql);
			rs.next();
			String order[]=new String[3];
			for(int i=0;i<3;i++){
				order[i]=rs.getString(i+1);
				System.out.println("order="+order[i]);
			}
			if(order[2].equals("1")){
				//如果是套餐查询
			}else{
				sql="select vege.vege_name,orderdishconfirm.odc_vegePri,orderdishconfirm.odc_vegeQua," +
						"countunit.unit_name" +
						" from vege,orderdishconfirm,countunit where " +
						"vege.vege_id=orderdishconfirm.odc_vege_id and " +
						"vege.unit_id=countunit.unit_id and orderdishconfirm.odc_vegeQua>0";
				System.out.println(sql);
			rs=st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[11];
				str[0]=rs.getString(1);
				str[1]=rs.getFloat(2)+"";
				str[2]=rs.getInt(3)+"";
				str[3]=rs.getString(4);
				str[4]=rs.getString(5);
				str[5]=rs.getString(6);
				str[6]=rs.getString(7);
				str[7]=rs.getString(8);
				str[8]=rs.getString(9);
				str[9]=order[0];
				str[10]=order[1];
				list.add(str);
			}
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//修改菜品数目
	private static Object UPDATENUM_LOCK=new Object();
	public static String updateClientNum(String vnum,String vegeprice,
						String orderid,String vegename){	
		synchronized(UPDATENUM_LOCK){		
			Connection con = getConnection();
			Statement st = null;
			String sql=null;
			int num=Integer.parseInt(vnum);
			float price=Float.parseFloat(vegeprice);
			String vid=getVegeIdByName(vegename);
			try{			 
				st=con.createStatement();
				sql="update orderdishconfirm set odc_vegeQua="+num+" where odc_orderId='"+orderid+"'and odc_vege_id='"+vid+"'";
				System.out.println(sql);
				st.executeUpdate(sql);
				sql="update orderinfo set o_totalPri="+price;
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
//服务员手持端===========================================
	//修改密码
		private static Object UPDATEPASSWORD=new Object();
		public static String updatePassWord(String wid,String password){ 		
			synchronized(UPDATEPASSWORD){			
				Connection con = getConnection();
				Statement st = null;
				try{				
					st = con.createStatement();
					String sql = "update workerinfo set w_password='"+password+"' where w_id='"+wid+"'";
					st.executeUpdate(sql);
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
				return "ok";
			}
		}
	//获取所有的餐厅id,编号和名称 信息
	public static List<String[]> getRoomIdName(){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select room_id,room_no,room_name from Room where room_delflg='0'";
			System.out.println(task);
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[3];
			    str[0]=rs.getString(1);
			    str[1]=rs.getString(2);
			    str[2]=rs.getString(3);
			    System.out.println(str[0]);
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}	
	//获取所有的餐厅类型的ID和名称
	public static List<String[]> getRoomTypeIdName(){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select ptype_id,ptype_name from pointtype where ptype_delflg='0'";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[2];
				str[0]=rs.getString(1);
				str[1] = rs.getString(2);
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//获取服务员手持端需要的餐台信息
	public static List<String[]> getPointForWaiter(String room_id,String ptype_id){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql = "select point_id,point_no,point_name,point_state,point_num,room_name," +
						"ptype_name from point,room,pointtype " +"where point_delflg='0' and " +
						"room.room_id=point.room_id and point.ptype_id=pointtype.ptype_id and " +
						"point.room_id='"+room_id+"' and point.ptype_id='"+ptype_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[7];
				for(int i=0; i<str.length; i++){				
					str[i] = rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//获取服务员手持端无人的餐台信息
	public static List<String[]> getNoPersonPoint(String room_id,String ptype_id){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql = "select point_id,point_no,point_name,point_state,point_num,room_name," +
						"ptype_name from point,room,pointtype " +"where point_delflg='0' and " +
						"room.room_id=point.room_id and point.ptype_id=pointtype.ptype_id and " +
						"point.room_id='"+room_id+"' and point.ptype_id='"+ptype_id+"' and point.point_state='0'";
			rs = st.executeQuery(sql);
			while(rs.next()){			
				String[] str = new String[7];
				for(int i=0; i<str.length; i++){				
					str[i] = rs.getString(i+1);
				}
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//--------------begin--(服务员手持端)
	////////////-------------orderManage-begin
	//服务员端获取订单确认单中的部分信息
	public static List<String[]> getOrderConfirmForWaiter(String pName){ 	
	Connection con = getConnection();
	Statement st = null;
	ResultSet rs = null;
	String sql=null;
	String pId=null;
	@SuppressWarnings("unused")
	String tempId=null;
	//存放点菜确认单中的信息
	List<String[]> vegeInfo=new ArrayList<String[]>();  
	//存放主桌和所有的辅桌的id
    List<String[]> mergeTableNum=new ArrayList<String[]>();
	try{	
		st = con.createStatement();
		pId=searchPointIdByName(pName);
		mergeTableNum.add( new String[]{pId,pName});		
		sql="select odc_user_id,odc_orderId,odc_serveTime,odc_vege_id,odc_vegeQua," +
			"odc_vegePri,odc_code"+
			" from orderdishconfirm" +
			" where odc_orderId in(select o_id from orderinfo where point_id='"+pId+"' and o_SAFlg='0' ) and" +
			" odc_delFlg='0' ";
		rs=st.executeQuery(sql);
		System.out.println("sql2="+sql);
		while(rs.next()){		
			int j=0;
			String str[]=new String[9];  //str[16]存桌子名，str[17]存放菜品名称
			//获取结果集中的数据
			for(;j<str.length-2;j++){			
				str[j]=rs.getString(j+1);
			}
			str[j]=mergeTableNum.get(0)[1];  //将桌子名加到每条信息的最后位置
			vegeInfo.add(str);
		}				
		//判断上面的语句是否查询到菜品信息，如果不判断,当菜品信息为空时，就会产生空指针异常
		if(vegeInfo!=null){		
			//根据菜品id获取菜品名称，并添加到每条菜品数据的尾部
			String vegeId[]=new String[vegeInfo.size()];
			String vegeName[];
			for(int i=0; i<vegeId.length; i++){			
				System.out.println("vegeId[i]"+vegeInfo.get(i)[3]);
				vegeId[i]=vegeInfo.get(i)[3];
			}
			vegeName=getVegeNameForWaiter(vegeId);
			for(int i=0; i<vegeName.length;i++){			
				vegeInfo.get(i)[8]=vegeName[i];
			}
		}
	}
	catch(Exception e){	
		e.printStackTrace();
		return null;
	}
	finally{	
		try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		try {st.close();} catch (SQLException e) {e.printStackTrace();}
		try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
	return vegeInfo;
	}
	//根据餐台名称查询餐台id
	public static String searchPointNameById(String id){ 	
	Connection con=getConnection();
	Statement st=null;
	ResultSet rs=null;
	String pointName=null;
	try{
		st=con.createStatement();
		String sql="select point_name from point where point_id='"+id+"'";
		rs=st.executeQuery(sql);
	    if(rs.next()){	    
	    	pointName=rs.getString(1);
	    }		
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
	finally{
		try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		try {st.close();} catch (SQLException e) {e.printStackTrace();}
		try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
	return pointName;
	}
	//服务员端-----更新点菜确认单数据
	private static Object UPDATE_W_ORDERCONFIRM=new Object();
	public static String updateOrderConfirm(List<String[] > confirmList){	
	synchronized(UPDATE_W_ORDERCONFIRM){	
		Connection con=getConnection();
		Statement st=null;
	    String sql=null;
	    String odc_cancelCauseID = null;
	    String odc_cancelQua = null;
		try{		
			st=con.createStatement();
			for(String[] confirmArray : confirmList){			
				odc_cancelCauseID =  confirmArray[10];
				odc_cancelQua =  confirmArray[11];
				sql = "update OrderDishConfirm set odc_cancelCauseID= '"+odc_cancelCauseID+"', odc_cancelQua= '"+odc_cancelQua+"' " +
							"where odc_vege_id='"+confirmArray[3]+"'  and odc_orderId='"+confirmArray[1]+"'";
				System.out.println(sql);
				st.executeUpdate(sql);	
			}
		}
		catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{		
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return "ok";
	}
	}
	//服务员：根据一组菜品id得到菜品名称
	public static String[] getVegeNameForWaiter(String vege_id[]){ 	
	Connection con = getConnection();
	Statement st = null;
	ResultSet rs = null;
	String[] str=new String[vege_id.length];
	try{	
		st = con.createStatement();
		for(int i=0; i<vege_id.length; i++){		
			String sql="select vege_name from vege where vege_id ='"+vege_id[i]+"'";
			rs=st.executeQuery(sql);
			if(rs.next()){			
				str[i]=rs.getString(1);
			}
		}
	}
	catch(Exception e){	
		e.printStackTrace();
		return null;
	}
	finally{	
		try {if(rs!=null){rs.close();} } catch (SQLException e) {e.printStackTrace();}
		try {if(st!=null){st.close();} } catch (SQLException e) {e.printStackTrace();}
		try {if(con!=null){con.close();} } catch (SQLException e) {e.printStackTrace();}
	}
	return str;
	}
	//删除点菜确认单中的菜品
		private static Object DELETEOCVEGE=new Object();
		public static String deleteOrderConfirmVege(String[] params){		
		synchronized(DELETEOCVEGE){		
			Connection con=getConnection();
			Statement st=null;
		    String sql=null;
		    ResultSet rs=null;
		    @SuppressWarnings("unused")
			String curCount=null;
		    String result="fail";
		    @SuppressWarnings("unused")
			String distributeFlg=null;
			try{			
				st=con.createStatement();
					sql="update orderdishconfirm set odc_delFlg='1'" +
							"where  odc_orderId='"+
							params[2]+"' and odc_vege_id='"+params[3]+"' and odc_code='"+params[4]+"'";
					System.out.println(sql);
					st.executeUpdate(sql);	
					sql="SELECT odc_vegeQua FROM orderdishconfirm"+
							" where odc_user_id='"+params[1]+"' and odc_orderId='"+
							params[2]+"' and odc_vege_id='"+params[3]+"' and odc_code='"+params[4]+"'";
					rs=st.executeQuery(sql);
					if(rs.next()){					
						curCount=rs.getString(1);
					}
			}
		catch(Exception e){		
			e.printStackTrace();
			return result;
		}
		finally{		
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	}	
	//将菜品的数量减1,即将退菜数量加1
	private static Object SUBVEGECOUNT=new Object();
	public static String subVegeCount(String[] params){	
	synchronized(SUBVEGECOUNT){	
		Connection con=getConnection();
		Statement st=null;
		ResultSet rs=null;
	    String sql=null;
	    List<String[]> list=new ArrayList<String[]>();
	    String result="fail";
	    @SuppressWarnings("unused")
		String distributeFlg=null;
	    int curVegeCount=0;
		try{		
			st=con.createStatement();
			sql="select odc_vegeQua from OrderDishConfirm " +
					"where  odc_orderId='"+
					params[2]+"' and odc_vege_id='"+params[3]+"' and odc_code='"+params[4]+"'";
			rs=st.executeQuery(sql);
			if(rs.next()){			
				curVegeCount=rs.getInt(1);
				System.out.println("-----curVegeCount="+curVegeCount);
			}
//			//如果当前的数量为1，则不允许再退菜，只能够删除
//			//只有在操作人员连续快速的点击退菜按钮时，点击的速度大过屏幕菜品数量的改变速度，
//			//就会造成当菜品数实际上已经为1了，可仍然还在执行减少菜品的操作，造成数据库中的数量为负值，而界面显示的数量为1
			if(curVegeCount<=1){			
				result="ok";
				return result;
			}
			sql="update orderdishconfirm set odc_vegeQua=odc_vegeQua-1 where  odc_orderId='"+
					params[2]+"' and odc_vege_id='"+params[3]+"' and odc_user_id='"+params[1]+"' and odc_code='"+params[4]+"'";
			System.out.println(sql);
			st.executeUpdate(sql);	
			list.add(new String[]{params[3],"1"}); //odc_vege_id,数量1
		}		
		catch(Exception e){		
			e.printStackTrace();
			return result;
		}
		finally{		
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	}	
	//将菜品的数量加1
	private static Object ADDVEGECOUNT=new Object();
	public static String addVegeCount(String[] params){	
		synchronized(ADDVEGECOUNT){		
			Connection con=getConnection();
			Statement st=null;
		    String sql=null;
		    String result="fail";
			try{			
				st=con.createStatement();
					sql="update orderdishconfirm set odc_vegeQua=odc_vegeQua+'1' "+
							"where odc_user_id='"+params[1]+"' and odc_orderId='"+
							params[2]+"' and odc_vege_id='"+params[3]+"' and odc_code='"+params[4]+"'";
					System.out.println(sql);
					st.executeUpdate(sql);	
			}
			catch(Exception e){			
				e.printStackTrace();
				return result;
			}
			finally{			
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return result;
		}
		}
	//根据服务员的id,查询该服务员所下的订单所在的桌子号
	public static String[] queryDeskNameByUserId(){	
	Connection con=getConnection();
	Statement st=null;
	ResultSet rs=null;
	String sql=null;
	List<String> pointId=new ArrayList<String>();
	String pointName[];
	try{	
		st=con.createStatement();
		sql="SELECT point_id FROM orderInfo " +
				"WHERE o_SAFlg='0'";
		rs=st.executeQuery(sql);
		while(rs.next()){		
			pointId.add(rs.getString(1));
		}	
		pointName=new String[pointId.size()];
		for(int i=0; i<pointId.size(); i++){		
			pointName[i]=searchPointNameById(pointId.get(i).trim());
		}
		for(String strr: pointName){		
			System.out.println(strr+" ");
		}
	}
	catch(Exception e){	
		e.printStackTrace();
		return null;
	}
	finally{	
		try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		try {st.close();} catch (SQLException e) {e.printStackTrace();}
		try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
		return pointName;
	}
	////////////-------------orderManage-end
	//根据主类别得到子类别的名称
	public static List<String> getCGByMCG(String string){	
		Connection con = getConnection();
 		Statement st = null;
 		ResultSet rs = null;
 		List<String> list=new ArrayList<String>();
 		try{		
 			st = con.createStatement();
 			String task ="select cate_name from  category,maincategory" +
 					" where maincategory.mcate_name='"+string+"' and " +
 							"category.mcate_id=maincategory.mcate_id and category.cate_delflag='"+0+"'";
 			System.out.println(task);
 			rs = st.executeQuery(task);
            while(rs.next()){           
            	list.add(rs.getString(1));
            }			
 		}catch(Exception e){		
 			e.printStackTrace();
 			return null;
 		}finally{ 		
 			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
 			try {st.close();} catch (SQLException e) {e.printStackTrace();}
 			try {con.close();} catch (SQLException e) {e.printStackTrace();}
 		}
 		return list;
	}
	//根据菜品id得到菜品的相关信息
	public static String[] searchVegeByid(String string){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String[] vegemsg = new String[5];
		try{		
			st = con.createStatement();
			String sql="select vege_id,vege_name,vege_vegeprice,vege_intro,unit_name from vege,countunit where  vege_useflg='"+1+"' and vege.unit_id =  countunit.unit_id and " +
			"vege_id='"+string+"'";
			rs = st.executeQuery(sql);
			if(rs.next()){			
				for(int i=0;i<vegemsg.length;i++){				
					vegemsg[i]=rs.getString(i+1);
				}
			}
		}
		catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return vegemsg;
	}
	//获取主类别信息
	public static List<String[]> getMCGinfo(String string){ 	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		List<String[]>ll=new ArrayList<String[]>();
		try{		  
			st=con.createStatement();
			String sql="select distinct maincategory.mcate_id,maincategory.mcate_name " +
					"from vege,category,MainCategory where vege_useflg='"+1+"' and " +
			"vege.cate_id=category.cate_id and category.mcate_id=maincategory.mcate_id ";
			rs=st.executeQuery(sql);
			while(rs.next()){			
				String[] s=new String[2];
				for(int i=0;i<s.length;i++){				
					s[i]=rs.getString(i+1);
				}
				ll.add(s);
			}	
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return ll;
	}
	//根据名称查询餐台状态
	public static String getTableStateByname(String tname){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs=null;
		String sql=null;
		String state=null;
		try{		 
			st=con.createStatement();
			sql="select point_state from point where point_name='"+tname+"'";
			System.out.println(sql);
			rs=st.executeQuery(sql);
			if(rs.next()){
			 state=rs.getString(1);
			}else{			
				return null;
			}
		}catch(Exception e){		
			e.printStackTrace() ;
			return null;
		}finally{		
			try{st.close();}catch(Exception e) {e.printStackTrace();}
			try{con.close();}catch(Exception e) {e.printStackTrace();}
		}
		return state;
	}
	//得到当前餐台的人数
	public static String getCurDeskGuestNum(String tablename){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs=null;
		String sql=null;
		String gnum=null;
		String tableid=searchPointIdByName(tablename);
		try{		 
			System.out.println(tableid);
			st=con.createStatement();
			sql="select o_guestnum from orderinfo where point_id='"+tableid+"'";
			System.out.println(sql);
			rs=st.executeQuery(sql);
			if(rs.next()){			
				gnum=rs.getInt(1)+"";
			}else{
				return null;
			}
			System.out.println(gnum);
		}catch(Exception e){		
			e.printStackTrace() ;
			return null;
		}finally{		
			try{rs.close();}catch(Exception e) {e.printStackTrace();}
			try{st.close();}catch(Exception e) {e.printStackTrace();}
			try{con.close();}catch(Exception e) {e.printStackTrace();}
		}
		return gnum;
	}
	//修改订单人数
	private static Object CHANGE_GUESTNUM_LOCK=new Object();
	public static String changeGuestnum(String tablename, String num){	
		synchronized(CHANGE_GUESTNUM_LOCK){		
			Connection con = getConnection();
			Statement st = null;
			String sql=null;
			String tableid=searchPointIdByName(tablename);
			int gnum=Integer.parseInt(num);
			try{			 
				st=con.createStatement();
				sql="update orderinfo set o_guestnum="+gnum+" where point_id='"+tableid+"' and o_SAFlg='0'";
				System.out.println(sql);
				st.executeUpdate(sql);				
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}		
//===========================餐台信息修改结束==
	//得到菜品的所有图片
	public static List<String[]> getALLVegeImage(){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String task = "select vimage_id,vimage_path from vegeimage where del_flg='"+0+"'";
			rs = st.executeQuery(task);
			while(rs.next()){			
				String[] str = new String[2];
			   for(int i=0;i<str.length;i++){			   
				   str[i]=rs.getString(i+1);
			   }
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//得到菜品图片的最大编号
	public static String getVegeImageMaxNo(){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
			st = con.createStatement();
			String task = "select max(vimage_id) from vegeimage";
			rs = st.executeQuery(task);
		    rs.next();
		    if(rs.getString(1)==null){		    
		    	str=String.valueOf(0); 
		    }
		    else{		    
		    	str=rs.getString(1);
		    }
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;		
	}
	//将菜品图片添加进库
	private static Object ADDVEGEIMAGE=new Object();
	public static String addVegeImage(String string, String getinfo, String getinfo2, String getinfo3){ 	
		synchronized(ADDVEGEIMAGE){		
			Connection con = getConnection();
			Statement st = null;
			try{			
				st = con.createStatement();
				String sql="insert into vegeimage(vimage_id,vege_id,vimage_path,vimage_ismain) values('"+string+"','"+getinfo+"','"+getinfo2+"','"+getinfo3+"')";
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//根据菜品id得到图片
	public static String vegetoimage(String string){	
		Connection con = getConnection();
	    Statement st = null;
	    ResultSet rs = null;
		StringBuilder msg = new StringBuilder();
		try{		  
			st=con.createStatement();
			String sql="select vimage_path from vegeimage where vegeimage.vege_id='"+string+"'and del_flg='"+0+"';";
			rs=st.executeQuery(sql);
			while(rs.next()){			
				msg.append(rs.getString(1));
				System.out.println("msg="+msg);
				msg.append(",");
			}
		}catch(Exception e){		
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		System.out.println(msg);
		return msg.substring(0,msg.length());
	}
	//判断当前菜品是否存在主图
	public static String pdVegeImage(String string){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
			st = con.createStatement();
			String task = "select vimage_id from vegeimage where vege_id='"+string+"' " +
					"and vimage_ismain='"+1+"'and del_flg='"+0+"'";
			rs = st.executeQuery(task);
		    if(rs.next()){		    
		    	str=String.valueOf(1); 
		    }
		    else{		    
		    	str=String.valueOf(0);
		    }
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;		
	}
	//得到指定菜品的主图片
	public static String getVegeMainImage(String string){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
		    st = con.createStatement();
		    String task = "select vimage_path from vegeimage where " +
		    		"vege_id='"+string+"' and vimage_ismain='"+1+"'and del_flg='"+0+"'";
			rs = st.executeQuery(task);
		    if(rs.next()){		    
		    	str=rs.getString(1);
		    }
		    else{		    
		    	str="";
		    }
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//修改菜品主图
	private static Object UPDATEVEGEMAINIMAGE=new Object();
	public static String updateVegeMainImage(String[] getstr){ 	
		synchronized(UPDATEVEGEMAINIMAGE){		
			Connection con = getConnection();
			Statement st = null;
			String sql=null;
			try{			 
				st=con.createStatement();
				sql="update vegeimage set del_flg='1' where vege_id='"+getstr[2]+"'and vimage_ismain='1'";
				System.out.println(sql);
				st.executeUpdate(sql);
				sql="insert into vegeimage(vimage_id,vege_id,vimage_path,vimage_ismain) " +
						"values('"+getstr[1]+"','"+getstr[2]+"','"+getstr[3]+"','"+getstr[4]+"')";
				st.executeUpdate(sql);
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}		
	}
	//通过菜品ID获得菜品图品信息，并将该菜品的主图片路径传回服务员手持端
	public static String getPicByVegeid(String vege_id){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String str = null;
		try{		
			st = con.createStatement();
			String sql="select vimage_path from vegeimage where vege_id='"+vege_id+
						"' and vimage_ismain='1' and del_flg='0';";
			rs=st.executeQuery(sql);
			rs.next();
			str=rs.getString(1);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str;
	}
	//删除主图的大图
	public static String delZDImage(String string, String string2){ 	
		Connection con = getConnection();
		Statement st = null;
		try{		
			st = con.createStatement();
			String sql="update vegeimage set del_flg='1' where vege_id='"+string+"'and vimage_path= '"+string2+"'";
			st.executeUpdate(sql);
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return "ok";
	}
	//根据类型名称得到所有菜
	public static  List<String[]> searchVegeByCate(String cag){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list=new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql="select vege.vege_id,vege.vege_name,vege.vege_vegeprice,countunit.unit_name from vege,countunit where vege.unit_id=countunit.unit_id and vege.cate_id in(select cate_id from category where cate_name='"+cag+"')";
			rs = st.executeQuery(sql);
			while(rs.next()){				
				String str[]=new String[4];
				str[0]=rs.getString(1);
				str[1]=rs.getString(2);	
				str[2]=rs.getFloat(3)+"";
				str[3]=rs.getString(4);
				System.out.println(str[1]+str[3]);
				list.add(str);
			}
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//根据菜品id得到这个菜的所有子图
	public static String searchzpathByvgid(String getstr){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		StringBuilder str = new StringBuilder();
		try{		
		    st = con.createStatement();
		    String task = "select vimage_path from vegeimage where vege_id='"+getstr+"' " +
		    		"and del_flg='"+0+"' and (vimage_ismain='"+0+"' or vimage_ismain='"+2+"') ";
			rs = st.executeQuery(task);
		   while(rs.next()){		   
			   str.append(rs.getString(1));
			   str.append(",");
		   }		   
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return str.substring(0,str.length());
	}
	//服务员手持端得到菜品信息
	public static List<String[]> getWVege(String cate_name){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try{		
			st = con.createStatement();
			String sql="select vege_id" +
					",vege_name,vege_vegeprice," +
					"vege_intro,unit_name,cate_name,mcate_name,vt_name,vs_name " +
					"from vege,countunit,category,vegetype,MainCategory,VegeStandard " +
					"where  vege_useflg='"+1+"' and vege.unit_id =  countunit.unit_id and " +
					"vege.cate_id = category.cate_id  and category.mcate_id = maincategory.mcate_id and vege.vt_id = vegetype.vt_id  " +
					" and vege.vs_id = VegeStandard.vs_id  and category.cate_name='"+cate_name+"';";
			rs = st.executeQuery(sql);
			System.out.println(sql);
		
			while(rs.next()){			
				String[] str = new String[27];
				for(int i=0;i<9;i++){				
					str[i]=rs.getString(i+1);
				}
				//根据菜品id得到主图路径
				String mainpath=DBUtil.getVegeMainImage(str[0]);
				//根据菜品id得到子图路径
				String imagemsg=DBUtil.searchzpathByvgid(str[0]);
				str[22]=mainpath;
				str[23]=imagemsg;
				list.add(str);
			}	
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}	
	//得到菜品信息
	public static String[] searchWVegeByid(String vege_id){	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String[] list=new String[22];
		try{		
			st = con.createStatement();
			String sql="select vege_id,vege_name,vege_vegeprice," +
			"unit_name,cate_name,mcate_name,vt_name,vs_name,vege_intro" +
			"from vege,countunit,category,vegetype,WorkerInfo,MainCategory,VegeStandard" +
			" where  vege_useflg='"+1+"' and vege.unit_id =  countunit.unit_id and " +
			"vege.cate_id = category.cate_id  and category.mcate_id = maincategory.mcate_id  " +
			" and vege.vs_id = VegeStandard.vs_id and vege.vege_id='"+vege_id+"';";
			rs = st.executeQuery(sql);
			while(rs.next()){			
			for(int i=0;i<list.length;i++){			
				list[i]=rs.getString(i+1);
			}
			}			
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return list;
	}
	//根据菜品的id得到菜品介绍的相关信息
	public static String[] getVegeIntroByid(String string){ 	
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String[] vegemsg=new String[5] ;
		try{		
			st = con.createStatement();
			String sql="select vege_id,vege_name,vege_vegeprice,vege_intro," +
					"unit_name from vege,countunit where  vege_useflg='"+1+
					"' and vege.unit_id =  countunit.unit_id and vege.vege_id='"+string+"'";
			rs = st.executeQuery(sql);
			rs.next();
			for(int i=0;i<vegemsg.length;i++){			
				vegemsg[i]=rs.getString(i+1);
				System.out.println("["+i+"]==="+vegemsg[i]);
			}
		}
		catch(Exception e){		
			e.printStackTrace();
			return null;
		}
		finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return vegemsg;
	}
	//创建点菜确认单
	private static Object ADDNEWDETIALORDER=new Object();
	public static String addNewDetialOrder(List<String[]> orderList){	
		synchronized(ADDNEWDETIALORDER){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs=null;
			int order_code;
			@SuppressWarnings("unused")
			String addVegeFlg="0";
			try{			
				st = con.createStatement();
				String sql="select max(odc_code) from orderdishconfirm where odc_orderid='"+orderList.get(0)[2]+"';";
				rs=st.executeQuery(sql);
				rs.next();
				String s=rs.getString(1);
				if(s!=null){//若有该订单，则为加菜操作，获取编号最大值				
					order_code=Integer.parseInt(s)+1;
					addVegeFlg="1";
				}
				else{//若无该订单号，则为添加新订单动作				
					order_code=0;
				}
				for(int i=0;i<orderList.size();i++){//添加详细订单				
					String str[]=orderList.get(i);
					String odc_serverTime=TypeExchangeUtil.getCurTime();
					sql="insert into orderdishconfirm(odc_vege_id,odc_user_id," +
							"odc_orderid,odc_vegequa,odc_vegepri,odc_serveTime," +
							"odc_code" +") values('"+str[0]+"','"+str[1]+"','"+
							str[2]+"',"+Float.parseFloat(str[3])+",'"+str[4]+"','"+odc_serverTime+
							"','"+(order_code+Integer.parseInt(str[7]))+
							"')";
					st.executeUpdate(sql);
					System.out.println("add detailorder: sql"+sql);
				}
			}
			catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}
			finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}
	}
	//根据菜品ID，从库存中扣除该菜品所用到的材料的用量，若库存不足，则返回菜品ID
    //通过餐台Id获得查询该餐台未结账账单号，若为空，则获取当前最大订单号并根据餐台Id、员工Id、总价格、顾客人数创建新订单
	private static Object ADDNEWORDER=new Object();
	public static String addNewOrder(List<String[]> orderList,
			String pointName,String o_workerid, String o_totalPri,String o_guestnum){	
		synchronized(ADDNEWORDER){		
			 Connection con = getConnection();
			 Statement st = null;
			 ResultSet rs = null;
			 String str = "";
			 String orderId=null;
			 try{			 
				 st = con.createStatement(); 
					//通过餐台Id查询该餐台是否已下单且未结账，并获得餐台的订单号
					String pointId;
					String sql="select point_id from point where point_name='"+pointName+"';";				
					rs=st.executeQuery(sql);
					rs.next();
					pointId=rs.getString(1);
					sql="select o_id from orderinfo where point_id='"+pointId+"' and o_SAFlg='0';";
					rs=st.executeQuery(sql);
					if(!rs.next()){//该餐台未下订单					
						String time=(TypeExchangeUtil.getCurTime()).split(" ")[0].replace("-", "");
						System.out.println(time+"/////////////////////////////////////////");
						//查询最大订单号
						sql="select max(o_id) from orderinfo where o_id like '"+"YY00010001-"+time+"-____"+"'";
						rs=st.executeQuery(sql);
						rs.next();
						String maxNo=rs.getString(1);
						System.out.println("maxNo============"+maxNo);
						if(maxNo==null){						
							orderId="YY00010001-"+time+"-0001";
							String o_ODTime=TypeExchangeUtil.getCurTime();
							//添加新订单
							sql="insert into orderinfo(o_id,point_id,o_workerid" +
									",o_totalPri,o_ODTime,o_guestnum) values('"+orderId+"','"+pointId+
									"','"+o_workerid+"','"+o_totalPri+"','"+o_ODTime+"','"+o_guestnum+"')";
							st.executeUpdate(sql);
						}
						else{//即将下单的订单的订单号						
						 /*******************************************************************************/
							String startId=maxNo.substring(0, 10);
							String endId=maxNo.substring(20, 24);
							if(Integer.parseInt(endId)<9){							
								System.out.println("orderId============"+endId);
								orderId="000"+(Integer.parseInt(endId)+1);
								System.out.println("orderId============"+endId);
							}
							else
								if(Integer.parseInt(endId)<99){								
									orderId="00"+(Integer.parseInt(endId)+1);
								}
								else
									if(Integer.parseInt(endId)<999){									
										orderId="0"+(Integer.parseInt(endId)+1);
									}
									else{									
										orderId=""+(Integer.parseInt(endId)+1);
									}
							orderId=startId+"-"+time+"-"+orderId;
							System.out.println("orderId=========================="+orderId);
						 /*******************************************************************************/
							String o_ODTime=TypeExchangeUtil.getCurTime();
							//添加新订单
							sql="insert into orderinfo(o_id,point_id,o_workerid" +
									",o_totalPri,o_ODTime,o_guestnum) values('"+orderId+"','"+pointId+
									"','"+o_workerid+"','"+o_totalPri+"','"+o_ODTime+"','"+o_guestnum+"')";
							st.executeUpdate(sql);
						}  
					}
					else{//该餐台已下单，操作为为该订单加菜					
						orderId=rs.getString(1);
						sql="update orderinfo set o_totalPri=o_totalPri+'"+o_totalPri+
								"' where o_id='"+orderId+"';";
						st.executeUpdate(sql);
					}
					 str="sucess"+orderId;
				 }			 
			 catch(Exception e){			 
				 e.printStackTrace();
				 return null;
			 }
			 finally{			 
					try {rs.close();} catch (SQLException e) {e.printStackTrace();}
					try {st.close();} catch (SQLException e) {e.printStackTrace();}
					try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return str;
		}
	}
//根据餐台名称查询餐台id
	public static String searchPointIdByName(String name){ 	
		Connection con=getConnection();
		Statement st=null;
		ResultSet rs=null;
	    String pointid=null;
		try{
			st=con.createStatement();
			String sql="select point_id from point where point_name='"+name+"' and point_state='1'";
			rs=st.executeQuery(sql);
		    if(rs.next()){		    
			  pointid=rs.getString(1);
		    }
		    else{		    
		    	return null;
		    }
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return pointid;
	}
//手持端更新餐台信息
	private static Object CLIENTPOINT_LOCK=new Object();
	public static String updateClientPoint(String pname,String state){	
		synchronized(CLIENTPOINT_LOCK){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs=null;
			String sql=null;
			try{			 
				st=con.createStatement();
				sql="select point_state from Point where point_name='"+pname+"'and point_delflg='0'";
				rs=st.executeQuery(sql);
				rs.next();
				if(rs.getString(1).equals("0")){				
			      sql="update  point set point_state='"+state+"' where point_name='"+pname+"'";
				  System.out.println(sql);
				  st.executeUpdate(sql);
				}
				else{				
					return null;
				}
			}catch(Exception e){			
				e.printStackTrace() ;
				return null;
			}finally{			
				try{rs.close();}catch(Exception e) {e.printStackTrace();}
				try{st.close();}catch(Exception e) {e.printStackTrace();}
				try{con.close();}catch(Exception e) {e.printStackTrace();}
			}
			return "ok";
		}		
	}
	public static String getPointGuestNumByName(String name,String guestnum){	
		Connection con=getConnection();
		Statement st=null;
		ResultSet rs=null;
		try{		
		  String str[]=new String[2];
		  st=con.createStatement();
		  String sql="select point_num,point_state from point where point_name='"+name+"' and point_delflg='0'";
		  rs=st.executeQuery(sql);
		  System.out.println(sql);
		  if(rs.next()){		  
			  str[0]=rs.getInt(1)+"";
			  str[1]=rs.getString(2);
		  }
		  if(str[1].equals("1")||Integer.parseInt(str[0])<Integer.parseInt(guestnum)){		  
			  return null;
		  }
		}catch(Exception e){		
			e.printStackTrace();
			return null;
		}finally{		
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {st.close();} catch (SQLException e) {e.printStackTrace();}
			try {con.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return "ok";
	}
	//取消开台
	private static Object CANCLEPOINT_LOCK=new Object();
	public static String canclePointUpdate(String pname,String state){		
			synchronized(CANCLEPOINT_LOCK){			
				Connection con = getConnection();
				Statement st = null;
				String sql=null;
				ResultSet rs=null;
				try{				
					String pstate=null;
					st=con.createStatement();
					sql="select point_state from point where point_name='"+pname+"'";
					rs=st.executeQuery(sql);
					if(rs.next()){					
						pstate=rs.getString(1);
					}
					else{					
						return null;
					}
					if(pstate.equals(state)){					
						return null;
					}
					else{					
			          sql="update  point set point_state='"+state+"' where point_name='"+pname+"'";
				      System.out.println(sql);
				      st.executeUpdate(sql);
				      return "ok";
					}
				}catch(Exception e){				
					e.printStackTrace() ;
					return null;
				}finally{				
					try{rs.close();}catch(Exception e) {e.printStackTrace();}
					try{st.close();}catch(Exception e) {e.printStackTrace();}
					try{con.close();}catch(Exception e) {e.printStackTrace();}
				}
			}
			}
//获取服务员手持端有人的餐台信息
	public static List<String[]> getHasPersonPoint(String room_id,String ptype_id){ 		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			List<String[]> list = new ArrayList<String[]>();
			String sql=null;
			try{			
				st = con.createStatement();
				 sql = "select point_id,point_no,point_name,point_state,point_num,room_name,ptype_name" +
							" from point,room,pointtype " +"where point_delflg='0' and " +
							"room.room_id=point.room_id and point.ptype_id=pointtype.ptype_id and " +
							"point.room_id='"+room_id+"' and point.ptype_id='"+ptype_id+"' and point.point_state!='0' " ;									
				rs = st.executeQuery(sql);
				while(rs.next()){				
					String[] str = new String[7];
					
					for(int i=0; i<str.length; i++){					
						str[i] = rs.getString(i+1);
					}
					System.out.println("has order"+str[2]);
					list.add(str);
				}
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return list;
		}	
	//把时间按一定格式转换为String
	public static long getMillisDate( final String dataString){	  
		long time=0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        try {  
            Date date = df.parse(dataString); 
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            time=calendar.getTimeInMillis();
            System.out.println("millis="+time);
        } catch (Exception ex) {  
            System.out.println(ex.getMessage());  
        }  
       return time;
	} 
	//根据菜品id得到这个菜的标志为0的子图
		public static String searchzimagepath(String getstr){		
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			StringBuilder str=new StringBuilder() ;
			try{			
			    st = con.createStatement();
			    String task = "select vimage_path from vegeimage where " +
			    		"vege_id='"+getstr+"' and vimage_ismain='"+0+"'and del_flg='"+0+"'";
				rs = st.executeQuery(task);
				while(rs.next()){				   
					   str.append(rs.getString(1));
					   str.append(",");
				   }				
			}catch(Exception e){			
				e.printStackTrace();
				return null;
			}finally{			
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
				try {con.close();} catch (SQLException e) {e.printStackTrace();}
			}
			if(str.length()>0){			
				System.out.println("str.length>>>>>>>>>>>>>>>>>0");
				return str.substring(0,str.length());
			}else{
				System.out.println("str.length===================0");
				return str.toString();
			}			
		}
		//根据菜品id得到这个菜标志位2 的主图的大图
				public static String searchmbimagepath(String getstr){				
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					String str ;
					try{					
					    st = con.createStatement();
					    String task = "select vimage_path from vegeimage " +
					    		"where vege_id='"+getstr+"' and vimage_ismain='"+2+"'and del_flg='"+0+"'";
						rs = st.executeQuery(task);
						rs.next();
						str=rs.getString(1);					   
					}catch(Exception e){					
						e.printStackTrace();
						return null;
					}finally{					
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return str;
				}
				//修改菜品主图的大图
				private static Object UPDATEVEGEMAINBIGIMAGE=new Object();
				public static String updateVegeMainBigImage(String[] getstr){ 				
					synchronized(UPDATEVEGEMAINBIGIMAGE){					
						Connection con = getConnection();
						Statement st = null;
						String sql=null;
						//根据菜品id查找得到当前菜品的主图的大图是否是不存在图片的那张图
						String path=searchmbimagepath(getstr[2]);
						System.out.println("path========="+path);
						try{						
							st=con.createStatement();
							if(path.equals(Constant.IMAGE_PATH+Constant.IMAGE_NULLBIGNAME)){							
								System.out.println("////////////////////////");
								sql="update vegeimage set del_flg='1' where vege_id='"+getstr[2]+"' and vimage_ismain='"+2+"'";
								st.executeUpdate(sql);
							}
							else{							
								sql="update vegeimage set vimage_ismain='0' where vege_id='"+getstr[2]+"' and vimage_ismain='"+2+"'";
								st.executeUpdate(sql);
							}
							sql="update vegeimage set vimage_ismain='2' where vege_id='"+getstr[2]+"' and vimage_path='"+getstr[3]+"'";
							st.executeUpdate(sql);
						}catch(Exception e){						
							e.printStackTrace() ;
							return null;
						}finally{						
							try{st.close();}catch(Exception e) {e.printStackTrace();}
							try{con.close();}catch(Exception e) {e.printStackTrace();}
						}
						return "ok";
					}					
				}
				//得到pc端有用的菜品信息
				public static List<String[]> getUseVege() {
					Connection con = getConnection();
					Statement st = null;
					ResultSet rs = null;
					List<String[]> list = new ArrayList<String[]>();
					try{					
						st = con.createStatement();
						String sql="select vege_id,vege_name,vege_vegeprice," +
								"unit_name,cate_name,mcate_name,vt_name,vs_name,vege_intro " +
								"from vege,countunit,category,vegetype,MainCategory,VegeStandard where  vege_useflg='"+1+"' and vege.unit_id =  countunit.unit_id and " +
								"vege.cate_id = category.cate_id  and category.mcate_id = maincategory.mcate_id and vege.vt_id = vegetype.vt_id  " +
								"and vege.vs_id = VegeStandard.vs_id ";
								rs = st.executeQuery(sql);
						while(rs.next()){						
							String[] str = new String[9];
							for(int i=0;i<str.length;i++){
								str[i]=rs.getString(i+1);
							}
							list.add(str);
						}
						System.out.println("--------list.size="+list.size());
					}catch(Exception e){					
						e.printStackTrace();
						return null;
					}finally{					
						try {rs.close();} catch (SQLException e) {e.printStackTrace();}
						try {st.close();} catch (SQLException e) {e.printStackTrace();}
						try {con.close();} catch (SQLException e) {e.printStackTrace();}
					}
					return list;
				}
		public static void main(String args[]){		              //测试方法
			String str[]={"","001","YY00010001-20130116-0003","003","1"};
			 subVegeCount(str);
		}
}