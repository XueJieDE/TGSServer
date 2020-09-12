package Server;
import Encrypt.DataOption;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendKey {
    public SendKey()throws Exception{
        String host = "192.168.43.218";
        int port = 8888;
        // �����˽�������
        Socket socket = new Socket(host, port);
        // �������Ӻ�����Ϣ
        BufferedReader console=new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
        DataInputStream dis=new DataInputStream(socket.getInputStream());
        String data="b";
        data+= DataOption.INSTANCE.key;
        String getData="";
                //������Ϣ
                //String msg=console.readLine();
        while(true) {
            //������Ϣ
            //String msg=console.readLine();
            System.out.println("发送密钥"+data);
            dos.writeUTF(data);
            dos.flush();
            //��ȡ��Ϣ
            getData=dis.readUTF();
            System.out.println("server"+getData);
            if(getData.contains("s1")) {
                break;
            }else{
                Thread.sleep(1000);
            }
        }
        System.out.println("服务器密钥发送完毕");
        dis.close();
        dos.close();
        socket.close();
    }
}
