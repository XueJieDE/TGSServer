package Server;

import Function.JudgeCommand;
import Function.ReturnPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        new Thread(()->{
            try {
                System.out.println("接收密钥");
                new GetKey();
                System.out.println("接收成功");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                System.out.println("发送密钥");
                new SendKey();
                System.out.println("发送成功");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        int port=55533;
        ServerSocket server=new ServerSocket(port);
        System.out.println("server building");
        while(true) {
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
                    JudgeCommand judgeCommand = new JudgeCommand(data);
                    ReturnPacket returnPacket = new ReturnPacket(judgeCommand);
                    data = returnPacket.GetResult();
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
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
