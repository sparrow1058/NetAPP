package com.bn.vege;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import static com.bn.util.TypeExchangeUtil.*;
import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;

import static com.bn.constant.Constant.*;
import static com.bn.pcinfo.TableListener.title;
/*添加子类别的界面*/
public class CateGoryUI implements ActionListener
{
     String strinfo;
     MainUI mui;
     public CateGoryUI(MainUI mui)
     {
    	 this.mui=mui;
     }
	@Override
	public void actionPerformed(ActionEvent e)
	{
		SocketClient.ConnectSevert(GET_CGMAXNO);//获得子类最大id
		String index=SocketClient.readinfo;
		System.out.println("index"+index);
		strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
			(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
        addCateGory addcg=new addCateGory(mui);
        addcg.setVisible(true);//添加子类界面，并设置为可见
	}
   @SuppressWarnings("serial")
class addCateGory extends JFrame
{
	   DefaultMutableTreeNode curenode=Constant.vegenode[1];//当前节点是菜品类别节点
	   MainUI mainui;
	   int width;//设置宽度
	   int height;//设置高度
	   Font font=new Font("宋体",Font.BOLD,14);//设置两种字体
	   Font fonttitle=new Font("宋体",Font.BOLD,20);
		 @SuppressWarnings("rawtypes")
	public addCateGory(final MainUI mainui)
		 {
			 this.mainui=mainui;
			 JPanel jp=new JPanel();//定义JPanel
			  jp.setLayout(null);//设置JPanel布局器为默认
			 this.setBounds(200, 100, 520, 230);//设置界面的位置与大小
			 width=540;//设置宽度
			 height=300;//设置高度
			 //左边距
			 int leftwidth=width/20;
			 //高的距离
			 int topheight=height/60;
			 //jlabel的宽度
			 int lwidth=width/2/3;
			 //JLabel的高度
			 int lheight=height/10;
			 //text的宽度
			 int twidth=width/2/2;
			 //text的高度
			 int theight=height/12;
			 //得到左边text左边的边距
			 int tleftwidth=leftwidth+lwidth;
			//得到主类的名称
			 SocketClient.ConnectSevert(GET_MCG);
			 String getmcgname=SocketClient.readinfo;
			 System.out.println(getmcgname);
			 List<String[]> mcgdata=TypeExchangeUtil.strToList(getmcgname);
			 Vector mcgstr=getnamebynotwo(mcgdata);
			 
			 JLabel jltitle=new JLabel("添加菜品类别");//定义标题
			 jltitle.setFont(fonttitle);//设置字体
			 jltitle.setBounds(width/3, topheight, 2*lwidth, lheight);//设置位置与大小
			 jp.add(jltitle);//将标题添加到JPanel中
				 
		//添加第一行控件
			//添加编号控件
			 JLabel jlid=new JLabel("类别ID：");//定义类别id JLabel
			 jlid.setFont(font);//设置字体
			 jlid.setBounds(leftwidth, 2*topheight+lheight, lwidth, lheight);//设置位置与大小
			 jp.add(jlid);//将JLabel添加到JPanel中
			 JLabel jlidshow=new JLabel(strinfo);//定义“类别ID”JLabel
			 jlidshow.setFont(font);//设置字体
			 jlidshow.setBounds(tleftwidth, 2*topheight+lheight, twidth, lheight);//设置位置与大小
			 jp.add(jlidshow);//将JLabel添加到JPanel中
		
	       //添加类别名称
	         final JLabel jlcgname=new JLabel("类别名称：");//定义名称JLabel
	         jlcgname.setFont(font);//设置字体
	         jlcgname.setBounds(leftwidth,3*topheight+2*lheight,lwidth,lheight);//设置位置与大小
			 jp.add(jlcgname);//将JLabel添加到JPanel中
	         final JTextField jetcgname=new JTextField();//定义“类别名称”JTextField
	         jetcgname.setBounds(tleftwidth, 3*topheight+2*lheight, twidth, theight);//设置位置与大小
	         jp.add(jetcgname);//将JTextField添加到JPanel中
         
	         ////所属主类
	         JLabel jlmcg=new JLabel("所属主类：");//定义主类JLabel
	         jlmcg.setFont(font);//设置字体
	         jlmcg.setBounds(leftwidth, 4*topheight+3*lheight,lwidth,lheight);//设置位置与大小
			 jp.add(jlmcg);//将JLabel添加到JPanel中
			 final JComboBox jcbmcg=new JComboBox(mcgstr);//定义“所属主类”JComboBox
			 jcbmcg.setBounds(tleftwidth, 4*topheight+3*lheight, twidth, theight);//设置位置与大小
	         jp.add(jcbmcg);//将JComboBox添加到JPanel中
         
	         //添加第四行控件
	         //添加按钮控件
	         JButton jbok=new JButton("添加");//定义添加按钮
	         jbok.setFont(font);//设置字体
	         jp.add(jbok);//将添加按钮添加到JPanel中
	         jbok.setBounds(width/2/4,  6*topheight+lheight*4, lwidth, theight);//设置位置与大小
	         jbok.addActionListener//添加按钮添加监听
	         (
	            new ActionListener()
	            {
					@SuppressWarnings({ "unchecked", "static-access" })
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(jetcgname.getText().equals(""))
						{
							JOptionPane.showMessageDialog(addCateGory.this, "菜品名称不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else 
						{
							String cgname=jetcgname.getText().toString();
							String cgmcg=jcbmcg.getSelectedItem().toString();
							String time=TypeExchangeUtil.gettime();
							//添加菜品类别
							String msg=ADD_CG+strinfo+ADD_CG+cgname+
							ADD_CG+time+ADD_CG+time+ADD_CG+cgmcg;
							SocketClient.ConnectSevert(msg);//将菜品类别信息传给服务器
							String readinfo=SocketClient.readinfo;
							
							if(readinfo.equals("ok"))
							{
								JOptionPane.showMessageDialog(addCateGory.this, "菜品类别添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
								SocketClient.ConnectSevert(GET_CG);//获得菜品类别信息
								String getinfo=SocketClient.readinfo;
								title=new Vector();//标题
								{
									title.add("类别ID");title.add("类别编号");title.add("名称");
									title.add("关键字");title.add("创建时间");title.add("最后编辑时间");
		                            title.add("所属主类");
								}
								if(getinfo.equals("fail"))
								{
		                        	JOptionPane.showMessageDialog(addCateGory.this, "获取信息失败","提示",JOptionPane.INFORMATION_MESSAGE);
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
								addCateGory.this.setVisible(false);//添加类别界面不可见
							}
							else
							{
								JOptionPane.showMessageDialog(addCateGory.this, "菜品类别添加失败","提示",JOptionPane.INFORMATION_MESSAGE);
								addCateGory.this.setVisible(false);
							}
					  }
				}
            }
         );
         //取消按钮控件
         JButton jbfali=new JButton("取消");
         jbfali.setFont(font);//设置字体
         jp.add(jbfali);//添加到Jpanel中
         jbfali.setBounds(width/2+width/2/4, 6*topheight+lheight*4, lwidth, theight);//设置位置与大小
         //取消按钮监听
         jbfali.addActionListener
         (
           new ActionListener()
           {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				jetcgname.setText("");
			}
           }
         );
         this.add(jp);//将JPanel添加到界面中
         this.setVisible(true);//设置界面为可见
         this.setEnabled(true);//设置界面为可用        
         this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
 		 this.setResizable(false);//设置界面尺寸固定
 		 this.setTitle("添加菜品类别");//设置界面标题
 		 Image image=this.getToolkit().getImage("src/com/bn/img/tb1.jpg");//定义图片
		 this.setIconImage(image);//设置界面图标
					          
		}
   }
}
