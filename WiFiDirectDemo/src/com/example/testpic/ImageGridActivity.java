package com.example.testpic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.example.testpic.ImageGridAdapter.TextCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.android.wifidirect.*;
public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;//鐢ㄦ潵瑁呰浇鏁版嵁婧愮殑鍒楄〃
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;// 鑷畾涔夌殑閫傞厤鍣�
	AlbumHelper helper;
	Button bt;
	private String adress=null;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("testpic","imgain");
		setContentView(R.layout.activity_image_grid);

		Intent mIntent=getIntent();
		adress=mIntent.getStringExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);
		
		initView();
		
		//bt跳转到PublishedActivity
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
//					Intent ItemIntent = new Intent(ImageGridActivity.this, PublishedActivity.class);  
//					ItemIntent.setType("image/*");
//					startActivityForResult(ItemIntent,1);
					
					Intent intent = new Intent(ImageGridActivity.this,
							PublishedActivity.class);
					intent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
							adress);
					startActivity(intent);
//					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 9) {
						Bimp.drr.add(list.get(i));
						Log.d("list",list.get(i));//文件绝对路径
					}
				}
							
//				finish();
			}

		});
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		// User has picked an image. Transfer it to group owner i.e peer using
//		// FileTransferService.  点击图片后
//		switch(requestCode){
//		case 1:
//			ArrayList<String> mArrayList=data.getStringArrayListExtra("list");
//			for (int i = 0; i < mArrayList.size(); i++) {
//				Uri uri=Uri.fromFile(new File(mArrayList.get(i)));
//				//Uri uri = data.getData();
//				TextView statusText = (TextView)ImageGridActivity.this.findViewById(R.id.status_text);
//				statusText.setText("发送: " + uri);
//				Log.d(WiFiDirectActivity.TAG, "Intent----------- " + uri);
//				Intent serviceIntent = new Intent(ImageGridActivity.this, FileTransferService.class);//注册客户端传文件的意图
//				serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
//				serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
//				serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
//						info.groupOwnerAddress.getHostAddress());
//				serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
//				getActivity().startService(serviceIntent);
//			}
//		case 2:
//			break;
//
//		}
//	}
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//返回上一个ac
		int id = item.getItemId();//每一个MenuItem
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:			
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			
			NavUtils.navigateUpTo(this,
					new Intent(this, TestPicActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 鍒濆鍖杤iew瑙嗗浘
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 鏍规嵁position鍙傛暟锛屽彲浠ヨ幏寰楄窡GridView鐨勫瓙View鐩哥粦瀹氱殑瀹炰綋绫伙紝鐒跺悗鏍规嵁瀹冪殑isSelected鐘舵
				 * �锛� 鏉ュ垽鏂槸鍚︽樉绀洪�涓晥鏋溿� 鑷充簬閫変腑鏁堟灉鐨勮鍒欙紝涓嬮潰閫傞厤鍣ㄧ殑浠ｇ爜涓細鏈夎鏄�
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
				 */
				adapter.notifyDataSetChanged();
			}

		});

	}
}
