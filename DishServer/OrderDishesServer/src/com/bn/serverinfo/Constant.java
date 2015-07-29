package com.bn.serverinfo;//声明包语句

public class Constant {//定义主类Constant
	  public static final String IMAGE_PATH="pic/";//定义图片路径的字符串
	  public static final String IMAGE_NULLNAME="1.jpg";//定义无图时显示的图片名称字符串
	  public static final String IMAGE_NULLBIGNAME="0.jpg";//定义无图时显示的大图片名称字符串
	  
	  public static boolean flag=true;//定义boolean类型的标志位
	  public static final int width=400;//定义宽度
	  public static final int height=300;//定义高度
	  public static final int xwidth=260;//定义单位宽度
	  public static final String TESTCONNECT="TESTCONNECT";//定义测试连接字符串	
	  //主类别
	  public static final String GET_MCG="GET_MCG";//得到主类
	  public static final String GET_MCGMAXNO="GET_MCGMAXNO";//得到主类最大数
	  public static final String UPDATE_CMG="UPDATE_CMG";//更新主类
	  public static final String ADD_CMG="ADD_CMG";//添加主类
	  public static final String DEL_CMG="DEL_CMG";//删除主类
	  //菜品类别
	  public static final String GET_CG="GET_CG";//得到类别
	  public static final String GET_CGMAXNO="GET_CGMAXNO";//得到类别最大数
	  public static final String ADD_CG="ADD_CG";//添加类别
	  public static final String DEL_CG="DEL_CG";//删除类别
	  public static final String UPDATE_CG="UPDATE_CG";//更新类别
	//菜系
	  public static final String GET_VT="GET_VT";//得到菜系
	  public static final String GET_VTMAXNO="GET_VTMAXNO";//得到菜系最大数
	  public static final String UPDATE_VT="UPDATE_VT";//更新菜系
	  public static final String DEL_VT="DEL_VT";//删除菜系
	  	  
	//计量单位
	  public static final String GET_CU="GET_CU";//得到计量单位
	  public static final String GET_CUMAXNO="GET_CUMAXNO";//得到计量单位最大数
	  public static final String UPDATE_CU="UPDATE_CU";//更新计量单位
	  public static final String DEL_CU="DEL_CU";//删除计量单位
	  
	  //规格
	  public static final String GET_VS="GET_VS";//得到规格
	  public static final String GET_VSMAXNO="GET_VSMAXNO";//得到规格最大数
	  public static final String UPDATE_VS="UPDATE_VS";//更新规格
	  public static final String DEL_VS="DEL_VS";//删除规格
	  
	  //菜品图片
	  public static final String DELETE_ZBIMAGE="DELETE_ZBIMAGE";//删除主图
	  public static final String ISHAVENULLPIC="ISHAVENULLPIC";//是否无图
	  public static final String GET_VEGEIMAGE="GET_VEGEIMAGE";//得到菜品图片
	  public static final String GET_VEGEIMAGEMAXNO="GET_VEGEIMAGEMAXNO";//得到菜品图片最大数
	  public static final String INSERT_IMAGE="INSERT_IMAGE";//插入图片
	  public static final String PDVEGEMAIN="PDVEGEMAIN";//判断是否存在主图
	  public static final String GET_VEGEMAINIMAGE="GET_VEGEMAINIMAGE";//得到菜品主图
	  public static final String UPDATE_MAINIMAGE="UPDATE_MAINIMAGE";//修改主图
	  public static final String UPLOAD_MIMAGE="UPLOAD_MIMAGE";//更新主图
	  public static final String UPLOAD_ZIMAGE="UPLOAD_ZIMAGE";//更新子图
	  //菜品
	  public static final String GET_VEGE="GET_VEGE";//得到菜品信息
	  public static final String GET_VEGEMAXNO="GET_VEGEMAXNO";//得到菜品最大数
	  public static final String GETMCGNAMEBYCGNAME="GETMCGNAMEBYCGNAME";//根据子类别得到主类别
	  public static final String ADD_VEGE="ADD_VEGE";//添加菜品
	  public static final String DEL_VEGE="DEL_VEGE";//删除菜品
	  public static final String UPDATE_VEGE="UPDATE_VEGE";//更新菜品信息
	  public static final String SEARCH_VEGE="SEARCH_VEGE";//查询菜品
	  public static final String GS_VEGE="GS_VEGE";//根据菜品ID得到菜品信息
	  public static final String GET_USEVEGE="GET_USEVEGE";//获得pc端有用的菜品信息
	  	//员工
	  public static final String GET_WOKERCG="GET_WOKERCG";   //得到员工信息（手持端）
	  public static final String GET_WOKERWAITER="GET_WOKERWAITER";//得到服务员信息（手持端）
	 
	  public static final String GET_WORKERINFO="GET_WORKERINFO";//得到员工信息（PC端）
	  public static final String GET_ROLEINFO="GET_ROLEINFO";//得到角色信息
	  public static final String GET_AUTHORITYINFO="GET_AUTHORITYINFO";//得到权限
	  public static final String GET_AUTHORITY_ROLE="GET_AUTHORITY_ROLE";//得到权限--角色对应信息
	  public static final String ADD_WORKERINFO = "ADD_WORKERINFO";//添加员工
	  public static final String DEL_WORKERINFO="DEL_WORKERINFO";//删除员工信息
	  public static final String UPDATE_WORKERINFO="UPDATE_WORKERINFO";//更新员工信息
	  public static final String GET_WORKERMAXNO="GET_WORKERMAXNO";//得到员工最大数
	  public static final String GET_WORKERNAMEBYID="GET_WORKERNAMEBYID";//根据ID查询员工姓名
	  public static final String QUERY_W_ONEOFWORKERINFOBYID="QUERY_W_ONEOFWORKERINFOBYID";//根据员工id,查询某一个员工的信息
	
	  //图片
	  public static final String UPLOAD_IMAGE="UPLOAD_IMAGE";//上传图片
	  public static final String GET_IMAGE="GET_IMAGE";//得到图片
	  public static final String UPLOAD_MAIN_VEGEIAMGE="UPLOAD_MAIN_VEGEIAMGE";//上传菜品主图
	  public static final String GET_MBIMAGE="GET_MBIMAGE";//pc端根据菜品id得到菜品主图的大图
	  public static final String GET_ZBIMAGE="GET_ZBIMAGE";//pc端根据菜品id得到菜品子图
	  public static final String UPDATE_MAINBIGIMAGE="UPDATE_MAINBIGIMAGE";//修改菜品主图的大图

	//餐厅
		 public static final String GET_ROOM="GET_ROOM";//得到餐厅信息
		 public static final String GET_RMNAME="GET_RMNAME";//得到餐厅名称
		 public static final String GET_POINT="GET_POINT";//得到所有餐台
		 public static final String GET_RTNAME="GET_RTNAME";//得到餐台类型
		 public static final String GET_ORDER="GET_ORDER";//得到未结账的所有订单信息
		 public static final String GET_MAXPOINTNO="GET_MAXPOINTNO";//得到餐台最大数
		 public static final String GET_MAXROOMNO="GET_MAXROOMNO";//得到餐厅最大数
		 public static final String ADD_POINT="ADD_POINT";//添加餐台
		 public static final String ADD_ROOM="ADD_ROOM";//添加餐厅
		
		 public static final String UPDATE_ROOM="UPDATE_ROOM";//更新餐厅
		 public static final String UPDATE_POINT="UPDATE_POINT";//更新餐台 
		 public static final String DEL_POINT="DEL_POINT";//删除餐台
		 public static final String DEL_ROOM="DEL_ROOM";//删除餐厅
	   	public static final String UPDATE_REASON="UPDATE_REASON";
		//订单
		 public static final String ADD_ORDER="ADD_ORDER";//添加订单
		 public static final String SEARCH_ACCORDER="SEARCH_ACCORDER";//根据餐台查询订单
		 public static final String SEARCH_ORDER="SEARCH_ORDER";//得到订单信息
		 public static final String SEARCH_ODXX="SEARCH_ODXX";//得到订单详细信息
		
	 public static final String GET_VGSOMEINFO="GET_VGSOMEINFO";//得到菜品部分信息

		 //结算
		 public static final String GET_ALACCORDER="GET_ALACCORDER";//得到所有结账信息
		 public static final String SEARCH_ACCOUNT_VEGE="SEARCH_ACCOUNT_VEGE";//查询要结账的餐台上的菜品
		 public static final String INSERT_ACCOUNT_VEGE="INSERT_ACCOUNT_VEGE";//插入结账单进行保存
		 
		 //后厨手持端
		 public static final String WOKERMAKENAME="厨师";
	     public static final String WOKERCGNAME="仓库管理员";		
		 public static final String GET_LOGINAUTH="GET_LOGINAUTH";//查询登陆权限
		 
        //服务员手持端
		 public static final String GET_W_ROOM_POINTTYPE="GET_W_ROOM_POINTTYPE";       //得到餐厅和餐台类型信息
		 public static final String GET_W_POINTINFO="GET_W_POINTINFO"; //获取餐台信息   
		 public static final String UPDATE_W_ORDERINFO="UPDATE_W_ORDERINFO";//向服务器添加手持端所下订单
		 public static final String GET_WAITERPASSWORD = "GET_WAITERPASSWORD";//查询员工密码
		 public static final String GET_ROOMIDNAME="GET_ROOMIDNAME";//查询餐厅名称
		 public static final String GET_W_MCGINFO="GET_W_MCGINFO";//获得指定餐厅的菜品类别
		 public static final String GET_W_PICTUREINFO="GET_W_PICTUREINFO";//根据路径得到图片
		 public static final String SURE_WAITER="SURE_WAITER";//员工登陆权限验证
		 public static final String IS_HASlOGIN="IS_HASlOGIN";//是否已经登陆
		 public static final String UPDATE_LOGINFLG="UPDATE_LOGINFLG";//检查员工是否已登陆
		 public static final String RESET_PASSWORD="RESET_PASSWORD";//重置员工密码
		 
		 public static final String GET_W_ORDERDISHCONFIRM="GET_W_ORDERDISHCONFIRM";  //得到点菜确认单信息
		 public static final String GET_W_VEGEIDTONAME="GET_W_VEGEIDTONAME";  //将一组菜品id,转换成一组菜品名称
		 public static final String ADD_W_NEWORDER="ADD_W_NEWORDER";//根据菜品ID，从库存中扣除该菜品所用到的材料的用量
		 public static final String ADD_W_NEWDETIALORDER="ADD_W_NEWDETIALORDER";//创建点菜确认单
		 public static final String QUERY_W_POINTIDFROMORDER="QUERY_W_POINTIDFROMORDER";//查询是否存在相应桌号的订单
		 public static final String DELETE_W_ORDERCONFIRMVEGE="DELETE_W_ORDERCONFIRMVEGE";//删除点菜确认单中的菜品
		 public static final String QUERY_W_REFRUSHVEGEINFO="QUERY_W_REFRUSHVEGEINFO";//刷新菜品信息
		 public static final String DELETE_W_VEGESINGLECOUNT="DELETE_W_VEGESINGLECOUNT";//将某个菜品的数量减1
		 public static final String ADD_W_VEGESINGLECOUNT="ADD_W_VEGESINGLECOUNT";//将某个菜品的数量加1
		 public static final String QUERY_W_DESKNAMEOFORDER="QUERY_W_DESKNAMEOFORDER";//查询所有订单对应的桌子名称
		 public static final String UPDATECLIENT_POINT="UPDATECLIENT_POINT";//修改开台状态
		 public static final String GET_W_GUESTNUMBYNAME="GET_W_GUESTNUMBYNAME";//更新餐台信息
		 public static final String SEAR_SELECTTABLE_STATEMENT="SEAR_SELECTTABLE_STATEMENT";//根据餐台名称查询状态		 
		 //得到修改前的人数
		 public static final String GET_CURDESK_GUESTNUM="GET_CURDESK_GUESTNUM";
		 //修改餐台人数
		 public static final String CHANGE_GUESTNUM="CHANGE_GUESTNUM";
		 public static final String GET_W_NOPERSONPOINTINFO="GET_W_NOPERSONPOINTINFO";//得到所有无人的餐台
		 public static final String GET_W_HASPERSONPOINTINFO="GET_W_HASPERSONPOINTINFO";//得到所有有人的餐台
		 public static final String GET_W_MAINHASPERSON="GET_W_MAINHASPERSON";//用于取消开台只显示主桌
		 
		 public static final String GET_CGBYMCG="GET_CGBYMCG";//通过主类名称查询子类别
		 
		 public static final String UPDATE_W_ORDERDISHCONFIRM = "UPDATE_W_ORDERDISHCONFIRM";//更新点菜确认单

		 public static final String GET_W_PICBYVEGEID="GET_W_PICBYVEGEID";//通过菜品ID查找主图片路径
		 public static final String SEARCH_VGBYCATE="SEARCH_VGBYCATE";//根据类别获取菜品		
		 public static final String SEARCH_ZPATHBYVGID="SEARCH_ZPATHBYVGID"; //根据菜品id得到这个菜的所有子图
		 public static final String GET_W_VEGEINFO="GET_W_VEGEINFO";      //服务员手持端获得菜品信息
		 public static final String SEARCH_W_VGBYID="SEARCH_W_VGBYID";//根据菜品id得到菜品信息
		 public static final String DOWNLOAD_VEGE="DOWNLOAD_VEGE";//服务员端加载菜品信息
		 
		 //========================================PC端-前台管理
		 public static final String GOBACKORDER_GET_OC="GOBACKORDER_GET_OC"; //根据桌子名称，查询点菜确认单(即详细订单)中的内容
		 
}
