package com.bn.selectvege;

/*作为VegeImageGrid控件的监听接口，VegeImageGrid中每一道菜品的图片和数据作为一个单元格，
onItemClick()的参数是被点击的单元格的位置*/
public interface VegeImageGridListener 
{
   void onItemClick(int index,int sum);
}
