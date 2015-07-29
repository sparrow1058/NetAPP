package com.bn.constant;

import java.util.ArrayList;     
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant 
{
public static final String RESETPASSWORD="8888";
public static  String IP;
public static  int POINT;
//默认情况下的ip和point
public static final String ip="192.168.2.113";
public static final int point=9999;
//暂定桌子号
public static String roomId="001";
public static String deskId;//记录当前桌子号
public static List<String[]> vegeMsgList;//记录点菜未提交之前的菜品信息
public static String deskName;//记录当前的桌子名称
public static String defaultDeskName;
public static String userId;//记录当前的使用者
public static int guestNum;//记录当前桌子客人数量
public static final String IMAGEPATH="/mnt/sdcard/pic";//图片存储路径
public static Map<String,Integer> allguestNum=new HashMap<String,Integer>();//用来存放所有开台但没有点菜的餐台人数
//当前订单所点的菜品的信息
public static List<String[]> dcvegemsg=new ArrayList<String[]>();
public static List<String[]> curdcvegemsg=new ArrayList<String[]>();
public static Map<String,List<String[]>> allpointvegeinfo= new HashMap<String,List<String[]>>();
public static int ddd=0;
public static String FAIL="fail";
public static String OK="ok";
//用来标识查询具体餐台的条件
public static final String ALLPERSON="all";
public static final String NOPERSON="noperson";
public static final String HASPERSON="hasperson";
public static final String MAINHASPERSON="MAINHASPERSON";
public static final String COMBINEPOINT="COMBINEPOINT";
//=========================菜品图片文本信息以及订单信息================
public static final String GET_MCG="GET_MCG";//获得这类别信息（将应用其名称和id）
public static final String GET_W_MCGINFO="GET_W_MCGINFO";//获得指定餐厅的菜品类别
public static final String GET_W_PICTUREINFO="GET_W_PICTUREINFO";//通过菜品id获得图片byte[]
public static final String GET_CGBYMCG="GET_CGBYMCG";//通过主类获得子类信息
public static final String GET_W_VEGEINFO="GET_W_VEGEINFO"; //根据菜品子类别查询菜品信息
public static final String SEARCH_VGBYID="SEARCH_VGBYID";//根据菜品ID查询菜品信息显示
public static final String SEARCH_VGBYKEY="SEARCH_VGBYKEY";//根据菜品关键字查询菜品信息显示
public static final String SEARCH_VGBYNAME="SEARCH_VGBYNAME";//根据菜品名称查询菜品信息显示
public static final String UPDATE_W_VEGEMATERIAL="UPDATE_W_VEGEMATERIAL";//下单后更新菜品材料
public static final String GET_W_ORDERMAXNO="GET_W_ORDERMAXNO";//得到菜单的最大值
public static final String ADD_W_NEWORDER="ADD_W_NEWORDER";//添加信息订单
public static final String ADD_W_NEWDETIALORDER="ADD_W_NEWDETIALORDER";//添加对应的详细订单菜品信息
public static final String SEARCH_W_VGBYID="SEARCH_W_VGBYID";//根据菜品id得到菜品的信息
public static final String SEARCH_ZPATHBYVGID="SEARCH_ZPATHBYVGID";	//根据菜品id的到子图的路径
public static final String DOWNLOAD_VEGE="DOWNLOAD_VEGE";//加载菜品信息
public static final String GET_IMAGE="GET_IMAGE";//根据菜品路径得到菜品图片信息
//========================餐厅餐台信息============================
public static final String SURE_WAITER="SURE_WAITER";//根据用户名和密码判断是否合法
public static final String GET_LOGINAUTH="GET_LOGINAUTH";//获取员工权限
public static final String RESET_PASSWORD="RESET_PASSWORD";
public static final String GET_W_ROOM_POINTTYPE="GET_W_ROOM_POINTTYPE";//获取餐台的餐厅和餐厅类型
public static final String GET_W_POINTINFO="GET_W_POINTINFO";//获取对应的餐台信息
public static final String GET_W_GUESTNUMBYNAME="GET_W_GUESTNUMBYNAME";
public static final String UPDATECLIENT_POINT="UPDATECLIENT_POINT";//修改所选餐台的状态
public static final String SEAR_CURPOINT_ORDER="SEAR_CURPOINT_ORDER";//取消订单查询是否已经下单
public static final String GET_CURDESK_GUESTNUM="GET_CURDESK_GUESTNUM";//得到当前餐台的人数
public static final String GET_W_NOPERSONPOINTINFO="GET_W_NOPERSONPOINTINFO";//得到所有无人的餐台
public static final String GET_W_HASPERSONPOINTINFO="GET_W_HASPERSONPOINTINFO";//得到所有有人的餐台
public static final String CUR_POINT_ISMERGER="CUR_POINT_ISMERGER";
//================================订单管理===========================
public static final String GET_W_ORDERDISHCONFIRM="GET_W_ORDERDISHCONFIRM"; //根据桌号，获取点菜确认单中的数据(orderdishconfirm表)
public static final String GET_W_VEGEIDTONAME="GET_W_VEGEIDTONAME"; //根据菜品id获得菜品名称S
public static final String DELETE_W_ORDERCONFIRMVEGE="DELETE_W_ORDERCONFIRMVEGE";//删除点菜确认单中的菜品
public static final String DELETE_W_VEGESINGLECOUNT="DELETE_W_VEGESINGLECOUNT";//将某个菜品的数量减1
public static final String ADD_W_VEGESINGLECOUNT="ADD_W_VEGESINGLECOUNT";//将某个菜品的数量加1
//=================测试连接信息=========================
public static final String TESTCONNECT="TESTCONNECT";//测试连接
public static final String GET_W_VEGEINTROINFO="GET_W_VEGEINTROINFO";//根据菜品的id得到菜品介绍的相关信息
//==============================================
public static final String SEARCH_USERCID="SEARCH_USERCID";
public static final String SEARCH_USERBYNAME="SEARCH_USERBYNAME";
//检查员工是否已经登陆
public static final String IS_HASlOGIN="IS_HASlOGIN";
public static final String UPDATE_LOGINFLG="UPDATE_LOGINFLG";
//异常类型标识
public static final int UnknownHost=0;
public static final int SocketTimeout=1;
public static final int IOExcep=2;
public static final int StringIndexOutOfBounds=3;
public static final int NullPointer=4;
public static final int SocketExcep=5;
public static final int Excep=5;
public static final int EOFExcep=6;
public static final int ToastMessage=7;

public static Map<String,Map<String,boolean[]>> vegeMap=new HashMap<String,Map<String,boolean[]>>();

}