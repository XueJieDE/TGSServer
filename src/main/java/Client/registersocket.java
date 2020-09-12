package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class registersocket {
    static String truth = "1";
    static String wrong = "0";

    public registersocket() {
    }

    public static String register(String host, int port, String data, String account) throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);
        System.out.println("register");
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        System.out.println("data" + data);
        String receiver;
        if (data.substring(0, 1).equals("d")) {
            receiver = dis.readUTF();
            return receiver;
        } else {
            dos.writeUTF(data);
            dos.flush();
            receiver = dis.readUTF();
            System.out.println("receiver  " + receiver);
            String tag = receiver.substring(0, 1);
            String sign;
            if (tag.equals("6")) {
                sign = receiver.substring(1, 2);
                if (sign.equals("1")) {
                    dis.close();
                    dos.close();
                    socket.close();
                    return truth;
                }
            }

            if (tag.equals("7")) {
                sign = receiver.substring(1, 2);
                if (sign.equals("1")) {
                    dis.close();
                    dos.close();
                    socket.close();
                    return truth;
                }
            }

            String msg;
            if (tag.equals("8")) {
                sign = receiver.substring(1, 2);
                if (sign.equals("1")) {
                    msg = receiver.substring(2);
                    dis.close();
                    dos.close();
                    socket.close();
                    return msg;
                }
            }

            if (tag.equals("9")) {
                sign = receiver.substring(1, 2);
                if (sign.equals("1")) {
                    msg = receiver.substring(2);
                    dis.close();
                    dos.close();
                    socket.close();
                    return msg;
                }
            }

            if (tag.equals("a")) {
                sign = receiver.substring(1, 2);
                if (sign.equals("1")) {
                    msg = receiver.substring(3);
                    dis.close();
                    dos.close();
                    socket.close();
                    return msg;
                }
            }

            if (tag.equals("c")) {
                sign = receiver.substring(1);

                do {
                    data = null;
                    msg = console.readLine();
                    data = 5 + account + msg;
                    dos.writeUTF(data);
                    dos.flush();
                    data = dis.readUTF();
                    System.out.println("from client message" + data);
                } while(!data.equals("end"));
            }

            dis.close();
            dos.close();
            socket.close();
            return wrong;
        }
    }
}
