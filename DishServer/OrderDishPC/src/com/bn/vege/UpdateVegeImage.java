package com.bn.vege;

import static com.bn.constant.Constant.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
/*修改菜品的图片的界面*/
@SuppressWarnings("serial")
public class UpdateVegeImage extends JFrame
{
	MainUI mainui;
	DefaultMutableTreeNode curenode=Constant.vegenode[5];//当前节点为菜品管理
    int width=560;//定义宽度
    int height=700;//定义高度
    Font font=new Font("宋体",Font.BOLD,14);//定义两种字体
    Font fonttitle=new Font("宋体",Font.BOLD,20);
    
    String vegeId;
    List<byte[]> imagelist=new ArrayList<byte[]>();
    int mainindex;
    int mainpathlength;
    @SuppressWarnings("unused")
	public UpdateVegeImage(final MainUI mainui, final String vegeId)
    {
	   this.mainui=mainui;
	   this.vegeId=vegeId;
	   final JPanel jp=new JPanel();//定义JPanel
	   jp.setLayout(null);//设置JPanel布局器为默认
	   this.setBounds(200, 0, 560, 700);//界面位置与大小
	    //左边距
		 int leftwidth=width/30;
		 //高的距离
		 int topheight=height/90;
		 //JLabel的宽度
		 int lwidth=width/2/3;
		 //JLabel的高度
		 int lheight=height/22;
		 //九宫格之间的距离
		 int span=width/100;
		 
		 int vigwidth=510;
		 int vigheight=540;
		 //根据菜品id得到菜品主图的大图
		 SocketClient.ConnectSevert(GET_MBIMAGE+vegeId);
		 final String vegemainbigpath=SocketClient.readinfo;
		 SocketClient.ConnectSevert(GET_IMAGE+vegemainbigpath);
		 byte mainbigimagemsg[] =SocketClient.data;
		 imagelist.add(mainbigimagemsg);
		 mainindex=0;
		 
		//根据菜品id得到菜品主图路径
		 SocketClient.ConnectSevert(GET_W_PICBYVEGEID+vegeId);
		 final String vegemainpath=SocketClient.readinfo;
		 SocketClient.ConnectSevert(GET_IMAGE+vegemainpath);
		 byte mainimagemsg[] =SocketClient.data;
		 
		 //根据菜品id得到菜品子图片路径
		 SocketClient.ConnectSevert(GET_ZBIMAGE+vegeId);
		 String vegechildpath=SocketClient.readinfo;
	     final List<String> childpath=new ArrayList<String>();
	     if(vegechildpath.equals(""))
	     {
	    	 mainpathlength=1;
	     }
	     else
	     {
			 String[] path=vegechildpath.split(",");
			 mainpathlength=path.length+1;//记录最开始图片的个数
		     for(int i=0;i<path.length;i++)
			 {
		    	 childpath.add(path[i]);
				 SocketClient.ConnectSevert(GET_IMAGE+childpath.get(i));
				 byte imagemsg[] =SocketClient.data;
				 //如果是主图的大图则标志位1，否则标志位0 
				 imagelist.add(imagemsg);
			 }
	     }
		 //调用UpdataImageGird方法，更新菜品图片
		 final UpdateImageGrid uig =new UpdateImageGrid(vigwidth,vigheight,span,imagelist,mainindex);
	     JScrollPane jsp=new JScrollPane();//定义滚动条
	     jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//设置纵向滚动条 
	     jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//设置横向滚动条
	     jsp.setAutoscrolls(true);//设置为自动调整尺寸
	     jsp.setBounds(leftwidth, 2*topheight+2*lheight, vigwidth, vigheight);
	     jsp.setViewportView(uig);//将滚动条放到添加图片框中
	     jp.add(jsp);//将滚动条添加到JPanel中

		 JLabel jltitle=new JLabel("修改菜品图片");//定义JLabel
		 jltitle.setFont(fonttitle);//设置字体
		 jltitle.setBounds(width/2-width/2/4, topheight, 2*lwidth, lheight);//设置标题位置与大小
		 jp.add(jltitle);//将标题添加到JPanel中
		 
		 final JButton jbselect=new JButton("添加图片");//定义JButton
         jbselect.setFont(font);//设置字体
         jp.add(jbselect);//将JButton添加到JPanel中
         jbselect.setBounds(leftwidth, topheight+lheight, lwidth, lheight);//设置按钮位置与大小
         jbselect.addActionListener//按钮添加监听
         (
        	new ActionListener()
        	{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser jfc=new JFileChooser("c:\\");
					jfc.showOpenDialog(jbselect);//弹出文件选择器
					jfc.setCurrentDirectory(null);
					File file=jfc.getSelectedFile();
					 if(!(file.exists() && file.isFile() && file.getName().endsWith(".jpg"))) 
					 {  
						 JOptionPane.showMessageDialog(UpdateVegeImage.this, "选择文件错误，请重新选择一个正确的文件。","提示",JOptionPane.INFORMATION_MESSAGE);
				     } 
					 else
					 {
						 //将图片放入九宫格中
						 byte[] imagemsg=TypeExchangeUtil.filetobyte(file);
						 imagelist.add(imagemsg);
						 UpdateImageGrid.imagemsg=imagemsg;
						 //重绘
						 uig.drawImageGrid();
						 jp.updateUI();
					 }
				  }	
				}		 
           );
         //上传控件
         final JButton jbupload=new JButton("保存");
         jbupload.setFont(font);//设置字体
         jp.add(jbupload);//将按钮添加到JPanel中
         jbupload.setBounds(width/2+width/4, topheight+lheight, lwidth, lheight);//设置位置与大小
         jbupload.addActionListener//按钮添加监听
         (
           new ActionListener()
           {
			@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//添加主图的小图
				if(mainindex!=uig.mainindex)
				{
	    			//加载新的主图存入服务器
	    			byte[] imagemsg=imagelist.get(uig.mainindex);
	    			SocketClient.ConnectSevertBYTE(UPLOAD_IMAGE, UPLOAD_MAIN_VEGEIAMGE, imagemsg);
                    String imagepth=SocketClient.readinfo;
                    
                    //将新的主图插入记录中
                    SocketClient.ConnectSevert(GET_VEGEIMAGEMAXNO);
                    String index=SocketClient.readinfo;
				    String strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
					(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
				   
				   SocketClient.ConnectSevert(UPDATE_MAINIMAGE+strinfo+UPDATE_MAINIMAGE+vegeId+UPDATE_MAINIMAGE+imagepth+UPDATE_MAINIMAGE+"1");
				   String info=SocketClient.readinfo;
                   //如果是已经存在的子图则只需要修改标志位
				   if(uig.mainindex<mainpathlength)
				   {
					 String path=childpath.get(uig.mainindex-1);
					 SocketClient.ConnectSevert(UPDATE_MAINBIGIMAGE+strinfo+UPDATE_MAINBIGIMAGE+vegeId+UPDATE_MAINBIGIMAGE+path+UPDATE_MAINBIGIMAGE+"2");
					 info=SocketClient.readinfo;
				   }
				}
				//添加子图
				  for(int i=0;i<imagelist.size();i++)
				  {
					if(i>=mainpathlength)//如果不是从库里获取的则要加载到库中并添加进数据库中
					{
						//加载新的主图存入服务器
		    			byte[] imagemsg=imagelist.get(i);
		    			SocketClient.ConnectSevertBYTE(UPLOAD_IMAGE,UPLOAD_ZIMAGE, imagemsg);
	                    String readinfo=SocketClient.readinfo;
	                  //将新的主图插入记录中
	                    SocketClient.ConnectSevert(GET_VEGEIMAGEMAXNO);
	                    String index=SocketClient.readinfo;
					    String strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
						(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
					    SocketClient.ConnectSevert(INSERT_IMAGE+strinfo+INSERT_IMAGE+vegeId+INSERT_IMAGE+readinfo+INSERT_IMAGE+"0");
					     String info=SocketClient.readinfo;	
					    //如果不是主图的大图则直接插，如果是主图的大图则将原有主图的大图标志置0
					    if(i==uig.mainindex)
					    {
					    //如果是后添加的主图的大图则将原来主图的大图标志位改为0
					    SocketClient.ConnectSevert(UPDATE_MAINBIGIMAGE+strinfo+UPDATE_MAINBIGIMAGE+vegeId+UPDATE_MAINBIGIMAGE+readinfo+UPDATE_MAINBIGIMAGE+"2");
					    info=SocketClient.readinfo;	
					    }
					   
					 }
				  }
				  
				  JOptionPane.showMessageDialog(UpdateVegeImage.this, "添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
					SocketClient.ConnectSevert(GET_USEVEGE);//获取菜品信息
			    	String getinfo=SocketClient.readinfo;
			    	Vector title=new Vector();//标题
					{
						title.add("菜品ID");
						title.add("菜品名称");
						title.add("价格");
						title.add("计量单位");
						title.add("菜品类别");
						title.add("菜品主类");
						title.add("系别");
						title.add("规格");
						title.add("介绍");
					}
					if(getinfo.equals("fail"))
					{
                  	    JOptionPane.showMessageDialog(UpdateVegeImage.this, "获取信息失败","提示",JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{   //重新建表
						Vector<Vector<String>> data=TypeExchangeUtil.strToVector(getinfo);
						int topwidth=(int) (768*0.07);
						int midwidth=(int) ((1024-1024*0.25)*0.78);
						int buttomheight=(int) (768*0.6);
						mainui.createJTable(data,title,20,topwidth,midwidth,buttomheight);
						mainui.createRight(curenode);
					}
			    	UpdateVegeImage.this.setVisible(false);//关闭更新菜品图片界面
				}
           }
         );
        this.add(jp);//将JPanel添加到界面中
		this.setResizable(false);//界面设置为固定尺寸
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setBounds(200, 0, 560, 700);//设置界面位置与大小
		this.setVisible(true);//设置界面为可见
		this.setTitle("菜品编辑");//设置界面标题
    }
}
