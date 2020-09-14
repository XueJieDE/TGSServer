package Function;

import Encrypt.DataOption;
import Encrypt.DesEncrypt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReturnPacket {
    private ReadWriteLock rw1=new ReentrantReadWriteLock();
    private Lock r=rw1.readLock();

    private String command;
    private String mark;
    private String authentictor_tgs;

    private String resultPacket;

    private String KeyV="ijklmnop";

    private JudgeCommand judgeCommand;

    //发送报文
    private String kcv;
    private String request;
    private String timeT4;
    //private String replace;
    private String ticketV;

    //ticketV
    private String t_kcv;
    private String t_userId;
    private String t_ads;
    private char t_IDv;
    private String t_timeT4;
    private String t_Lifetime4;

    public ReturnPacket(JudgeCommand judgeCommand){
        this.judgeCommand=judgeCommand;
    }

    public String GetResult()throws Exception{
        ResultSet();
        return resultPacket;
    }

    private void ResultSet()throws Exception{
        command="9";
        if(judgeCommand.Result()){
            mark="1";
            authentictor_tgs=SetAuthentic();
            resultPacket=command+mark+authentictor_tgs;
        }else {
            mark="0";
            authentictor_tgs=new String();
            for(int i=0;i<1472;i++){
                authentictor_tgs+='0';
            }
            resultPacket=command+mark+authentictor_tgs;
        }
    }

    private String SetTicketV()throws Exception{
        //t_kcv=new String();//密钥
        r.lock();
        t_kcv=DataOption.getRandomKey(64);
        r.unlock();
        System.out.println("t_Kcv   "+t_kcv);
        t_userId=judgeCommand.t_userId;
        t_ads=judgeCommand.t_ads;
        t_IDv='1';//服务器v
        t_timeT4=String.valueOf(System.currentTimeMillis());//时间戳
        t_Lifetime4=String.valueOf(System.currentTimeMillis()+60000);//时间长度

        r.lock();
        String instanceKey=DataOption.INSTANCE.key;
        r.unlock();
        String message= t_kcv+t_userId+t_ads+t_IDv+t_timeT4+t_Lifetime4;//ticketV的加密
        System.out.println("ticket v :"+message);
        DesEncrypt desEncrypt=new DesEncrypt(message,instanceKey);
        //System.out.println(DataOption.INSTANCE.key);
        desEncrypt.encrypt();
        String ciphertexts= desEncrypt.ciphertexts;
        //r.unlock();
        return ciphertexts;
    }

    private String SetAuthentic()throws Exception{

        ticketV=SetTicketV();
        //kcv=t_kcv;
        //kcv=DataOption.getRandomKey(64);
        kcv=t_kcv;
        System.out.println("KCV   "+kcv+"   length:  "+kcv);
        request="1";//服务器v
        timeT4=String.valueOf(System.currentTimeMillis());

        String Tgs=kcv+request+timeT4;
        String asciiTgs=Tgs;
        /*for(int i=0;i<Tgs.length();i++)asciiTgs+= DataOption.chartoascii(Tgs.charAt(i));
        System.out.println(asciiTgs);
        System.out.println(asciiTgs.length());
        for(int i=0;i<16;i++)asciiTgs+="0";*/
        System.out.println(asciiTgs);
        System.out.println(asciiTgs.length());


        System.out.println("ticketV"+ticketV);
        System.out.println("ticketVLength"+ticketV.length());
        System.out.println("RestPart"+asciiTgs);
        System.out.println("RestPart"+asciiTgs.length());

        //return kcv+request+timeT4+replace+ticketV;

        String message=asciiTgs+ticketV;
        DesEncrypt desEncrypt=new DesEncrypt(message,judgeCommand.t_kc_tgs);
        desEncrypt.encrypt();
        String ciphertexts= desEncrypt.ciphertexts;
        System.out.println("FinalPart"+ciphertexts);
        System.out.println("FinalPart"+ciphertexts.length());
        return ciphertexts;
    }
}
