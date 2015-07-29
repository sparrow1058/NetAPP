package com.bn.util;

import static com.bn.table.SelectTableActivity.*;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import static com.bn.table.TableInfoConstant.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.bn.constant.Constant;
import com.bn.login.LoginConstant;
//import com.bn.main.MainActivity;
import com.bn.main.MainConstant;
import com.bn.manageorder.OrderManageActivity;
import com.bn.manageorder.OrderManageConstant;
import com.bn.reset.ResetConstant;
import com.bn.resetpw.ResetPassWordConstant;
import com.bn.selectvege.SelectVegeConstant;
import com.bn.selectvege.SelectVegeActivity;
import com.bn.showorder.ShowOrderConstant;
import com.bn.table.SelectTableActivity;
import com.bn.table.TableInfoConstant;
//import com.bn.user.UserConstant;
import com.bn.util.DataGetUtilSimple;
import com.bn.vegeimage.VegeImageConstant;
import com.bn.vegeimage.VegeImageUIActivity;
import com.bn.vegeinfo.VegeActivity;
/* DataUtil工具类，用来封装耗时的工作 */
public class DataUtil 
{
	public static void resetPassWord(final String userid,final String password,final String newpw,final Handler handler)
	{
		new Thread()
		{
			public void run()
			{
				try{
					 DataGetUtilSimple.ConnectSevert(Constant.SURE_WAITER+userid+Constant.SURE_WAITER+password);
					 if(DataGetUtilSimple.readinfo.equals("ok"))
					 {
						 DataGetUtilSimple.ConnectSevert(Constant.RESET_PASSWORD+userid+Constant.RESET_PASSWORD+newpw);
						 if(DataGetUtilSimple.readinfo.equals("ok"))
						 {
							 sendHandlerMsg
								(
									ResetPassWordConstant.RESET_PASSWORD_SUCCESS,
									"修改成功",
									Constant.ToastMessage,
									handler
								);
						 }
						 else
						 {
							 sendHandlerMsg
								(
									ResetPassWordConstant.RESET_PASSWORD_MESSAGE,
									"修改失败！！请检查网络",
									Constant.ToastMessage,
									handler
								); 
						 }
			         }
					 else
					 {
						 sendHandlerMsg
							(
								ResetPassWordConstant.RESET_PASSWORD_MESSAGE,
								"密码用户输入错误!!",
								Constant.ToastMessage,
								handler
							); 
					 }
				}
				  catch(Exception e)
				{
					  sendHandlerMsg
						(
							ResetPassWordConstant.RESET_PASSWORD_MESSAGE,
							"更新失败！！",
							Constant.UnknownHost,
							handler
						);
				}
			}
		}.start();
	}
	//通过餐厅ID初始化类别及主类别信息
	public static void initCateInfoByRoomId(String roomId,final Handler handler)
	{
		new Thread()
		{
			public void run() 
			{
				try
				{
					List<String[]> childCateList=new ArrayList<String[]>();
					Map<String,Object> vegeInfo = new HashMap<String,Object>();
					Map<String,boolean[]> vegeFlg=new HashMap<String,boolean[]>();
					//根据餐厅ID获得相应主类别菜品信息
					List<String[]> mainCateList=VegeMainCateUtil.getMainCateInfo(Constant.roomId);
				    //根据数据库中的菜品主类信息以及餐厅限制ID，获得类别信息  		
					for(int i=0;i<mainCateList.size();i++)
				    {
						childCateList.add(VegeChildCateUtil.getChildCateInfo(mainCateList.get(i)[1]));
				    }
					//默认显示的菜品
					List<String[]> list = VegeManagerUtil.getVege(childCateList.get(0)[0]);
					//该菜品已选标志位数组
					boolean []booFlg=new boolean[list.size()];
					Bitmap bmArr[] = new Bitmap[list.size()];
					for(int i=0; i<list.size(); i++)
					{		
						String[] vgi=list.get(i)[5].split("/");
						String bm=vgi[vgi.length-1];
						String bmId=bm.replace(".jpg","");
						Bitmap broadcastBmpt=PicManagerUtil.getBM(bmId);
						bmArr[i]=broadcastBmpt;
						booFlg[i]=false;
					}
					vegeInfo.put("vegeInfolist", list);
					vegeInfo.put("bmArr", bmArr);
					vegeInfo.put("vegeMainCateInfo", mainCateList);
					vegeInfo.put("vegeChildCateInfo", childCateList);
					vegeFlg.put(childCateList.get(0)[0], booFlg);
					SelectVegeActivity.vegeInfoMap=vegeInfo;	
					SelectVegeActivity.vegeFlg=vegeFlg;
					SelectVegeActivity.childcateVege=childCateList.get(0)[0];
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
				}
				catch(UnknownHostException e)
				{
				    e.printStackTrace();
				    //关闭等待对话框
				    handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
				    sendHandlerMsg
					(
						MainConstant.OPEN_ERROR_TOAST_MESSAGE,
						"连接服务器失败，请查看网络配置重新尝试",
						Constant.UnknownHost,
						handler
					);
		    	}
				catch(SocketTimeoutException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_GRIDERROR_DIALOG_MESSAGE,
						"网络连接超时，请检查您的网络情况,是否设置",
						Constant.SocketTimeout,
						handler
					);
				}
				catch(OutOfMemoryError e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_ERROR_TOAST_MESSAGE,
						"获取数据异常，请重新尝试",
						Constant.SocketTimeout,
						handler
					);
				}
				catch(StringIndexOutOfBoundsException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_GRIDERROR_DIALOG_MESSAGE,
						"数据获取错误，是否重试",
						Constant.StringIndexOutOfBounds,
						handler
					);
				}
				catch(EOFException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_ERROR_TOAST_MESSAGE,
						"获取数据失败，请检查您的网络\n您点击过快请等菜品信息下载完再点击",
						Constant.EOFExcep,
						handler
					);
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_GRIDERROR_DIALOG_MESSAGE,
						"获取数据失败，请检查您的网络",
						Constant.NullPointer,
						handler
					);
				}
				catch(SocketException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					//开启错误对话框
					sendHandlerMsg
					(
						MainConstant.OPEN_GRIDERROR_DIALOG_MESSAGE,
						"由于网络不稳定可能导致数据出错，是否重新获取数据",
						Constant.SocketExcep,
						handler
					);
				} 
				catch(IOException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					sendHandlerMsg
					(
						MainConstant.OPEN_ERROR_TOAST_MESSAGE,
						"数据加载慢，请加载完后再点击",
						Constant.IOExcep,
						handler
					);
				}
				catch (Exception e) 
				{  
					e.printStackTrace();
					//关闭等待对话框
					handler.sendEmptyMessage(MainConstant.CANCEL_WAIT_DIALOG_FOR_GRID);
					sendHandlerMsg
					(
						MainConstant.OPEN_GRIDERROR_DIALOG_MESSAGE,
						"数据加载出错，是否重新加载",
						Constant.IOExcep,
						handler
					);
				}
			}
		}.start();
	}
	//根据类别名称查询所有菜品信息，再根据菜品图片获取图片显示
	public static void getVegeInfo(final String str,final Handler handler)
	{
		 
		Map<String,Object> vegeInfo = new HashMap<String,Object>();
		List<String[]> list;
		try
		{
			list = VegeManagerUtil.getVege(str);
		    Bitmap bmArr[] = new Bitmap[list.size()];
		    boolean []booFlg=new boolean[list.size()];
			for(int i=0; i<list.size(); i++)
			{		
				String[] vgi=list.get(i)[5].split("/");
				String bm=vgi[vgi.length-1];
				String bmId=bm.replace(".jpg","");
				Bitmap broadcastBmpt=PicManagerUtil.getBM(bmId);
				bmArr[i]=broadcastBmpt;
				booFlg[i]=false;
			}
			vegeInfo.put("vegeInfolist", list);
			vegeInfo.put("bmArr", bmArr);
			SelectVegeActivity.childVegeInfo=vegeInfo;
			if(!SelectVegeActivity.vegeFlg.containsKey(str))
			{//不存在以该子类别名为键的值，添加该值
			  SelectVegeActivity.vegeFlg.put(str, booFlg);
			}
		   }
		catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"获取数据超时，是否重试",
				Constant.SocketTimeout,
				handler
			);
		}
		catch(OutOfMemoryError e)
		{
			e.printStackTrace();
			//开启错误对话框
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"由于网络问题数据获取出问题...",
				Constant.SocketTimeout,
				handler
			);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"数据获取错误，是否重试",
				Constant.StringIndexOutOfBounds,
				handler
			);
		}
		catch(EOFException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"数据获取慢，请耐心等待...",
				Constant.EOFExcep,
				handler
			);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"可能由于网络不稳定导致数据不全，是否重新获取数据",
				Constant.SocketExcep,
				handler
			);
		} 
		catch(IOException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"获取数据失败,请等待...",
				Constant.IOExcep,
				handler
			);
		}
		catch (Exception e) 
		{  
			e.printStackTrace();
			//开启错误对话框
		    sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"获取数据失败，请重新尝试",
				Constant.Excep,
				handler
			);
		}		
	}
	//根据查询的菜品的类别，查询该类别中的所有菜品信息
	public static void getVegeInfoFor(final String str,final Handler handler)
	{		 
		Map<String,Object> vegeInfo = new HashMap<String,Object>();
		List<String[]> list;
		try
		{
			list = VegeManagerUtil.getVege(str);
		    Bitmap bmArr[] = new Bitmap[list.size()];
		    boolean []booFlg=new boolean[list.size()];
			for(int i=0; i<list.size(); i++)
			{		
				String[] vgi=list.get(i)[5].split("/");
				String bm=vgi[vgi.length-1];
				String bmId=bm.replace(".jpg","");
				Bitmap broadcastBmpt=PicManagerUtil.getBM(bmId);
				bmArr[i]=broadcastBmpt;
				booFlg[i]=false;
			}
			vegeInfo.put("vegeInfolist", list);
			vegeInfo.put("bmArr", bmArr);
			VegeImageUIActivity.childVegeInfo=vegeInfo;
			if(VegeImageUIActivity.childVegeInfo==null)
			{
				Message msg=new Message();
				msg.what=SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE;
				Bundle b=new Bundle();
				b.putString("msg","获取数据出错是否重新获取");
				handler.sendMessage(msg);
			}
			else
			{
				handler.sendEmptyMessage(SelectVegeConstant. GOTO_VEGE_INTRO_MESSAGE);
			}
		   }
		catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"获取数据超时，是否重试",
				Constant.SocketTimeout,
				handler
			);
		}
		catch(OutOfMemoryError e)
		{
			e.printStackTrace();
			//开启错误对话框
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"由于网络问题数据获取出问题...",
				Constant.SocketTimeout,
				handler
			);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"数据获取错误，是否重试",
				Constant.StringIndexOutOfBounds,
				handler
			);
		}
		catch(EOFException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"数据获取慢，请耐心等待...",
				Constant.EOFExcep,
				handler
			);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"可能由于网络不稳定导致数据不全，是否重新获取数据",
				Constant.SocketExcep,
				handler
			);
		} 
		catch(IOException e)
		{
			e.printStackTrace();
			sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_TOAST_MESSAGE,
				"获取数据失败,请等待...",
				Constant.IOExcep,
				handler
			);
		}
		catch (Exception e) 
		{  
			e.printStackTrace();
			//开启错误对话框
		    sendHandlerMsg
			(
				SelectVegeConstant.OPEN_ERROR_DIALOG_MESSAGE,
				"获取数据失败，请重新尝试",
				Constant.Excep,
				handler
			);
		}		
	}

	//登陆界面根据员工id查看是否有登录权限
	@SuppressWarnings("unused")
	public static void loginValidate(String userid,String userpw,Handler handler)throws Exception 
	{
		//判断输入的密码和用户id是否合法
		 DataGetUtilSimple.ConnectSevert(Constant.SURE_WAITER+userid+Constant.SURE_WAITER+userpw);
		 if(DataGetUtilSimple.readinfo.equals("ok"))
		 {
			//先判断此员工是否在其他地方登陆
			 DataGetUtilSimple.ConnectSevert(Constant.IS_HASlOGIN+userid);
			 if(DataGetUtilSimple.readinfo.equals("0"))
			 {
				int i=0;
				DataGetUtilSimple.ConnectSevert(Constant.GET_LOGINAUTH+userid);
				String authoryinfo[][]=TypeExchangeUtil.getString(DataGetUtilSimple.readinfo);
				for(i=0;i<authoryinfo.length;i++)
				{
					   if(authoryinfo[i][0].equals("登陆"))
					   {
						   DataGetUtilSimple.ConnectSevert(Constant.UPDATE_LOGINFLG+userid+Constant.UPDATE_LOGINFLG+"1");
						   Message msg=new Message();
							Bundle b=new Bundle();
							msg.what=LoginConstant.LOGIN_SUCCESS;
							b.putString("msg","登陆成功");
							b.putString("action","INIT_VEGEIMAGEGRID");
							msg.setData(b);
							handler.sendMessage(msg);
					   }
					   break;
				  }				
				 if(i>=authoryinfo.length)
				 {
					 sendHandlerMsg
						(
						    LoginConstant.SHOW_AUTH_TOST_MESSAGE,
							"您无登陆权限！！！",
							Constant.Excep,
							handler
						); 
				 }
		    }
			else
			 {
				sendHandlerMsg
				(
				    LoginConstant.SHOW_AUTH_TOST_MESSAGE,
				    "您在其他地方已经登陆请先注销！！！",
					Constant.Excep,
					handler
				);
			 }
		 }
		 else 
		 {
			 sendHandlerMsg
				(
				    LoginConstant.SHOW_AUTH_TOST_MESSAGE,
					"您输入的密码与用户名不符合！！！",
					Constant.Excep,
					handler
				);
		 }
	}
	//注销时候检查权限
	//登陆界面根据员工id查看是否有登录权限
	public static boolean logoutValidate(String userid,String userpw,Handler handler)throws Exception
	{
		//判断输入的密码和用户id是否合法
		 DataGetUtilSimple.ConnectSevert(Constant.SURE_WAITER+userid+Constant.SURE_WAITER+userpw);
		 if(DataGetUtilSimple.readinfo.equals("ok"))
		 {
			int i=0;
			DataGetUtilSimple.ConnectSevert(Constant.GET_LOGINAUTH+userid);
			String authoryinfo[][]=TypeExchangeUtil.getString(DataGetUtilSimple.readinfo);
			for(i=0;i<authoryinfo.length;i++)
			{
			   if(authoryinfo[i][0].equals("登陆"))
			   {
				return true;
			   }
			 }
			 if(i>=authoryinfo.length)
			 {
				return false; 
			 }
		 }else{
			 return false;
		 }
		return false;
	}
	public static void updateLoginFlg(String userid)
	{
		try
		{
			DataGetUtilSimple.ConnectSevert(Constant.UPDATE_LOGINFLG+userid+Constant.UPDATE_LOGINFLG+"0");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void openTableInfo(final Handler handler)
	{
		new Thread()
		   {
			   public void run()
			   {
				   try{
						Map<String,Object> pointInfo = new HashMap<String,Object>();
						DataGetUtilSimple.ConnectSevert(Constant.GET_W_ROOM_POINTTYPE);
						String str[] = DataGetUtilSimple.readinfo.split("~");
						//获取餐厅信息
						String[][] listroom=TypeExchangeUtil.getString(str[0]);
						//获取餐台类型
						String[][] listptype=TypeExchangeUtil.getString(str[1]);
						DataGetUtilSimple.ConnectSevert(Constant.GET_W_NOPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_NOPERSONPOINTINFO+listptype[0][0]);
						List<String []> initpointinfo = TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
						pointInfo.put("room",listroom);
						pointInfo.put("pointtype",listptype);
						pointInfo.put("initpointinfo",initpointinfo);
						SelectTableActivity.pointInfo=pointInfo;
						handler.sendEmptyMessage(TableInfoConstant.GO_SELECT_TABLE);
					}
					catch(SocketTimeoutException e)
					{
						e.printStackTrace();
						//关闭等待对话框
						//开启错误对话框
						sendHandlerMsg
						(
							TableInfoConstant.SHOW_TOAST_MESSAGE,
							"连接超时，请检出您的网络问题，请重新尝试",
							Constant.ToastMessage,
							handler
						);
					}
					catch(IOException e)
					{
						e.printStackTrace();
						//关闭等待对话框
						sendHandlerMsg
						(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
								"数据获取异常，请重新获取",
								Constant.ToastMessage,
								handler
						);
					}
					catch(Exception e)
					  {
						e.printStackTrace();
						//关闭等待对话框
						sendHandlerMsg
						(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
								"数据获取异常，请重新获取",
								Constant.ToastMessage,
								handler
						 );
					}
			   }
		   }.start();
	}
public static void setNewOrder(final String userid,final String password,final Handler handler)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
				   DataGetUtilSimple.ConnectSevert(Constant.SURE_WAITER+userid+Constant.SURE_WAITER+password);
				   if(DataGetUtilSimple.readinfo.equals("ok"))
				   {
					List<String[]> orderList=Constant.dcvegemsg;
					float totalPri = 0;
					String getresult=null;
					List<String[]> passOrderList = new ArrayList<String[]>(orderList.size());
					List<String[]> passVegeList=new ArrayList<String[]>(orderList.size());
					String orderId[];
					
					if(orderList.size()==0)
					{
						Message msg=new Message();
						Bundle b=new Bundle();
						b.putString("prompt_toast","您未选择菜品");
						msg.setData(b);
						msg.what=ShowOrderConstant.OPEN_PROMPT_TOAST_MESSAGE;
						handler.sendMessage(msg);
					}
					for(int i=0;i<orderList.size();i++)
					{
						if(!orderList.get(i)[2].equals("0"))
						{//将菜品ID和菜品数量传给服务器进行操作
							String vegeStr[]=new String[2];
							vegeStr[0]=orderList.get(i)[4];//菜品ID
							vegeStr[1]=orderList.get(i)[2];//菜品数量
							passVegeList.add(vegeStr);		 
							//计算菜品总价格
							totalPri=totalPri+Float.parseFloat(orderList.get(i)[2])*Float.parseFloat(orderList.get(i)[1]);
						}
					}
					String tempVegeStr=TypeExchangeUtil.listToString(passVegeList);
					DataGetUtilSimple.ConnectSevert(Constant.ADD_W_NEWORDER+tempVegeStr+
													Constant.ADD_W_NEWORDER+Constant.deskName+
													Constant.ADD_W_NEWORDER+userid+
													Constant.ADD_W_NEWORDER+(totalPri+"")+
													Constant.ADD_W_NEWORDER+Constant.guestNum);
					getresult=DataGetUtilSimple.readinfo;
					if(!getresult.startsWith("sucess"))
					{
						if(getresult.equals("fail"))
						{//订单创建失败
							Message msg=new Message();
							Bundle b=new Bundle();
							b.putString("prompt_toast","出现错误，请重新提交");
							msg.setData(b);
							msg.what=ShowOrderConstant.OPEN_PROMPT_TOAST_MESSAGE;
							handler.sendMessage(msg);
						}
						else
						{
							Message msg=new Message();
							Bundle b=new Bundle();
							b.putString("prompt_toast", getresult.substring(0, getresult.length()-1)+"已售罄，请选择其他菜品");
							msg.setData(b);
							msg.what=ShowOrderConstant.OPEN_PROMPT_TOAST_MESSAGE;
							handler.sendMessage(msg);
						}
					}
					else
					{//订单创建成功
						orderId=getresult.split("sucess");
						for(int i=0;i<orderList.size();i++)
						{
							if(!orderList.get(i)[2].equals("0"))
							{//将数量不为0的菜品提交
								String orderStr[]=new String[8];
								orderStr[0]=orderList.get(i)[4];//菜品ID
						   		orderStr[1]=userid;//点菜人ID
						   		orderStr[2]=orderId[1];//订单ID
						   		orderStr[3]=orderList.get(i)[2];//单个菜品数量
						   		orderStr[4]=orderList.get(i)[1];//单个菜品价格
//						   		orderStr[5]=orderList.get(i)[3];//特殊要求
						   		orderStr[7]=i+"";//序号
								passOrderList.add(orderStr);
							}
						}
						String tempOrderStr=TypeExchangeUtil.listToString(passOrderList);
						DataGetUtilSimple.ConnectSevert(Constant.ADD_W_NEWDETIALORDER+tempOrderStr);
						String getin=DataGetUtilSimple.readinfo;
						if(getin.equals("ok"))
						{//订单提交成功
							Message msg=new Message();
							Bundle b=new Bundle();
							b.putString("prompt_toast","订单提交成功");
							msg.setData(b);
							msg.what=ShowOrderConstant.REMOVE_GUESTNUM_MESSAGE;
							handler.sendMessage(msg);
						}
					}
				}
				else
				{
					Message msg=new Message();
					Bundle b=new Bundle();
					b.putString("prompt_toast","密码错误无权下订单");
					msg.setData(b);
					msg.what=ShowOrderConstant.OPEN_PROMPT_TOAST_MESSAGE;
					handler.sendMessage(msg);
				}
				}
				catch(SocketTimeoutException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						ShowOrderConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"获取数据超时，请检查您的网络是否连通，是否直接重试",
						Constant.SocketTimeout,
						handler
					);
				}
				catch(EOFException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						ShowOrderConstant.OPEN_ERROR_TOAST_MESSAGE,
						"获取数据失败，请检查您的网络\n您点击过快请等菜品信息下载完再点击",
						Constant.EOFExcep,
						handler
					);
				}
				catch(SocketException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						ShowOrderConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"可能由于网络不稳定导致数据不全，是否重新获取数据",
						Constant.SocketExcep,
						handler
					);
				} 
				catch(NullPointerException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						ShowOrderConstant.OPEN_ERROR_TOAST_MESSAGE,
						"NullPointerException获取数据失败，请检查您的网络",
						Constant.NullPointer,
						handler
					);
				}
				catch (Exception e) 
				{  
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						ShowOrderConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"获取数据失败，请重新尝试",
						Constant.Excep,
						handler
					);
				}
			}
		}.start();
	}
	//根据所点的餐厅类型和餐台类型查询所对应的餐台
	public static void getPointInfo(String roomid,String ptypeid,String ishasperson,Handler handler)
		{
			try{
				if(ishasperson.equals(Constant.ALLPERSON))//所有
				{
					DataGetUtilSimple.ConnectSevert(Constant.GET_W_POINTINFO+roomid+Constant.GET_W_POINTINFO+ptypeid);
				}else if(ishasperson.equals(Constant.NOPERSON))
				{
					DataGetUtilSimple.ConnectSevert(Constant.GET_W_NOPERSONPOINTINFO+roomid+Constant.GET_W_NOPERSONPOINTINFO+ptypeid);
				}
				else if(ishasperson.equals(Constant.HASPERSON))
				{
					DataGetUtilSimple.ConnectSevert(Constant.GET_W_HASPERSONPOINTINFO+roomid+Constant.GET_W_HASPERSONPOINTINFO+ptypeid);
				}
				initTableInfo=TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
			}catch(SocketTimeoutException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					SHOW_TABLEERROE_DIALOG,
					"连接超时，请检出您的网络问题，是否重试",
					Constant.SocketTimeout,
					handler
				);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				sendHandlerMsg
				(
						SHOW_TABLEERROE_DIALOG,
						"获取数据超时，请检查您的网络后，重新连接",
						Constant.IOExcep,
						handler
				);
			}
			catch(Exception e)
			  {
				e.printStackTrace();
				sendHandlerMsg
				(
						SHOW_TABLEERROE_DIALOG,
						"获取数据超时，请检查您的网络后，重新连接",
						Constant.IOExcep,
						handler
				);
				}
		}
	//修改餐台的状态（开台）
		public static void openTableUpdate(String tname,String guestnum,Handler handler)
		{
			try
			{
				    DataGetUtilSimple.ConnectSevert(Constant.GET_W_GUESTNUMBYNAME+tname+Constant.GET_W_GUESTNUMBYNAME+guestnum);
				    if(DataGetUtilSimple.readinfo.equals("ok"))
				    {
				//更新餐台状态
					 DataGetUtilSimple.ConnectSevert(Constant.UPDATECLIENT_POINT+tname+Constant.UPDATECLIENT_POINT+"1");
					 if(DataGetUtilSimple.readinfo.equals("ok"))
					 {
						 sendHandlerMsg
							(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
							     "开台成功，请继续操作",
								Constant.ToastMessage,
								handler
							);
						 handler.sendEmptyMessage(CLOSE_CUR_ACTIVITY);
					 }
					 else
					 {
						 sendHandlerMsg
							(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
							     "开台失败，此餐台已经开台，您此界面已过期",
								Constant.ToastMessage,
								handler
							); 
					 }
				    }
				 else
				 {
					 sendHandlerMsg
						(
							TableInfoConstant.SHOW_TOAST_MESSAGE,
						     "请检查您输入的人数是否超出或餐台已经开台",
							Constant.ToastMessage,
							handler
						); 
				 }
			}
			catch(SocketTimeoutException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					TableInfoConstant.SHOW_UPDATETABLEERROR_DIALOG,
					"连接超时，请检出您的网络问题，是否重试",
					Constant.SocketTimeout,
					handler
				);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				sendHandlerMsg
				(
						TableInfoConstant.SHOW_UPDATETABLEERROR_DIALOG,
						"获取数据超时，请检查您的网络后，重新连接",
						Constant.IOExcep,
						handler
				);
			}
			catch(Exception e)
			  {
				e.printStackTrace();
				sendHandlerMsg
				(
						TableInfoConstant.SHOW_UPDATETABLEERROR_DIALOG,
						"获取数据超时，请检查您的网络后，重新连接",
						Constant.IOExcep,
						handler
				);
				}
		}
	//在SelectVege中直接初始化餐台界面
	public static void searchTableState(final Handler handler,final String ishasperson)
			{
		
			new Thread()
			{
				public void run()
			      {
					this.setName("cancelopentableThread--MoreFunctionActivity");
					try{
						Map<String,Object> pointInfo = new HashMap<String,Object>();
						DataGetUtilSimple.ConnectSevert(Constant.GET_W_ROOM_POINTTYPE);
						String str[] = DataGetUtilSimple.readinfo.split("~");
						//获取餐厅信息
						String[][] listroom=TypeExchangeUtil.getString(str[0]);
						//获取餐台类型
						String[][] listptype=TypeExchangeUtil.getString(str[1]);
						if(ishasperson.equals(Constant.ALLPERSON))//所有
						{
							DataGetUtilSimple.ConnectSevert(Constant.GET_W_POINTINFO+listroom[0][0]+Constant.GET_W_POINTINFO+listptype[0][0]);
						}else if(ishasperson.equals(Constant.NOPERSON))
						{
							DataGetUtilSimple.ConnectSevert(Constant.GET_W_NOPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_NOPERSONPOINTINFO+listptype[0][0]);
						}
						else if(ishasperson.equals(Constant.HASPERSON))
						{
							DataGetUtilSimple.ConnectSevert(Constant.GET_W_HASPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_HASPERSONPOINTINFO+listptype[0][0]);
						}					
						List<String []> initpointinfo = TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
						
						pointInfo.put("room",listroom);
						pointInfo.put("pointtype",listptype);
						pointInfo.put("initpointinfo",initpointinfo);
						SelectTableActivity.pointInfo=pointInfo;
						handler.sendEmptyMessage(SelectVegeConstant.GO_SELECT_TABLE);
					}
				catch(SocketTimeoutException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					//开启错误对话框
					sendHandlerMsg
					(
						SelectVegeConstant.SHOW_TABLEERROE_DIALOG,
						"连接超时，请检出您的网络问题，请重新尝试",
						Constant.SocketTimeout,
						handler
					);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					sendHandlerMsg
					(
							SelectVegeConstant.SHOW_TABLEERROE_DIALOG,
							"获取数据超时，请检查您的网络后，重新连接",
							Constant.IOExcep,
							handler
					);
				}
				catch(Exception e)
				  {
					e.printStackTrace();
					sendHandlerMsg
					(
							SelectVegeConstant.SHOW_TABLEERROE_DIALOG,
							"获取数据超时，请检查您的网络后，重新连接",
							Constant.IOExcep,
							handler
					);
					}
			      }
			}.start();
		      
			}	
	//餐台显示无人餐台信息
	public static void newTableInfo(Handler handler)
			{
				try{
					Map<String,Object> pointInfo = new HashMap<String,Object>();
					DataGetUtilSimple.ConnectSevert(Constant.GET_W_ROOM_POINTTYPE);
					String str[] = DataGetUtilSimple.readinfo.split("~");
					//获取餐厅信息
					String[][] listroom=TypeExchangeUtil.getString(str[0]);
					//获取餐台类型
					String[][] listptype=TypeExchangeUtil.getString(str[1]);
					DataGetUtilSimple.ConnectSevert(Constant.GET_W_NOPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_NOPERSONPOINTINFO+listptype[0][0]);
					List<String []> initpointinfo = TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
					pointInfo.put("room",listroom);
					pointInfo.put("pointtype",listptype);
					pointInfo.put("initpointinfo",initpointinfo);
					SelectTableActivity.pointInfo=pointInfo;
//					handler.sendEmptyMessage(TableInfoConstant.GO_SELECT_TOW_TABLE);
				}
				catch(SocketTimeoutException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					//开启错误对话框
					sendHandlerMsg
					(
						TableInfoConstant.SHOW_TOAST_MESSAGE,
						"连接超时，请检出您的网络问题，请重新尝试",
						Constant.ToastMessage,
						handler
					);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					//关闭等待对话框
					sendHandlerMsg
					(
							TableInfoConstant.SHOW_TOAST_MESSAGE,
							"数据获取异常，请重新获取",
							Constant.ToastMessage,
							handler
					);
				}
				catch(Exception e)
				  {
					e.printStackTrace();
					//关闭等待对话框
					sendHandlerMsg
					(
							TableInfoConstant.SHOW_TOAST_MESSAGE,
							"数据获取异常，请重新获取",
							Constant.ToastMessage,
							handler
					);
					}
			}
	//有人餐台显示餐台信息
	public static void oldTableInfo(final Handler handler)
		{
		   new Thread()
		   {
			   public void run()
			   {
				   try{
						Map<String,Object> pointInfo = new HashMap<String,Object>();
						DataGetUtilSimple.ConnectSevert(Constant.GET_W_ROOM_POINTTYPE);
						String str[] = DataGetUtilSimple.readinfo.split("~");
						//获取餐厅信息
						String[][] listroom=TypeExchangeUtil.getString(str[0]);
						//获取餐台类型
						String[][] listptype=TypeExchangeUtil.getString(str[1]);
						DataGetUtilSimple.ConnectSevert(Constant.GET_W_HASPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_HASPERSONPOINTINFO+listptype[0][0]);
						List<String []> initpointinfo = TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
						pointInfo.put("room",listroom);
						pointInfo.put("pointtype",listptype);
						pointInfo.put("initpointinfo",initpointinfo);
						SelectTableActivity.pointInfo=pointInfo;
						handler.sendEmptyMessage(TableInfoConstant.GO_SELECT_TABLE);
					}
					catch(SocketTimeoutException e)
					{
						e.printStackTrace();
						//关闭等待对话框
						//开启错误对话框
						sendHandlerMsg
						(
							TableInfoConstant.SHOW_TOAST_MESSAGE,
							"连接超时，请检出您的网络问题，请重新尝试",
							Constant.ToastMessage,
							handler
						);
					}
					catch(IOException e)
					{
						e.printStackTrace();
						//关闭等待对话框
						sendHandlerMsg
						(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
								"数据获取异常，请重新获取",
								Constant.ToastMessage,
								handler
						);
					}
					catch(Exception e)
					  {
						e.printStackTrace();
						//关闭等待对话框
						sendHandlerMsg
						(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
								"数据获取异常，请重新获取",
								Constant.ToastMessage,
								handler
						 );
					}
			   }
		   }.start();
			
		}
	//有人餐台显示餐台信息
		public static void mergeTableInfo(final Handler handler)
			{
			   new Thread()
			   {
				   public void run()
				   {
					   try{
							Map<String,Object> pointInfo = new HashMap<String,Object>();
							DataGetUtilSimple.ConnectSevert(Constant.GET_W_ROOM_POINTTYPE);
							String str[] = DataGetUtilSimple.readinfo.split("~");
							//获取餐厅信息
							String[][] listroom=TypeExchangeUtil.getString(str[0]);
							//获取餐台类型
							String[][] listptype=TypeExchangeUtil.getString(str[1]);
							DataGetUtilSimple.ConnectSevert(Constant.GET_W_HASPERSONPOINTINFO+listroom[0][0]+Constant.GET_W_HASPERSONPOINTINFO+listptype[0][0]);
							List<String []> initpointinfo = TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
							pointInfo.put("room",listroom);
							pointInfo.put("pointtype",listptype);
							pointInfo.put("initpointinfo",initpointinfo);
							SelectTableActivity.pointInfo=pointInfo;
//							handler.sendEmptyMessage(TableInfoConstant.GO_SELECT_TOW_TABLE);
						}
						catch(SocketTimeoutException e)
						{
							e.printStackTrace();
							//关闭等待对话框
							//开启错误对话框
							sendHandlerMsg
							(
								TableInfoConstant.SHOW_TOAST_MESSAGE,
								"连接超时，请检出您的网络问题，请重新尝试",
								Constant.ToastMessage,
								handler
							);
						}
						catch(IOException e)
						{
							e.printStackTrace();
							//关闭等待对话框
							sendHandlerMsg
							(
									TableInfoConstant.SHOW_TOAST_MESSAGE,
									"数据获取异常，请重新获取",
									Constant.ToastMessage,
									handler
							);
						}
						catch(Exception e)
						  {
							e.printStackTrace();
							//关闭等待对话框
							sendHandlerMsg
							(
									TableInfoConstant.SHOW_TOAST_MESSAGE,
									"数据获取异常，请重新获取",
									Constant.ToastMessage,
									handler
							 );
						}
				   }
			   }.start();				
			}		
//根据菜品id得到菜品信息
public static void getVegeIntro(final String vegeid,final Handler handler) 
{
	new Thread()
	{
		public void run()
		{	
			 Map<String,Object> vegeintro=new HashMap<String,Object>();
			 try
			 {
			   String[] vegemsg=VegeIntroMsgUtil.getVege(vegeid);
		       final String[] str=vegemsg[7].split(",");
		       Bitmap[] vegeimage=new Bitmap[str.length];
		       for(int i=0;i<str.length;i++)
		       {
		    	   String[] vgi=str[i].split("/");
				   String bm=vgi[vgi.length-1];
				   String bmId=bm.replace(".jpg","");
				   Bitmap broadcastBmp=PicManagerUtil.getBM(bmId);
				   vegeimage[i]=broadcastBmp;
		       }
		       vegeintro.put("vegeinfo", vegemsg);
		       vegeintro.put("vegebminfo", vegeimage);	    
		       VegeActivity.vegeintromap=vegeintro;
		       handler.sendEmptyMessage(VegeImageConstant. GOTO_VEGE_INTRO_MESSAGE);		       
			 }
			 catch(UnknownHostException e)
			 {
			    e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					VegeImageConstant.OPEN_ERROR_TOAST_MESSAGE,
					"连接服务器失败，请查看网络配置重新尝试",
					Constant.UnknownHost,
					handler
				);
		     }
			 catch(SocketTimeoutException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"获取数据超时，请检查您的网络",
						Constant.SocketTimeout,
						handler
					);
				}
				catch(StringIndexOutOfBoundsException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"数据获取错误，请检查您的网络",
						Constant.StringIndexOutOfBounds,
						handler
					);
				}
				catch(EOFException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_TOAST_MESSAGE,
						"数据获取错误，请检查您的网络",
						Constant.EOFExcep,
						handler
					);
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"NullPointerException获取数据失败，请检查您的网络",
						Constant.NullPointer,
						handler
					);
				}
				catch(SocketException e)
				{
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"网络连接失败，请检查您的网络",
						Constant.SocketExcep,
						handler
					);
				} 
				catch(IOException e)
				{
					e.printStackTrace();
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_TOAST_MESSAGE,
						"获取数据超时，是否重试",
						Constant.IOExcep,
						handler
					);
				}
				catch (Exception e) 
				{  
					e.printStackTrace();
					//开启错误对话框
					sendHandlerMsg
					(
						VegeImageConstant.OPEN_ERROR_DIALOG_MESSAGE,
						"获取数据失败，请重新尝试",
						Constant.Excep,
						handler
					);
				}
		}
	}.start();
}
//发送测试，是否连接的上服务器
public static void testConnect(final Handler handler)
{
	 final Message msg=new Message();
	 final Bundle b=new Bundle();
	 msg.what=ResetConstant.TESTCONNECT;
	new Thread()
	{
		public void run()
		{
			 try
			 {
				DataGetUtilSimple.ConnectSevert(Constant.TESTCONNECT);
				 String testmsg=DataGetUtilSimple.readinfo;
				 b.putString("msg",testmsg);
				 msg.setData(b);
				 handler.sendMessage(msg);
			 }
			 catch(UnknownHostException e)
			 {
				 e.printStackTrace();
				 handler.sendEmptyMessage(ResetConstant.TESTCONNECTERROR);
			 }
			 catch(SocketException e)
			 {
				 e.printStackTrace();
				//开启错误对话框
				handler.sendEmptyMessage(ResetConstant.TESTCONNECTERROR);
			 }
			catch (Exception e) 
			{  
				e.printStackTrace();
				//开启错误对话框
				handler.sendEmptyMessage(ResetConstant.TESTCONNECTERROR);
			}
		}
	}.start();
}
//根据餐台号得到人数
public static int guestNumByDeskId(final String deskname,final Handler handler)
{	
    int orderguestNum=0;
	 try
	 {
		 DataGetUtilSimple.ConnectSevert(Constant.GET_CURDESK_GUESTNUM+deskname);
		 if(!DataGetUtilSimple.readinfo.equals("fail"))
		 {
			 orderguestNum=Integer.parseInt(DataGetUtilSimple.readinfo);
		 }
		 handler.sendEmptyMessage(TableInfoConstant.GUEST_NUM_MESSAGE);
	 }
	 catch(UnknownHostException e)
	 {
		 sendHandlerMsg
			(
				TableInfoConstant.SHOW_TOAST_MESSAGE,
				"网络出错，请重新尝试",
				Constant.ToastMessage,
				handler
			);
	 }
	 catch(SocketException e)
	 {
		 sendHandlerMsg
			(
				TableInfoConstant.SHOW_TOAST_MESSAGE,
				"网络出错请重新尝试",
				Constant.ToastMessage,
				handler
			);
	 }
	catch (Exception e) 
	{  
		sendHandlerMsg
		(
			TableInfoConstant.SHOW_TOAST_MESSAGE,
			"网络出错请重新尝试",
			Constant.ToastMessage,
			handler
		);
	}
	return orderguestNum;
}
//发送错误handler信息
public static void sendHandlerMsg(int msgwhat,String errorpoint,int excepFlg,Handler h)
{
	//开启错误对话框
	Message msg=new Message();
	Bundle b=new Bundle();
	msg.what=msgwhat;
	b.putInt("excepFlg",excepFlg);
	b.putString("msg",errorpoint);
	msg.setData(b);
	h.sendMessage(msg);
}
//==========================================================订单管理begin
//初始化订单管理信息
//if语句的判断和异常处理机制应该结合使用
public static void initOrderManageInfo(String deskName,Handler handler)
{
	try
	{
		//判断当前是否开台，没有开台则给出提示
		if(deskName==null)
		{
			Message msg=new Message();
			Bundle bundle=new Bundle();
			msg.what=OrderManageConstant.TOAST_HINT;
			bundle.putString("hint", "未开台，请点击查询按钮查询订单");
			msg.setData(bundle);
			handler.sendMessage(msg);
			return;  
		}
		//根据餐台id,获取相应的点菜确认单中的信息
		DataGetUtilSimple.ConnectSevert(Constant.GET_W_ORDERDISHCONFIRM+deskName);
		String orderDishConfirmInfo=DataGetUtilSimple.readinfo;
		//对开台但没有下单的情况进行判断
		if(orderDishConfirmInfo.length()==0)
		{	//根据桌子名查询的结果如果为null，说明当前的桌子无订单
			Message msg=new Message();
			Bundle bundle=new Bundle();
			msg.what=OrderManageConstant.TOAST_HINT;
			if(OrderManageActivity.alreadExistOrder || (OrderManageActivity.tv_orderid!=null && OrderManageActivity.tv_orderid.length()!=0))
			{
				bundle.putString("hint", "当前桌子订单中无菜品信息");
				OrderManageActivity.alreadExistOrder=false;
				OrderManageActivity.vegeInforesult=null;
				OrderManageActivity.orderDishConfirm=null;
				handler.sendEmptyMessage(OrderManageConstant.FRESH_ADAPTER_DATA);
			}
			else
			{
				bundle.putString("hint", "当前桌子无订单");
			}
			msg.setData(bundle);
			handler.sendMessage(msg);
			return;
		}
		//点菜确认单中的信息
		List<String[]> orderDishConfirm = TypeExchangeUtil.strToList(orderDishConfirmInfo);
		OrderManageActivity.vegeInforesult=orderDishConfirm;
	}
	catch(UnknownHostException e)
	{
	    e.printStackTrace();
		//开启错误对话框
	    sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"连接服务器失败，请查看网络配置重新尝试",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	}
	catch(SocketTimeoutException e)
	{
		e.printStackTrace();
		//开启错误对话框
		sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"网络连接超时，请检查您的网络情况,是否设置",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	}
	catch(NullPointerException e)
	{
		e.printStackTrace();
		//通过Toast给出异常提示，因为有些获取到的数据是空，因此发生空指针异常
		Message msg=new Message();
		Bundle bundle=new Bundle();
		msg.what=OrderManageConstant.TOAST_HINT;
		bundle.putString("hint", "数据下载失败请重新尝试");
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	catch(OutOfMemoryError e)
	{
		e.printStackTrace();
		//开启错误对话框
		sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"获取数据异常，请重新尝试",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	}
	catch(EOFException e)
	{
		e.printStackTrace();
		//开启错误对话框
		sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"获取数据失败，是否重试",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	}
	catch(SocketException e)
	{
		e.printStackTrace();
		//开启错误对话框
		sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"由于网络连接异常，是否重新获取数据",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	} 
	catch (Exception e) 
	{  
		//开启错误对话框
		sendHandlerMsg
		(
			OrderManageConstant.OPEN_ERROR_DIALOG_MESSAGE,
			"获取数据失败，请重新尝试",
			OrderManageConstant.INIT_ORDERMANAGE_EXCEP,
			handler
		);
	}
}
//删除点菜确认单中的菜品
public static String deleteVegeInfo(String[] vegeInfo)
{
	String result=Constant.FAIL;
	StringBuffer sb=new StringBuffer();
	try
	{
		if(vegeInfo!=null)
		{
			for(int i=0; i<vegeInfo.length; i++)
			{
				sb.append(Constant.DELETE_W_ORDERCONFIRMVEGE+vegeInfo[i]);
			}
		}
		DataGetUtilSimple.ConnectSevert(sb.toString());
		result=DataGetUtilSimple.readinfo;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return result;
	}
	return result;
}
//添加菜品数量
public static String addVegeCount(String[] vegeInfo)
{
	String result=Constant.FAIL;
	StringBuffer sb=new StringBuffer();
	try
	{
		if(vegeInfo!=null)
		{
			for(int i=0; i<vegeInfo.length; i++)
			{
				sb.append(Constant.ADD_W_VEGESINGLECOUNT+vegeInfo[i]);
			}
		}
		DataGetUtilSimple.ConnectSevert(sb.toString());
		result=DataGetUtilSimple.readinfo;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return result;
	}
	return result;
}
//减少菜品数量
public static String subVegeCount(String[] vegeInfo)
{
	String result=Constant.FAIL;
	StringBuffer sb=new StringBuffer();
	try
	{
		if(vegeInfo!=null)
		{
			for(int i=0; i<vegeInfo.length; i++)
			{
				sb.append(Constant.DELETE_W_VEGESINGLECOUNT+vegeInfo[i]);
			}
		}
		DataGetUtilSimple.ConnectSevert(sb.toString());
		result=DataGetUtilSimple.readinfo;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return result;
	}
	return result;
}
   //加载菜品信息并存入sqlite数据库
   public static void uploadvege(final Handler handler)
   {
	   new Thread()
	 {
		public void run() 
		{
		try{
				//加载菜品信息
				DataGetUtilSimple.ConnectSevert(Constant.DOWNLOAD_VEGE);
				List<String[]> vegemsg=TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);				
				//得到菜品的图片信息
				for(int i=0;i<vegemsg.size();i++)
				{
			      //得到菜品的主图片
				  DataGetUtilSimple.ConnectSevert(Constant.GET_IMAGE+vegemsg.get(i)[22]);
				  byte[] main_image= DataGetUtilSimple.data;
				  //生成新的图片路径
				  String mainpath=vegemsg.get(i)[22];
				  String[] imagepath=mainpath.split("/");
				  String imagename=imagepath[imagepath.length-1];
				  //存入sdcard中
				  PicUtil.saveImage(main_image, imagename);
				  //将路径进行修改
				  vegemsg.get(i)[22]=Environment.getExternalStorageDirectory().toString()+"/OrderDish/pic/"+imagename;				  
				  //得到菜品的子图片
				  String[] path=vegemsg.get(i)[23].split(",");
				  StringBuilder sbmsgpath=new StringBuilder();
				  for(int j=0;j<path.length;j++)
				  {
					  DataGetUtilSimple.ConnectSevert(Constant.GET_IMAGE+path[j]);
 					  byte[] imagemsg= DataGetUtilSimple.data;
 					  //生成新的图片路径
 					  imagepath=path[j].split("/");
 					  imagename=imagepath[imagepath.length-1];
					  //存入sdcard中
					  PicUtil.saveImage(imagemsg, imagename);
					  sbmsgpath.append(Environment.getExternalStorageDirectory().toString()+"/OrderDish/pic/"+imagename+",");
				   }
				  //将路径进行修改
				  vegemsg.get(i)[23]=sbmsgpath.toString();
				}	
				//插入数据库
			    DBUtil.insert("vege",vegemsg);
			    
			    List<String[]> childCateList=new ArrayList<String[]>();
				//根据餐厅ID获得相应主类别菜品信息
				DataGetUtilSimple.ConnectSevert(Constant.GET_W_MCGINFO+Constant.roomId);
				List<String[]> mainCateList=TypeExchangeUtil.strToList(DataGetUtilSimple.readinfo);
			    //根据数据库中的菜品主类信息以及餐厅限制ID，获得类别信息  		
				for(int i=0;i<mainCateList.size();i++)
			    {
					DataGetUtilSimple.ConnectSevert(Constant.GET_CGBYMCG+mainCateList.get(i)[1]);
					String cgmsginfo=DataGetUtilSimple.readinfo;
					String[] c=cgmsginfo.split("#");
					if(cgmsginfo.length()==0)
					{
						childCateList.add(null);
					}
					else
					{
						childCateList.add(c);
					}
			    }
				//插入数据库
			    DBUtil.insertCate(mainCateList,childCateList);
			    handler.sendEmptyMessage(MainConstant.OPEN_UPLOAD_DIALOG_MESSGE);
			}
			catch(UnknownHostException e)
			{
			    e.printStackTrace();
				//开启错误对话框
			    sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"连接服务器失败，请查看网络配置",
					Constant.UnknownHost,
					handler
				);
	    	}
			catch(NoSdCardException e)
			{
			    e.printStackTrace();
				//开启错误对话框
			    sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"未检测到SD卡,请插入！！！",
					Constant.ToastMessage,
					handler
				);
	    	}
			catch(SocketTimeoutException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"网络连接超时，请检查您的网络情况",
					Constant.SocketTimeout,
					handler
				);
			}
			catch(OutOfMemoryError e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"获取数据异常，请查看网络情况",
					Constant.SocketTimeout,
					handler
				);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"数据获取错误，请稍后再试",
					Constant.StringIndexOutOfBounds,
					handler
				);
			}
			
			catch(NullPointerException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"获取数据失败，请检查您的网络",
					Constant.NullPointer,
					handler
				);
			}
			catch(SocketException e)
			{
				e.printStackTrace();
				//开启错误对话框
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"由于网络不稳定可能导致数据出错，请查看网络情况",
					Constant.SocketExcep,
					handler
				);
			} 
			catch(IOException e)
			{
				e.printStackTrace();
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"数据加载慢，请稍后再试",
					Constant.IOExcep,
					handler
				);
			}
			catch (Exception e) 
			{  
				e.printStackTrace();
				sendHandlerMsg
				(
					MainConstant.OPEN_UPLOADERROR_DISLOG_MESSAGE,
					"数据加载出错，请稍后再试",
					Constant.IOExcep,
					handler
				);
			}
		}
	 }.start();
   }
}