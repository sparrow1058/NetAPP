package com.bn.constant;


import javax.swing.tree.DefaultMutableTreeNode;

public class Constant {
	public static final String SOCKET_ERROR="0";
	public static final String SOCKET_IOERROR="1";
	public static String operator;
	//在菜品没有图片时上传的图片
	public static final String IMAGENULL_PATH="C:\\Users\\lenovo\\Desktop\\上传的图片\\null.jpg";
	//得到服务器上当前菜品没有图片的图片路径
	public static String vegenullimage;
	public static String vegenullBigimage;
	public final static String IP="localhost";//获取当前ip
	public final static int POINT=9999;//定义piont为9999
	//树 的节点静态值
	public static DefaultMutableTreeNode temproot=new DefaultMutableTreeNode("点菜系统");
	//子节点
	public static DefaultMutableTreeNode[] dmtn=
	{
		new DefaultMutableTreeNode("菜谱管理"),new DefaultMutableTreeNode("餐位管理"),
		new DefaultMutableTreeNode("员工管理"),new DefaultMutableTreeNode("订单管理"),
		new DefaultMutableTreeNode("结算管理")
	};
	//菜品管理子节点
	public static DefaultMutableTreeNode[] vegenode=
	{
			new DefaultMutableTreeNode("主类管理"),new DefaultMutableTreeNode("类别管理"),
			new DefaultMutableTreeNode("菜系管理"),new DefaultMutableTreeNode("计量单位管理"),
			new DefaultMutableTreeNode("规格管理"),new DefaultMutableTreeNode("菜品管理")
	};
   //员工管理子节点
	public static DefaultMutableTreeNode[] authority=
	{
		new DefaultMutableTreeNode("员工管理")
	};
	//员工信息
	public static String workerPrivateInfo[] = 
	{
		"员工ID"
		,"员工姓名"
		,"员工密码"
		,"性别"
		,"角色"
	};
	//桌子信息子节点
	public static DefaultMutableTreeNode[] desknode=
	{
		new DefaultMutableTreeNode("大厅"),new DefaultMutableTreeNode("包厢")
	};
	
	//餐厅节点
	public static DefaultMutableTreeNode[] roompointnode=
	{
		new DefaultMutableTreeNode("餐厅信息")
    };
	//订单管理子节点
	public static DefaultMutableTreeNode[] ordernode=
	{
		new DefaultMutableTreeNode("订单信息")
    };
	//结账节点
	public static DefaultMutableTreeNode[] accountnode=
	{
		new DefaultMutableTreeNode("结账")
    };
	
 //=========================连接服务器请求具体信息的字段头=============================
  //菜品主类
  public static final String GET_MCG="GET_MCG";
  public static final String GET_MCGMAXNO="GET_MCGMAXNO";
  public static final String UPDATE_CMG="UPDATE_CMG";
  public static final String ADD_CMG="ADD_CMG";
  public static final String DEL_CMG="DEL_CMG";
  ///菜品类别
  public static final String GET_CG="GET_CG";
  public static final String GET_CGMAXNO="GET_CGMAXNO";
  public static final String ADD_CG="ADD_CG";
  public static final String UPDATE_CG="UPDATE_CG";
  public static final String DEL_CG="DEL_CG";
  //菜系
  public static final String GET_VT="GET_VT";
  public static final String GET_VTMAXNO="GET_VTMAXNO";
  public static final String UPDATE_VT="UPDATE_VT";
  public static final String DEL_VT="DEL_VT";
  //计量单位
  public static final String GET_CU="GET_CU";
  public static final String GET_CUMAXNO="GET_CUMAXNO";
  public static final String UPDATE_CU="UPDATE_CU";
  public static final String DEL_CU="DEL_CU";
  //规格
  public static final String GET_VS="GET_VS";
  public static final String GET_VSMAXNO="GET_VSMAXNO";
  public static final String UPDATE_VS="UPDATE_VS";
  public static final String DEL_VS="DEL_VS";
  //菜品图片
  public static final String DELETE_ZBIMAGE="DELETE_ZBIMAGE";
  public static final String ISHAVENULLPIC="ISHAVENULLPIC";
  public static final String GET_VEGEIMAGE="GET_VEGEIMAGE";
  public static final String GET_VEGEIMAGEMAXNO="GET_VEGEIMAGEMAXNO";
  public static final String INSERT_IMAGE="INSERT_IMAGE";
  public static final String PDVEGEMAIN="PDVEGEMAIN";
  public static final String GET_VEGEMAINIMAGE="GET_VEGEMAINIMAGE";
  public static final String UPDATE_MAINIMAGE="UPDATE_MAINIMAGE";
  public static final String UPLOAD_IMAGE="UPLOAD_IMAGE";
  public static final String GET_IMAGE="GET_IMAGE";
  public static final String UPLOAD_MIMAGE="UPLOAD_MIMAGE";
  public static final String UPLOAD_ZIMAGE="UPLOAD_ZIMAGE";
  public static final String GET_W_PICBYVEGEID="GET_W_PICBYVEGEID";//根据菜品id得到主图路径
  public static final String UPLOAD_MAIN_VEGEIAMGE="UPLOAD_MAIN_VEGEIAMGE";
  public static final String GET_MBIMAGE="GET_MBIMAGE";//根据菜品id得到主图大图路径
  public static final String GET_ZBIMAGE="GET_ZBIMAGE";//根据菜品id得到子图路径
  public static final String UPDATE_MAINBIGIMAGE="UPDATE_MAINBIGIMAGE";
  
  //菜品
  public static final String GET_VEGE="GET_VEGE";
  public static final String GET_VEGEMAXNO="GET_VEGEMAXNO";
  public static final String GETMCGNAMEBYCGNAME="GETMCGNAMEBYCGNAME";
  public static final String ADD_VEGE="ADD_VEGE";
  public static final String DEL_VEGE="DEL_VEGE";
  public static final String GET_YYCF="GET_YYCF";
  public static final String GET_CLCF="GET_CLCF";
  public static final String UPDATE_VEGE="UPDATE_VEGE";
  public static final String UPDATESSJG="UPDATESSJG";
  public static final String UPDATECXJG="UPDATECXJG";
  public static final String SEARCH_VEGE="SEARCH_VEGE";
  public static final String GS_VEGE="GS_VEGE";
  public static final String GET_USEVEGE="GET_USEVEGE";
  //员工
  public static final String GET_WOKERCG="GET_WOKERCG"; 
  public static final String GET_WOKERWAITER="GET_WOKERWAITER";
  
  public static final String GET_WORKERINFO="GET_WORKERINFO";
  public static final String GET_ROLEINFO="GET_ROLEINFO";
  public static final String GET_AUTHORITYINFO="GET_AUTHORITYINFO";
  public static final String GET_AUTHORITY_ROLE="GET_AUTHORITY_ROLE";
  public static final String GET_DEPTIDNAME = "GET_DEPTIDNAME";
  public static final String GET_GROUPIDNAME ="GET_GROUPIDNAME";
  public static final String GET_WORKERIMAGE ="GET_WORKERIMAGE";
  
  public static final String ADD_WORKERINFO = "ADD_WORKERINFO";
  public static final String UPLOAD_WORKERIMAGE = "UPLOAD_WORKERIMAGE";
  public static final String DEL_WORKERINFO="DEL_WORKERINFO";
  public static final String DEL_ROLE="DEL_ROLE";
  public static final String DEL_AUTHORITY="DEL_AUTHORITY";
  public static final String DEL_AUTHROLE="DEL_AUTHROLE";

  public static final String UPDATE_WORKERINFO="UPDATE_WORKERINFO";
  public static final String UPDATE_ROLE="UPDATE_ROLE";
  public static final String UPDATE_AUTHORITY="UPDATE_AUTHORITY";
  public static final String UPDATE_AUTHROLE="UPDATE_AUTHROLE";

  public static final String GET_WORKERMAXNO="GET_WORKERMAXNO";
  public static final String GET_ROLEMAXNO="GET_ROLEMAXNO";
  public static final String GET_AUTHORITYMAXNO="GET_AUTHORITYMAXNO";
  public static final String GET_AUTHROLEMAXNO="GET_AUTHROLEMAXNO";
  public static final String GET_WORKERNAMEBYID="GET_WORKERNAMEBYID";
  public static final String QUERY_W_ONEOFWORKERINFOBYID="QUERY_W_ONEOFWORKERINFOBYID";//根据员工id,查询某一个员工的信息
	
  //餐台
  public static final String GET_ROOM="GET_ROOM";//获取餐厅信息
  public static final String GET_RMNAME="GET_RMNAME";//得到餐台名称
  public static final String GET_POINT="GET_POINT";//得到餐台信息
  public static final String GET_RTNAME="GET_RTNAME";//得到餐台名称 
  public static final String GET_MAXPOINTNO="GET_MAXPOINTNO";//得到对应餐厅中的餐台最大值
  public static final String GET_MAXROOMNO="GET_MAXROOMNO";//得到餐厅最大值
  //更新操作
  public static final String ADD_POINT="ADD_POINT";
  public static final String ADD_ROOM="ADD_ROOM";
  public static final String UPDATE_ROOM="UPDATE_ROOM";
  public static final String UPDATE_POINT="UPDATE_POINT";
  public static final String DEL_POINT="DEL_POINT";
  public static final String DEL_ROOM="DEL_ROOM";
 
  //订单
  public static final String GET_ORDER="GET_ORDER";//得到未结账的订单信息
  public static final String SEARCH_ACCORDER="SEARCH_ACCORDER";//
  public static final String SEARCH_ORDER="SEARCH_ORDER";//根据输入的桌子名称查询订单
  public static final String SEARCH_ODXX="SEARCH_ODXX";//查询具体的订单信息
  //结算方式
  public static final String GET_SA_MODE="GET_SA_MODE";//得到所有的结算方式
  public static final String DEL_SA_MODE="DEL_SA_MODE";
  public static final String UPDATE_SA_MODE="UPDATE_SA_MODE";
  public static final String GET_SA_MAXNO="GET_SA_MAXNO";

  public static final String SEARCH_ACCOUNT_VEGE="SEARCH_ACCOUNT_VEGE";//查询要结账的餐台上的菜品
  public static final String INSERT_ACCOUNT_VEGE="INSERT_ACCOUNT_VEGE";
   
//订单
  public static final String GET_NOBOOKDESKNAME="GET_NOBOOKDESKNAME";//根据预订时间查看该时间段没有被预订的桌子名称
  public static final String INSERT_BOOKORDER="INSERT_BOOKORDER"; //提交预订订单
  public static final String GET_ALLTCNAME="GET_ALLTCNAME";
  public static final String GET_TCINFO_BYNAME="GET_TCINFO_BYNAME";//根据套餐色名称查看此套餐的数量，价格信息
  public static final String GET_BOOKORDER="GET_BOOKORDER";//得到所有预定信息
  public static final String GET_BORDER_BYTIME="GET_BORDER_BYTIME";//根据时间查询订单
  public static final String DEL_BXX_BYID="DEL_BXX_BYID";//根据给定的预定订单号查询详细订单内容  
  public static final String SURE_WAITER="SURE_WAITER";//密码
  public static final String GET_LOGINAUTH="GET_LOGINAUTH";//权限
  
  //登陆成功后检查今日是否有预定把预定桌子都置忙
  public static final String UPDATE_TODAY_POINTSTATE="UPDATE_TODAY_POINTSTATE";
}
