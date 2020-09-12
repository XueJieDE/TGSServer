package Function;

import Encrypt.DataOption;
import Encrypt.DesDecrypt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JudgeCommand {
    private ReadWriteLock rw1=new ReentrantReadWriteLock();
    private Lock r=rw1.readLock();

    private char staticCommand='3';
    private char staticServerId='1';

    //拆包后的第一层
    public String ticketTgs;
    public String authemticator;
    public char command;
    public char serverId;
    //public String KeyCTgs="abcdefgh";

    //ticket的内容
    public String t_kc_tgs;
    public String t_userId;
    public String t_ads;
    public char t_request;
    public String t_timeT2;
    public String t_Lifetime2;

    //认证的内容
    public String a_idc;
    public String a_ads;
    public String a_timeT3;

    public String input;
    public JudgeCommand(String input){
        this.input=input;
    }

    //拆第一层
    private boolean split(){
        System.out.println("长度测试");
        if(input.length()>=1218) {
            System.out.println("长度符合");
            command = input.charAt(0);
            serverId = input.charAt(1);
            ticketTgs = input.substring(2, 898);
            System.out.println("ticket"+ticketTgs);
            System.out.println(ticketTgs.length());
            authemticator = input.substring(898, 1218);
            System.out.println("authemicator"+authemticator);
            System.out.println(authemticator.length());
            //ticketTgs=input.substring(2,114);
            //authemticator=input.substring(114,148);
            System.out.println("command:"+command+"    serverId:"+serverId);
            if(IsCommandSame()&&IsServerIdSame()) return true;
            else return false;
        }else {
            return false;
        }
    }

    //拆第二层
    private boolean Decryption()throws Exception{
        r.lock();
        //String afterDecryptTicketTgs=ticketTgs;//=function(ticketTgs)解密程序
        //String afterDecryptAuthemticator=authemticator;//=function(ticketTgs)解密程序
        System.out.println(DataOption.INSTANCE.tgsKey);
        DesDecrypt decrypt=new DesDecrypt(ticketTgs, DataOption.INSTANCE.tgsKey);
        decrypt.decrypt();
        String afterDecryptTicketTgs=decrypt.message;

        r.unlock();
        System.out.println("ticket解密成功");
        if(afterDecryptTicketTgs.length()==112) {
            t_kc_tgs = afterDecryptTicketTgs.substring(0, 64);
            System.out.println("tgs:" + t_kc_tgs);
            t_userId = afterDecryptTicketTgs.substring(64, 73);
            System.out.println("用户id：" + t_userId);
            t_ads = afterDecryptTicketTgs.substring(73, 85);
            System.out.println("ads" + t_ads);
            t_request = afterDecryptTicketTgs.charAt(85);
            System.out.println("request" + t_request);
            t_timeT2 = afterDecryptTicketTgs.substring(86, 99);
            System.out.println("时间戳:" + t_timeT2);
            t_Lifetime2 = afterDecryptTicketTgs.substring(99, 112);
            System.out.println("存在时间：" + t_Lifetime2);

        }else return false;

        DesDecrypt decryptAuth=new DesDecrypt(authemticator,t_kc_tgs);
        decryptAuth.decrypt();
        String afterDecryptAuthemticator=decryptAuth.message;

        System.out.println("authemticator解密成功");

        if(afterDecryptAuthemticator.length()==34) {
            a_idc = afterDecryptAuthemticator.substring(0, 9);
            System.out.println("idc:" + a_idc);
            a_ads = afterDecryptAuthemticator.substring(9, 21);
            System.out.println("ads:" + a_ads);
            a_timeT3 = afterDecryptAuthemticator.substring(21, 34);
            System.out.println("时间戳：" + a_timeT3);
        }else return false;
        return IsJudgeSuccess();
    }

    private boolean IsCommandSame()
    {
        System.out.println("指令成功");
        return command==staticCommand;
    }
    private boolean IsServerIdSame(){
        System.out.println("服务器相同");
        return serverId==staticServerId;
    }
    private boolean IsJudgeSuccess(){
        return t_userId.equals(a_idc)&&t_ads.equals(a_ads)&&t_request=='1'&&IsTimeSuccess();
    }
    private boolean IsTimeSuccess(){
        System.out.println("时间判断");
        long timeBef=Long.parseLong(t_timeT2);
        long length=Long.parseLong(t_Lifetime2);
        long currentTime=System.currentTimeMillis();
        long sendTIme=Long.parseLong(a_timeT3);
        System.out.println(currentTime);
        System.out.println(sendTIme>=timeBef);
        System.out.println(sendTIme<=length);
        System.out.println(currentTime>=timeBef);
        System.out.println(currentTime<=length);
        //if(sendTIme>=timeBef&&sendTIme<=length&&currentTime>=timeBef&&currentTime<=length)return true;
        //else return false;
        if(currentTime<=length)return true;
        else return false;
    }

    public boolean Result()throws Exception{
        System.out.println("操作中……");
        if(split()&&Decryption()){
            System.out.println("操作完成……");
            return true;
        } else return false;
    }
}
