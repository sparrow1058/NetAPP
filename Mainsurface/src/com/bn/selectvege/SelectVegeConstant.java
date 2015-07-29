package com.bn.selectvege;

//菜品主显示界面中要用到的静态常量
public class SelectVegeConstant 
{
		public static final float PIC_WIDTH=260;//图片的宽
		public static final float PIC_HEIGHT=195;//图片的高
		public static final float PIC_FLG_HEIGHT=40;//标示所用图片的高
		public static final float PIC_FLG_WIDTH=40;//标示所用图片的宽
		public static final float PIC_SPAN=5;//图片的间距	
		public static final int PIC_COLCOUNT=3;//九宫格的列数	
		public static final int FONT_SIZE=18;//字体大小
		public static final String FONT_COLOR="#000000";//字体颜色		
		//handler消息
		public static final int FRUSH_VEGEIMAGEGRID=0;//缓冲图片handlerid
		public static final int OPEN_ERROR_DIALOG_MESSAGE=1;//打开错误对话框handler
		public static final int OPEN_ERROR_TOAST_MESSAGE=2;
		public static final int GOTO_VEGE_INTRO_MESSAGE=3;//进入VegeActivity界面的handler Id
		public static final int OPEN_WAIT_DIALOG_MESSAGE=4;
		public static final int CANCEL_WAIT_DIALOG_MESSAGE=5;
		public static final int GO_SELECT_TABLE=6;//初始化餐台界面
		public static final int SHOW_TABLEERROE_DIALOG=7;
		public static final int INIT_AUTOCOMPLETETEXTVIEW=8;
		//显示对话框
		public final static int OPEN_ERROR_DIALOG_ID=0;//错误对话框ID
		public final static int OPEN_WAIT_DIALOG_ID=1;//等待对话框ID
		public final static int CANCEL_WAIT_DIALOG_ID=2;//取消等待对话框ID
		public final static int TABLEERROR_DIALOG_ID=3;//桌子初始化错误ID
		public final static String TEXT_ENTRY_KEY="TEXT_ENTRY_KEY";
		public final static String ADDING_ITEM_KEY="ADDING_ITEM_KEY";
		public final static String CUR_CHILDCATE_NAME="CUR_CHILDCATE_NAME"; //当前选中的子类的名称
		public final static String CUR_CHILDCATE_POSI="CUR_CHILDCATE_POSI";//当前选中的子类的位置
		public final static String MAINCATE_STATE_STACK="MAINCATE_STATE_STACK";
		public final static String FIRSH_INTO_SELECTVEGEACTIVITY="FIRSH_INTO_SELECTVEGEACTIVITY";
}
