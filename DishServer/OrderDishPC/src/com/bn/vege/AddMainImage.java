package com.bn.vege;

import static com.bn.constant.Constant.DEL_VEGE;
import static com.bn.constant.Constant.GET_VEGEIMAGEMAXNO;
import static com.bn.constant.Constant.INSERT_IMAGE;
import static com.bn.constant.Constant.UPLOAD_IMAGE;
import static com.bn.constant.Constant.UPLOAD_MIMAGE;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.bn.constant.Constant;
import com.bn.pcinfo.MainUI;
import com.bn.util.SocketClient;
import com.bn.util.TypeExchangeUtil;
/*在添加菜品的时候添加菜品主图的界面*/
@SuppressWarnings("serial")
public class AddMainImage extends JFrame
{
	MainUI mainui;
	String vegeId;//菜品ID
	int width=350;//设置宽度
	int height=400;//设置高度
	int leftSpan;//左留白
	int topSpan=20;//上留白
	int imageWidth=280;//图片宽度
	int imageHeight=210;//图片高度
	int jAddHeight=30;//按钮、提示框及输入框高度
	int jTextWidth=200;//输入框宽度
	int buttonWidth=68;//按钮宽度
	JButton buttonNext;//“下一步”按钮
	JButton buttonCancel;//“取消”按钮
	JButton buttonAdd;//“浏览”按钮
	JTextField jText;//路径编辑框
	JLabel jImage;
	JPanel jPanel=new JPanel();
	byte[] imageByte;
	JLabel jTitle=new JLabel("添加菜品主图");//标题
	Font font=new Font("宋体",Font.BOLD,22);//设置字体
	Font buttonFont=new Font("宋体",Font.TRUETYPE_FONT,12);
	public AddMainImage(final MainUI mainui, final String vegeId)
	{
		try
		{//windows风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    	this.mainui=mainui;
		this.vegeId=vegeId;
		
		leftSpan=(width-imageWidth)/2;
		jPanel.setLayout(null);
		this.setBounds(200, 200, width, height);
		//设置图片显示区域
		jImage=new JLabel("");
		jImage.setBounds
		(
			leftSpan,
			topSpan,
			imageWidth,
			imageHeight
		);
		jImage.setBorder(BorderFactory.createLoweredBevelBorder());//设置边框
		jPanel.add(jImage);
		//设置添加图片区域
		//提示信息设置
		JLabel jAdd=new JLabel("请选择要添加的主图图片:");
		jAdd.setBounds
		(
			5,
			topSpan+imageHeight,
			200, 
			jAddHeight
		);
		jPanel.add(jAdd);
		//输入框设置
		jText=new JTextField();
		jText.setBounds
		(
			leftSpan,
			topSpan+imageHeight+jAddHeight,
			jTextWidth,
			jAddHeight
		);
		jPanel.add(jText);
		//“浏览”按钮设置
		buttonAdd=new JButton();
		buttonAdd.setText("浏  览");
		buttonAdd.setFont(buttonFont);//设置按钮字体
		buttonAdd.setBounds
		(
			leftSpan+jTextWidth+15,
			topSpan+imageHeight+jAddHeight,
			buttonWidth,
			jAddHeight
		);
		//为“浏览”按钮添加监听
		buttonAdd.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					JFileChooser jfc=new JFileChooser("c:\\");
					jfc.showOpenDialog(buttonAdd);//弹出文件选择框
					jfc.setCurrentDirectory(null);
					File file=jfc.getSelectedFile();
					String path="";
					try
					{
						 path=file.getAbsolutePath().toString();//返回文件路径
						 jText.setText(path);
						 if(!(file.exists()&&file.isFile()&&file.getName().endsWith(".jpg")))  
						 {  
							 JOptionPane.showMessageDialog
							 (
								 AddMainImage.this,
								 "选择文件错误，请重新选择一个正确的文件。","提示",
								 JOptionPane.INFORMATION_MESSAGE
							 );
						 } 
						 else
						 {   //创建图片的缩放版本
							 Image image = getToolkit().getImage(path);  
							 image = image.getScaledInstance(imageWidth,imageHeight,Image.SCALE_DEFAULT);  
							 jImage.setIcon(new ImageIcon(image));
							 imageByte=TypeExchangeUtil.filetobyte(file);  
						 }
					}
					catch(Exception e)
					{
						System.out.println("Exception: the path is null");
					}
				}
			}
		);
		jPanel.add(buttonAdd);//将按钮添加到JPanel中
		//“下一步”按钮设置
		buttonNext=new JButton();
		buttonNext.setText("下一步");
		buttonNext.setFont(buttonFont);//设置按钮字体
		buttonNext.setBounds//设置按钮位置与大小
		(
			2*leftSpan,
			2*topSpan+imageHeight+jAddHeight+jAddHeight+5,
			buttonWidth,
			jAddHeight
		);
		//“下一步”按钮注册监听
		buttonNext.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					if(imageByte==null)
					{
					    SocketClient.ConnectSevert(GET_VEGEIMAGEMAXNO);
	                	String i=SocketClient.readinfo;
						String indexx=((Integer.parseInt(i)+1)<10)?("00"+(Integer.parseInt(i)+1)):
							(((Integer.parseInt(i)+1)<100)?("0"+(Integer.parseInt(i)+1)):((Integer.parseInt(i)+1)+""));
						//添加主图的大图
						SocketClient.ConnectSevert(INSERT_IMAGE+indexx+INSERT_IMAGE+vegeId+INSERT_IMAGE+Constant.vegenullBigimage+INSERT_IMAGE+"2");
		    		    String info=SocketClient.readinfo;
		    			String mid=((Integer.parseInt(indexx)+1)<10)?("00"+(Integer.parseInt(indexx)+1)):
							(((Integer.parseInt(indexx)+1)<100)?("0"+(Integer.parseInt(indexx)+1)):((Integer.parseInt(indexx)+1)+""));
		    			//添加主图的小图
		    			SocketClient.ConnectSevert(INSERT_IMAGE+mid+INSERT_IMAGE+vegeId+INSERT_IMAGE+Constant.vegenullimage+INSERT_IMAGE+"1");
		    			info=SocketClient.readinfo;
		    			if(info.equals("fail"))
				    	{
                        	JOptionPane.showMessageDialog
                        	(
                    			AddMainImage.this, 
                    			"图片添加失败",
                    			"提示",
                    			JOptionPane.INFORMATION_MESSAGE
                        	);
						}
				    	else
				    	{
							//添加主图成功进行下一步
							AddChildImage aci=new AddChildImage(mainui,vegeId);
							aci.setVisible(true);
							AddMainImage.this.setVisible(false);//添加主图界面设置为不可见
						}
					}
					else
					{
		    			SocketClient.ConnectSevertBYTE(UPLOAD_IMAGE, UPLOAD_MIMAGE, imageByte);
						String readinfo=SocketClient.readinfo;
		    			if(!readinfo.equals(null))
		    			{
		    				String[] path=readinfo.split("#");
		    				SocketClient.ConnectSevert(GET_VEGEIMAGEMAXNO);
							String index=SocketClient.readinfo;
							String strinfo=((Integer.parseInt(index)+1)<10)?("00"+(Integer.parseInt(index)+1)):
								(((Integer.parseInt(index)+1)<100)?("0"+(Integer.parseInt(index)+1)):((Integer.parseInt(index)+1)+""));
		    				//添加主图的大图
			    			SocketClient.ConnectSevert(INSERT_IMAGE+strinfo+
								    					INSERT_IMAGE+vegeId+
								    					INSERT_IMAGE+path[0]+
								    					INSERT_IMAGE+"2");
			    			String info=SocketClient.readinfo;
			    			String mid=((Integer.parseInt(strinfo)+1)<10)?("00"+(Integer.parseInt(strinfo)+1)):
								(((Integer.parseInt(strinfo)+1)<100)?("0"+(Integer.parseInt(strinfo)+1)):((Integer.parseInt(strinfo)+1)+""));
			    			//添加主图的小图
			    			SocketClient.ConnectSevert(INSERT_IMAGE+mid+
								    					INSERT_IMAGE+vegeId+
								    					INSERT_IMAGE+path[1]+
								    					INSERT_IMAGE+"1");
			    			info=SocketClient.readinfo;
					    	if(info.equals("fail"))
					    	{
	                        	JOptionPane.showMessageDialog
	                        	(
                        			AddMainImage.this, 
                        			"图片添加失败",
                        			"提示",
                        			JOptionPane.INFORMATION_MESSAGE
	                        	);
							}
					    	else
					    	{
								//添加主图成功进行下一步
								AddChildImage aci=new AddChildImage(mainui,vegeId);
								aci.setVisible(true);
								AddMainImage.this.setVisible(false);
							}
		    			}
		    			else
		    			{
		    				JOptionPane.showMessageDialog
		    				(
	    						AddMainImage.this,
	    						"图片上传失败",
	    						"提示",
	    						JOptionPane.INFORMATION_MESSAGE
		    				);
					    	AddMainImage.this.setVisible(false);
		    			}
			    	}
			    }
			}
		);
		jPanel.add(buttonNext);
		//“取消”按钮设置
		buttonCancel=new JButton();
		buttonCancel.setText("取  消");
		buttonCancel.setFont(buttonFont);//设置取消按钮字体
		buttonCancel.setBounds//设置取消按钮位置与大小
		(
			width-2*leftSpan-buttonWidth, 
			2*topSpan+imageHeight+jAddHeight+jAddHeight+5,
			buttonWidth,
			jAddHeight
		);
		buttonCancel.addActionListener//取消按钮添加监听
		(
		  new ActionListener()	
		  {
			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//按下取消按钮则将上一步添加的菜品从库中删除
			 SocketClient.ConnectSevert(DEL_VEGE+vegeId);
	    	 String backinfo=SocketClient.readinfo;
	    	 AddMainImage.this.setVisible(false);
			}
		  }
		);
		jPanel.add(buttonCancel);//将取消按钮添加到JPanel中
		this.add(jPanel);//将JPanel添加到界面中
		this.setVisible(true);//设置界面为可见
		this.setEnabled(true);//设置界面可用
		this.setTitle("添加菜品主图");//设置界面标题
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭方式
		this.setResizable(false);//设置界面尺寸固定
		Image image=this.getToolkit().getImage("src/com/bn/img/kt.jpg");//定义图片
		this.setIconImage(image);//设置界面图标
	}
}
