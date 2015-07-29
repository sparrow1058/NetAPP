package com.bn.serverinfo;//声明包语句

import java.net.ServerSocket;
import java.net.Socket;

import static  com.bn.serverinfo.Constant.*;
public class ServerThread extends Thread{              //创建一个叫ServerThread的继承线程的类
	     public ServerSocket ss;                        //定义一个ServerSocket对象
	     @Override
	     public void run(){                            //重写run方法
	     	try{                                        //因用到网络，需要进行异常处理
	     		ss=new ServerSocket(9999);                //创建一个绑定到端口9999上的ServerSocket对象
	     		System.out.println("Socket success :9999");//打印提示信息：Socket对象创建成功9999
	     	}
	     	catch(Exception e){                           //捕获异常	     	
	     		e.printStackTrace();
	     	}
	 		while(flag){                          //开启While循环	 		
	 			try{	 			
	 				Socket sc=ss.accept();//接受客户端的连接请求，若有连接请求返回连接对应的Socket对象
	 				System.out.println("客户端请求到达："+sc.getInetAddress());//打印提示信息
	 				ServerAgentThread sat=new ServerAgentThread(sc); 	//创建一个代理服务线程	
	 				sat.start();	//开启代理服务线程
	 			}
	 			catch(Exception e){		     	
		     		e.printStackTrace();
		     	} 			
	 		}
	     }	
	     public static void main(String args[]){            //编写主方法
	    	 (new ServerThread()).start();               //创建一个服务线程并启动
	     }
	}