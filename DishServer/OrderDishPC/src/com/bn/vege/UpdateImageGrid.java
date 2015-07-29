package com.bn.vege;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPanel;

/*VegeImageGrid自定义控件，在修改菜品时作为修改图片的用到的控件
 */
@SuppressWarnings("serial")
public class UpdateImageGrid extends JPanel implements ActionListener
{
	//列数
    int colNum=3;
    //单位宽度
    static int unitWidth;
    //图片高度
    static int unitHeight;
    //间距
    static int span;
    //九宫格的宽度
    static int totalwidth;
    //九宫格的高度
    static int totalheight;
    //图片信息
    static List<byte[]> imageList;
    static byte[] imagemsg;
    static int mainindex;
    int imagemsglength;
	@SuppressWarnings("static-access")
	public UpdateImageGrid(int vigwidth, int vigheight, int span2, List<byte[]> imagelist2, int mainindex2) 
	{
		this.totalwidth=vigwidth;
		this.totalheight=vigheight;
		this.span=span2;
		this.imageList=imagelist2;
		this.mainindex=mainindex2;
		//计算图片的宽高
		unitWidth=(totalwidth-4*span)/3;
		unitHeight=(totalheight-4*span)/3;
		initdrawImageGrid();
	}
	
	private void initdrawImageGrid()
	{
		this.setLayout(null);//设置界面布局器为默认
		for(int i=0;i<imageList.size();i++)
		{
			JButton jb=new JButton();//定义按钮
			jb.setIcon(new ImageIcon(imageList.get(i)));//设置按钮图标
			jb.addActionListener(this);//按钮添加监听
			jb.setLayout(null);//按钮布局器为默认
			if(i==mainindex)
			{   //设置按钮位置与大小
				jb.setBounds(span+(i%colNum)*(unitWidth+span)+unitWidth/10/2, span+(i/colNum)*(unitHeight+span)+unitHeight/10/2, unitWidth-unitWidth/10, unitHeight-unitHeight/10);
				JLabel jl=new JLabel();//定义JLabel
				jl.setIcon(new ImageIcon("src/com/bn/image/flag.png"));//定义JLabel的图标
				jl.setBounds(0, 0, unitWidth/4, unitHeight/6);//设置JLabel的位置与大小
				jb.add(jl);
			}else{
				jb.setBounds(span+(i%colNum)*(unitWidth+span), span+(i/colNum)*(unitHeight+span), unitWidth, unitHeight);
			}
			this.add(jb);//将按钮添加到界面中
		}
		int length=span+(imageList.size()/colNum)*(unitHeight+span);//定义长度
		if(imageList.size()%colNum!=0)
		{
		     length=span+(imageList.size()/colNum+1)*(unitHeight+span);
			 this.setPreferredSize(new Dimension(totalwidth, length));
		}
		else
		{
			 this.setPreferredSize(new Dimension(totalwidth, length));
		}
		this.setVisible(true);
	}
	public  void drawImageGrid()
	{
		this.setLayout(null);//定义界面布局器为默认
		JButton jb=new JButton();//定义按钮
		jb.addActionListener(this);//按钮添加监听
		jb.setIcon(new ImageIcon(imagemsg));//设置按钮图标
		jb.setBounds(span+((imageList.size()-1)%colNum)*(unitWidth+span), span+((imageList.size()-1)/colNum)*(unitHeight+span), unitWidth, unitHeight);
		this.add(jb);
		int length=span+(imageList.size()/colNum)*(unitHeight+span);
		if(imageList.size()%colNum!=0)
		{
		     length=span+(imageList.size()/colNum+1)*(unitHeight+span);
			 this.setPreferredSize(new Dimension(totalwidth, length));//设置界面的首选大小
		}
		else
		{
			 this.setPreferredSize(new Dimension(totalwidth, length));
		}
		this.setVisible(true);
	}
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JButton jb=(JButton) e.getSource();
		int x=jb.getX();
		int y=jb.getY();
		int row=y/(span+unitHeight);
		int col=x/(span+unitWidth);
		int index=row*this.colNum+col;
		this.removeAll();
		this.mainindex=index;
		this.initdrawImageGrid();
		this.updateUI();
	}
}
