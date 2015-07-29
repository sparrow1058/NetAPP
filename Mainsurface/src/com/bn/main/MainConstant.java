package com.bn.main;

//主界面中需要用到的静态常量 
public class MainConstant 
{	
	//画廊图片的宽和高
//    public static final float GALLERY_PIC_WIDTH=260;
//    public static final float GALLERY_PIC_HEIGHT=195;    
    //画廊图片的间距
//    public static final float GALLERY_PIC_SPAN=20;    
    //设置按钮的起始坐标和宽，高
    public static final float BUTTONBAR_LEFTMARGIN=77;
    public static final float BUTTON_WIDTH=145;
    public static final float BUTTON_HEIGHT=109;
    public static final float BUTTON_SPAN=0;    
    //文字大小
    public static final float FONT_SIZE=20;
    public static final String FONT_COLOR="#000000";  
	//对话框id
	public final static int OPEN_WAITDIALOG_ID=0;//打开等待对话框
//	public final static int OPEN_GALLERYERROR_DIALOG_ID=1;//打开画廊错误对话框
	public final static int OPEN_GRIDERROR_DIALOG_ID=2;//打开九宫格错误对话框
	public final static int OPEN_FINISH_DIALOG_ID=3;//打开完成加载对话框
	//handler消息id
	public final static int OPEN_WAIT_DIALOG_MESSAGE=0;//发送打开等待对话框消息
	public final static int CANCEL_WAIT_DIALOG_MESSAGE=1;//发送取消对话框消息
	public final static int GOTO_SELECT_VEGE=2;//发送跳转到选菜界面
//	public final static int INIT_GALLERY_INFO=3;//初始化画廊消息
//	public final static int OPEN_GALLERYERROR_DIALOG_MESSAGE=4;//发送打开画廊错误
	public final static int OPEN_ERROR_TOAST_MESSAGE=5;//发送显示toast消息
	public final static int OPEN_GRIDERROR_DIALOG_MESSAGE=6;//发送九宫格错误消息
	public final static int GOTO_VEGE_INTRO_MESSAGE=7;//发送跳转到菜品介绍界面
	public final static int OPEN_WAIT_DIALOG_FOR_GRID=8;//打开九宫格等待消息
	public final static int CANCEL_WAIT_DIALOG_FOR_GRID=9;//取消九宫格等待消息
	public static final int OPEN_UPLOADERROR_DISLOG_MESSAGE=11;//打开下载错误消息
	public static final int OPEN_UPLOAD_DIALOG_MESSGE=12;
}
