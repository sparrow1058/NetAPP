package com.bn.worker;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import static com.bn.constant.Constant.*;
@SuppressWarnings("serial")
/*
 * 添加员工信息
 * */
public class AddWorkerInfo extends JFrame 
{
	MainUI mainUI;
	Vector<String> title=null;
	int width=500;//定义宽度
	int height=450;//定义高度
	int jlWidth=60;
	int jlHeight=35;
	int lwidth=100;
	int tWidth=120;
	int tHeight=35;
	int left=15;             //左边距
	int topSpan=10;          //第一行距离顶部距离
	int rowSpan=jlHeight+12; //行间距
	int colSpan=130;  //列间距
	int tLeft=left+jlWidth+10;
	int midwidth=(int) ((1024-1024*0.25)*0.78);
	int contentFont=12;
	
	JPanel jp=new JPanel();//定义JPanel
	byte[] imagebyte;
	//定义JLabel数组
	JLabel jl[] = 
	{
			new JLabel("员工id：")
			,new JLabel("员工姓名：")
			,new JLabel("登录密码：")
			,new JLabel("性别：")
			,new JLabel("角色：")
	};
	public AddWorkerInfo(final MainUI mainUI,final Vector<String> title)//添加员工信息方法
	{
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");//定义图片
		this.setIconImage(image);//设置界面图标
		this.mainUI = mainUI;
		this.title = title;
		final DefaultMutableTreeNode curenode=Constant.authority[0];//当前节点为员工管理
		this.setTitle("添加员工信息");//设置标题
		this.setBounds(300,40,width,height);//设置位置与大小
		jp.setLayout(null);//设置JPanel布局器为默认
		this.add(jp);//将JPanel添加到界面中
		this.setVisible(true);//界面设置为可见		
		//设置Label字体的大小
		for(JLabel jlabel : jl)
		{
			jlabel.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));
		}
		//获取员工的最大id
		String nextId =""; 
	    SocketClient.ConnectSevert(GET_WORKERMAXNO);
		String maxId=SocketClient.readinfo;
		int nId = Integer.parseInt(maxId)+1;
		if(nId<10)
		{
			nextId = "00" + nId;
		}
		else if(nId<100)
		{
			nextId = "0" + nId;
		}
		else
		{
			nextId = ""+ nId;
		}
		//对于外键值得获取，作为下拉列表框的选项
		String gender[] = {"男","女"};  //0-男   1-女
		List<String[][]> FKList = WorkerInfoTransform.getFKName();
		final String roleArr[][] = FKList.get(0);
		String roleName[] = new String[roleArr.length];//获得角色名称
		for(int i=0; i<roleArr.length; i++)
		{
			roleName[i] = roleArr[i][1]; //将角色名称赋给roleName数组
		}
		//员工id
		jl[0].setBounds(left,topSpan,jlWidth,jlHeight);//设置员工id位置与大小
		jp.add(jl[0]);//将jl[0]添加到JPanel中
		final JLabel jlid = new JLabel(); //为了在提交按钮监听中使用jlid变量，因此给它定义为final型
		jlid.setText(nextId);//设置JLabel的值
		jlid.setBounds(tLeft,topSpan,jlWidth,jlHeight);//设置JLabel的位置与大小
		jp.add(jlid);//将JLabel添加到JPanel中		
		//姓名，登录密码
		jl[1].setBounds(left,topSpan+rowSpan,jlWidth,jlHeight);//设置姓名JLabel位置与大小
		jp.add(jl[1]);//将姓名JLabel添加到JPanel中
		final JTextField tfName = new JTextField();//定义姓名JTextField
		tfName.setText("");//设置姓名为空
		tfName.setBounds(tLeft,topSpan+rowSpan,lwidth,tHeight);//设置姓名JTextField位置与大小
		tfName.requestFocus();  //姓名对话框首先获得焦点，因为员工编号以为默认给出
		jp.add(tfName);//员工姓名添加到JPanel中
		JLabel jHint1 = new JLabel("*");//定义JLabel
		jHint1.setBounds(tLeft+lwidth+10,topSpan+rowSpan,35,35);//定义JLabel位置与大小
		jp.add(jHint1);//将JLabel添加到JPanel中
		
		jl[2].setBounds(left,topSpan+rowSpan*2,jlWidth,jlHeight);//设置密码JLabel位置与大小
		jp.add(jl[2]);//将密码JLabel添加到JPanel中
		final JTextField tfPassword = new JTextField();//定义密码JTextField
		tfPassword.setText("8888");//设置密码默认为8888
		tfPassword.setBounds(tLeft,topSpan+rowSpan*2,lwidth,tHeight);//设置密码JTextField位置与大小
		jp.add(tfPassword);//员工密码添加到JPanel中
		JLabel jHint2 = new JLabel("*");//定义JLabel
		jHint2.setBounds(tLeft+lwidth+10,topSpan+rowSpan*2,35,35);//定义JLabel位置与大小
		jp.add(jHint2);//将JLabel添加到JPanel中
		//性别
		jl[3].setBounds(left,topSpan+rowSpan*3,jlWidth,jlHeight);//定义性别JLabel位置与大小
		jp.add(jl[3]);//将JLabel添加到JPanel中
		final JComboBox jSex=new JComboBox (gender);//定义性别JComboBox
		jSex.setSelectedItem(gender[0]);//将选项添加到性别JComboBox中
		jSex.setBounds(tLeft,topSpan+rowSpan*3,lwidth,tHeight);//设置JComboBox位置与大小
		jSex.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		jp.add(jSex);//将性别JComboBox添加到JPanel中
		jSex.addKeyListener(new KeyListener()//JCombobox添加监听
		{
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode()==KeyEvent.VK_DOWN )
				{
					jSex.setPopupVisible(true);
					jSex.showPopup();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) { }
		});
		//角色id
		jl[4].setBounds(left,topSpan+rowSpan*4,jlWidth,jlHeight);//设置角色id JLabel的位置与大小
		jp.add(jl[4]);//将角色id JLabel添加到JPanel中
		final JComboBox jRole=new JComboBox (roleName);//定义角色id JCombobox
		jRole.setSelectedItem(roleName[0]);//将选项添加到JCombobox
		jRole.setBounds(tLeft,topSpan+rowSpan*4,tWidth,tHeight);//设置JComboBox位置与大小
		jRole.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		jp.add(jRole);//将角色id JComboBox添加到JPanel中
		jRole.addKeyListener(new KeyListener()//JComboBox添加监听
		{
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode()==KeyEvent.VK_DOWN )
				{
					jRole.setPopupVisible(true);
					jRole.showPopup();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) { }
		});
		JLabel jHint3 = new JLabel("*");//定义JLabel
		jHint3.setBounds(tLeft+jlWidth+colSpan+jlWidth+tWidth+20,topSpan+rowSpan*10,35,35);
		jp.add(jHint3);//添加到JPanel中
		
		JButton submit = new JButton("提交");//定义提交按钮
		submit.setBounds(tLeft,topSpan+rowSpan*7,jlWidth,jlHeight);//设置按钮位置与大小
		submit.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		JButton close = new JButton("返回");//设置返回按钮
		close.setBounds(tLeft+jlWidth+colSpan+jlWidth+10,topSpan+rowSpan*7,jlWidth,jlHeight);//设置位置与大小
		close.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		jp.add(submit);//将“提交”按钮添加到JPanel中
		jp.add(close);//将“取消”按钮添加到JPanel中
		submit.addActionListener//提交按钮添加监听
		( 
			new ActionListener()
			{
			  @SuppressWarnings("static-access")
			  @Override
			  public void actionPerformed(ActionEvent arg0)
			  {
				String value[] = new String[5];
				value[0] = jlid.getText();
				value[1] = tfName.getText();
				value[2] = tfPassword.getText();
				value[3] = jSex.getSelectedItem().toString().equals("男")?"0":"1";
				value[4] = roleArr[jRole.getSelectedIndex()][0]; //根据被选的项的索引值找到选项对应的id
				if(value[1].equals("") || value[2].equals("") || value[3].equals("") || value[4].equals(""))
				{
					JOptionPane.showMessageDialog
           	       (
           	    		   AddWorkerInfo.this,
	       	    		   "请将必要信息填写完整",
	       	    		   "提示",
	       	    		   JOptionPane.INFORMATION_MESSAGE
           	    	);
				}
				//姓名的输入格式的控制
				else if(!value[1].matches("[^0-9-_]*"))
				{
					JOptionPane.showMessageDialog
	           	    (
           	    		   AddWorkerInfo.this,
	       	    		   "姓名的输入格式有误",
	       	    		   "提示",
	       	    		   JOptionPane.INFORMATION_MESSAGE
	           	    );
				}
				//密码的输入格式的控制
				else if(!value[2].matches("\\d+"))
				{
					JOptionPane.showMessageDialog
	           	    (
           	    		   AddWorkerInfo.this,
	       	    		   "密码请用数字表示",
	       	    		   "提示",
	       	    		   JOptionPane.INFORMATION_MESSAGE
	           	    );
				}
				else
				{
					StringBuffer submitContent = new StringBuffer();//定义服务器
					for(int i=0; i<value.length; i++)
					{
						submitContent.append(ADD_WORKERINFO+value[i]);//将员工信息添加到字符串中
					}
					SocketClient.ConnectSevert(submitContent.toString());//将员工信息传给服务器
					String readinfo=SocketClient.readinfo;
					if(readinfo.equals("ok"))//如果返回“ok”
					{   //重新建表
						JOptionPane.showMessageDialog(AddWorkerInfo.this, "员工信息添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
						SocketClient.ConnectSevert(GET_WORKERINFO);
						String getinfo=SocketClient.readinfo;
						Vector<Vector<String>> data = WorkerInfoTransform.Transform(getinfo);
						mainUI.createJTable(data,title,20,250,midwidth,400);
						mainUI.createRight(curenode);
						AddWorkerInfo.this.setVisible(false);//添加员工信息界面关闭
					}
					else
					{
						JOptionPane.showMessageDialog(AddWorkerInfo.this, "员工信息添加失败","提示",JOptionPane.INFORMATION_MESSAGE);
						AddWorkerInfo.this.setVisible(false);
					}
				}
			}
		  }
		);
		close.addActionListener//取消按钮监听
		( 
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					AddWorkerInfo.this.setVisible(false);//界面关闭
				}
		   }
	   );
	}
}
