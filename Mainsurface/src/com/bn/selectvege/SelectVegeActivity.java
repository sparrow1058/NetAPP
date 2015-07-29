package com.bn.selectvege;

import static com.bn.selectvege.SelectVegeConstant.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.bn.R;
import com.bn.constant.Constant;
import com.bn.error.ResetErrorActivity;
import com.bn.main.MainActivity;
import com.bn.manageorder.OrderManageActivity;
import com.bn.showorder.ShowOrderActivity;
import com.bn.table.OpenTableActivity;
import com.bn.table.SelectTableActivity;
import com.bn.util.DataUtil;
import com.bn.vegeimage.VegeImageUIActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*SelectVegeActivity是主点菜界面，左侧显示菜品的主类别、子类别，当点击菜品
 子类别时，在右侧显示类别中包含的所有菜品的图片和数据
 菜品的图片和菜品数据是通过自定义的控件VegeImageGrid来显示的*/
public class SelectVegeActivity extends Activity 
{
	public static int count=0;
	//存放菜品信息的Map
	public static Map<String,Object> vegeInfoMap;
	//存放菜品类别信息的Map
	public static Map<String,Object> childVegeInfo;
	//存放菜品已选标志位的Map
	public static Map<String,boolean[]> vegeFlg;
	public static String waitMsg;
	//保存离开SelectVegeActivity界面时所显示的子类别名称
	public static String childcateVege;
	//保存离开SelectVegeActivity界面时所选中的菜品主类的名称
	public boolean mainCateStack[];
	//保存离开SelectVegeActivity界面时所选中的菜品子类的名称
	public String curChildCatePosi="0-0";
	public int groupPosi=-1;
	public int childPosi=-1;
	boolean storeState=false;
	boolean firstInto=true;
	public static int intoCount;
	public static boolean onclickflag=true;
	public static boolean onchildclickflag=true;
	//存放菜品图片
	Bitmap vegeImage[]=null;
	//存放菜品数据
	List<String[]> vegeInfo=new ArrayList<String[]>();
	//自定义控件
	VegeImageGrid vegeImageGrid;
	//子类别名称
	Dialog errordialog;
    String errorMsg;
    int excepFlg;
    ProgressDialog waitDialog;
    ArrayAdapter<String> autoCompleteAdapter;
    String selectedVegeName;
    boolean vegeSelectedFlg=false;
    //查询对话框
    TextView tvtableid;
    //菜品类别ListView控件
    ExpandableListView vegeCateExpLV;
    Bundle b;
    public static int inde=-1;
    Intent intent=null;
	Handler handler=new Handler()
   	{
		@Override
   		public void handleMessage(Message msg) 
   		{
   			super.handleMessage(msg);
   			switch(msg.what)
   			{
   				case FRUSH_VEGEIMAGEGRID:
					if(vegeImageGrid!=null)
					{
						vegeImageGrid.invalidate();
					}
				break;
				//获取餐台信息错误信息Dialog
	    		case SHOW_TABLEERROE_DIALOG:
	    			b=msg.getData();
	    			ResetErrorActivity.errorMsg=b.getString("msg");
					ResetErrorActivity.errorFlg="CancleTableActivityFlg";
					intent=new Intent(SelectVegeActivity.this,ResetErrorActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	    		break;
	    		//打开错误对话框
				case OPEN_ERROR_DIALOG_MESSAGE:
					//获取消息中的数据
					b=msg.getData();
					ResetErrorActivity.errorMsg=b.getString("msg");
					ResetErrorActivity.errorFlg="SelectVegeActivityFlg";
					intent=new Intent(SelectVegeActivity.this,ResetErrorActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
				 break;
				 //显示toast提示信息
				 case OPEN_ERROR_TOAST_MESSAGE:
					 b = msg.getData();
					 Toast.makeText(SelectVegeActivity.this,b.getString("msg"),Toast.LENGTH_LONG).show();
			     break; 
			     //跳转到菜品介绍
			     case GOTO_VEGE_INTRO_MESSAGE:
			    	 intent=new Intent(SelectVegeActivity.this,VegeImageUIActivity.class);
			    	 intent.putExtra("selectedVegeName", selectedVegeName);
	   				 startActivity(intent);
	   				 overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
				 break;
				 //跳转到选择餐台界面
			     case GO_SELECT_TABLE:
	   				 Intent i=new Intent(SelectVegeActivity.this,SelectTableActivity.class);
	   				 startActivity(i);
	   				 overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
	   		     break;
	   		}
   		}
   	};
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		MainActivity.al.add(this);		
		super.onCreate(savedInstanceState);
		//强制横屏，无标题，全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(4);
		setContentView(R.layout.vege);
		//对进入选菜界面的次数进行累加,当intoCount=0时表示首次进入选菜界面，应该将界面设为初始状态
		intoCount++;		
		if(vegeInfoMap!=null)
		{
			//获取菜品图片
			vegeImage=(Bitmap[])vegeInfoMap.get("bmArr");
			//获取菜品数据
			vegeInfo=(List<String[]>)vegeInfoMap.get("vegeInfolist");
			System.out.println("vegeInfo==================="); //
			for(String[] arr : vegeInfo)
			{
				for(String str : arr)
				{
					System.out.print(str+"**");
				}
				System.out.println();
			}
			//ExpandableListView用来显示菜品的主类别以及每个主类别包含的子类别
			vegeCateExpLV=(ExpandableListView)this.findViewById(R.id.selectvege_eplv);
			if(vegeCateExpLV==null)
			{
				System.out.println("is null================");
			}
			//传入主类别信息     
			vegeCateExpLV.setAdapter(new MyExpandableListView(vegeInfoMap));
			//自定义控件
			vegeImageGrid=(VegeImageGrid)this.findViewById(R.id.vig);
			restoreUIState();
			//初始化VegeImageGrid对象中的数据
			initGridThread(); 
			//对自定义控件添加监听//////////////////////////////////////////////////////////////////////////////////
		   vegeImageGrid.addVegeImageGridListener
	         (
	        	new VegeImageGridListener()
	        	{
					public void onItemClick(int index,int sum) 
					{
						if(onclickflag&&sum==1)
						{
							if(!vegeFlg.get(childcateVege)[index])
							{
								String str[]=new String[8];
								str[0]=vegeInfo.get(index)[1];//菜品名称
								str[1]=vegeInfo.get(index)[2];//菜品价格
								str[2]="1";//菜品数量
								str[3]="";//要求
								str[4]=vegeInfo.get(index)[0];//菜品ID
								str[6]=childcateVege;
								str[7]=index+"";
								Constant.dcvegemsg.add(str);
								vegeFlg.get(childcateVege)[index]=true;
								initGridThread();
							}
							else
							{
								vegeFlg.get(childcateVege)[index]=false;
								for(int i=0;i<Constant.dcvegemsg.size();i++)
								{
									if(Constant.dcvegemsg.get(i)[4].equals(vegeInfo.get(index)[0]))
									{
										Constant.dcvegemsg.remove(i);
										i--;
									}
								}
								initGridThread();
							}
						}
						else if(onclickflag)
					    {
						  inde=index;
						  VegeImageUIActivity.indexno=index;
//						  DataUtil.getVegeIntro(vegeInfo.get(index)[0], handler);
						  onclickflag=false;
						  childcateVege=vegeInfo.get(index)[4];
				    	  intent=new Intent(SelectVegeActivity.this,VegeImageUIActivity.class);
		   				  startActivity(intent);
		   				  overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
		   				  SelectVegeActivity.this.finish();
					    }	
					}
	        	}
	        );
		}
		//返回主封面
		Button back=(Button)findViewById(R.id.vege_back);
       	//返回主封面按钮的注册监听
		back.setOnClickListener
		(	
		   new OnClickListener()
		   {
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(SelectVegeActivity.this,MainActivity.class);
				    i.putExtra("selectvege","selectvege");
					startActivity(i);
					SelectVegeActivity.this.finish();
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
				}       
		   }
		); 
		//开台
		Button point_open=(Button)findViewById(R.id.vege_opentable);
		//开台按钮注册监听
		point_open.setOnClickListener
		(
       		new OnClickListener() 
       		{
       			@Override
    			public void onClick(View v) 
    			{
				   Intent intent=new Intent();
   				   intent.setClass(SelectVegeActivity.this,OpenTableActivity.class);
   				   intent.putExtra("resource","selectVege");
   				   startActivity(intent);
    			}
			}
		);
		//添加菜品按钮
		Button addvege=(Button) this.findViewById(R.id.vege_addorder);	
		addvege.setOnClickListener
		(
		   new OnClickListener()
	       {
				@Override
				public void onClick(View v) 
				{
					new Thread()
					{
						public void run()
						{
							this.setName("addvege Thread");
							SelectTableActivity.operState=5;//添加菜品选择餐台
							SelectTableActivity.ishasPerson=Constant.HASPERSON;
							DataUtil.searchTableState(handler,Constant.HASPERSON);
						}
					}.start();
				}
	       	}
        );
		//我的账单
		Button moregn=(Button) this.findViewById(R.id.vege_moregn);
        //我的账单按钮的注册监听
		moregn.setOnClickListener
		(
		   new OnClickListener()
	       {
				@Override
				public void onClick(View v) 
				{		
					Intent intent=new Intent();
   				    intent.setClass(SelectVegeActivity.this, OrderManageActivity.class);
   				    startActivity(intent);
   				    overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
				}
	       	}
        );
		//已点菜品
		Button bt_order=(Button)findViewById(R.id.vege_showorder);
        bt_order.setOnClickListener
        (
	    	new OnClickListener() 
	    	{
				@Override
				public void onClick(View v)
				{//将已选的菜品放入订单数组
			        Intent intent = new Intent(SelectVegeActivity.this,ShowOrderActivity.class);
			        startActivity(intent);
			        SelectVegeActivity.this.finish();
			        overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
				}    	
			}  
        );
		tvtableid=(TextView) this.findViewById(R.id.vege_tablebt);
		if(Constant.deskName==null)
		{
			tvtableid.setText("未开台");
		}
		else
		{
    	   tvtableid.append(Constant.deskName);
    	   Constant.defaultDeskName=Constant.deskName;
		}
		
	vegeCateExpLV.setOnGroupExpandListener(new OnGroupExpandListener()
		{
			@Override
			public void onGroupExpand(int groupPosition) 
			{
				mainCateStack[groupPosition]=true;
			}
			
		});
		vegeCateExpLV.setOnGroupCollapseListener( new OnGroupCollapseListener()
		{
			@Override
			public void onGroupCollapse(int groupPosition) 
			{
				mainCateStack[groupPosition]=false;
			}
		});
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		firstInto=false;
		storeState=true;
		SharedPreferences uiState=getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = uiState.edit();
		//标示已经对原有状态进行保存
		editor.putBoolean(ADDING_ITEM_KEY, storeState);
		//保存当前VegeImageGrid展示的菜品的子类名称
		editor.putString(CUR_CHILDCATE_NAME, childcateVege);
		//保存当前被选中的子类的位置
		editor.putString(CUR_CHILDCATE_POSI, curChildCatePosi);
		//保存ExpandableListView中GroupView被打开的状态
		StringBuffer mainCateState=new StringBuffer();
		for(boolean b : mainCateStack)
		{
			if(b)
			{
				mainCateState.append("1"+"、");
			}
			else
			{
				mainCateState.append("0"+"、");
			}
		}
		editor.putString(MAINCATE_STATE_STACK,mainCateState.toString());
		editor.commit();
	}	
	@SuppressWarnings("unused")
	private void restoreUIState()
	{
		SharedPreferences uiState=getPreferences(Context.MODE_PRIVATE);
		//程序启动后，第一次进入选菜界面
		if(intoCount==1)
		{
			SharedPreferences.Editor editor = uiState.edit();
			//标示是否对Activity原有状态进行保存
			editor.putBoolean(ADDING_ITEM_KEY, false);
			storeState=false;
			for(int i=0; i<mainCateStack.length; i++)
			{
				mainCateStack[i]=false;
			}
			groupPosi=-1;
			childPosi=-1;
		}
		else
		{ 
			storeState = uiState.getBoolean(ADDING_ITEM_KEY, false);
			String childCateNameInfo=uiState.getString(CUR_CHILDCATE_NAME,null);
			String childCatePosiInfo=uiState.getString(CUR_CHILDCATE_POSI,null);
			String mainCateState=uiState.getString(MAINCATE_STATE_STACK,null);
			String first=uiState.getString(FIRSH_INTO_SELECTVEGEACTIVITY,"");
			
			if(storeState) //如果状态信息被保存
			{
				childcateVege=childCateNameInfo;
				if(childCatePosiInfo!=null)
				{
					curChildCatePosi=childCatePosiInfo;
				}
				String posi[]=curChildCatePosi.split("-");
				groupPosi=Integer.parseInt(posi[0]);
				childPosi=Integer.parseInt(posi[1]);
				String state[]=mainCateState.split("、");
				for(int i=0; i<state.length; i++)
				{
					if(state[i].equals("0"))
					{
						mainCateStack[i]=false;
					}
					else
					{
						mainCateStack[i]=true;
						vegeCateExpLV.expandGroup(i);
					}
				}				
			}
		}
	}
	private class MyExpandableListView extends BaseExpandableListAdapter
	{
		//菜品主类信息
		List<String[]> mainCateList;
		//菜品子类信息
		List<String[]> childCateList;
		
	 	@SuppressWarnings("unchecked")
		public MyExpandableListView(Map<String,Object> vegeCateInfo)
		{
	 		super();
	 		mainCateList=(List<String[]>)vegeCateInfo.get("vegeMainCateInfo");
	 		childCateList=(List<String[]>)vegeCateInfo.get("vegeChildCateInfo");
	 		//用来保存每个GroupView的打开状态
	 		mainCateStack=new boolean[mainCateList.size()];
		}
		@Override
		public Object getChild(int groupPosition, int childPosition) 
		{
			return childCateList.get(groupPosition)[childPosition];
		}		
		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}
		@Override
		public View getChildView(final int groupPosition,final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) 
		{		
			LinearLayout llayout = new LinearLayout(SelectVegeActivity.this);
			llayout.setOrientation(LinearLayout.VERTICAL);
			llayout.setPadding(5,5,5,5);//设置四周留白
			if(storeState==true && groupPosi==groupPosition && childPosi==childPosition)
			{
				storeState=false;
				llayout.setBackgroundColor(Color.rgb(240, 144, 0));
			}
			else
			{
				if(childcateVege.equals(childCateList.get(groupPosition)[childPosition]))
				{
					llayout.setBackgroundColor(Color.rgb(240, 144, 0));
				}
				else
				{
					llayout.setBackgroundResource(R.drawable.expandlvline);
				}
			}
			TextView childTV= new TextView(SelectVegeActivity.this);
			childTV.setHeight(60);
			childTV.setTextSize(21);//设置字体大小
			childTV.setGravity(Gravity.CENTER);
			childTV.setTextColor(Color.rgb(204, 102, 0));
			childTV.setText(getChild(groupPosition,childPosition).toString());
			llayout.addView(childTV);
			//对菜品子类别的Item注册监听
			childTV.setOnClickListener
				(
					new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							if(onchildclickflag)
							{
								onchildclickflag=false;
								View tempV=v;
								//用于获得该类别的Text内容
								TextView inner_tv=(TextView)tempV;
								//获得属于该子类别的菜品信息
								childcateVege=inner_tv.getText().toString();
								//获得该子类的位置
								curChildCatePosi=groupPosition+"-"+childPosition;
								 //绘制当客户按下该类别时，ExpandableListView子项颜色变化
								for(int i=0;i<vegeCateExpLV.getChildCount();i++)
								{
									LinearLayout inner_ll=(LinearLayout)vegeCateExpLV.getChildAt(i);
									inner_ll.setBackgroundResource(R.drawable.expandlvline);
								}
								LinearLayout ll=(LinearLayout)((TextView)v).getParent();
								//绘制当前子项颜色
								ll.setBackgroundColor(Color.rgb(240, 144, 0));
								initGridThread();
							}
						}	
					}
				);
			
			return llayout;
		}
		@Override
		public int getChildrenCount(int arg0) 
		{
			return childCateList.get(arg0).length;
		}
		//获取主类别名称
		@Override
		public Object getGroup(int arg0) 
		{
			return mainCateList.get(arg0)[1];
		}
		//获取主类别数量
		@Override
		public int getGroupCount() 
		{
			return mainCateList.size();
		}
		@Override
		public long getGroupId(int arg0) 
		{
			return arg0;
		}
		@Override
		public View getGroupView(final int groupPosition, boolean arg1, View arg2,
				ViewGroup arg3) 
		{
			LinearLayout llayout = new LinearLayout(SelectVegeActivity.this);
			llayout.setOrientation(LinearLayout.HORIZONTAL);
			llayout.setPadding(5,2,0,0);//设置四周留白
			llayout.setBackgroundResource(R.drawable.expandlvline);	
			TextView mainTV= new TextView(SelectVegeActivity.this);
			mainTV.setHeight(60);
			mainTV.setTextSize(21);//设置字体大小
			mainTV.setGravity(Gravity.CENTER);
			mainTV.setTextColor(Color.rgb(204, 102, 0));
			mainTV.setText(getGroup(groupPosition).toString());//设置主项目的名称
			llayout.addView(mainTV,0);
			return llayout;
		}
		@Override
		public boolean hasStableIds() 
		{
			return true;
		}
		@Override
		public boolean isChildSelectable(int arg0, int arg1) 
		{
			return true;
		}
	 }
	public void initVegeImageGrid()
	{
		//设置菜品的图片
		vegeImageGrid.setBMSer(vegeImage,vegeFlg.get(childcateVege));
		//设置菜品数据
		vegeImageGrid.setTextSer(vegeInfo, SelectVegeConstant.FONT_COLOR);
		//设置图片，字体的宽高，间距，列数，左侧留白
		vegeImageGrid.setSizeSer
		(
			SelectVegeConstant.PIC_HEIGHT,
			SelectVegeConstant.PIC_WIDTH,
			SelectVegeConstant.PIC_FLG_HEIGHT,
			SelectVegeConstant.PIC_FLG_WIDTH,
			SelectVegeConstant.FONT_SIZE,
			SelectVegeConstant.PIC_SPAN,
			SelectVegeConstant.PIC_COLCOUNT
		);
		vegeImageGrid.calTotalHeight();
	}
	//显示九宫格信息的线程
   @SuppressWarnings("unchecked")
   public void initGridThread()
   {
	   	new Thread()
		{
			public void run()
			{
				this.setName("initGrid Thread");
				DataUtil.getVegeInfo(childcateVege,handler);
				if(childVegeInfo==null)
				{
					VegeImageUIActivity.childVegeInfo=childVegeInfo;
					Message msg=new Message();
					msg.what=SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE;
					Bundle b=new Bundle();
					b.putString("msg","获取数据出错是否重新获取");
					handler.sendMessage(msg);
				}
				else
				{
					vegeImage=(Bitmap[])childVegeInfo.get("bmArr");
					vegeInfo = (List<String[]>)childVegeInfo.get("vegeInfolist");
					VegeImageUIActivity.childVegeInfo=childVegeInfo;
					initVegeImageGrid();
					handler.sendEmptyMessage(FRUSH_VEGEIMAGEGRID); 
				}
			}
		}.start();	
	  }
   public void initGalleryThread(final String vegeCate)
   {
	   	new Thread()
		{
			public void run()
			{
				this.setName("initGalleryThread");
				DataUtil.getVegeInfoFor(vegeCate,handler);
			}
		}.start();	
	  }
}