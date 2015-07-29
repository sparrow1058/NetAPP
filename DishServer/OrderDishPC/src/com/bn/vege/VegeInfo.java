package com.bn.vege;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.bn.constant.Constant;
import com.bn.util.SocketClient;

import static com.bn.constant.Constant.*;

@SuppressWarnings("serial")
public class VegeInfo extends JFrame{
	JLabel jtitle=new JLabel("菜品详情");//定义JLabel
	Font font=new Font("宋体",Font.BOLD,22);//定义字体
	DefaultMutableTreeNode curenode=Constant.vegenode[5];//当前节点为菜品管理
	int width;//定义宽度
	int height;//定义高度
	JPanel jp=new JPanel();//定义JPanel
	 byte[] imagebyte;
	@SuppressWarnings({ "unused" })
	public VegeInfo(final String[] s) {
		
		Image image=this.getToolkit().getImage("src/com/bn/img/kt.jpg");//定义图片
		this.setIconImage(image);//设置界面图标
		width=560;//定义宽度值
		height=710;//定义高度值
		 //左边距
		 int leftwidth=width/35;
		 //高的距离
		 int topheight=height/90;
		 //右边距
		 int rightwidth=width/2+leftwidth;
		 //jlabel的宽度
		 int lwidth=width/2/3;
		 //JLabel的高度
		 int lheight=height/26;
		 //text的宽度
		 int twidth=width/5;
		 //text的高度
		 int theight=height/26;
		 //得到左边text左边的边距
		 int tleftwidth=leftwidth+lwidth;
		 //得到右边text左边的边距
		 int trightwidth=rightwidth+lwidth;
         //jep的宽度
         int jepwidth=width/2-leftwidth*2/4;
         //jep的高度
         int jepheight=5*lheight+4*topheight; 
		                      
		   jp.setLayout(null);//设置JPanel布局器为默认
	       //第一行控件
	       JLabel jlid=new JLabel();//定义JLabel
	       jlid.setText("菜 品 id：");//设置JLabel的值
	       jlid.setBounds(leftwidth, topheight, lwidth, lheight);//设置位置与大小
		   jp.add(jlid);//将JLabel添加到JPanel中
		   JTextField jlidshow=new JTextField();//定义“菜品id”JTextField
		   jlidshow.setEditable(false);//设置为不可编辑
		   jlidshow.setText(s[0]);//设置JTextField的值
		   jlidshow.setBounds(leftwidth+lwidth, topheight, twidth, lheight);//设置位置与大小
		   jp.add(jlidshow);//将JTextField添加到JPanel中

		   //第二行控件
		   JLabel jlprice=new JLabel();//定义JLabel
		   jlprice.setText("菜品价格：");//设置JLabel的值
		   jlprice.setBounds(leftwidth, topheight*2+lheight, lwidth, lheight);//设置位置与大小
		   jp.add(jlprice);//将JLabel添加到Jpanel中
		   final JTextField jetprice=new JTextField();//定义“菜品价格”JTextField
		   jetprice.setText(s[2]);//设置JTextField的值
		   jetprice.setEditable(false);//设置为不可编辑
		   jetprice.setBounds(leftwidth+lwidth, topheight*2+lheight, twidth, lheight);//设置位置与大小
		   jp.add(jetprice);//将JTextField添加到JPanel中
		   
		   //第三行控件
		   JLabel jlcg=new JLabel();//定义JLabel
		   jlcg.setText("所属类别：");//设置JLabel的值
		   jlcg.setBounds(leftwidth, topheight*3+lheight*2, lwidth, lheight);//设置位置与大小
		   jp.add(jlcg);//将JLable添加到JPanel中
		   final JTextField jcbcg=new JTextField();//定义“所属类别”JTextField
		   jcbcg.setText(s[4]);//设置JTextField的值
		   jcbcg.setEditable(false);//设置为不可编辑
		   jcbcg.setBounds(leftwidth+lwidth, topheight*3+lheight*2, twidth, lheight);//设置位置与大小
		   jp.add(jcbcg); //将JTextField添加到JPanel中
		   		   
		   //第四行控件
		   JLabel jlcu=new JLabel();//定义JLabel
		   jlcu.setText("计量单位：");//设置JLabel的值
		   jlcu.setBounds(leftwidth, topheight*4+lheight*3, lwidth, lheight);//设置位置与大小
		   jp.add(jlcu);//将JLabel添加到JPanel中
		   final JTextField jcbcu=new JTextField();//定义“计量单位”JTextField
		   jcbcu.setText(s[3]);//设置JTextField的值
		   jcbcu.setEditable(false);//设置为不可编辑
		   jcbcu.setBounds(leftwidth+lwidth, topheight*4+lheight*3, twidth, lheight);//设置位置与大小
		   jp.add(jcbcu);//将  JTextField添加到JPanel中
		   
		   //第五行控件
		   JLabel jlvt=new JLabel();//定义JLabel
		   jlvt.setText("所属系别：");//设置JLabel的值
		   jlvt.setBounds(leftwidth, topheight*5+lheight*4, lwidth, lheight);//设置位置与大小
		   jp.add(jlvt);//将JLabel添加到JPanel中
		   final JTextField jcbvt=new JTextField();//定义“所属系别”JTextField
		   jcbvt.setText(s[6]);//设置JTextField的值
		   jcbvt.setEditable(false);//设置为不可编辑
		   jcbvt.setBounds(leftwidth+lwidth, topheight*5+lheight*4, twidth, lheight);//设置位置与大小
		   jp.add(jcbvt);//将JTextField添加到JPanel
		   
		   //第六行控件
		   JLabel jlgg=new JLabel();//定义JLabel
		   jlgg.setText("规    格：");//设置JLabel的值
		   jlgg.setBounds(leftwidth, topheight*6+lheight*5, lwidth, lheight);//设置位置与大小
		   jp.add(jlgg);//将JLabel添加到JPanel中
		   final JTextField jcbgg=new JTextField();//定义“规格”JTextField
		   jcbgg.setText(s[7]);//设置JTextField的值
		   jcbgg.setEditable(false);//设置为不可编辑
		   jcbgg.setBounds(leftwidth+lwidth, topheight*6+lheight*5, twidth, lheight);//设置位置与大小
		   jp.add(jcbgg);//将JTextField添加到JPanel
		   
           //第七行控件
		   JLabel jlintro=new JLabel();//定义JLabel
		   jlintro.setText("菜品介绍：");//定义JLabel的值
		   jlintro.setBounds(leftwidth, topheight*7+lheight*6, lwidth, lheight);//设置位置与大小
		   jp.add(jlintro);//将JLabel添加到JPanel中
		   final JTextArea jtajs=new JTextArea();//定义“菜品介绍”JTextArea
		   JScrollPane jspintro=new JScrollPane(jtajs);//定义滚动条，并添加到JTextArea中
		   jtajs.setLineWrap(true);//设置为自动换行
		   jtajs.setEditable(false);//设置为不可编辑
		   jtajs.setText(s[8]);//设置JTextField的值
		   jspintro.setBounds(tleftwidth,topheight*7+lheight*6, twidth*2, 6*lheight);//设置位置与大小
		   jp.add(jspintro);//将JTextField添加到JPanel中
		 		     
		  	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);//设置横向分隔条
	        jsp.setDividerSize(4);//设置分隔条尺寸
	        jsp.setDividerLocation(350);//设置分隔条位置
	        jsp.setLeftComponent(jp);//将JPanel设置到分隔条的左边
	          
	         List<byte[]> imagelist=new ArrayList<byte[]>();
	         //根据菜品id得到菜品主图的大图
	  		 SocketClient.ConnectSevert(GET_MBIMAGE+s[0]);
	  		 final String vegemainbigpath=SocketClient.readinfo;
	  		 SocketClient.ConnectSevert(GET_IMAGE+vegemainbigpath);
	  		 byte mainbigimagemsg[] =SocketClient.data;
	  		 imagelist.add(mainbigimagemsg);
	  		 
	  		 //根据菜品id得到菜品子图片路径
			 SocketClient.ConnectSevert(GET_ZBIMAGE+s[0]);
			 String vegechildpath=SocketClient.readinfo;
		     if(!vegechildpath.equals(""))
		     {
			   String[] path=vegechildpath.split(",");
		       for(int i=0;i<path.length;i++)
			    {
				   SocketClient.ConnectSevert(GET_IMAGE+path[i]);
				   byte imagemsg[] =SocketClient.data;
				   //如果是主图的大图则标志位1，否则标志位0 
				   imagelist.add(imagemsg);
			    }
		     }
		     
		     JScrollPane jspr=new JScrollPane();//定义滚动条
		     jspr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//设置纵向滚动条
		     jspr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//设置横向滚动条
		     jspr.setAutoscrolls(true);
		     JPanel jpr=new JPanel();//定义JPanel
		     jpr.setLayout(null);//设置JPanel布局器为默认
		     int span=5;
		     int unitWidth=350;
		     int unitHeight=300;
	
		     for(int i=0;i<imagelist.size();i++)
				{
					JButton jb=new JButton();//定义JButton
					jb.setIcon(new ImageIcon(imagelist.get(i)));//设置按钮图标
					jb.setLayout(null);//设置按钮布局器为默认
					if(i==0)
					{   //设置按钮位置与大小
						jb.setBounds(span, span+i*(unitHeight+span-unitHeight/9), unitWidth-unitWidth/9, unitHeight-unitHeight/9);
						JLabel jl=new JLabel();//定义JLabel
						jl.setIcon(new ImageIcon("src/com/bn/image/flag.png"));//设置JLabel图标
						jl.setBounds(0, 0, unitWidth/4, unitHeight/6);//设置位置与大小
						jb.add(jl);//JLabel添加到JButton中
					}
					else
					{
						jb.setBounds(span, span+i*(unitHeight+span-unitHeight/9), unitWidth-unitWidth/9, unitHeight-unitHeight/9);
					}
					jpr.add(jb);//JButton添加到JPanel中
				}
		        int length=span+imagelist.size()*(unitHeight+span-unitHeight/10);
		        jpr.setPreferredSize(new Dimension(400, length));
		        jspr.setViewportView(jpr);//将滚动条添加到JPanel中
		        jsp.setRightComponent(jspr);//将滚动条添加到分隔条右边
		        this.add(jsp);//将分隔条添加到界面中
				this.setResizable(false);//设置界面固定尺寸
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
				this.setBounds(200, 0, 700, 500);//设置界面位置与大小
				this.setVisible(true);//设置界面可见
				this.setTitle("菜品详情");//设置界面标题
	}
}