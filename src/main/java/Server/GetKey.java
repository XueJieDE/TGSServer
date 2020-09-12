package Server;

import Encrypt.DataOption;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GetKey {
    private static ServerSocket server = null;
    private static Socket ss = null;
    private static String key = null; // Kcv秘钥
    volatile boolean isGet=false;
    /**
     * 客户端集合
     */

    public GetKey() throws IOException {
        int port=8887;
        ServerSocket server=new ServerSocket(port);
        System.out.println("server building");
        while(isGet ==false) {
            Socket socket=server.accept();
            System.out.println("Socket building");
            new Thread(()->{
                System.out.println("New user in");
                DataInputStream dis = null;
                try {
                    dis = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                DataOutputStream dos = null;
                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //	boolean isrunning=true;
                //while(isrunning) {
                String data = null;
                try {
                    data = dis.readUTF();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(data);

                try {
                    if(data.charAt(0)=='d') {
                        String key = data.substring(1);
                        DataOption.INSTANCE.tgsKey=key;
                        System.out.println(key);
                        isGet =true;
                    }
                    data ="s1";
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    dos.writeUTF(data);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    dos.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //		if(data.equals("end")) {
                //			break;
                //		}
                //	}
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("socket over");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("over");
    }

}
