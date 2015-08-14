package com.example.androidui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.example.android.wifidirect.*;
import com.example.android_app.AppMainViewActivity;
import com.example.android_file.FileMainActivity;
import com.example.testpic.TestPicActivity;
/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends Activity implements
		ItemListFragment.Callbacks {//返回

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private String adress=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);//展示一个布局，可改为two
		//setContentView(R.layout.activity_item_twopane);//展示two
		
		Intent mIntent=getIntent();
		adress=mIntent.getStringExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
				
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = false;//two时的标志

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getFragmentManager().findFragmentById(//getF..实例化一个布局.在一个activity中找到一个布局-item_list
					R.id.item_list)).setActivateOnItemClick(true);//开启监听
		}
		
		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle(); 
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();//点击后显示的xml

		} else {//里面不存在内容？
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			if(id.equals("1")){
				Intent testPicIntent = new Intent(this,TestPicActivity.class);
				testPicIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
						adress);
				startActivity(testPicIntent);				
			}
			else if(id.equals("2")){
				Intent mIntent = new Intent(this,AppMainViewActivity.class);
				mIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
						adress);
				startActivity(mIntent);		
			}	
			else if(id.equals("3")){
				Intent mIntent = new Intent(this,FileMainActivity.class);
				mIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
						adress);
				startActivity(mIntent);		
			}							
			else{			
				Intent detailIntent = new Intent(this, ItemDetailActivity.class);
				detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);
			}
		}
	}
}
