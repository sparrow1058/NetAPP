package com.bn.vege;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static com.bn.util.TypeExchangeUtil.*;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
import javax.swing.JTextField;
//import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;

import static com.bn.constant.Constant.*;
/*添加菜品的界面*/
public class AddVegeUI implements ActionListener
{
    MainUI mui;
	String strinfo;
	public AddVegeUI(MainUI mui)//添加菜品方法
	{
		this.mui=mui;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		SocketClient.ConnectSevert(GET_VEGEMAXNO);//获得菜品信息
		String index=SocketClient.readinfo;
		System.out.println("index"+index);
		strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
			(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
        addVege addvege=new addVege(mui);//添加菜品界面
        addvege.setVisible(true);//设置为可见
	}
	@SuppressWarnings("serial")
	class addVege extends JFrame//添加菜品界面
	{
		   DefaultMutableTreeNode curenode=Constant.vegenode[5];//菜品管理节点
		   MainUI mainui;
		   int width;
		   int height;
		   Font font=new Font("宋体",Font.BOLD,14);//定义两种字体
		   Font fonttitle=new Font("宋体",Font.BOLD,20);
		   @SuppressWarnings("rawtypes")
		public addVege(final MainUI mainui)
		   {
			   this.mainui=mainui;
			   JPanel jp=new JPanel();//定义JPanel
			   jp.setLayout(null);//设置JPanel布局器为默认
				 this.setBounds(200, 0, 560, 500);//设置界面的位置与大小
				 width=560;
				 height=720;
				 //左边距
				 int leftwidth=width/25;
				 //高的距离
				 int topheight=height/90;
				 //右边距
				 int rightwidth=width/2+leftwidth;

				 //jlabel的宽度
				 int lwidth=width/2/3;
				 //JLabel的高度
				 int lheight=height/22;
				 //text的宽度
				 int twidth=width/2/2;
				 //text的高度
				 int theight=height/22;
				 //得到左边text左边的边距
				 int tleftwidth=leftwidth+lwidth;
				 //得到右边text左边的边距
				 int trightwidth=rightwidth+lwidth;
				 //得到计量单位
				 SocketClient.ConnectSevert(GET_CU);
				 String getcumsg=SocketClient.readinfo;
				 List<String[]> cudata=TypeExchangeUtil.strToList(getcumsg);
				 Vector custr=getnamebynoone(cudata);
				 //得到菜系的名称
				 SocketClient.ConnectSevert(GET_VT);
				 String getvt=SocketClient.readinfo;
				 List<String[]> vtdata=TypeExchangeUtil.strToList(getvt);
				 Vector vtstr=getnamebynoone(vtdata);
				 
				 //得到类别的名称
				 SocketClient.ConnectSevert(GET_CG);
				 String getcg=SocketClient.readinfo;
				 List<String[]> cgdata=TypeExchangeUtil.strToList(getcg);
				 Vector cgstr=getnamebynotwo(cgdata);
				 
				 //得到规格的名称
				 SocketClient.ConnectSevert(GET_VS);
				 String getgg=SocketClient.readinfo;
				 List<String[]> ggdata=TypeExchangeUtil.strToList(getgg);
				 Vector ggstr=getnamebynoone(ggdata);
				 
				 JLabel jltitle=new JLabel("添加菜品");//定义标题
				 jltitle.setFont(fonttitle);//设置标题字体
				 jltitle.setBounds(width/3, topheight, 2*lwidth, lheight);//设置标题位置与大小
				 jp.add(jltitle);//将标题添加到JPanel中
				 
				//添加第一行控件
				//添加编号控件
				 JLabel jlid=new JLabel("菜 品 ID：");//定义编号JLabel
				 jlid.setFont(font);//设置字体
				 jlid.setBounds(leftwidth, 2*topheight+lheight, lwidth, lheight);//设置位置与大小
				 jp.add(jlid);//将编号JLabel添加到JPanel中
				 JLabel jlidshow=new JLabel(strinfo);//定义“菜品id”JLabel
				 jlidshow.setFont(font);//设置字体
				 jlidshow.setBounds(tleftwidth, 2*topheight+lheight, twidth, lheight);//设置位置与大小
				 jp.add(jlidshow);//将“菜品id”JLabel添加到JPanel中
			   //添加第二行控件
		       //添加主类名称名称
		         final JLabel jlmcgname=new JLabel("菜品名称：");//定义名称JLabel
		         jlmcgname.setFont(font);//设置字体
		         jlmcgname.setBounds(leftwidth,3*topheight+2*lheight,lwidth,lheight);//设置位置与大小
				 jp.add(jlmcgname);//将JLabel添加到JPanel中
		         final JTextField jetcgname=new JTextField();//定义“菜品名称”JTextField
		         jetcgname.setBounds(tleftwidth, 3*topheight+2*lheight, twidth, theight);//设置位置与大小
		         jp.add(jetcgname);//将“菜品名称”JTextField添加到JPanel中
		         
		       //标准价格
		         JLabel jlprice=new JLabel("标准价格：");//定义价格JLabel
		         jlprice.setFont(font);//设置字体
		         jlprice.setBounds(rightwidth, 3*topheight+2*lheight,lwidth,lheight);//设置位置与大小
				 jp.add(jlprice);//将JLabel添加到JPanel中
				 final JTextField jetprice=new JTextField();//定义“标准价格”JTextField
				 jetprice.setBounds(trightwidth, 3*topheight+2*lheight, twidth, theight);//设置位置与大小
		         jp.add(jetprice);//将“标准价格”JTextField添加到JPanel中
		         
		       //添加第三行控件
		       //添加计量单位控件
				 final JLabel jlcu=new JLabel("计量单位：");//定义计量单位JLabel
				 jlcu.setFont(font);//设置字体
				 jlcu.setBounds(leftwidth,  4*topheight+lheight*3, lwidth, lheight);//设置位置与大小
		         jp.add(jlcu);//将JLabel添加到JPanel中
		         final JComboBox jcbcu=new JComboBox(custr);//定义“计量单位”JComboBox
		         jcbcu.setBounds(tleftwidth, 4*topheight+lheight*3, twidth, theight);//设置位置与大小
		         jp.add(jcbcu);//将JComboBox添加到JPanel中
		         
		       //添加类别控件
				 JLabel jlcg=new JLabel("所属类别：");//定义类别JLabel
				 jlcg.setFont(font);//设置字体
				 jlcg.setBounds(rightwidth, 4*topheight+lheight*3, lwidth, lheight);//设置位置与大小
				 jp.add(jlcg);//将JLabel添加到JPanel中
				 final JComboBox jcbcg=new JComboBox(cgstr);//定义“所属类别”JComboBox
				 jcbcg.setBounds(trightwidth,4*topheight+lheight*3, twidth, theight);//设置位置与大小
				 jp.add(jcbcg);//将JComboBox添加到JPanel中
		         
		         //添加第四行控件
				//添加菜系控件
				 JLabel jlvt=new JLabel("菜    系：");//定义菜系JLabel
				 jlvt.setFont(font);//设置字体
				 jlvt.setBounds(leftwidth, 5*topheight+lheight*4, lwidth, lheight);//设置位置与大小
				 jp.add(jlvt);//将JLabel添加到JPanel中
				 final JComboBox jcbvt=new JComboBox(vtstr);//定义“菜系”JComboBox
				 jcbvt.setBounds(tleftwidth,5*topheight+lheight*4, twidth, theight);//设置位置与大小
				 jp.add(jcbvt);//将JComboBox添加到JPanel中
				 
				//添加规格
				 final JLabel jlgg=new JLabel("规    格：");//定义规格JLabel
				 jlgg.setFont(font);//设置字体
				 jlgg.setBounds(rightwidth,  5*topheight+lheight*4, lwidth, lheight);//设置位置与大小
		         jp.add(jlgg);//将JLabel添加到JPanel中
		         final JComboBox jcbgg=new JComboBox(ggstr);//定义“规格”JComboBox
		         jcbgg.setBounds(trightwidth,  5*topheight+lheight*4, twidth, theight);//设置位置与大小
		         jp.add(jcbgg);//将JComboBox添加到JPanel中
		         
				//添加第五行控件
				 //添加介绍控件
				 JLabel jljs=new JLabel("介    绍：");//定义介绍JLabel
				 jljs.setFont(font);//设置字体
				 jljs.setBounds(leftwidth, 6*topheight+lheight*6+lheight/4, lwidth, 2*lheight);//设置位置与大小
				 jp.add(jljs);//将JLabel添加到JPanel中
				 final JTextArea jtajs=new JTextArea();//定义“介绍”JTextArea
				 jtajs.setLineWrap(true);//设置为自动换行
				 JScrollPane jspjs=new JScrollPane(jtajs);//定义滚动条并添加到JTextArea中
				 jspjs.setBounds(tleftwidth, 6*topheight+lheight*6+lheight/4, trightwidth, theight+lheight*2);//设置位置与大小
				 jp.add(jspjs);//将JTextArea添加到JPanel中
				 
				//添加第六行控件
		         //添加按钮控件
		         JButton jbok=new JButton("下一步");//定义下一步JButton
		         jbok.setFont(font);//设置字体
		         jp.add(jbok);//将JButton添加到JPanel中
		         jbok.setBounds(width/2-width/2/4,  8*topheight+lheight*8+lheight*2+lheight/4, lwidth, theight);//设置位置与大小
		         jbok.addActionListener//按钮添加监听
		         (
		           new ActionListener()
		           {
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						Pattern pattern = Pattern.compile("[0-9]+.?[0-9]*");  
						if(jetcgname.getText().equals(""))
						{
							JOptionPane.showMessageDialog(addVege.this, "菜品名称不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else if(jetprice.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(addVege.this, "菜品价格不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else if(!pattern.matcher(jetprice.getText().toString()).matches())
						{
							JOptionPane.showMessageDialog(addVege.this, "菜品价格格式输入不正确，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}						
						else
						{
							String vegename=jetcgname.getText().toString();
							String vegejg=jetprice.getText();
							String vegecu=jcbcu.getSelectedItem().toString();
							String vegegg=jcbgg.getSelectedItem().toString();
							String vegecg=jcbcg.getSelectedItem().toString();
							String vegevt=jcbvt.getSelectedItem().toString();
							String vegeinfo=jtajs.getText().toString();
							//添加菜品
							String sendmsg=ADD_VEGE+strinfo+ADD_VEGE+vegename+ADD_VEGE+vegejg			
							+ADD_VEGE+vegecu+ADD_VEGE+vegegg+ADD_VEGE+vegecg+ADD_VEGE+vegevt
							+ADD_VEGE+vegeinfo;
				 
							SocketClient.ConnectSevert(sendmsg);//将菜品信息传给服务器
							String readinfo=SocketClient.readinfo;
							
							if(readinfo.equals("ok"))//如果返回“ok”
							{
								//如果菜品添加成功则进行下一步
						        AddMainImage ami=new AddMainImage(mainui,strinfo);
						        ami.setVisible(true);//跳到添加主图界面
						        addVege.this.setVisible(false);//添加菜品界面设为不可见
						    }
							else
						    {
						    	JOptionPane.showMessageDialog(addVege.this, "菜品添加失败","提示",JOptionPane.INFORMATION_MESSAGE);
						    	addVege.this.setVisible(false);
						    }
						}
					  }
					}	
		         );
		         JButton jbfali=new JButton("取消");
		         jbfali.setFont(font);//设置“取消”按钮字体
		         jp.add(jbfali);//将“取消”按钮添加到JPanel中
		         jbfali.setBounds(width/2+width/2/4, 8*topheight+lheight*8+lheight*2+lheight/4, lwidth, theight);
		         jbfali.addActionListener//“取消”按钮添加监听
		         (
		            new ActionListener()
		            {
						@Override
						public void actionPerformed(ActionEvent e)
						{
							addVege.this.setVisible(false);
						}
		            }
		         );
		         
		         this.add(jp);//将JPanel添加到界面中
		         this.setVisible(true);//设置界面为可见
		         this.setEnabled(true);//设置界面为可用
		         
		         this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置界面关闭方式
		 		 this.setResizable(false);//设置界面尺寸固定
		 		 this.setTitle("添加菜品");//设置标题
		 		 Image image=this.getToolkit().getImage("src/com/bn/img/tb1.jpg");//定义图片
				 this.setIconImage(image);//设置界面图标
		   }
	}
}
