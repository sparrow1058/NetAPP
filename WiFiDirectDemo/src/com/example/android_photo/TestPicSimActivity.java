package com.example.android_photo;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.android.discovery.WiFiServiceDiscoveryActivity;
import com.example.android.wifidirect.R;
import com.example.android_appSim.AppSimMainViewActivity;
import com.example.android_fileSim.FileSimMainActivity;
public class TestPicSimActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;  //�?个目录的相册对象
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配�?
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set, menu);
		return true;	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//		// Handle action bar item clicks here. The action bar will
		//		// automatically handle clicks on the Home/Up button, so long
		//		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_chat:
			Intent intent=new Intent();
			intent.setClass(TestPicSimActivity.this,WiFiServiceDiscoveryActivity.class);
			TestPicSimActivity.this.startActivity(intent);	
			return true;
		case R.id.action_photo:	
			Intent intentset=new Intent(TestPicSimActivity.this,TestPicSimActivity.class);
			TestPicSimActivity.this.startActivity(intentset);
			return true;

		case R.id.action_app:	
			Intent intentapp=new Intent(TestPicSimActivity.this,AppSimMainViewActivity.class);
			TestPicSimActivity.this.startActivity(intentapp);
			return true;

		case R.id.action_file:	
			Intent intentbro=new Intent(TestPicSimActivity.this,FileSimMainActivity.class);
			TestPicSimActivity.this.startActivity(intentbro);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
	/**
	 * 
	 *
	 * 初始化数�?
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或�?�本地解析好了数据，�?以直接在这里模拟�?10个实体类，直接装进列表中
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
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(TestPicSimActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状�?�，
				 * 来判断是否显示�?�中效果�? 至于选中效果的规则，下面适配器的代码中会有说�?
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视�?
				 */
				// adapter.notifyDataSetChanged();
				Intent intent = new Intent(TestPicSimActivity.this,
						ImageGridActivity.class);
				intent.putExtra(TestPicSimActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
//				finish();
			}

		});
	}
}
