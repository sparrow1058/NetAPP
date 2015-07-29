package com.bn.vege;

import static com.bn.constant.Constant.GET_VEGEIMAGEMAXNO;
import static com.bn.constant.Constant.INSERT_IMAGE;
import static com.bn.constant.Constant.UPLOAD_IMAGE;
import static com.bn.constant.Constant.UPLOAD_ZIMAGE;

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
/*在添加菜品的时候添加菜品子图*/
@SuppressWarnings("serial")
public class AddChildImage extends JFrame
{
	MainUI mainui;
	DefaultMutableTreeNode curenode=Constant.vegenode[5];//菜品信息节点
    int width=560;//定义尺寸
    int height=700;
    Font font=new Font("宋体",Font.BOLD,14);//定义两种字体
    Font fonttitle=new Font("宋体",Font.BOLD,20);
    List<File> imagelist=new ArrayList<File>();
    String vegeId;
    static int length;
    public AddChildImage(final MainUI mainui, final String vegeId)//添加子图方法
    {
	   this.mainui=mainui;
	   this.vegeId=vegeId;
	   final JPanel jp=new JPanel();//定义JPanel
	   jp.setLayout(null);//设置JPanel布局器为默认
	   this.setBounds(200, 0, 560, 700);//设置JPanel的位置与大小
	    //左边距
		 int leftwidth=width/30;
		 //高的距离
		 int topheight=height/90;
		 //jlabel的宽度
		 int lwidth=width/2/3;
		 //JLabel的高度
		 int lheight=height/22;
		 //九宫格之间的距离
		 int span=width/100;
		 
		 int vigwidth=510;
		 int vigheight=540;
		 
		 
		 final VegeImageGrid vig =new VegeImageGrid(vigwidth,vigheight,span,imagelist);
	     JScrollPane jsp=new JScrollPane();//定义滚动条
	     jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //设置纵向滚动条
	     jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//设置横向滚动条
	     jsp.setAutoscrolls(true);
	     jsp.setViewportView(vig);//将滚动条添加到vig
	     jsp.setBounds(leftwidth, 2*topheight+2*lheight, vigwidth, vigheight);//设置滚动条的位置与大小
	     jp.add(jsp);//将滚动条添加到JPanel
		 
		 JLabel jltitle=new JLabel("添加菜品子图");//设置标题
		 jltitle.setFont(fonttitle);//设置标题字体
		 jltitle.setBounds(width/2-width/2/4, topheight, 2*lwidth, lheight);//设置标题位置与大小
		 jp.add(jltitle);//将标题添加到JPanel中
		 
		 final JButton jbselect=new JButton("选择图片");//设置按钮
         jbselect.setFont(font);//设置按钮字体
         jp.add(jbselect);//将按钮添加到JPanel中
         jbselect.setBounds(leftwidth, topheight+lheight, lwidth, lheight);//设置按钮位置与大小
         jbselect.addActionListener//按钮添加监听
         (
        	 new ActionListener()
        		 {
						@SuppressWarnings("unused")
						@Override
					public void actionPerformed(ActionEvent e) 
						{
							JFileChooser jfc=new JFileChooser("c:\\");
							jfc.showOpenDialog(jbselect);//弹出文件选择器
							jfc.setCurrentDirectory(null);//设置当前目录为空
							File file=jfc.getSelectedFile();//定义选中文件
							String path=file.getAbsolutePath().toString();//定义路径
							 if(!(file.exists() && file.isFile() && file.getName().endsWith(".jpg")))
							 {  
								 JOptionPane.showMessageDialog(AddChildImage.this, "选择文件错误，请重新选择一个正确的文件。","提示",JOptionPane.INFORMATION_MESSAGE);
						     } 
							 else
							 {   //文件添加到imagelist
								 imagelist.add(file);
								 VegeImageGrid.curfile=file;
								 VegeImageGrid.imageList=imagelist;
								 //重绘
								 vig.drawImageGrid();
								 jp.updateUI();
							 }
						  }	
					}		 
                 );
         //上传控件
         final JButton jbupload=new JButton("上传");//定义按钮
         jbupload.setFont(font);//设置按钮字体
         jp.add(jbupload);//将按钮添加到JPanel中
         jbupload.setBounds(width/2+width/4, topheight+lheight, lwidth, lheight);//设置按钮的位置与大小
         jbupload.addActionListener//按钮添加监听
         (
           new ActionListener()
            {
				@SuppressWarnings({ "unchecked", "static-access" })
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					int num=0;
					for(int i=0;i<imagelist.size();i++)
					{
						byte[] imagemsg=TypeExchangeUtil.filetobyte(imagelist.get(i));//得到图片
						SocketClient.ConnectSevertBYTE(UPLOAD_IMAGE ,UPLOAD_ZIMAGE, imagemsg);
						String readinfo=SocketClient.readinfo;
						if(!readinfo.equals(null))//如果为空
						{
							//得到最大编号
							SocketClient.ConnectSevert(GET_VEGEIMAGEMAXNO);
							String index=SocketClient.readinfo;
							String strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
								(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
				    		SocketClient.ConnectSevert(INSERT_IMAGE+strinfo+INSERT_IMAGE+vegeId+INSERT_IMAGE+readinfo+INSERT_IMAGE+"0");
				    		String info=SocketClient.readinfo;
					    	if(info.equals("fail"))//如果返回“fail”,出提示信息"菜品添加失败"
					    	{
	                        	JOptionPane.showMessageDialog(AddChildImage.this, "菜品添加失败","提示",JOptionPane.INFORMATION_MESSAGE);
							}
					    	else
					    	{
					    		num++;
					    	}
			    	    }
					}
				  if(num>=imagelist.size())
					  {
						JOptionPane.showMessageDialog(AddChildImage.this, "菜品添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
				    	SocketClient.ConnectSevert(Constant.GET_USEVEGE);
				    	String getinfo=SocketClient.readinfo;
						@SuppressWarnings("rawtypes")
						Vector title=new Vector();//标题
						{
							title.add("菜品ID");
							title.add("菜品名称");
							title.add("价格");
							title.add("介绍");
							title.add("计量单位");
							title.add("菜品类别");
							title.add("菜品主类");
							title.add("系别");
							title.add("规格");
						}
						if(getinfo.equals("fail"))
						{
	                    	JOptionPane.showMessageDialog(AddChildImage.this, "获取信息失败","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{   //将得到的数据、标题传给createJTable，建表
							Vector<Vector<String>> data=TypeExchangeUtil.strToVector(getinfo);
							int topwidth=(int) (768*0.07);
							int midwidth=(int) ((1024-1024*0.25)*0.78);
							int buttomheight=(int) (768*0.6);
							mainui.createJTable(data,title,20,topwidth,midwidth,buttomheight);
							mainui.createRight(curenode);
						}
						AddChildImage.this.setVisible(false);//添加子图界面设置为不可见
					}		
				  }
			}
         );
        this.add(jp);//将JPanel添加到界面中
		this.setResizable(false);//设置界面为固定尺寸
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setBounds(200, 0, 560, 700);//设置界面的位置与大小
		this.setVisible(true);//设置界面为可见
		this.setTitle("菜品编辑");//设置界面标题
    }
}
