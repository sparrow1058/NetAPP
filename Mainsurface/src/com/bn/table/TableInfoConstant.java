package com.bn.table;

public class TableInfoConstant 
{
   //列数
   public static final int COLCOUNT=5;
   //桌子图片的宽高
   public static final float TIMAGE_WIDHT=150;
   public static final float TIMAGE_HEIGHT=50;
   
   //左边距
   public static final float LEFT_MARGIN=20;
   public static final float TOP_MARGIN=20;
   //图片间距
   public static final float SPAN=20;
   //字体大小颜色
   public static final float TEXTSIZE=16;
   public static final String FONT_COLOR="#000000";
   public static final float TEXTHEIGHT=50;
   //handlerid
   public static final int INIT_TABLEGRID_HANDLERID=0;//初始化餐台信息
   public static final int GO_SELECT_TABLE=1;//进入选台界面
 //  public static final int GO_SELECT_TOW_TABLE=8;//选择并台界面
   public static final int SHOW_TABLEERROE_DIALOG=2;//初始餐台信息出现错误显示错误dialog
   public static final int SHOW_UPDATETABLEERROR_DIALOG=6;//更新餐台状态时候出现错误
   public static final int SHOW_WAIT_DIALOG=3;//打开等待对话框
   public static final int CANCEL_WAIT_DIALOG=4;//取消等待对话框
   public static final int SHOW_TOAST_MESSAGE=5;//权限错误toast显示
   public static final int CLOSE_CUR_ACTIVITY=7;
   public static final int CLOSE_CANCLETABLE=8;
   public static final int GUEST_NUM_MESSAGE=9;
   
   //Dialog——id
   public static final int TABLEERROR_DIALOG_ID=0;//餐台错误dialog,点击确认后重新执行
   public static final int UPDATETABLEERROR_DIALOG_ID=3;//更新餐台信息dialog
   public static final int TABLEWAIT_DIALOG_ID=1;//等待对话框
}
