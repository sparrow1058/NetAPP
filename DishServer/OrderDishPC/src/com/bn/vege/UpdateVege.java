package com.bn.vege;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static com.bn.util.TypeExchangeUtil.*;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;

import static com.bn.constant.Constant.*;
/*修改菜品的界面*/
@SuppressWarnings("serial")
public class UpdateVege extends JFrame
{
	StringBuilder yysb=new StringBuilder();//定义两个字符串
	StringBuilder clsb=new StringBuilder();
	JLabel jtitle=new JLabel("编辑菜品");//定义标题
	Font font=new Font("宋体",Font.BOLD,22);//设置字体
	DefaultMutableTreeNode curenode=Constant.vegenode[5];//当前节点为菜品管理
	int width;//设置宽度
	int height;//设置高度
	JPanel jp=new JPanel();//定义JPanel
	 byte[] imagebyte;
	 MainUI mainui;
	@SuppressWarnings({ "rawtypes", "unused" })
	public UpdateVege(final MainUI mainui,final String[] s)//更新菜品方法
	{
		Image image=this.getToolkit().getImage("src/com/bn/img/kt.jpg");//定义图片
		this.setIconImage(image);//设置界面图标
		this.mainui=mainui;
		width=560;//设置宽度
		height=710;//设置高度
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
		 int twidth=width/4;
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
        //得到计量单位的名称
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
		 
		 jp.setLayout(null);
		   //第一行控件
		   JLabel jlid=new JLabel();//定义菜品id JLabel
		   jlid.setText("菜 品 id：");//设置JLabel的值
		   jlid.setBounds(leftwidth, topheight, lwidth, lheight);//设置位置与大小
		   jp.add(jlid);//将JLabel添加到JPanel中
		   JLabel jlidshow=new JLabel();//定义“菜品id”JLabel
		   jlidshow.setText(s[0]);//设置JLabel的值
		   jlidshow.setBounds(tleftwidth, topheight, twidth, lheight);//设置JLabel位置与大小
		   jp.add(jlidshow);//将JLabel添加到JPanel中
           //第二行控件
		   JLabel jlname=new JLabel();//定义名称JLabel
		   jlname.setText("菜品名称：");//设置JLabel的值
		   jlname.setBounds(leftwidth, topheight*2+lheight, lwidth, lheight);//设置位置与大小
		   jp.add(jlname);//将JLabel添加到JPanel中
		   final JTextField jetname=new JTextField();//定义“菜品名称”JTextField
		   jetname.setText(s[1]);//设置JTextField的值
		   jetname.setBounds(tleftwidth, topheight*2+lheight, twidth+leftwidth, lheight);//设置位置与大小
		   jp.add(jetname);//将JTextField添加到JPanel中
		   
		   JLabel jlcg=new JLabel();//定义类别JLabel
		   jlcg.setText("所属类别：");//设置JLabel的值
		   jlcg.setBounds(rightwidth, topheight*2+lheight, lwidth, lheight);//设置位置与大小
		   jp.add(jlcg);//将JLabel添加到JPanel中
		   final JComboBox jcbcg=new JComboBox(cgstr);//定义“所属类别”JComboBox
		   jcbcg.setSelectedItem(s[4]);//设置JComboBox的选项
		   jcbcg.setBounds(trightwidth, topheight*2+lheight, twidth, lheight);//设置位置与大小
		   jp.add(jcbcg);//将JCombobox添加到JPanel中
		   
		 //第三行控件
		   JLabel jlprice=new JLabel();//定义价格JLabel
		   jlprice.setText("菜品价格：");//设置JLabel的值
		   jlprice.setBounds(leftwidth, topheight*3+lheight*2, lwidth, lheight);//设置位置与大小
		   jp.add(jlprice);//将JLabel添加到JPanel中
		   final JTextField jetprice=new JTextField();//定义“菜品价格”JTextField
		   jetprice.setText(s[2]);//设置JTextField的值
		   jetprice.setBounds(tleftwidth, topheight*3+lheight*2, twidth+leftwidth, lheight);//设置位置与大小
		   jp.add(jetprice);//将JTextField添加到JPanel中
		   
		    JLabel jlcu=new JLabel();//定义计量单位JLabel
		   jlcu.setText("计量单位：");//设置JLabel的值
		   jlcu.setBounds(rightwidth, topheight*3+lheight*2, lwidth, lheight);//设置位置与大小
		   jp.add(jlcu);//将JLabel添加到JPanel中
		   final JComboBox jcbcu=new JComboBox(custr);//定义“计量单位”JComboBox
		   jcbcu.setSelectedItem(s[3]);//设置JComboBox的选项
		   jcbcu.setBounds(trightwidth, topheight*3+lheight*2, twidth, lheight);//设置位置与大小
		   jp.add(jcbcu);//将JComboBox添加到JPanel中
		   
		 //第四行控件
		   JLabel jlgg=new JLabel();//定义JLabel
		   jlgg.setText("规    格：");//设置JLabel的值
		   jlgg.setBounds(leftwidth, topheight*4+lheight*3, lwidth, lheight);//设置位置与大小
		   jp.add(jlgg);//将JLabel添加到JPanel中
		   final JComboBox jcbgg=new JComboBox(ggstr);//定义“规格”JComboBox
		   jcbgg.setSelectedItem(s[7]);//设置JComboBox的值
		   jcbgg.setBounds(tleftwidth, topheight*4+lheight*3, twidth, lheight);//设置位置与大小
		   jp.add(jcbgg);//将JComboBox添加到JPanel中
		   
		   JLabel jlvt=new JLabel();//定义JLabel
		   jlvt.setText("所属系别：");//设置JLabel的值
		   jlvt.setBounds(rightwidth, topheight*4+lheight*3, lwidth, lheight);//设置位置与大小
		   jp.add(jlvt);//将JLabel添加到JPanel中
		   final JComboBox jcbvt=new JComboBox(vtstr);//定义“所属系别”JComboBox
		   jcbvt.setSelectedItem(s[6]);//设置JComboBox的值
		   jcbvt.setBounds(trightwidth, topheight*4+lheight*3, twidth, lheight);//设置位置与大小
		   jp.add(jcbvt);//将JComboBox添加到JPanel中
		     
		   //第五行控件
	       JLabel jlintro=new JLabel();//定义介绍JLabel
	  	   jlintro.setText("菜品介绍：");//设置标题
	  	   jlintro.setBounds(leftwidth, topheight*7+lheight*6, lwidth, lheight);//设置位置与大小
	  	   jp.add(jlintro);//将JLabe添加到JPanel中
	  	   final JTextArea jtajs=new JTextArea();//定义“菜品介绍”JTextArea
	  	   JScrollPane jspintro=new JScrollPane(jtajs);//定义滚动条，并添加到JTextArea中
		   jtajs.setLineWrap(true);//设置JTextArea为自动换行
		   jtajs.setText(s[8]);//设置JTextArea的值
	  	   jspintro.setBounds(tleftwidth,topheight*7+lheight*6, twidth*3, 3*lheight);//设置位置与大小
	  	   jp.add(jspintro);//将滚动条添加到JPanel中

	       JButton jbok=new JButton("下一步");//定义下一步按钮
	       jbok.setBounds(6*leftwidth+2*lwidth, topheight*10+lheight*11, lwidth+leftwidth, lheight);
	       jp.add(jbok);//将按钮添加到JPanel中
		    jbok.addActionListener//按钮添加监听
		    (
		       new ActionListener()
		       {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Pattern pattern = Pattern.compile("[0-9]+.?[0-9]*");//正则式表达 
						if(jetname.getText().equals(""))
						{
							JOptionPane.showMessageDialog(UpdateVege.this, "菜品名称不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else if(jetprice.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(UpdateVege.this, "菜品价格不能为空，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else if(!pattern.matcher(jetprice.getText().toString()).matches())
						{
							JOptionPane.showMessageDialog(UpdateVege.this, "菜品价格格式输入不正确，请输入","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
						String vegename=jetname.getText().toString();
						String vegejg=jetprice.getText();
						String vegecu=jcbcu.getSelectedItem().toString();
						String vegegg=jcbgg.getSelectedItem().toString();
						String vegecg=jcbcg.getSelectedItem().toString();
						String vegevt=jcbvt.getSelectedItem().toString();
						String vegeinfo=jtajs.getText().toString();

						//更新菜品
						String sendmsg=UPDATE_VEGE+s[0]
						+UPDATE_VEGE+vegename						
						+UPDATE_VEGE+vegejg						
						+UPDATE_VEGE+vegecu
						+UPDATE_VEGE+vegecg
						+UPDATE_VEGE+vegevt
						+UPDATE_VEGE+vegegg
						+UPDATE_VEGE+vegeinfo;
						SocketClient.ConnectSevert(sendmsg);//将菜品信息传给服务器
						String readinfo=SocketClient.readinfo;
					    if(readinfo.equals("ok"))
					    {
					    	//修改成功后进入下一界面，对图片进行修改
					    	UpdateVegeImage uvi=new UpdateVegeImage(mainui,s[0]);
					    	uvi.setVisible(true);
					    	UpdateVege.this.setVisible(false);//更新菜品界面不可见
					    }else
					    {
					    	JOptionPane.showMessageDialog(UpdateVege.this, "菜品修改失败","提示",JOptionPane.INFORMATION_MESSAGE);
					    	UpdateVege.this.setVisible(false);
					    }
					}
				}  
		    }
		  );
		       
       JButton jbback=new JButton("取消");//定义“取消”按钮
       jbback.setBounds(8*leftwidth+3*lwidth,topheight*10+lheight*11, lwidth+leftwidth, lheight);
       jp.add(jbback);//将按钮添加到JPanel中
       jbback.addActionListener//按钮添加监听
       (
    	  new ActionListener()
    	  {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				UpdateVege.this.setVisible(false);//更新菜品界面关闭
			}
    	  }
       );
        this.add(jp);//将JPanel添加到界面中
		this.setResizable(false);//将界面设置为固定尺寸
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setBounds(200, 0, 560, 450);//设置位置与大小
		this.setVisible(true);//界面设置为可见
		this.setTitle("菜品编辑");//设置界面标题		
	}
}
