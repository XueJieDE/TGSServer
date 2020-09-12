import Encrypt.DataOption;
import Encrypt.DesDecrypt;
import Encrypt.DesEncrypt;

import java.io.*;
import java.net.Socket;



public class test {

    public static String decimaltoBinary(int t, int flag) {
        String numstr;
        for(numstr = ""; t > 0; t /= 2) {
            int res = t % 2;
            numstr = res + numstr;
        }

        if (numstr.length() < 4 && flag == 1) {
            switch(numstr.length()) {
                case 0:
                    numstr = "0000";
                    break;
                case 1:
                    numstr = "000" + numstr;
                    break;
                case 2:
                    numstr = "00" + numstr;
                    break;
                case 3:
                    numstr = "0" + numstr;
            }
        }

        return numstr;
    }

    public static String chartoascii(char c) {
        String binary = "";
        binary = decimaltoBinary(c, 0);
        if (c >= ' ' && c <= '?') {
            binary = "00" + binary;
        } else if (c >= '@' && c <= 128) {
            binary = "0" + binary;
        }

        return binary;
    }
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 55533;
        // �����˽�������
        Socket socket = new Socket(host, port);
        // �������Ӻ�����Ϣ
        BufferedReader console=new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
        DataInputStream dis=new DataInputStream(socket.getInputStream());
        /*String data="31";
        String s=new String();
        for (int i=0;i<64;i++)s+="2";
        String user="123456789";
        String net="123456789012";
        char request='a';
        String timeLine=String.valueOf(System.currentTimeMillis());
        String LifeTime=String.valueOf(System.currentTimeMillis()+5000);
        //String LifeTime="0000000000000";
        String tgs=data+s+user+net+request+timeLine+LifeTime;

        String aut=user+net+timeLine;

        String fin=tgs+aut;*/

        String data="31";
        String s=DataOption.getRandomKey(64);
        System.out.println("Key:   "+s);
        String user="123456789";
        String net="123456789012";
        char request='1';
        String timeLine=String.valueOf(System.currentTimeMillis());
        String LifeTime=String.valueOf(System.currentTimeMillis()+5000);
        //String LifeTime="0000000000000";
        String tgs=s+user+net+request+timeLine+LifeTime;

        String aut=user+net+timeLine;

        //String fin=tgs+aut;

        String key="abcdefgh";
        DesEncrypt desEncrypt=new DesEncrypt(tgs,key);
        desEncrypt.encrypt();
        String ciphertexts= desEncrypt.ciphertexts;
        //ciphertexts="11011100110011100100111101110000010000111100010011011110010101110001111101010001000011000101101010010111011101010000010000100100110110100011101001000010110001100000010111101010101100001111000001001011010110101000111110110110001000110000110101111111111010010010111111101110001000000011111101110111000011110101110101011000101101100011011011101101000111110100011010000111100010000111100011001111100011001111101001010100111010101011000000001101110101000100011100011001100001101010101100101010010011110000100101010010001000011100011000001101101001010011010000100100100010111100111010101010010100001001110110001111111100000101011010110100111110001110000001011010110000100101011011010100011101100001100111000110110100000000101010111011011011101010011110100110110001110110110111010011001011000011101010011100000101100001101100100110101011011111110011111011000100000100110101101011010110001010001000000100";
        System.out.println(ciphertexts);
        System.out.println(ciphertexts.length());

        //DesDecrypt decrypt=new DesDecrypt(ciphertexts,key);
        //decrypt.decrypt();
        //System.out.println(decrypt.message);
        //System.out.println(tgs.equals(decrypt.message));

        System.out.println();
        System.out.println();

        DesEncrypt desEncrypt1=new DesEncrypt(aut,s);
        desEncrypt1.encrypt();
        String ciphertexts1=desEncrypt1.ciphertexts;
        System.out.println(ciphertexts1);
        System.out.println(ciphertexts1.length());

        //DesDecrypt decrypt1=new DesDecrypt(ciphertexts1,key);
        //decrypt1.decrypt();
        //System.out.println(decrypt1.message);
        //System.out.println(decrypt1.message.equals(aut));

        String fin=data+ciphertexts+ciphertexts1;

        System.out.println(fin.length());

        while(true) {
                //������Ϣ
                //String msg=console.readLine();
            dos.writeUTF(fin);
            dos.flush();
                //��ȡ��Ϣ
            data=dis.readUTF();
            System.out.println("server"+data);
            if(data.equals("end")) {
                break;
            }
        }
        dis.close();
        dos.close();
        socket.close();
        /*String data="31";
        String s=new String();
        for (int i=0;i<64;i++)s+="2";
        String user="123456789";
        String net="123456789012";
        char request='1';
        String timeLine=String.valueOf(System.currentTimeMillis());
        String LifeTime=String.valueOf(System.currentTimeMillis()+5000);
        //String LifeTime="0000000000000";
        String tgs=s+user+net+request+timeLine+LifeTime;

        String aut=user+net+timeLine;

        String fin=tgs+aut;

        String key="abcdefgh";
        DesEncrypt desEncrypt=new DesEncrypt(tgs,key);
        desEncrypt.encrypt();
        String ciphertexts= desEncrypt.ciphertexts;
        System.out.println(ciphertexts);
        System.out.println(ciphertexts.length());

        DesDecrypt decrypt=new DesDecrypt(ciphertexts,key);
        decrypt.decrypt();
        System.out.println(decrypt.message);
        System.out.println(tgs.equals(decrypt.message));

        System.out.println();
        System.out.println();

        DesEncrypt desEncrypt1=new DesEncrypt(aut,key);
        desEncrypt1.encrypt();
        String ciphertexts1=desEncrypt1.ciphertexts;
        System.out.println(ciphertexts1);
        System.out.println(ciphertexts1.length());

        DesDecrypt decrypt1=new DesDecrypt(ciphertexts1,key);
        decrypt1.decrypt();
        System.out.println(decrypt1.message);
        System.out.println(decrypt1.message.equals(aut));
        */
        //ticket v
        /*
        String key="";
        for(int i=0;i<64;i++)key+="a";
        String s="";
        for(int i=0;i<64;i++)s+="c";
        String userId="123456789";
        String netId="123456789012";
        char request='a';
        String time=String.valueOf(System.currentTimeMillis());
        String timeLife=String.valueOf(System.currentTimeMillis()+6000);

        String ticketv=s+userId+netId+request+time+timeLife;

        DesEncrypt desEncrypt1=new DesEncrypt(ticketv,key);
        desEncrypt1.encrypt();
        String ciphertexts1=desEncrypt1.ciphertexts;
        System.out.println(ciphertexts1);
        System.out.println(ciphertexts1.length());

        String Tgs=s+request+time;
        String asciiTgs="";
        for(int i=0;i<Tgs.length();i++)asciiTgs+=chartoascii(Tgs.charAt(i));
        System.out.println(asciiTgs);
        System.out.println(asciiTgs.length());
        for(int i=0;i<16;i++)asciiTgs+="0";
        System.out.println(asciiTgs);
        System.out.println(asciiTgs.length());
        /*DesEncrypt desE=new DesEncrypt(Tgs,key);
        desE.encrypt();
        String tgsAfter=desE.ciphertexts;
        System.out.println(tgsAfter);
        System.out.println(tgsAfter.length());*/

        /*String returnPart=asciiTgs+ciphertexts1;
        System.out.println(returnPart.length());
        DesEncrypt Final=new DesEncrypt(returnPart,key);
        Final.encrypt();
        String AfterFinal=Final.ciphertexts;
        System.out.println(AfterFinal);
        System.out.println(AfterFinal.length());

        String data= DataOption.getRandomKey(64);
        System.out.println(data);
        System.out.println(data.length());
       // DesDecrypt deFinal=new DesDecrypt()*/
    }
}
