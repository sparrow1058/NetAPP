package com.example.android_photo;

import java.io.DataInputStream;
import java.io.DataOutputStream;    
import java.io.File;
import java.io.FileInputStream;    
import java.io.FileNotFoundException;
import java.io.IOException;      
import java.io.OutputStream;
import java.net.ServerSocket;    
import java.net.Socket; 
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.R.integer;
import android.util.Log;
import android.widget.Toast;

public class SocketSend {
	File file;
	Socket socket;
	private int port=9091;
	private String ip="192.168.1.101";
	
	public void link() {  
        DataOutputStream dos;  
        FileInputStream fis; 
        try{
                socket = new Socket(ip, port);                    
                                       
        }catch (SocketTimeoutException e) {  
            e.printStackTrace();              
        }catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
	
	
	
    //得到文件夹下的所有图片绝对路�?  
//         public  String[] listFile(String derect) {  
//             File file = new File(derect);  
//             File[] f = file.listFiles();  
//             String Path[] = new String[f.length];  
//             for (int i = 0; i < f.length; i++)  
//             {  
//                     Path[i] = f[i].getPath();  
//                     System.out.println(Path[i]);  
//             }  
//             return Path;        
//         }  
  
    // 根据图片名称上传照相机中单个照片  
//    private void upload(String path,String scrip) {  
//        DataOutputStream dos;  
//        FileInputStream fis;  
//        try {  
//            ///sdcard/DCIM/Camera/照相机拍摄后图片�?存路�?  
//            file = new File("/sdcard/DCIM/Camera/" + path.trim());  
//            if (file.length() == 0) {  
//                return;  
//            } else {  
//                socket = new Socket(ip, port);  
//                dos = new DataOutputStream(socket.getOutputStream());  
//                fis = new FileInputStream(file);  
//                dos.writeUTF(path.substring(0,path.indexOf("."))+scrip+path.substring(path.indexOf(".")));  
//                dos.flush();  
//                byte[] sendBytes = new byte[1024 * 8];  
//                int length;  
//                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
//                    dos.write(sendBytes, 0, length);  
//                    dos.flush();// 发�?�给服务�?  
//                }  
//                dos.close();//在发送消息完之后�?定关闭，否则服务端无法继续接收信息后处理，手机卡�?  
//                /*reader = new BufferedReader(new InputStreamReader( 
//                        socket.getInputStream())); 
//                result = Boolean.parseBoolean(reader.readLine().toString()); 
//                System.out.println("上传结果" + result);//运行时�?�是提示socket关闭，不能接收服务端返回的消�? 
//                reader.close();*/  
//                fis.close();  
//                socket.close();  
//            }  
//        } catch (UnknownHostException e) {  
//            e.printStackTrace();  
//        } catch (FileNotFoundException e) {  
//            e.printStackTrace();  
//  
//        }catch (SocketTimeoutException e) {  
//            e.printStackTrace();        
//        }catch (IOException e) {  
//            e.printStackTrace();  
//        }  
//    }  
    //根据文件夹路径上传所有的图片到服务器  
    //此dirpath是图片绝对路�?  
    public void seriesUpload(String dirpath) {   	   
    //	dirpath="/storage/emulated/0/formats/IMG_20140411_182117.JPEG" ;
    //	dirpath="/storage/emulated/0/Tencent/QQ_Images/434370832-94a4f46c3a19994.jpg" ;
        DataOutputStream dos;  
        FileInputStream fis;  
        try {         	       	 
        	 OutputStream out=socket.getOutputStream();										
				String msg="Hello World123";
				out.write(msg.getBytes());
            file = new File(dirpath);  
            if (file.length() == 0) {  
            	System.out.println("faile");
                return;  
            } else {  
               socket = new Socket(ip, port);  
            
          //创建将要被发送的图片的文件输入流  
//             fis = new java.io.FileInputStream(dirpat h);  
//            //获得套接字的输出流，并包装成数据输出�?  
//            dos = new java.io.DataOutputStream(socket.getOutputStream());  
//            // 向输出流中写入文件数据长�?  
//            dos.writeInt(fis.available());//注：此处未�?�虑图片太大超出int范围，以至于出现内存溢出的情�?       
//            // 将实际的图片数据读取到byte[] data�?  
//            byte[] data = new byte[fis.available()];  
//            fis.read(data);  
//            // 将图片数据写入到输出流中  
//            dos.write(data);  
//            dos.flush(); 
            
            
                dos = new DataOutputStream(socket.getOutputStream());  
                fis = new FileInputStream(file);  
                dos.writeUTF(dirpath.substring(dirpath.lastIndexOf("/")+1));//截取图片名称  
           
                dos.flush();  
                byte[] sendBytes = new byte[1024 * 8];  
                int length;  
                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
                    dos.write(sendBytes, 0, length);  
                    dos.flush();// 发�?�给服务�?  
                }  
                dos.close();//在发送消息完之后�?定关闭，否则服务端无法继续接收信息后处理，手机卡�?  
                fis.close();  
                socket.close();  
            }  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }catch (SocketTimeoutException e) {  
            e.printStackTrace();              
        }catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
} 

