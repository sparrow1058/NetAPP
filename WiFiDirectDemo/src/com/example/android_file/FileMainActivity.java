package com.example.android_file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wifidirect.FileTransferService;
import com.example.android.wifidirect.R;
import com.example.testpic.PublishedActivity;
public class FileMainActivity extends ListActivity implements OnItemLongClickListener{
	
	private List<String> mFileName=null;//��ʾ���ļ��б���
	private List<String> mFilePaths=null;//��Ӧ·��
	private String mRootPath=java.io.File.separator;//��ʼĿ¼
	//SD��Ŀ¼
	private String mSDCard=Environment.getExternalStorageDirectory().toString();
	private String mOldFilePath="";
	private String mNewFilePath="";
	private String keyWords;
	private TextView mPath;//��ʾ·����
	//���ڷ��ù�����
	private GridView mGridViewToolbar;
	private int[] gridview_menu_image={R.drawable.menu_phone,R.drawable.menu_sdcard,R.drawable.menu_search,
			R.drawable.menu_create,R.drawable.menu_palse,R.drawable.menu_exit};
	private String[] gridview_menu_title={"�ֻ�","SD��","����","����","ճ��","�˳�"};
	private static int menuPosition=1;//1=�ֻ�  2=SD
	private String adress;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_activity_main);
		Intent mIntent=getIntent();
		adress=mIntent.getStringExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
		initGridViewMenu();
		initMenuListener();
		getListView().setOnItemLongClickListener(this);
		mPath=(TextView)findViewById(R.id.mPath);
		//һ��ʼ�����ʱ������ֻ�Ŀ¼�µ��ļ��б�
		initFileListInfo(mSDCard);
	}
	
	private void initGridViewMenu(){
		mGridViewToolbar=(GridView)findViewById(R.id.file_gridview_toolbar);
		//����ѡ��ʱ��ı���ͼƬ
		mGridViewToolbar.setSelector(R.drawable.menu_item_selected);
		mGridViewToolbar.setBackgroundResource(R.drawable.background);
		//��������
		mGridViewToolbar.setNumColumns(6);
		//���ж���
		mGridViewToolbar.setGravity(Gravity.CENTER);
		mGridViewToolbar.setVerticalSpacing(10);
		mGridViewToolbar.setHorizontalSpacing(10);
		//����������
		mGridViewToolbar.setAdapter(getMenuAdapter(gridview_menu_title,gridview_menu_image));						
	}

	//�˵�������
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,int[] imageResourceArray){
		ArrayList<HashMap<String,Object>> mData=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<menuNameArray.length;i++){
			HashMap<String,Object> mMap=new HashMap<String,Object>();
			//��Image,titleӳ���ͼƬ��Դ
			mMap.put("image", imageResourceArray[i]);
			mMap.put("title",menuNameArray[i]);
			mData.add(mMap);	
		}
		//�½����������������������Ĳ����ļ���ӳ���ϵ
		SimpleAdapter mAdapter=new SimpleAdapter(this, mData, R.layout.file_item_menu, new String[]{"image","title"},
				new int[]{R.id.item_image,R.id.item_text});
		return mAdapter;		
	}
	//�˵���ļ���
	protected void initMenuListener(){
		mGridViewToolbar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO �Զ����ɵķ������
				switch (position) {
				case 0:
					menuPosition=1;
					initFileListInfo(mRootPath);					
					break;
				case 1:
					menuPosition=2;
					initFileListInfo(mSDCard);					
					break;
				case 2:					
					searchDilalog();
					break;
				case 3:
					createFolder();				
					break;
				case 4:
					palseFile();				
					break;
				case 5:
					FileMainActivity.this.finish();				
					break;
				default:
					break;
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_items, menu);
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_setting) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/


	/**����������**/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO �Զ����ɵķ������
		if(isAddBackUp==true){//˵�����ڷ��ظ�Ŀ¼�ͷ����ϼ����У�������
			if(position!=0&&position!=1){
				initItemLongClickListener(new File(mFileName.get(position)));
			}

		}
		if(mCurrentFilePath.equals(mRootPath)||mCurrentFilePath.equals(mSDCard)){
			initItemLongClickListener(new File(mFileName.get(position)));
		}
		return false;
	}


	private String mCopyFileName;
	private boolean isCopy=false;

	private void initItemLongClickListener(final File file){
		OnClickListener listen=new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
//				if(file.canRead()){
					if(which==0){			
						String Str=mCurrentFilePath+java.io.File.separator+file.getName();
						Intent serviceIntent = new Intent(FileMainActivity.this, FileTransferService.class);//ע��ͻ��˴��ļ�����ͼ
						serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
						serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, Str);
						serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
								adress);
						serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
						FileMainActivity.this.startService(serviceIntent);		
					}					
					else if(which==1){
						if(file.isFile()&&"txt".equals(file.getName()
								.substring(file.getName().lastIndexOf(".")+1,
										file.getName().length()).toLowerCase())){
							Toast.makeText(FileMainActivity.this, "�Ѹ���", Toast.LENGTH_SHORT).show();
							isCopy=true;
							mCopyFileName=file.getName();
							//��¼�����ļ���·��
							mOldFilePath=mCurrentFilePath+java.io.File.separator+mCopyFileName;
						}else{
							Toast.makeText(FileMainActivity.this,"only�ı��ܸ���",Toast.LENGTH_SHORT).show();
						}
					}else if(which==2){
						initRenameDialog(file);						
					}else if(which==3){
						initRenameDialog(file);
					}
				}
			/*else{
					Toast.makeText(FileMainActivity.this,"����Ȩ�޲���",Toast.LENGTH_SHORT).show();
				}*/				
//			}
		};
		//�б�������
		String[] mMenu={"����","����","������","ɾ��"};
		//��ʾ����ѡ��Ի���
		new AlertDialog.Builder(FileMainActivity.this)
		.setTitle("��ѡ�����")
		.setItems(mMenu, listen)
		.setPositiveButton("ȡ��", null).show();

	}

	/**��ʾָ��Ŀ¼����**/
	public static String mCurrentFilePath="";

	//����
	private void initFileListInfo(String filePath){
		isAddBackUp=false;
		mCurrentFilePath=filePath;
		//��ʾ��ǰ·��
		mPath.setText(filePath);
		mFileName=new ArrayList<String>();
		mFilePaths=new ArrayList<String>();
		File mFile=new File(filePath);
		//�����ļ����µ�����..
		File[] mFiles=mFile.listFiles();
		//ֻҪ��ǰ·�����Ǹ�Ŀ¼������ʾ���ء���һ���͸���
		if(menuPosition==1&&!mCurrentFilePath.equals(mRootPath)){
			initAddBackUp(filePath,mRootPath);			
		}
		if(menuPosition==2&&!mCurrentFilePath.equals(mSDCard)){
			initAddBackUp(filePath,mSDCard);			
		}
		for(File mCurrentFile:mFiles){
			mFileName.add(mCurrentFile.getName());
			mFilePaths.add(mCurrentFile.getPath());			
		}		
		setListAdapter(new FileAdapter(FileMainActivity.this,mFileName,mFilePaths));
	}

	private boolean isAddBackUp=false;
	
	private void initAddBackUp(String filePath,String phone_sdcard){
		if(!filePath.equals(phone_sdcard)){
			mFileName.add("BacktoRoot");
			mFilePaths.add(phone_sdcard);
			mFileName.add("BacktoUp");
			mFilePaths.add(new File(filePath).getParent());
			isAddBackUp=true;
		}		 
	}
	
	private String mNewFolderName = "";
    private File mCreateFile;
    private RadioGroup mCreateRadioGroup;
    private static int mChecked;
    /**�����ļ��еķ���:���û�����������Ĵ����˵���ʱ�����ڵ�ǰĿ¼�´�����һ���ļ���
     * ��̬����mCurrentFilePath�洢�ľ��ǵ�ǰ·��
     * java.io.File.separator��JAVA�������ṩ��һ��File���еľ�̬��Ա���������ϵͳ�Ĳ�ͬ�������ָ���
     * mNewFolderName��������Ҫ���������ļ������ƣ���EditText����ϵõ���*/
    private void createFolder(){
    	//���ڱ�ʶ��ǰѡ�е����ļ������ļ���
    	mChecked = 2;
    	LayoutInflater mLI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	//��ʼ���Ի��򲼾�
    	final LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.file_create_dialog, null);
    	mCreateRadioGroup = (RadioGroup)mLL.findViewById(R.id.radiogroup_create);
    	final RadioButton mCreateFileButton = (RadioButton)mLL.findViewById(R.id.create_file);
    	final RadioButton mCreateFolderButton = (RadioButton)mLL.findViewById(R.id.create_folder);
    	//����Ĭ��Ϊ�����ļ���
    	mCreateFolderButton.setChecked(true);
    	//Ϊ��ť���ü�����
    	mCreateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
    		//��ѡ��ı�ʱ����
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(arg1 == mCreateFileButton.getId()){
					mChecked = 1;
				}else if(arg1 == mCreateFolderButton.getId()){
					mChecked = 2;
				}
			}   		
    	});
    	//��ʾ�Ի���
    	Builder mBuilder = new AlertDialog.Builder(FileMainActivity.this)
    	.setTitle("�½�")
    	.setView(mLL)
    	.setPositiveButton("����", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				//�����û����������
				mNewFolderName = ((EditText)mLL.findViewById(R.id.new_filename)).getText().toString();
				if(mChecked == 1){
					try {
						mCreateFile = new File(mCurrentFilePath+java.io.File.separator+mNewFolderName+".txt");
						mCreateFile.createNewFile();
						//ˢ�µ�ǰĿ¼�ļ��б�
						initFileListInfo(mCurrentFilePath);
					} catch (IOException e) {
						Toast.makeText(FileMainActivity.this, "�ļ���ƴ�ӳ���..!!", Toast.LENGTH_SHORT).show();
					}
				}else if(mChecked == 2){
					mCreateFile = new File(mCurrentFilePath+java.io.File.separator+mNewFolderName);
					if(!mCreateFile.exists()&&!mCreateFile.isDirectory()&&mNewFolderName.length() != 0){
						if(mCreateFile.mkdirs()){
							//ˢ�µ�ǰĿ¼�ļ��б�
							initFileListInfo(mCurrentFilePath);
						}else{
							Toast.makeText(FileMainActivity.this, "����ʧ�ܣ�������ϵͳȨ�޲�����rootһ�£���", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(FileMainActivity.this, "�ļ���Ϊ�գ������������أ�", Toast.LENGTH_SHORT).show();
					}
				}			
			}
    	}).setNeutralButton("ȡ��", null);
    	mBuilder.show();
    }
    
    EditText mET;
    //��ʾ�������Ի���
    private void initRenameDialog(final File file){
    	LayoutInflater mLI = LayoutInflater.from(FileMainActivity.this);
    	//��ʼ���������Ի���
    	LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.file_rename_dialog, null);
    	mET = (EditText)mLL.findViewById(R.id.new_filename);
    	//��ʾ��ǰ���ļ���
    	mET.setText(file.getName());
    	//���ü�����
    	OnClickListener listener = new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog,int which){
    			String modifyName = mET.getText().toString();
    			final String modifyFilePath = file.getParentFile().getPath()+java.io.File.separator;
    			final String newFilePath = modifyFilePath+modifyName;
    			//�жϸ��µ��ļ����Ƿ��Ѿ��ڵ�ǰĿ¼�´���
    			if(new File(newFilePath).exists()){
    				if(!modifyName.equals(file.getName())){//�ѡ�������������ʱû���κ��޸ĵ�������˵�
    					//����������������ļ��Ѿ����ڵ���ʾ������ʾ�������Ĳ���
    					new AlertDialog.Builder(FileMainActivity.this)
						.setTitle("��ʾ!")
						.setMessage("���ļ����Ѵ��ڣ��Ƿ�Ҫ����?")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,int which){
								file.renameTo(new File(newFilePath));
								Toast.makeText(FileMainActivity.this,
							"the file path is "+new File(newFilePath), Toast.LENGTH_SHORT).show();
								//���µ�ǰĿ¼��Ϣ
								initFileListInfo(file.getParentFile().getPath());
							}
						})
						.setNegativeButton("ȡ��", null).show();
    				}
    			}else{
    				//�ļ������ظ�ʱֱ���޸��ļ������ٴ�ˢ���б�
    				file.renameTo(new File(newFilePath));
					initFileListInfo(file.getParentFile().getPath());
    			}
    		}   		
    	};
    	//��ʾ�Ի���
    	AlertDialog renameDialog = new AlertDialog.Builder(FileMainActivity.this).create();
    	renameDialog.setView(mLL);
    	renameDialog.setButton("ȷ��", listener);
    	renameDialog.setButton2("ȡ��", new DialogInterface.OnClickListener(){		
    		public void onClick(DialogInterface dialog,int which){
    		//ʲô���������رյ�ǰ�Ի���
    		}		
    	});
    	renameDialog.show();
    }
	

	/***ɾ���ļ�***/
	private void initDeleteDialog(final File file){
		new AlertDialog.Builder(FileMainActivity.this)
		.setTitle("��ʾ")
		.setMessage("��ȷ��Ҫɾ����"+(file.isDirectory()?"�ļ���":"�ļ�")+"��?")
		.setPositiveButton("��ȷ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				if(file.isFile()){
					file.delete();
				}else{
					//�ļ���ɾ������
					deleteFolder(file);
				}
				initFileListInfo(file.getParent());				
			}
		})
		.setNegativeButton("ȡ��", null).show();
	}

	public void deleteFolder(File folder){
		File[] fileArray=folder.listFiles();
		if(fileArray.length==0){
			folder.delete();			
		}else{
			//������Ŀ¼
			for(File currentFile:fileArray){
				if(currentFile.exists()&&currentFile.isFile()){
					currentFile.delete();				
				}else{
					//�ݹ�ɾ��
					deleteFolder(currentFile);
				}
			}
			folder.delete();//?
		}

	}

	/***ճ���ļ�****/

	private void palseFile(){
		mNewFilePath=mCurrentFilePath+java.io.File.separator+mCopyFileName;
		Log.d("copy","mOldFilePath is "+mOldFilePath+"| mNewFilePath is "+mNewFilePath +
				"| isCopy is "+isCopy);
		if(!mOldFilePath.equals(mNewFilePath)&&isCopy==true){//��ͬ·����
			if(!new File(mNewFilePath).exists()){
				copyFile(mOldFilePath,mNewFilePath);
				Toast.makeText(FileMainActivity.this, "ִ��ճ��", Toast.LENGTH_SHORT).show();
				initFileListInfo(mCurrentFilePath);

			}else{
				new AlertDialog.Builder(FileMainActivity.this)
				.setTitle("��ʾ")
				.setMessage("�ļ��Ѿ����ڣ��Ƿ�Ҫ���ǣ�")
				.setPositiveButton("��ȷ", new DialogInterface.OnClickListener(){				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
						copyFile(mOldFilePath,mNewFilePath);					
						initFileListInfo(mCurrentFilePath);				
					}
				})
				.setNegativeButton("ȡ��", null).show();
			}

		}else{
			Toast.makeText(FileMainActivity.this, "Ϊ�����ļ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	private int i;
	FileInputStream fis;
	FileOutputStream fos;
	//�����ļ�
	private void copyFile(String oldFile,String newFile){
		try{
			fis=new FileInputStream(oldFile);
			fos=new FileOutputStream(newFile);	
			do{
				if((i=fis.read())!=-1){//���Byte�Ķ�
					fos.write(i);
				}
			}while(i!=-1);
			if(fis!=null){
				fis.close();
			}
			if(fos!=null){
				fos.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/***�����ļ�***/
	Intent serviceIntent;
    ServiceConnection mSC;
    RadioGroup mRadioGroup;
    static int mRadioChecked;
    public static final String KEYWORD_BROADCAST = "com.supermario.file.KEYWORD_BROADCAST";
	
	private void searchDilalog(){
		//ȷ�����ڵ�ǰĿ¼��������Ŀ¼����
		mRadioChecked=1;
		LayoutInflater mLI=LayoutInflater.from(FileMainActivity.this);
		final View mLL=(View)mLI.inflate(R.layout.file_search_dialog, null);
		mRadioGroup=(RadioGroup)mLL.findViewById(R.id.radiogroup_search);
		final RadioButton mCurrentPathButton=(RadioButton)mLL.findViewById(R.id.radio_currentpath);
		final RadioButton mWholePathButton=(RadioButton)mLL.findViewById(R.id.radio_wholepath);
		
		//Ĭ��ѡ���ڵ�ǰ·������
		mCurrentPathButton.setChecked(true);
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO �Զ����ɵķ������
				//��ǰ·���ı���Ϊ1
				if(checkedId==mCurrentPathButton.getId()){
					mRadioChecked=1;					
				}else if(checkedId==mWholePathButton.getId()){
					mRadioChecked=2;
				}				
			}
		});
		Builder mBuilder=new AlertDialog.Builder(FileMainActivity.this)
		.setTitle("����")
		.setView(mLL)
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				keyWords=((EditText)mLL.findViewById(R.id.edit_search)).getText().toString();
				if(keyWords.length()==0){
					Toast.makeText(FileMainActivity.this, "�ؼ��ֲ���Ϊ��", Toast.LENGTH_SHORT).show();
					searchDilalog();
				}else{
					if(menuPosition==1){
						mPath.setText(mRootPath);
					}else{
						mPath.setText(mSDCard);
					}
					//��ȡ�û������룬�����͹㲥
					Intent keyWordIntent=new Intent();
					keyWordIntent.setAction(KEYGUARD_SERVICE);
					//ȷ��������Χ,��ǰ/SD
					if(mRadioChecked==1){
						keyWordIntent.putExtra("searchpath", mCurrentFilePath);						
					}else{
						keyWordIntent.putExtra("searchpath", mSDCard);
					}
					//���ݹؼ���
					keyWordIntent.putExtra("keyword", keyWords);
					/*Я���ؼ��ֲ������˹㲥��Ȼ����service�����н��ոù㲥*/
					getApplicationContext().sendBroadcast(keyWordIntent);
					serviceIntent=new Intent("com.android.service.FILE_SEARCH_START");
					FileMainActivity.this.startService(serviceIntent);//������������
					isComeBackFromNotification=false;			 							
				}
			}
		})
		.setNegativeButton("ȡ��", null);
		mBuilder.show();
	}
	
	/**����ϵͳ�ķ����������ļ��ķ���*/
    private void openFile(File file){
    	if(file.isDirectory()){
    		initFileListInfo(file.getPath());
    	}else{
    		Intent intent = new Intent();
        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intent.setAction(android.content.Intent.ACTION_VIEW);
        	//���õ�ǰ�ļ�����
        	intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        	startActivity(intent);
    	}
    }
    
    /**���MIME���͵ķ���*/
    private String getMIMEType(File file){
    	String type = "";
    	String fileName = file.getName();
    	//ȡ���ļ���׺����ת��Сд
    	String  fileEnds = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();
    	if(fileEnds.equals("m4a")||fileEnds.equals("mp3")||fileEnds.equals("mid")||fileEnds.equals("xmf")||fileEnds.equals("ogg")||fileEnds.equals("wav")){
    		type = "audio/*";// ϵͳ���г����п��ܴ���Ƶ�ļ��ĳ���ѡ����
    	}else if(fileEnds.equals("3gp")||fileEnds.equals("mp4")){
    		type = "video/*";// ϵͳ���г����п��ܴ���Ƶ�ļ��ĳ���ѡ����
    	}else if(fileEnds.equals("jpg")||fileEnds.equals("gif")||fileEnds.equals("png")||fileEnds.equals("jpeg")||fileEnds.equals("bmp")){
    		type = "image/*";// ϵͳ���г����п��ܴ�ͼƬ�ļ��ĳ���ѡ����
    	}else{
    		type = "*/*"; // ϵͳ���г����п��ܴ򿪸��ļ��ĳ���ѡ����
    	}
    	return type;
    }
	
    /**�б�����ʱ���¼�����*/
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
    	final File mFile = new File(mFilePaths.get(position));
    	//������ļ��ǿɶ��ģ����ǽ�ȥ�鿴�ļ�
    	if(mFile.canRead()){
    		if(mFile.isDirectory()){
    			//������ļ��У���ֱ�ӽ�����ļ��У��鿴�ļ�Ŀ¼
    			initFileListInfo(mFilePaths.get(position));
    		}else{
    			//������ļ���������Ӧ�Ĵ򿪷�ʽ��
    			String fileName = mFile.getName();
    	    	String  fileEnds = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();
    			if(fileEnds.equals("txt")){
    				//��ʾ����������ʾ���ڶ�ȡ
					initProgressDialog(ProgressDialog.STYLE_HORIZONTAL);
    				new Thread(new Runnable(){
    					public void run(){
    						//���ı��ļ�
    						openTxtFile(mFile.getPath());
    					}
    				}).start();
    				new Thread(new Runnable(){
    					public void run(){
    						while(true){
    							if(isTxtDataOk == true){
    								//�رս�����
    								mProgressDialog.dismiss();
    								executeIntent(txtData.toString(),mFile.getPath());
    								break;
    							}
    							if(isCancleProgressDialog == true){
    								//�رս�����
    								mProgressDialog.dismiss();
    								break;
    							}
    						}
    					}
    				}).start();
    				//�����html�ļ������Լ�д�Ĺ��ߴ�
    			} else if(fileEnds.equals("html")||fileEnds.equals("mht")||fileEnds.equals("htm")){
    				Intent intent = new Intent(FileMainActivity.this,WebActivity.class);
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				intent.putExtra("filePath", mFile.getPath());
    				startActivity(intent);
    			} else {
    				openFile(mFile);
				}
    		}
    	}else{
    		//������ļ����ɶ������Ǹ�����ʾ���ܷ��ʣ���ֹ�û�����ϵͳ�ļ����ϵͳ������
    		Toast.makeText(FileMainActivity.this, "�Բ������ķ���Ȩ�޲���!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    //������  
    ProgressDialog mProgressDialog;
    boolean isCancleProgressDialog = false;
    /**�������ڽ����ı����ݵ�ProgressDialog*/
    private void initProgressDialog(int style){
    	isCancleProgressDialog = false;
    	mProgressDialog = new ProgressDialog(this);
    	mProgressDialog.setTitle("��ʾ");
    	mProgressDialog.setMessage("����Ϊ������ı����ݣ����Ժ�...");
    	mProgressDialog.setCancelable(true);
    	mProgressDialog.setButton("ȡ��", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				isCancleProgressDialog = true;
				mProgressDialog.dismiss();
			}   		
    	});
    	mProgressDialog.show();
    }
	
  //�Զ���Adapter�ڲ���
    class FileAdapter extends BaseAdapter{
    	//���ؼ������ָ�ʽ���ļ���ͼ��
    	private Bitmap mBackRoot;
    	private Bitmap mBackUp;
    	private Bitmap mImage;
    	private Bitmap mAudio;
    	private Bitmap mRar;
    	private Bitmap mVideo;
    	private Bitmap mFolder;
    	private Bitmap mApk;
    	private Bitmap mOthers;
    	private Bitmap mTxt;
    	private Bitmap mWeb;
    	
    	private Context mContext;
    	//�ļ����б�
    	private List<String> mFileNameList;
    	//�ļ���Ӧ��·���б�
    	private List<String> mFilePathList;
    	
    	public FileAdapter(Context context,List<String> fileName,List<String> filePath){
    		mContext = context;
    		mFileNameList = fileName;
    		mFilePathList = filePath;
    		//��ʼ��ͼƬ��Դ
    		//���ص���Ŀ¼
    		mBackRoot = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.back_to_root);
    		//���ص���һ��Ŀ¼
    		mBackUp = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.back_to_up);
    		//ͼƬ�ļ���Ӧ��icon
    		mImage = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.image);
    		//��Ƶ�ļ���Ӧ��icon
    		mAudio = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.audio);
    		//��Ƶ�ļ���Ӧ��icon
    		mVideo = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.video);
    		//��ִ���ļ���Ӧ��icon
    		mApk = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.apk);
    		//�ı��ĵ���Ӧ��icon
    		mTxt = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.txt);
    		//���������ļ���Ӧ��icon
    		mOthers = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.others);
    		//�ļ��ж�Ӧ��icon
    		mFolder = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.folder);
    		//zip�ļ���Ӧ��icon
    		mRar = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.zip_icon);
    		//��ҳ�ļ���Ӧ��icon
    		mWeb = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.web_browser);
    	}
    	//����ļ�������
		public int getCount() {
			return mFilePathList.size();
		}
		//��õ�ǰλ�ö�Ӧ���ļ���
		public Object getItem(int position) {
			return mFileNameList.get(position);
		}
		//��õ�ǰ��λ��
		public long getItemId(int position) {
			return position;
		}
		//�����ͼ
		public View getView(int position, View convertView, ViewGroup viewgroup) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater mLI = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//��ʼ���б�Ԫ�ؽ���
				convertView = mLI.inflate(R.layout.file_list_child, null);
				//��ȡ�б��ֽ���Ԫ��
				viewHolder.mIV = (ImageView)convertView.findViewById(R.id.image_list_childs);
				viewHolder.mTV = (TextView)convertView.findViewById(R.id.text_list_childs);
				//��ÿһ�е�Ԫ�ؼ������óɱ�ǩ
				convertView.setTag(viewHolder);
			} else {
				//��ȡ��ͼ��ǩ
				viewHolder = (ViewHolder) convertView.getTag();
			}
			File mFile = new File(mFilePathList.get(position).toString());
			//���
			if(mFileNameList.get(position).toString().equals("BacktoRoot")){
				//��ӷ��ظ�Ŀ¼�İ�ť
				viewHolder.mIV.setImageBitmap(mBackRoot);
				viewHolder.mTV.setText("���ظ�Ŀ¼");
			}else if(mFileNameList.get(position).toString().equals("BacktoUp")){
				//��ӷ�����һ���˵��İ�ť
				viewHolder.mIV.setImageBitmap(mBackUp);
				viewHolder.mTV.setText("������һ��");
			}else if(mFileNameList.get(position).toString().equals("BacktoSearchBefore")){
				//��ӷ�������֮ǰĿ¼�İ�ť
				viewHolder.mIV.setImageBitmap(mBackRoot);
				viewHolder.mTV.setText("��������֮ǰĿ¼");
			}else{
				String fileName = mFile.getName();
				viewHolder.mTV.setText(fileName);
				if(mFile.isDirectory()){
					viewHolder.mIV.setImageBitmap(mFolder);
				}else{
			    	String fileEnds = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();//ȡ���ļ���׺����ת��Сд
			    	if(fileEnds.equals("m4a")||fileEnds.equals("mp3")||fileEnds.equals("mid")||fileEnds.equals("xmf")||fileEnds.equals("ogg")||fileEnds.equals("wav")){
			    		viewHolder.mIV.setImageBitmap(mVideo);
			    	}else if(fileEnds.equals("3gp")||fileEnds.equals("mp4")){
			    		viewHolder.mIV.setImageBitmap(mAudio);
			    	}else if(fileEnds.equals("jpg")||fileEnds.equals("gif")||fileEnds.equals("png")||fileEnds.equals("jpeg")||fileEnds.equals("bmp")){
			    		viewHolder.mIV.setImageBitmap(mImage);
			    	}else if(fileEnds.equals("apk")){
			    		viewHolder.mIV.setImageBitmap(mApk);
			    	}else if(fileEnds.equals("txt")){
			    		viewHolder.mIV.setImageBitmap(mTxt);
			    	}else if(fileEnds.equals("zip")||fileEnds.equals("rar")){
			    		viewHolder.mIV.setImageBitmap(mRar);
			    	}else if(fileEnds.equals("html")||fileEnds.equals("htm")||fileEnds.equals("mht")){
			    		viewHolder.mIV.setImageBitmap(mWeb);
			    	}else {
			    		viewHolder.mIV.setImageBitmap(mOthers);
			    	}
				}				
			}
			return convertView;
		}
    	//���ڴ洢�б�ÿһ��Ԫ�ص�ͼƬ���ı�
		class ViewHolder {
			ImageView mIV;
			TextView mTV;
		}
    }
    

    /**ע��㲥*/
    private IntentFilter mFilter;
    private FileBroadcast mFileBroadcast;
    private IntentFilter mIntentFilter;
	private SearchBroadCast mServiceBroadCast;
	@Override
    protected void onStart() {
    	super.onStart();
    	mFilter = new IntentFilter();
    	mFilter.addAction(FileService.FILE_SEARCH_COMPLETED);
    	mFilter.addAction(FileService.FILE_NOTIFICATION);
    	mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(KEYWORD_BROADCAST);
    	if(mFileBroadcast == null){
    		mFileBroadcast = new FileBroadcast();
    	}
    	if(mServiceBroadCast == null){
    		mServiceBroadCast = new SearchBroadCast();
    	}
    	this.registerReceiver(mFileBroadcast, mFilter);
    	this.registerReceiver(mServiceBroadCast, mIntentFilter);
    }
    
   
	/**ע���㲥*/
    @Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("NullPointError", "onDestroy");
		mFileName.clear();
		mFilePaths.clear();
    	this.unregisterReceiver(mFileBroadcast);
    	this.unregisterReceiver(mServiceBroadCast);
	}
    
	private String mAction;
    public static boolean isComeBackFromNotification = false;
    /**�ڲ��㲥��*/
    class FileBroadcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			mAction = intent.getAction();
			// ������ϵĹ㲥
			if(FileService.FILE_SEARCH_COMPLETED.equals(mAction)){
				mFileName = intent.getStringArrayListExtra("mFileNameList");
				mFilePaths = intent.getStringArrayListExtra("mFilePathsList");
				Toast.makeText(FileMainActivity.this, "�������!", Toast.LENGTH_SHORT).show();
				//�����������֮��Ӧ�õ���һ����������ʾ�û�Ҫ��Ҫ��ʾ����
				searchCompletedDialog("������ϣ��Ƿ�������ʾ���?");
				getApplicationContext().stopService(serviceIntent);//��������ϵ�ʱ��ֹͣ����Ȼ���ڷ�����ȡ��֪ͨ
			// ���֪ͨ����ת�����Ĺ㲥
			}else if(FileService.FILE_NOTIFICATION.equals(mAction)){//���֪ͨ�ص���ǰActivity����ȡ������Ϣ
				String mNotification = intent.getStringExtra("notification");
				Toast.makeText(FileMainActivity.this, mNotification, Toast.LENGTH_LONG).show();
				searchCompletedDialog("��ȷ��Ҫȡ��������?");
			}
		}
    }
    
    //������Ϻ͵��֪ͨ����ʱ����ʾ��
    private void searchCompletedDialog(String message){
		Builder searchDialog = new AlertDialog.Builder(FileMainActivity.this)
		.setTitle("��ʾ")
		.setMessage(message)
		.setPositiveButton("ȷ��", new OnClickListener(){
			public void onClick(DialogInterface dialog,int which) {
				//��������ʱ����Ҫ�����ȷ����ť����һ���жϣ���ΪҪ�Բ�ͬ���������ͬ�Ĵ���2�������
				// 1.�������
				// 2.ȡ������
				if(FileService.FILE_SEARCH_COMPLETED.equals(mAction)){
					if(mFileName.size() == 0){
			    		Toast.makeText(FileMainActivity.this, "������ļ�/�ļ���!", Toast.LENGTH_SHORT).show();
			    		setListAdapter(new FileAdapter(FileMainActivity.this,mFileName,mFilePaths));//����б�
			    	}else{ 
			    		//��ʾ�ļ��б�
			    		setListAdapter(new FileAdapter(FileMainActivity.this,mFileName,mFilePaths));
			    	}
				}else{
					//����������־Ϊtrue��
					isComeBackFromNotification = true;
					//�رշ���ȡ������
					getApplicationContext().stopService(serviceIntent);
				}
			}
		})
		.setNegativeButton("ȡ��", null);
		searchDialog.create();
		searchDialog.show();
    }
    
    String txtData = "";
    boolean isTxtDataOk = false;
    //���ı��ļ��ķ���֮��ȡ�ļ�����
    private void openTxtFile(String file){
    	isTxtDataOk = false;
    	try {
			FileInputStream fis = new FileInputStream(new File(file));
			StringBuilder mSb = new StringBuilder(); 
			int m;
			//��ȡ�ı��ļ�����
			while((m = fis.read()) != -1){
				mSb.append((char)m);
			}
			fis.close();
			//�����ȡ��������
			txtData = mSb.toString();
			//��ȡ���
			isTxtDataOk = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    //ִ��Intent��ת�ķ���
    private void executeIntent(String data,String file){   	
    	Intent intent = new Intent(FileMainActivity.this,EditTxtActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//�����ļ���·�������������
		intent.putExtra("path", file);
		intent.putExtra("title", new File(file).getName());
		intent.putExtra("data", data.toString());
		//��ת��EditTxtActivity
		startActivity(intent);
    }

}









