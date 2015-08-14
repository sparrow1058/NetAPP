package com.example.testpic;


import com.example.testpic.SocketSend;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wifidirect.*;
//点击完成后的Act
public class PublishedActivity extends Activity implements Runnable {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;	
	private Socket socket;
	private String adress=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg);
		Intent mIntent=getIntent();
		adress=mIntent.getStringExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
//		new Thread(this).start();
		Init();		
	}
	
	public void run(){				
		try {			
			Log.d("TCP", "准备连接");		
			InetAddress serverAddr = InetAddress.getByName("192.168.1.101");//TCP服务器IP地址
			socket = new Socket(serverAddr, 9091);
			Log.d("TCP", "服务器：正在连接...");						
			OutputStream out=socket.getOutputStream();										
				String msg="Hello World123";
				out.write(msg.getBytes());								
			Log.d("TCP", "success");	
			socket.close();
			} catch (UnknownHostException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Log.e("TCP 3333", "ERROR:" + e.toString());
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Log.e("TCP 4444", "ERROR:" + e.toString());
			}			
		
	}

	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishedActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);				
		activity_selectimg_send.setOnClickListener(new OnClickListener() {			
					
			public void onClick(View v) {
				Log.d("TCP","onClick" );
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).toString();
					Log.d("list",Str);//文件绝对路径
					Intent serviceIntent = new Intent(PublishedActivity.this, FileTransferService.class);//注册客户端传文件的意图
					serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
					serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, Str);
					serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
							adress);
					serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
					PublishedActivity.this.startService(serviceIntent);		
					//可以加临界区
//					Intent data=new Intent();					
//					data.setData(Uri.fromFile(new File(Bimp.drr.get(i))));
//					setResult(i, data);					
				}
//				Intent intent = new Intent();				
//				ArrayList list = new ArrayList();//这个arraylist是可以直接在bundle里传的，所以我们可以借用一下它的功能
//				list.add(Bimp.drr);//这个list2才是你真正想要传过去的list。我们把它放在arraylis中，借助它传过去
//				intent.putStringArrayListExtra("list", list);							
//				setResult(RESULT_OK,intent);								
//				finish();							
//				AlertDialog.Builder builder=new AlertDialog.Builder(PublishedActivity.this);//this 出错
//				builder.setTitle("title");
////				builder.setMessage(list.get(0));
////				AlertDialog dialog =builder.create();
////				dialog.show();	
//								
//			    List<String> list = new ArrayList<String>();				
//				for (int i = 0; i < Bimp.drr.size(); i++) {
//					String Str = Bimp.drr.get(i).substring( 
//							Bimp.drr.get(i).lastIndexOf("/") + 1,
//							Bimp.drr.get(i).lastIndexOf("."));
//					list.add(FileUtils.SDPATH+Str+".JPEG");	
//					builder.setMessage(list.get(i));
//					AlertDialog dialog =builder.create();
//					dialog.show();
//					Log.d("TCP",list.get(i) );
//					Log.d("TCP",Bimp.drr.get(i) );																															
//					
//						DataOutputStream dos;  
//				        FileInputStream fis; 
//				        File file;
//				        int port=9091;
//				    	String ip="192.168.1.104";				    	
//				        try { 
//				        	socket = new Socket();      
//				        	Log.d("TCP","0faile" );
//				        	socket.bind(null);
//				        	socket.connect(new InetSocketAddress(ip,port),5000);
//				        	
//				        	 OutputStream out=socket.getOutputStream();										
//								String msg="Hello World123";
//								out.write(msg.getBytes());
//								Log.d("TCP","1faile" );	
//				            file = new File(list.get(i)); 
//				            Log.d("TCP","2faile" );	
//				            if (file.length() == 0) { 
//				            	Log.d("TCP","faile" );				            	
//				            	return;  
//				            } else { 
//				            	Log.d("TCP","new socket" );
//				                				               
//						 dos = new DataOutputStream(socket.getOutputStream());  
//			                fis = new FileInputStream(file);  
//			                dos.writeUTF(list.get(i).substring(list.get(i).lastIndexOf("/")+1));//截取图片名称  
//			           
//			                dos.flush();  
//			                byte[] sendBytes = new byte[1024 * 8];  
//			                int length;  
//			                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
//			                    dos.write(sendBytes, 0, length);  
//			                    dos.flush();// 发送给服务器  
//			                }  
//			                dos.close();//在发送消息完之后一定关闭，否则服务端无法继续接收信息后处理，手机卡机  
//			                fis.close();  
//			                socket.close();  
//			            }  
//			        } catch (UnknownHostException e) {  
//			            e.printStackTrace();  
//			        } catch (FileNotFoundException e) {  
//			            e.printStackTrace();  
//			        }catch (SocketTimeoutException e) {  
//			            e.printStackTrace();              
//			        }catch (IOException e) {  
//			            e.printStackTrace();  
//			        }  
											
					
				//	new SocketSend().seriesUpload("/storage/emulated/0/formats/434370832-94a4f46c3a19994.JPEG");
					Log.d("TCP","success" );
//				}
				// 高清的压缩图片全部就在  list 路径里面了   //扯淡   ，真是路径为Bimp.drr.get(i)
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				//自行补全？			
			
//				AlertDialog.Builder builder=new AlertDialog.Builder(PublishedActivity.this);//this 出错
//				builder.setTitle("title");
//				builder.setMessage(list.get(0));
//				AlertDialog dialog =builder.create();
//				dialog.show();				
				
//				FileUtils.deleteDir();
		}
		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();//取消对画框
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							TestPicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode != -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}

}
