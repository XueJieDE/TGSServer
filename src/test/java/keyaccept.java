import Function.JudgeCommand;
import Function.ReturnPacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class keyaccept {/*
    // 解密用户发送来的密文
    public String DesDecrypt(String ciphertexts, String key) throws IOException {
        String Message = null;
        // 第一次解密用户报文，得到第一位为操作指令，后续内容为加密后的Ticketv以及Authencation
        DesDecrypt decrypt = new DesDecrypt(ciphertexts, key);
        decrypt.decrypt();
        Message = decrypt.message;
        String Flag = Message.substring(0, 1); // 截取出操作指令;
        String secondTicketv = Message.substring(1, 897); // 截取出加密的Ticketv
        String secondAuthen = Message.substring(897); // 截取出加密的Authen
        // String secondciphertexts = Message.substring(1); //
        // 截取出加密后的Ticketv以及Authencation
        // 二次解密Ticketv
        DesDecrypt decryptTicketv = new DesDecrypt(secondTicketv, key);
        decryptTicketv.decrypt();
        String Ticketv = decryptTicketv.message;
        // 二次解密Authen
        DesDecrypt decryptAuthen = new DesDecrypt(secondAuthen, key);
        decryptAuthen.decrypt();
        String Authen = decryptAuthen.message;
        System.out.println("密文:" + Message);
        Message = null;
        Message = Flag + Ticketv + Authen;
        System.out.println("解密结果:" + Message);
        return Message;

    }
    public void SplitMessage(String Message) { // 分割报文中的各类信息

        String Flag = Message.substring(0, 1);
        String Kcv = Message.substring(1, 65);
        String IDcinTicketv = Message.substring(65, 74);
        String ADsinTicketv = Message.substring(74, 86);
        String IDvinTicketv = Message.substring(86, 87);
        String TS4 = Message.substring(87, 100);
        String LT4 = Message.substring(100, 113);
        String IDcinAuthen = Message.substring(113, 122);
        String ADsinAuthen = Message.substring(122, 134);
        String TS5 = Message.substring(134, 147);
        Sites.put("Flag", Flag);
        Sites.put("Kcv", Kcv);
        Sites.put("IDcinTicketv", IDcinTicketv);
        Sites.put("ADsinTicketv", ADsinTicketv);
        Sites.put("IDvinTicketv", IDvinTicketv);
        Sites.put("TS4", TS4);
        Sites.put("LT4", LT4);
        Sites.put("IDcinAuthen", IDcinAuthen);
        Sites.put("ADsinAuthen", ADsinAuthen);
        Sites.put("TS5", TS5);

        // return Sites;
    }
*/
        // String msg=console.readLine();
        // dos.writeUTF(msg);
        // System.out.println("data"+receiver);

}
