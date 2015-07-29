package com.bn.serverinfo;//声明包语句

import java.awt.image.BufferedImage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.bn.serverinfo.TypeExchangeUtil.*;
import static com.bn.serverinfo.Constant.*;
public class ServerAgentThread extends Thread{//创建一个叫ServerAgentThread的继承线程的类
		private Socket sc;         //声明Socket
		private DataInputStream in;//声明数据输入流
		private DataOutputStream out;//声明数据输出流
		private static final String ok="ok";//定义字符串ok
		private static final String fail="fail";//定义字符串fail
		InputStream ips;//声明输入流
		int count;//声明计数
		public ServerAgentThread(Socket sc){//定义构造器		
			try{			
				this.sc=sc;//接收Socket
				in=new DataInputStream(sc.getInputStream());//创建新数据输入流
				out=new DataOutputStream(sc.getOutputStream());	//创建新数据输出流
			}
			catch(Exception e){			
				e.printStackTrace();//捕获异常
			}
		}
		public void run(){//重写run方法
			try{
				String readinfo=IOUtil.readStr(in);//将从流中读取的数据放入字符串中
				System.out.println("readinfo==="+readinfo);//打印从流中读取的数据
		//================Pc端请求标识头 begin====================

		//得到计量单位的信息
		if(readinfo.equals(GET_CU)){				
					List<String[]> list=DBUtil.getCU();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到计量单位的最大编号
				else if(readinfo.equals(GET_CUMAXNO)){				
					String maxno=DBUtil.getCUMaxNO();
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}
				//更新计量单位
				else if(readinfo.startsWith(UPDATE_CU)){				
					String[] getmsg=readinfo.split(UPDATE_CU);
					String strinfo=DBUtil.updateCU(getmsg);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//删除计量单位信息
				else if(readinfo.startsWith(DEL_CU)){				
					String[] getmsg=readinfo.split(DEL_CU);
					String strinfo=DBUtil.delCU(getmsg[1]);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//得到菜系信息
				else if(readinfo.equals(GET_VT)){				
					List<String[]> list=DBUtil.getVegeType();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到菜系的最大编号
				else if(readinfo.equals( GET_VTMAXNO)){				
					String maxno=DBUtil.getVTMaxNo();
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}
				//更新菜系信息
				else if(readinfo.startsWith(UPDATE_VT)){
					String[] getmsg=readinfo.split(UPDATE_VT);
					String strinfo=DBUtil.updateVT(getmsg);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//删除菜系
				else  if(readinfo.startsWith(DEL_VT)){
					String[] getmsg=readinfo.split(DEL_VT);
					String strinfo=DBUtil.delVT(getmsg[1]);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//获得主类别的信息
				else if(readinfo.equals(GET_MCG)){				
					List<String[]> list=DBUtil.getMCG();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到主类别的最大编号
				else if(readinfo.equals(GET_MCGMAXNO)){
					String maxno=DBUtil.getMCGMaxNo();
					System.out.println(maxno);
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}//更新主类的信息
				else if(readinfo.startsWith(UPDATE_CMG)){				
					String[] getmsg=readinfo.split(UPDATE_CMG);
					//根据部门的名称得到部门的id
					String strinfo=DBUtil.updateMCG(getmsg[1],getmsg[2]);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
                // 添加主类信息
				else if(readinfo.startsWith(ADD_CMG)){				
					String[] getmsg=readinfo.split(ADD_CMG);
					//根据部门的名称得到部门的id
					String strinfo=DBUtil.addMCG(getmsg[1],getmsg[2]);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//删除主类信息
				else if(readinfo.startsWith(DEL_CMG)){				
					String[] getmsg=readinfo.split(DEL_CMG);
					String strinfo=DBUtil.delMCG(getmsg[1]);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}//得到菜品类别的信息
				else if(readinfo.equals(GET_CG)){				
					List<String[]> list=DBUtil.getCG();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out,MyConverter.escape(strinfo));
				}//得到菜品类别的最大编号
				else if(readinfo.equals(GET_CGMAXNO)){				
					String maxno=DBUtil.getCGMaxNo();
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}
				//添加菜品类别
				else if(readinfo.startsWith(ADD_CG)){				
					String[] getmsg=readinfo.split(ADD_CG);
					//根据主类的名称得到主类的id
					String mcgid=DBUtil.getMCGidByname(getmsg[5]);
					String strinfo=DBUtil.addCG(getmsg,mcgid);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//更新菜品类别信息
				else if(readinfo.startsWith(UPDATE_CG)){				
					String[] getmsg=readinfo.split(UPDATE_CG);
					//根据主类的名称得到主类的id
					String mcgid=DBUtil.getMCGidByname(getmsg[5]);
					String time=gettime();
					String strinfo=DBUtil.updateCG(getmsg,time,mcgid);//调用更新菜品类别信息的方法
					if(strinfo==null){					
						IOUtil.writeStr(out, MyConverter.escape(fail));
					}else{					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}//删除菜品类别
				else if(readinfo.startsWith(DEL_CG)){				
					String[] getmsg=readinfo.split(DEL_CG);
					String strinfo=DBUtil.delCG(getmsg[1]);//调用删除菜品类别的方法
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//得到餐厅信息
				else if(readinfo.equals(GET_ROOM)){				
					List<String[]> list=DBUtil.getRoom();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到规格的信息
				else if(readinfo.equals(GET_VS)){				
					List<String[]> list=DBUtil.getVS();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到规格的最大编号
				else if(readinfo.equals(GET_VSMAXNO)){				
					String maxno=DBUtil.getVSMaxNO();
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}
				//更新规格信息
				else if(readinfo.startsWith(UPDATE_VS)){			
					String[] getmsg=readinfo.split(UPDATE_VS);
					String strinfo=DBUtil.updateVS(getmsg);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//删除规格信息
				else if(readinfo.startsWith(DEL_VS)){				
					String[] getmsg=readinfo.split(DEL_VS);
					String strinfo=DBUtil.delVS(getmsg[1]);//调用删除规格信息的方法
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}//根据子类被的名称得到主类别的名称
				else if(readinfo.startsWith(GETMCGNAMEBYCGNAME)){				
					String[] getmsg=readinfo.split(GETMCGNAMEBYCGNAME);
					String strinfo=DBUtil.getMcgnameByCgname(getmsg[1]);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
					//将图片加载到服务器
				else if(readinfo.startsWith(UPLOAD_IMAGE)){				
                       //将String转换成byte数组
					   String mz=in.readUTF();
					   byte[] imagemsg=IOUtil.readBytes(in);
			            File dir = new File(IMAGE_PATH);//通过将给定路径path转换为抽象路径名来创建一个新 File 实例
			            //创建文件夹
			            if(!dir.exists()){//若此文件夹不存在，那么就新建一个文件夹			            
			            	dir.mkdir();
			            }
			           //创建文件
			            File file = new File(dir,count+".jpg");//根据父类文件夹的抽象路径名和图片的路径名字符串创建一个新 File 实例
			            while(file.exists()) {//当文件存在时			           
			               file = new File(dir,(++count)+".jpg"); //循环创造 新文件实例
			            }
			            String filename=count+".jpg";//创建文件名称
					   if(mz.equals(UPLOAD_MIMAGE)){//更新主图
						   BufferedImage image=PicScaleUtil.scaledImg(imagemsg, width, height);//缩放图片
						   PicScaleUtil.saveImage(image,IMAGE_PATH+filename);//保存图片
				           BufferedImage imagem=PicScaleUtil.scaledImgByWid(image, xwidth);//按宽度等比例缩放图片
				           String[] s=filename.split(".jpg");//将图片名称信息以".jpg"为界分割
				           String mname=String.valueOf((Integer.parseInt(s[0])+1));//定义主图名称字符串（主图名称为子图名称的数字+1）
				           PicScaleUtil.saveImage(imagem, IMAGE_PATH+mname+".jpg");//保存图片
				           IOUtil.writeStr(out,  MyConverter.escape(IMAGE_PATH+filename)+"#"+MyConverter.escape(IMAGE_PATH+mname+".jpg")); //将所有图信息写入流  
					   }else if(mz.equals(UPLOAD_ZIMAGE)){//更新子图
						   BufferedImage image=PicScaleUtil.scaledImg(imagemsg, width, height);//缩放图片
						   PicScaleUtil.saveImage(image,IMAGE_PATH+filename);//保存图片
				           IOUtil.writeStr(out, MyConverter.escape(IMAGE_PATH+filename));//将子图信息写入到流
					   }else if(mz.equals(UPLOAD_MAIN_VEGEIAMGE)) {//上传菜品主图					  
						   BufferedImage image=PicScaleUtil.scaledImg(imagemsg, width, height);//缩放图片
						   BufferedImage imagem=PicScaleUtil.scaledImgByWid(image, xwidth);//按宽度等比例缩放图片
				           PicScaleUtil.saveImage(imagem, IMAGE_PATH+filename);//保存图片
				           IOUtil.writeStr(out, MyConverter.escape(IMAGE_PATH+filename));//写入到流
					   }
				}//得到菜品信息
				else if(readinfo.equals(GET_VEGE)){				
					List<String[]> vegelist=DBUtil.getVege();
					String strinfo=TypeExchangeUtil.listToString(vegelist);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到菜品的最大编号
				else if(readinfo.equals(GET_VEGEMAXNO)){				
					String maxno=DBUtil.getVegeMaxNO();
					IOUtil.writeStr(out,  MyConverter.escape(maxno));
				}
				//添加菜品
				else if(readinfo.startsWith(ADD_VEGE)){
					String[] getmsg=readinfo.split(ADD_VEGE);
					//根据计量单位的名称得到计量单位的id
					String cuid=DBUtil.getCUidByname(getmsg[4]);
					//	//根据规格的名称得到规格的id
					String ggid=DBUtil.getVSidByname(getmsg[5]);
					//根据类别的名称得到类别的id
					String cgid=DBUtil.getCGidByname(getmsg[6]);
					//根据系别的名称得到系别的id
					String vtid=DBUtil.getVTidByname(getmsg[7]);
					//添加菜品信息
					String strinfo=DBUtil.addVege(getmsg,cuid,cgid,vtid,ggid);
					if(strinfo.equals("ok")){											
						IOUtil.writeStr(out, MyConverter.escape(ok));
						}else{
							IOUtil.writeStr(out,  MyConverter.escape(fail));
						}
				}
			      //删除菜品信息
			       else if(readinfo.startsWith(DEL_VEGE)){				
					String[] getmsg=readinfo.split(DEL_VEGE);
					String strinfo=DBUtil.delVege(getmsg[1]);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				else if(readinfo.startsWith(GET_IMAGE)){				
					String[] str=readinfo.split(GET_IMAGE);
					System.out.println("str[1]===="+str[1]);
					byte[] imagemsg=PicScaleUtil.getPic(str[1]);
					IOUtil.writeBytes(out, imagemsg);
				}//更新菜品信息
				else if(readinfo.startsWith(UPDATE_VEGE)){				
					String[] getmsg=readinfo.split(UPDATE_VEGE);
					//根据计量单位的名称得到计量单位的id
					String cuid=DBUtil.getCUidByname(getmsg[4]);
					//根据规格的名称得到规格的id
					String ggid=DBUtil.getVSidByname(getmsg[7]);
					//根据类别的名称得到类别的id
					String cgid=DBUtil.getCGidByname(getmsg[5]);
					//根据系别的名称得到系别的id
					String vtid=DBUtil.getVTidByname(getmsg[6]);
					//更新菜品信息
					String strinfo=DBUtil.updateVege(getmsg,cuid,cgid,vtid,ggid);
					if(strinfo.equals("ok")){					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}else{					
						IOUtil.writeStr(out, MyConverter.escape(fail));
					}
				}//根据菜品id得到菜品的信息
				else if(readinfo.startsWith(GS_VEGE)){				
					String[] getmsg=readinfo.split(GS_VEGE);//将根据id得到菜品信息以GS_VEGE为界分割
					String[] msgvege=getmsg[1].split("#");
					List<String[]> vegelist=DBUtil.getVegeByid(msgvege);
					List<String[]> list=new ArrayList<String[]>();//创建新的存放字符串数组类型的列表
					for(String[] s:vegelist){//遍历菜品信息列表					
						String[] msg=new String[s.length];//
						System.out.println(s.length);
						for(int i=0;i<s.length;i++){//遍历字符串数组					
							msg[i]=s[i];//将分裂后的菜品信息存入新的字符串数组中
						}
						list.add(msg);//将新字符串数组加入列表
					}
					String strinfo=TypeExchangeUtil.listToString(list);//数据类型转换
					IOUtil.writeStr(out,MyConverter.escape(strinfo));//写入流
				}//得到所有餐厅信息
				else if(readinfo.startsWith(GET_ROOM)){				
					List<String[]> list=DBUtil.getRoom();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//更新餐厅信息
				else if(readinfo.startsWith(UPDATE_ROOM)){				
					String str[]=readinfo.split(UPDATE_ROOM);
					String strinfo=DBUtil.updateRoom(str[1],str[2],str[3]);
					if(strinfo==null){					
						IOUtil.writeStr(out, MyConverter.escape(fail));
					}else{					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}//得到最大餐厅号
				else if(readinfo.startsWith(GET_MAXROOMNO)){				
					String strinfo=DBUtil.getMaxRoomId();
					IOUtil.writeStr(out,  MyConverter.escape(strinfo));
				}//得到所有餐厅名称
				else if(readinfo.startsWith(GET_RMNAME)){				
					List<String[]> list=DBUtil.getRoomName();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out,  MyConverter.escape(strinfo));
				}//得到所有餐台
				else if(readinfo.startsWith(GET_POINT)){				
					String str[]=readinfo.split(GET_POINT);
					List<String[]> list=DBUtil.getPointByroom(str[1]);
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//得到餐台最大no
				else if(readinfo.startsWith(GET_MAXPOINTNO)){			
					int maxnum=Integer.parseInt(DBUtil.getMaxPointId());
					String str=((maxnum+1)<10)?("00"+(maxnum+1)):("0"+(maxnum+1));
					IOUtil.writeStr(out, MyConverter.escape(str));
				}//得到餐台类型
				else if(readinfo.startsWith(GET_RTNAME)){				
					List<String[]> list=DBUtil.getRoomTypeName();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}//添加餐台
				else if(readinfo.startsWith(ADD_POINT)){
					String str[]=readinfo.split(ADD_POINT);
					int num=Integer.parseInt(str[3]);
					String strinfo=DBUtil.addPoint(str[1],str[2],num,str[4],str[5],str[6]);
					IOUtil.writeStr(out,  MyConverter.escape(strinfo));
				}//删除餐台信息
				else if(readinfo.startsWith(DEL_POINT)){				
					String str[]=readinfo.split(DEL_POINT);
					String strinfo=DBUtil.delPoint(str[1]);
					if(strinfo==null){					
						IOUtil.writeStr(out, MyConverter.escape(fail));
					}else{					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}//更新餐台信息
				else if(readinfo.startsWith(UPDATE_POINT)){				
					String str[]=readinfo.split(UPDATE_POINT);//将更新餐台信息以UPDATE_POINT为界分割
					int num=Integer.parseInt(str[3]);
					String strinfo=DBUtil.updatePoint(str[1],str[2],num, str[4],str[5],str[6],str[7]);
					if(strinfo==null){					
						IOUtil.writeStr(out, MyConverter.escape(fail));
					}else{					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//得到未结账的所有订单信息
				else if(readinfo.startsWith(GET_ORDER)){				
					List<String[]> list=DBUtil.getOrderInfo();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到今日结账的订单信息
				else if(readinfo.startsWith(GET_ALACCORDER)){				
					List<String[]> list=DBUtil.getAlAccOrderInfo();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//根据餐桌查询订单
				else if(readinfo.startsWith(SEARCH_ORDER)){				
					String str[]=readinfo.split(SEARCH_ORDER);
					List<String[]> list=DBUtil.SOrderInfoByPName(str[1]);
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到订单详细信息
				else if(readinfo.startsWith(SEARCH_ODXX)){				
					String str[]=readinfo.split(SEARCH_ODXX);
					List<String[]> list=DBUtil.SOrderXxByPid(str[1]);
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//根据菜品部分信息
				else if(readinfo.startsWith(GET_VGSOMEINFO)){				
					List<String[]> list=DBUtil.getVegeSomeInfo();
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到员工信息
				else if(readinfo.startsWith(GET_WORKERINFO)){				
					List<String[]> list = DBUtil.getWorkerInfo();
					String strinfo = TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//根据员工id,查询某一个员工的信息
				else if(readinfo.startsWith(QUERY_W_ONEOFWORKERINFOBYID)){				
					String params[]=readinfo.split(QUERY_W_ONEOFWORKERINFOBYID);
					String[] resultArr = DBUtil.getOneOfWorkerInfo(params[1]);
					String strinfo = TypeExchangeUtil.stringArrayToString(resultArr);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}

				//获得员工的最大编号
				else if(readinfo.equals(GET_WORKERMAXNO)){				
					String str = DBUtil.getWorkerMaxNo();
					IOUtil.writeStr(out, MyConverter.escape(str));
				}
				//添加员工信息
				else if(readinfo.startsWith(ADD_WORKERINFO)){				
					String str[] = readinfo.split(ADD_WORKERINFO);
					String strInfo = DBUtil.addWorkerInfo(str);
					if(strInfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//删除员工信息
				else if(readinfo.startsWith(DEL_WORKERINFO)){				
					String str[] = readinfo.split(DEL_WORKERINFO);
					String strinfo=DBUtil.delWorkerInfo(str[1]);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//更新员工
				else if(readinfo.startsWith(UPDATE_WORKERINFO)){				
					String str[] = readinfo.split(UPDATE_WORKERINFO);
					String strinfo = DBUtil.updateWorkerInfo(str);
					if(strinfo==null){
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}else{
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
				//得到角色信息
				else if(readinfo.startsWith(GET_ROLEINFO)){				
					List<String[]> list = DBUtil.getRole();
					String strinfo = listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到权限信息
				else if(readinfo.startsWith(GET_AUTHORITYINFO)){				
					List<String[]> list = DBUtil.getAuthority();
					String strinfo = listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//得到角色-权限信息
				else if(readinfo.startsWith(GET_AUTHORITY_ROLE)){				
					List<String[]> list = DBUtil.getAuthorityRole();
					String strinfo = listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				
				//-----------------------结算新添加---------
				//根据条件未结账的信息
				else if(readinfo.startsWith(SEARCH_ACCORDER)){				
					String str[]=readinfo.split(SEARCH_ACCORDER);//根据餐台查询订单
					List<String[]> list= DBUtil.SAccountInfoByPName(str[1]);//根据餐台名称查询未结账的订单信息(用于结账)
					if(list==null || list.size()==0){//若信息为空					
						System.out.println("null0---------------");//打印null0提示
					}
					for(String[] arr : list){//遍历信息列表					
						for(String strr : arr){						
							System.out.print(strr+" ");
						}
						System.out.println();
					}
					String strinfo=TypeExchangeUtil.listToString(list);//数据类型转换
					System.out.println("strinfo="+strinfo);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//根据餐台查询订单再来结算
				else if(readinfo.startsWith(SEARCH_ACCOUNT_VEGE)){				
					String str[]=readinfo.split(SEARCH_ACCOUNT_VEGE);
					List<String[]> list= DBUtil.SOrderACountByPid(str[1]);
					String strinfo=TypeExchangeUtil.listToString(list);
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
				//插入结账单进行保存
				else if(readinfo.startsWith(INSERT_ACCOUNT_VEGE)){				
				    String str[]=readinfo.split(INSERT_ACCOUNT_VEGE);
				    System.out.println(str.length);
					String strinfo= DBUtil.insertAccount(str);//把账单存入数据库
					if(strinfo==null){					
						IOUtil.writeStr(out,  MyConverter.escape(fail));
					}
					else{					
						IOUtil.writeStr(out, MyConverter.escape(ok));
					}
				}
		//==============PC端标识end=====================================
				
		//================后厨手持端信息begin===========================	
	   //判断登陆权限
			else if(readinfo.startsWith(SURE_WAITER)){			
				String[] getmsg=readinfo.split(SURE_WAITER);
				String wmm=DBUtil.pdWaiter(getmsg[1]);
				if(wmm!=null&&wmm.equals(getmsg[2])){//若信息不空，且员工密码正确
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}else{
					IOUtil.writeStr(out,  MyConverter.escape(fail));
				}
			}
			//检查员工是否已经登陆
			else if(readinfo.startsWith(IS_HASlOGIN)){			
				String[] getmsg=readinfo.split(IS_HASlOGIN);
				String str=DBUtil.isHasLogin(getmsg[1]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//更新员工登陆信息
			else if(readinfo.startsWith(UPDATE_LOGINFLG)){			
				String[] getmsg=readinfo.split(UPDATE_LOGINFLG);
				String str=DBUtil.updateLoginFlg(getmsg[1],getmsg[2]);
				if(str==null){				
					IOUtil.writeStr(out,  MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}
			}
				//修改服务员密码
			else if(readinfo.startsWith(RESET_PASSWORD)){			
				String[] getmsg=readinfo.split(RESET_PASSWORD);
				String str=DBUtil.updatePassWord(getmsg[1],getmsg[2]);
				if(str==null){				
					IOUtil.writeStr(out,  MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}
			}
			//查询登陆权限
			else if(readinfo.startsWith(GET_LOGINAUTH)){			
				String[] str=readinfo.split(GET_LOGINAUTH);
				List<String[]> list=DBUtil.getLoginAuth(str[1]);
				String strinfo=TypeExchangeUtil.listToString(list);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
	     //=================后厨手持end================
		 //=================客户手持端begin============
		//更新餐台信息（状态）
			else if(readinfo.startsWith(UPDATECLIENT_POINT)){			
				String[] getinfo=readinfo.split(UPDATECLIENT_POINT);
				String strinfo=DBUtil.updateClientPoint(getinfo[1],getinfo[2]);
				if(strinfo==null){				
					IOUtil.writeStr(out, MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}
			}
				//更新餐台信息（状态）
			else if(readinfo.startsWith(GET_W_GUESTNUMBYNAME)){			
				String[] getinfo=readinfo.split(GET_W_GUESTNUMBYNAME);
				String strinfo=DBUtil.getPointGuestNumByName(getinfo[1],getinfo[2]);
				if(strinfo==null){				
					IOUtil.writeStr(out, MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}
			}
				
		//============================客户手持end=============================		
		//========================服务员手持的begin=====================================
			//获取所有的餐厅信息和餐台类型信息
			else if(readinfo.equals(GET_W_ROOM_POINTTYPE)){ 			
				List<String[]> roomInfo = DBUtil.getRoomIdName();//获取所有的餐厅id,编号和名称 信息
				List<String[]> pointTypeInfo = DBUtil.getRoomTypeIdName();//获取所有的餐厅类型的ID和名称
				String str1 = TypeExchangeUtil.listToString(roomInfo);
				System.out.println("roominfo="+str1);//打印餐厅信息
				String str2 = TypeExchangeUtil.listToString(pointTypeInfo);
				IOUtil.writeStr(out, MyConverter.escape(str1+"~"+str2));
			}
			//通过餐厅id和餐台类型id 获取相应的餐台信息
			else if(readinfo.startsWith(GET_W_POINTINFO)){			
				String params[] = readinfo.split(GET_W_POINTINFO);
				List<String[]> pointInfo = DBUtil.getPointForWaiter(params[1],params[2]);//获取服务员手持端需要的餐台信息
				String str = TypeExchangeUtil.listToString(pointInfo);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}	
			//得到主类信息
			else if(readinfo.startsWith(GET_W_MCGINFO)){			
				String read[] = readinfo.split(GET_W_MCGINFO);
				List<String[]> info = DBUtil.getMCGinfo(read[1]);
				String str=TypeExchangeUtil.listToString(info);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}					
				//得到点菜确认单信息
			else if(readinfo.startsWith(GET_W_ORDERDISHCONFIRM)){			
				String params[] = readinfo.split(GET_W_ORDERDISHCONFIRM);
				List<String[]> orderConfirmInfo = DBUtil.getOrderConfirmForWaiter(params[1]);//服务员端获取订单确认单中的部分信息
				for(String[] arr: orderConfirmInfo){//将订单确认单中的信息从列表导入到字符串数组中				
					for(String str: arr){					
						System.out.print(str+" ");
					}
					System.out.println();
				}
				String str = TypeExchangeUtil.listToString(orderConfirmInfo);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}			
            //更新点菜确认单
			else if(readinfo.startsWith(UPDATE_W_ORDERDISHCONFIRM)){			
				String params[] = readinfo.split(UPDATE_W_ORDERDISHCONFIRM);
				List<String[] > str = TypeExchangeUtil.strToList(params[1]);
				String result = DBUtil.updateOrderConfirm(str);
				IOUtil.writeStr(out, MyConverter.escape(result));
			}		
			//将点菜确认单中的某个菜品的数量减1，即将该菜品的退菜数量加1
			else if(readinfo.startsWith(DELETE_W_VEGESINGLECOUNT)){			
				String params[] = readinfo.split(DELETE_W_VEGESINGLECOUNT);
				String result = DBUtil.subVegeCount(params);
				IOUtil.writeStr(out, MyConverter.escape(result));
			}
			//将点菜确认单中的某个菜品的数量加1
			else if(readinfo.startsWith(ADD_W_VEGESINGLECOUNT)){			
				String params[] = readinfo.split(ADD_W_VEGESINGLECOUNT);
				String result = DBUtil.addVegeCount(params);
				IOUtil.writeStr(out, MyConverter.escape(result));
			}
			//删除点菜确认单中的某个菜品
			else if(readinfo.startsWith(DELETE_W_ORDERCONFIRMVEGE)){			
				String params[] = readinfo.split(DELETE_W_ORDERCONFIRMVEGE);
				String result = DBUtil.deleteOrderConfirmVege(params);
				IOUtil.writeStr(out, MyConverter.escape(result));
			}
			//查询订单表中的所有订单
			else if(readinfo.startsWith(QUERY_W_DESKNAMEOFORDER)){			
				String[] resultArr = DBUtil.queryDeskNameByUserId();
				String result=TypeExchangeUtil.stringArrayToString(resultArr);
				IOUtil.writeStr(out, MyConverter.escape(result));
			}			
		//--------------------------------------end（手持端）
		//--------------------------------------begin（PC端前台）			
			//根据路径得到图片
			else if(readinfo.startsWith(GET_W_PICTUREINFO)){			
				String[] str=readinfo.split(GET_W_PICTUREINFO);
				String imagepath=IMAGE_PATH+str[1]+".jpg";//得到图片路径
				byte[] imagemsg=PicScaleUtil.getPic(imagepath);//从磁盘获得图片
				IOUtil.writeBytes(out, imagemsg);
			}
			//根据菜品ID，从库存中扣除该菜品所用到的材料的用量
			//通过餐台Id获得查询该餐台未结账账单号，若为空，则获取当前最大订单号并根据餐台Id、员工Id、总价格、顾客人数创建新订单
			else if(readinfo.startsWith(ADD_W_NEWORDER)){			
				String[] str=readinfo.split(ADD_W_NEWORDER);
				List<String []> orderList=TypeExchangeUtil.strToList(str[1]);//数据类型转换
				String strinfo = null;
				strinfo=DBUtil.addNewOrder(orderList,str[2],str[3],str[4],str[5]);//添加新订单
				if(strinfo==null){				
					IOUtil.writeStr(out, MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}	
			}
			//创建点菜确认单
			else if(readinfo.startsWith(ADD_W_NEWDETIALORDER)){			
				String str[]=readinfo.split(ADD_W_NEWDETIALORDER);
				List<String []> orderList=TypeExchangeUtil.strToList(str[1]);
				String strinfo=DBUtil.addNewDetialOrder(orderList);
				if(strinfo==null){				
					IOUtil.writeStr(out, MyConverter.escape(fail));
				}
				else{				
					IOUtil.writeStr(out, MyConverter.escape(ok));
				}
			}
					
			//根据主类别得到子类别的名称
			else if(readinfo.startsWith(GET_CGBYMCG)){			
				String[] getinfo=readinfo.split(GET_CGBYMCG);
				List<String> strinfo=DBUtil.getCGByMCG(getinfo[1]);
				String str=TypeExchangeUtil.listString(strinfo);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}						
			//得到要修改的餐台的原来人数
			else if(readinfo.startsWith(GET_CURDESK_GUESTNUM)){			
				String[] str=readinfo.split(GET_CURDESK_GUESTNUM);
				String strinfo=DBUtil.getCurDeskGuestNum(str[1]);//得到当前餐台的人数
				System.out.println("strinfo="+strinfo);
				if(strinfo==null){//若信息为空				
					System.out.println("fail");//打印失败提示
					IOUtil.writeStr(out,  MyConverter.escape(fail));
				}else{								
					System.out.println("ok");
				  IOUtil.writeStr(out, MyConverter.escape(strinfo));
				}
			}
				//通过餐厅id和餐台类型id 获取相应的餐台的无人餐台信息
			else if(readinfo.startsWith(GET_W_NOPERSONPOINTINFO)){			
				String params[] = readinfo.split(GET_W_NOPERSONPOINTINFO);
				List<String[]> pointInfo = DBUtil.getNoPersonPoint(params[1],params[2]);//获取服务员手持端无人的餐台信息
				String str = TypeExchangeUtil.listToString(pointInfo);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//得到所有有人的餐台
			else if(readinfo.startsWith(GET_W_HASPERSONPOINTINFO)){			
				String params[] = readinfo.split(GET_W_HASPERSONPOINTINFO);
				List<String[]> pointInfo = DBUtil.getHasPersonPoint(params[1],params[2]);//获取服务员手持端有人的餐台信息
				String str = TypeExchangeUtil.listToString(pointInfo);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}			
		   //修改餐台人数
			else if(readinfo.startsWith(CHANGE_GUESTNUM)){			
				String[] str=readinfo.split(CHANGE_GUESTNUM);
				String strinfo=DBUtil.changeGuestnum(str[1], str[2]);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}		  
		   //得到菜品的所有图片
			else if(readinfo.equals(GET_VEGEIMAGE)){			
				List<String[]> list=DBUtil.getALLVegeImage();
				String str=TypeExchangeUtil.listToString(list);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//得到菜品图片的最大编号
			else if(readinfo.startsWith(GET_VEGEIMAGEMAXNO)){			
				String maxno=DBUtil.getVegeImageMaxNo();
				IOUtil.writeStr(out, MyConverter.escape(maxno));
			}
			//将菜品图片添加进库
			else if(readinfo.startsWith(INSERT_IMAGE)){			
				String[] getinfo=readinfo.split(INSERT_IMAGE);
				String strinfo=DBUtil.addVegeImage(getinfo[1],getinfo[2],getinfo[3],getinfo[4]);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//判断当前菜品是否存在主图
			else if(readinfo.startsWith(PDVEGEMAIN)){			
				String[] getinfo=readinfo.split(PDVEGEMAIN);
				String strinfo=DBUtil.pdVegeImage(getinfo[1]);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//得到指定菜品的主图片
			else if(readinfo.startsWith(GET_VEGEMAINIMAGE)){			
				String[] getstr=readinfo.split(GET_VEGEMAINIMAGE);
				String strinfo=DBUtil.getVegeMainImage(getstr[1]);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//修改菜品主图
			else if(readinfo.startsWith(UPDATE_MAINIMAGE)){			
				String[] getstr=readinfo.split(UPDATE_MAINIMAGE);
				String strinfo=DBUtil.updateVegeMainImage(getstr);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
		   //通过菜品ID获得菜品图品信息，并将该菜品的第一张图片路径传回服务员手持端
			else if(readinfo.startsWith(GET_W_PICBYVEGEID)){			 
				String params[] = readinfo.split(GET_W_PICBYVEGEID);
				String str = DBUtil.getVegeMainImage(params[1]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//判断是否存在没有图片的那张图片
			else if(readinfo.startsWith(ISHAVENULLPIC)){			
				  File dir = new File(IMAGE_PATH);  //创建图片文件
		          File file = new File(dir,IMAGE_NULLNAME);//创建无图的文件  
		          if(file.exists()){ //若存在无图的文件		          
		        	 IOUtil.writeStr(out, MyConverter.escape(IMAGE_PATH+IMAGE_NULLBIGNAME+"#"+IMAGE_PATH+IMAGE_NULLNAME));//将无图的信息加密后写入流
		          }
		          else{		          
		            IOUtil.writeStr(out, MyConverter.escape(fail));//将失败提示信息写入流
		          }
			}
				//删除主图的大图
			else if(readinfo.startsWith(DELETE_ZBIMAGE)){			
				String[] getinfo=readinfo.split(DELETE_ZBIMAGE);
				String str=DBUtil.delZDImage(getinfo[1],IMAGE_PATH+getinfo[2]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
		   // 根据类别获取菜品
			else if(readinfo.startsWith(SEARCH_VGBYCATE)){			
				String[] getstr=readinfo.split(SEARCH_VGBYCATE);
				List<String[]> list=DBUtil.searchVegeByCate(getstr[1]);
				String sendstr=TypeExchangeUtil.listToString(list);
				IOUtil.writeStr(out, MyConverter.escape(sendstr));
			}
		   //根据菜品id得到这个菜的所有子图
			else if(readinfo.startsWith(SEARCH_ZPATHBYVGID)){			
				String[] getstr=readinfo.split(SEARCH_ZPATHBYVGID);
				String str=DBUtil.searchzpathByvgid(getstr[1]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//服务员手持端得到菜品信息
			else if(readinfo.startsWith(GET_W_VEGEINFO)){			
				String[] getstr=readinfo.split(GET_W_VEGEINFO);
				List<String[]> vegelist=DBUtil.getWVege(getstr[1]);
				String strinfo=TypeExchangeUtil.listToString(vegelist);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
		   //根据菜品id得到菜品的相关信息
			else if(readinfo.startsWith(SEARCH_W_VGBYID)){			
				String[] getinfo=readinfo.split(SEARCH_W_VGBYID);
				String[] vegelist=DBUtil.searchWVegeByid(getinfo[1]);
				//根据菜品id得到主图路径
				String mainpath=DBUtil.getVegeMainImage(getinfo[1]);
				//根据菜品id得到子图路径
				String imagemsg=DBUtil.searchzpathByvgid(getinfo[1]);
				String[] msg=new String[vegelist.length+2];//将列表长度加2放入新字符串数组
				for(int i=0;i<vegelist.length;i++){
					msg[i]=vegelist[i];
				}
				msg[22]=mainpath;//主图路径
				msg[23]=imagemsg;//子图路径
				String strinfo=TypeExchangeUtil.stringArrayToString(msg);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//测试是否连接上
			else if(readinfo.startsWith(TESTCONNECT)){			
				IOUtil.writeStr(out, MyConverter.escape("success"));//将提示成功信息写入流
			}			
			//------------------------------------------------end------------------------------------------------------//

				//pc端根据菜品id得到菜品主图的大图
			else if(readinfo.startsWith(GET_MBIMAGE)){			
				String[] getstr=readinfo.split(GET_MBIMAGE);
				String str=DBUtil.searchmbimagepath(getstr[1]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
				//pc端根据菜品id得到菜品子图
			else if(readinfo.startsWith(GET_ZBIMAGE)){			
				String[] getstr=readinfo.split(GET_ZBIMAGE);
				String str=DBUtil.searchzimagepath(getstr[1]);
				IOUtil.writeStr(out, MyConverter.escape(str));
			}
			//修改菜品主图的大图
			else if(readinfo.startsWith(UPDATE_MAINBIGIMAGE)){			
				String[] getstr=readinfo.split(UPDATE_MAINBIGIMAGE);
				String strinfo=DBUtil.updateVegeMainBigImage(getstr);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//获得pc端有用的菜品信息
			else if(readinfo.startsWith(GET_USEVEGE)){			
				List<String[]> vegelist=DBUtil.getUseVege();				
				String strinfo=TypeExchangeUtil.listToString(vegelist);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			//根据员工id得到员工姓名
			else if(readinfo.startsWith(GET_WORKERNAMEBYID)){			
				String[] getinfo=readinfo.split(GET_WORKERNAMEBYID);
				String strinfo=DBUtil.getWorkernameById(getinfo[1]);
				IOUtil.writeStr(out, MyConverter.escape(strinfo));
			}
			System.out.println("发送过去信息");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
			try {in.close();} catch (IOException e) {e.printStackTrace();}//关闭流
			try {out.close();} catch (IOException e) {e.printStackTrace();}
			try {sc.close();} catch (IOException e) {e.printStackTrace();}
			}			
		}	
		   public  String gettime(){//创建时间获取方法
			   Calendar   c   =   Calendar.getInstance(); // 使用默认时区和语言环境获得一个日历
		       c.setTime(new   java.util.Date()); //使用给定的 Date 设置此 Calendar 的时间
		       int   year   =   c.get(Calendar.YEAR); //返回给定日历年字段的值
		       int   month   =   c.get(Calendar.MONTH)+1; //返回给定日历月字段的值
		       int   day   =   c.get(Calendar.DAY_OF_MONTH);  //返回给定日历日字段的值
		       int   hour   =   c.get(Calendar.HOUR_OF_DAY); //返回给定日历时字段的值
		       int   minute   =   c.get(Calendar.MINUTE); //返回给定日历分字段的值
		       int   second   =   c.get(Calendar.SECOND);//返回给定日历秒字段的值
		       return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;//将得到的时间信息返回
		   }

}
