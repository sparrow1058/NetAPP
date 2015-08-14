package com.example.testpic;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.android.wifidirect.*;
public class TestPicActivity extends Activity {
	// ArrayList<Entity> dataList;//鐢ㄦ潵瑁呰浇鏁版嵁婧愮殑鍒楄〃
	List<ImageBucket> dataList;  //涓�涓洰褰曠殑鐩稿唽瀵硅薄
	GridView gridView;
	ImageBucketAdapter adapter;// 鑷畾涔夌殑閫傞厤鍣�
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private String adress=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);
		
		Intent mIntent=getIntent();
		adress=mIntent.getStringExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * 鍒濆鍖栨暟鎹�
	 */
	private void initData() {
		// /**
		// * 杩欓噷锛屾垜浠亣璁惧凡缁忎粠缃戠粶鎴栬�呮湰鍦拌В鏋愬ソ浜嗘暟鎹紝鎵�浠ョ洿鎺ュ湪杩欓噷妯℃嫙浜�10涓疄浣撶被锛岀洿鎺ヨ杩涘垪琛ㄤ腑
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 鍒濆鍖杤iew瑙嗗浘
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 鏍规嵁position鍙傛暟锛屽彲浠ヨ幏寰楄窡GridView鐨勫瓙View鐩哥粦瀹氱殑瀹炰綋绫伙紝鐒跺悗鏍规嵁瀹冪殑isSelected鐘舵�侊紝
				 * 鏉ュ垽鏂槸鍚︽樉绀洪�変腑鏁堟灉銆� 鑷充簬閫変腑鏁堟灉鐨勮鍒欙紝涓嬮潰閫傞厤鍣ㄧ殑浠ｇ爜涓細鏈夎鏄�
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
				 */
				// adapter.notifyDataSetChanged();
				
				Intent intent = new Intent(TestPicActivity.this,
						ImageGridActivity.class);
				intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				intent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
						adress);
				startActivity(intent);
				
//				finish();
			}

		});
	}
}
