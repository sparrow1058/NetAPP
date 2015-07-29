package com.bn.worker;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import static com.bn.constant.Constant.*;  //无需通过类名引用Constant类中的常量和方法
/*
 * 编辑修改员工信息并保存
 * */
@SuppressWarnings("serial")
public class EditWorkerInfo extends JFrame
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
	public EditWorkerInfo(MainUI mainUI,Vector<String> title)
	{
		this.mainUI = mainUI;
		this.title = title;
		Image image=this.getToolkit().getImage("src/com/bn/image/tb1.jpg");
		this.setIconImage(image);
	}
	public void editWorker(final String[] val)
	{
		final DefaultMutableTreeNode curenode=Constant.authority[0];//当前节点为员工管理
		this.setTitle("编辑员工信息");//设置界面标题
		this.setBounds(300,40,width,height);//设置界面位置与大小
		jp.setLayout(null);//设置JPanel布局器为默认
		this.add(jp);//将JPanel添加到界面
		this.setVisible(true);//设置界面为可见
		
		//设置Label字体的大小
		for(JLabel jlabel : jl)
		{
			jlabel.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));
		}
		//对于外键值得获取，作为下拉列表框的选项
		String gender[] = {"男","女"};  //0-男   1-女
		List<String[][]> FKList = WorkerInfoTransform.getFKName();
		final String roleArr[][] = FKList.get(0);
		String roleName[] = new String[roleArr.length];
		for(int i=0; i<roleArr.length; i++)
		{
			roleName[i] = roleArr[i][1]; //将角色名称赋给roleName数组
		}
		//员工id
		jl[0].setBounds(left,topSpan,jlWidth,jlHeight);
		jp.add(jl[0]);
		final JLabel jlid = new JLabel(); //为了在提交按钮监听中使用jlid变量，因此给它定义为final型
		jlid.setText(val[0]);
		jlid.setBounds(tLeft,topSpan,jlWidth,jlHeight);
		jp.add(jlid);
		//姓名，登录密码
		jl[1].setBounds(left,topSpan+rowSpan*1,jlWidth,jlHeight);
		jp.add(jl[1]);
		final JTextField tfName = new JTextField();
		tfName.setText(val[1]);
		tfName.setBounds(tLeft,topSpan+rowSpan*1,lwidth,tHeight);
		jp.add(tfName);
		JLabel jHint1 = new JLabel("*");
		jHint1.setBounds(tLeft+lwidth+10,topSpan+rowSpan*1,35,35);
		jp.add(jHint1);
		
		jl[2].setBounds(left,topSpan+rowSpan*2,jlWidth,jlHeight);
		jp.add(jl[2]);
		final JTextField tfPassword = new JTextField();
		tfPassword.setText(val[2]);
		tfPassword.setBounds(tLeft,topSpan+rowSpan*2,lwidth,tHeight);
		jp.add(tfPassword);
		JLabel jHint2 = new JLabel("*");
		jHint2.setBounds(tLeft+lwidth+10,topSpan+rowSpan*2,35,35);
		jp.add(jHint2);
		//性别
		jl[3].setBounds(left,topSpan+rowSpan*3,jlWidth,jlHeight);
		jp.add(jl[3]);
		final JComboBox jSex=new JComboBox (gender);
		jSex.setSelectedItem((val[3].equals("男"))?gender[0]:gender[1]);
		jSex.setBounds(tLeft,topSpan+rowSpan*3,lwidth,tHeight);
		jSex.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));
		jp.add(jSex);
		jSex.addKeyListener(new KeyListener()
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
		jl[4].setBounds(left,topSpan+rowSpan*4,jlWidth,jlHeight);
		jp.add(jl[4]);
		final JComboBox jRole=new JComboBox (roleName);
		jRole.setSelectedItem(val[4]);
		jRole.setBounds(tLeft,topSpan+rowSpan*4,tWidth,tHeight);
		jRole.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));
		jp.add(jRole);
		jRole.addKeyListener(new KeyListener()
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
		
		JLabel jHint3 = new JLabel("*");
		jHint3.setBounds(tLeft+jlWidth+colSpan+jlWidth+tWidth+20,topSpan+rowSpan*10,35,35);
		jp.add(jHint3);
		
		JButton submit = new JButton("提交");//定义提交按钮
		submit.setBounds(tLeft,topSpan+rowSpan*7,jlWidth,jlHeight);//设置提交按钮位置与大小
		submit.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		JButton close = new JButton("返回");//定义返回按钮
		close.setBounds(tLeft+jlWidth+colSpan+jlWidth+10,topSpan+rowSpan*7,jlWidth,jlHeight);//设置位置与大小
		close.setFont(new Font("宋体",Font.TRUETYPE_FONT,contentFont));//设置字体
		jp.add(submit);//将提交按钮添加到JPanel中
		jp.add(close);//将取消按钮添加到JPanel中
		
		submit.addActionListener//提交按钮添加监听
		( 
		  new ActionListener()
			{
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String value[] = new String[20];
				value[0] = jlid.getText();
				value[1] = tfName.getText();
				value[2] = tfPassword.getText();
				value[3] = jSex.getSelectedItem().toString().equals("男")?"0":"1";
				value[4] = roleArr[jRole.getSelectedIndex()][0]; //根据被选的项的索引值找到选项对应的id
				
				if(value[1].equals("") || value[2].equals("") || value[3].equals("") || value[4].equals(""))
				{
					JOptionPane.showMessageDialog
           	        (
           	    		EditWorkerInfo.this,
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
	           	    	EditWorkerInfo.this,
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
	           	    	EditWorkerInfo.this,
	       	    		"密码请用数字表示",
	       	    		"提示",
	       	    		JOptionPane.INFORMATION_MESSAGE
	           	    );
				}
				else
				{
					StringBuffer submitContent = new StringBuffer();//定义字符串
					for(int i=0; i<value.length; i++)
					{
						submitContent.append(UPDATE_WORKERINFO+value[i]);//将员工信息添加到字符串中
					}
					SocketClient.ConnectSevert(submitContent.toString());//将员工信息传给服务器
					String readinfo=SocketClient.readinfo;
					if(readinfo.equals("ok"))
					{   //重新建表
						JOptionPane.showMessageDialog(EditWorkerInfo.this, "员工信息修改成功","提示",JOptionPane.INFORMATION_MESSAGE);
						SocketClient.ConnectSevert(GET_WORKERINFO);
						String getinfo=SocketClient.readinfo;
						Vector<Vector<String>> data = WorkerInfoTransform.Transform(getinfo);
						mainUI.createJTable(data,title,20,250,midwidth,400);
						mainUI.createRight(curenode);
						EditWorkerInfo.this.setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(EditWorkerInfo.this, "员工信息修改失败","提示",JOptionPane.INFORMATION_MESSAGE);
						EditWorkerInfo.this.setVisible(false);
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
				EditWorkerInfo.this.setVisible(false);
			}
		}
	);
}		
}