package Client;

import Encrypt.DesDecrypt;
import Encrypt.DesEncrypt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class clientui extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    int p = 101;
    int q = 103;
    int e = 65537;
    int n;
    int jn;
    int d;
    String kctgs;
    String Tickettgs;
    String TGS;
    String ts3;
    String ts5;
    String result;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    clientui frame = new clientui();
                    frame.setVisible(true);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    public static int exgcd(int a, int b) {
        Stack<int[]> list = new Stack();
        int[] t = new int[]{a, b};
        list.push(t);

        while(a != 1 && b != 1) {
            int[] temp = new int[2];
            if (a < b) {
                temp[0] = a;
                temp[1] = Math.floorMod(b, a);
                list.push(temp);
            } else {
                temp[0] = Math.floorMod(a, b);
                temp[1] = b;
                list.push(temp);
            }

            a = temp[0];
            b = temp[1];
        }

        Iterator<int[]> iter = list.iterator();
        int x = 0;
        int y = 0;
        int f;
        int[] temp;
        if (b == 1) {
            x = 1;

            for(f = 1; iter.hasNext(); ++f) {
                temp = (int[])list.pop();
                if ((f & 1) == 1) {
                    y = (temp[0] * x - 1) / temp[1];
                    x = (temp[1] * y + 1) / temp[0];
                } else {
                    x = (temp[1] * y + 1) / temp[0];
                    y = (temp[0] * x - 1) / temp[1];
                }
            }
        }

        if (a == 1) {
            y = 0;

            for(f = 1; iter.hasNext(); ++f) {
                temp = (int[])list.pop();
                if ((f & 1) == 0) {
                    y = (temp[0] * x - 1) / temp[1];
                    x = (temp[1] * y + 1) / temp[0];
                } else {
                    x = (temp[1] * y + 1) / temp[0];
                    y = (temp[0] * x - 1) / temp[1];
                }
            }
        }

        return x;
    }

    public int Algorithm(int data, int e, int n) {
        String result = Integer.toBinaryString(e);
        result = (new StringBuffer(result)).reverse().toString();
        char[] r = result.toCharArray();
        int[] a = new int[result.length()];
        int output = 1;

        for(int i = 0; i < result.length(); ++i) {
            a[i] = data % n;
            int tag = n - a[i];
            if (tag < a[i]) {
                a[i] = 0 - tag;
            }

            data = a[i] * a[i];
            if (r[i] != '0') {
                output *= a[i];
                if (output > n || output < -n) {
                    output %= n;
                }
            }
        }

        output %= n;
        if (output < 0) {
            output += n;
        }

        return output;
    }


    public clientui() {
        this.n = this.p * this.q;
        this.jn = (this.p - 1) * (this.q - 1);
        this.d = exgcd(this.e, this.jn);
        this.kctgs = "";
        this.TGS = "127.0.0.1";
        this.result = null;
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 450, 300);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager)null);
        JButton btnNewButton = new JButton("注册");
        btnNewButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                final JFrame frame1 = new JFrame("新窗口");
                frame1.setLocation(100, 50);
                frame1.setSize(400, 300);
                JLabel lblNewLabel = new JLabel("账号");
                lblNewLabel.setBounds(91, 58, 58, 15);
                clientui.this.contentPane.add(lblNewLabel);
                JLabel lblNewLabel_1 = new JLabel("密码");
                lblNewLabel_1.setBounds(91, 113, 58, 15);
                clientui.this.contentPane.add(lblNewLabel_1);
                final TextField textField1 = new TextField();
                textField1.setBounds(129, 55, 203, 18);
                textField1.addFocusListener(clientui.this.new TextFieldHintListener(textField1, "请输入长度为9位的账号"));
                frame1.getContentPane().add(textField1);
                final TextField textField2 = new TextField();
                textField2.setBounds(132, 110, 200, 18);
                textField2.addFocusListener(clientui.this.new TextFieldHintListener(textField2, "密码长度位6-16位数字或英语字母"));
                frame1.getContentPane().add(textField2);
                JButton button1 = new JButton("取消");
                button1.setBounds(70, 180, 90, 25);
                button1.setBorderPainted(false);
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame1.setVisible(false);
                    }
                });
                JButton button2 = new JButton("注册");
                button2.setBounds(270, 180, 90, 25);
                button2.setBorderPainted(false);
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String account = textField1.getText().toString();
                        String password = textField2.getText().toString();
                        if (account.length() == 9 && password.length() < 16 && password.length() > 6) {
                            String data = null;
                            if (password.length() >= 10) {
                                data = 0 + account + password.length() + password;
                            }

                            if (password.length() < 10) {
                                data = 0 + account + 0 + password.length() + password;
                            }

                            System.out.println("reginst" + data);
                            String result = "";

                            try {
                                result = registersocket.register("192.168.43.82", 55533, data, account);
                            } catch (IOException var9) {
                                var9.printStackTrace();
                            }

                            JFrame framex;
                            JLabel lblNewLabe3;
                            if (result.equals("1")) {
                                data = 1 + data.substring(1, 10) + clientui.this.n + 65537;

                                try {
                                    result = registersocket.register("192.168.43.82", 55533, data, account);
                                } catch (IOException var8) {
                                    var8.printStackTrace();
                                }

                                if (result.equals("1")) {
                                    frame1.setVisible(false);
                                    framex = new JFrame("新窗口");
                                    framex.setLocation(200, 50);
                                    framex.setSize(100, 100);
                                    lblNewLabe3 = new JLabel("注册成功");
                                    lblNewLabe3.setBounds(91, 58, 58, 15);
                                    clientui.this.contentPane.add(lblNewLabe3);
                                    framex.add(lblNewLabe3);
                                    framex.setVisible(true);
                                } else {
                                    textField1.addFocusListener(clientui.this.new TextFieldHintListener(textField1, "请输入长度为9位的账号"));
                                    textField2.addFocusListener(clientui.this.new TextFieldHintListener(textField2, "密码长度位6-16位数字或英语字母"));
                                    framex = new JFrame("新窗口");
                                    framex.setLocation(200, 50);
                                    framex.setSize(100, 100);
                                    lblNewLabe3 = new JLabel("注册失败");
                                    lblNewLabe3.setBounds(91, 58, 58, 15);
                                    clientui.this.contentPane.add(lblNewLabe3);
                                    framex.add(lblNewLabe3);
                                    framex.setVisible(true);
                                }
                            } else {
                                textField1.addFocusListener(clientui.this.new TextFieldHintListener(textField1, "请输入长度为9位的账号"));
                                textField2.addFocusListener(clientui.this.new TextFieldHintListener(textField2, "密码长度位6-16位数字或英语字母"));
                                framex = new JFrame("新窗口");
                                framex.setLocation(200, 50);
                                framex.setSize(100, 100);
                                lblNewLabe3 = new JLabel("注册失败");
                                lblNewLabe3.setBounds(91, 58, 58, 15);
                                clientui.this.contentPane.add(lblNewLabe3);
                                framex.add(lblNewLabe3);
                                framex.setVisible(true);
                            }
                        } else {
                            textField1.addFocusListener(clientui.this.new TextFieldHintListener(textField1, "请输入长度为9位的账号"));
                            textField2.addFocusListener(clientui.this.new TextFieldHintListener(textField2, "密码长度位6-16位数字或英语字母"));
                            JFrame frame = new JFrame("新窗口");
                            frame.setLocation(200, 50);
                            frame.setSize(100, 100);
                            JLabel lblNewLabe4 = new JLabel("注册失败");
                            lblNewLabe4.setBounds(91, 58, 58, 15);
                            clientui.this.contentPane.add(lblNewLabe4);
                            frame.add(lblNewLabe4);
                            frame.setVisible(true);
                        }

                        System.out.println(account);
                        System.out.println(password);
                    }
                });
                Panel pan = new Panel();
                pan.setSize(100, 100);
                frame1.getContentPane().add(lblNewLabel);
                frame1.getContentPane().add(lblNewLabel_1);
                frame1.add(button1);
                frame1.add(button2);
                frame1.getContentPane().add(pan);
                frame1.setVisible(true);
            }
        });
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(79, 196, 97, 23);
        this.contentPane.add(btnNewButton);
        JButton btnNewButton_1 = new JButton("登录");
        btnNewButton_1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                final String account = clientui.this.textField.getText().toString();
                String password = clientui.this.textField_1.getText().toString();
                JFrame frame2 = new JFrame("新窗口");
                frame2.setLocation(100, 50);
                frame2.setSize(600, 500);
                JTextArea textField1 = new JTextArea();
                textField1.setBounds(40, 40, 500, 250);
                textField1.setLineWrap(true);
                frame2.getContentPane().add(textField1);
                final TextField textField2 = new TextField();
                textField2.setBounds(40, 310, 500, 80);
                frame2.getContentPane().add(textField2);
                Long startTs = System.currentTimeMillis();
                String data = 2 + account + 1 + startTs + password;

                try {
                    clientui.this.result = registersocket.register("192.168.43.82", 55533, data, account);
                    if (clientui.this.result.equals("0")) {
                        textField1.append("认证失败");
                    } else {
                        System.out.println("从as服务器获得票据" + clientui.this.result);
                        textField1.append("从as服务器获得票据" + clientui.this.result);
                        String asmesage = "";

                        int i;
                        String eauthc;
                        for(i = 0; i < 125; ++i) {
                            eauthc = clientui.this.result.substring(i * 5, i * 5 + 5);
                            int edata = Integer.parseInt(eauthc);
                            int ereslut = clientui.this.Algorithm(edata, clientui.this.d, clientui.this.n);

                            String s;
                            int j;
                            for(s = ""; ereslut > 0; ereslut /= 2) {
                                j = ereslut % 2;
                                s = j + s;
                            }

                            if (i == 124) {
                                for(j = 12 - s.length(); j > 0; --j) {
                                    s = "0" + s;
                                    System.out.println("s  " + s);
                                }
                            } else {
                                for(j = 13 - s.length(); j > 0; --j) {
                                    s = "0" + s;
                                }
                            }

                            asmesage = asmesage + s;
                        }

                        for(i = 0; i < 64; ++i) {
                            char pre = clientui.asciitochar(asmesage.substring(i * 8, i * 8 + 8));
                            clientui var10000 = clientui.this;
                            var10000.kctgs = var10000.kctgs + String.valueOf(pre);
                        }

                        clientui.this.Tickettgs = asmesage.substring(728);
                        System.out.println("kctgs" + clientui.this.kctgs);
                        System.out.println("TicketTgs  " + clientui.this.Tickettgs.length() + "  " + clientui.this.Tickettgs);
                        Long startTs3 = System.currentTimeMillis();
                        clientui.this.ts3 = "" + startTs3;
                        eauthc = account + "127127127127" + clientui.this.ts3;
                        System.out.println(eauthc.length() + "   eauthc  " + eauthc);
                        System.out.println(eauthc.length() + "   eauthc  " + eauthc);
                        System.out.println(clientui.this.kctgs.length() + "   kctgs  " + clientui.this.kctgs);
                        DesEncrypt Desencrypt = new DesEncrypt(eauthc, clientui.this.kctgs);
                        Desencrypt.encrypt();
                        String Authc = Desencrypt.ciphertexts;
                        data = 31 + clientui.this.Tickettgs + Authc;
                        System.out.println(data + "   data  " + data.length());
                        clientui.this.result = registersocket.register("192.168.43.180", 55533, data, account);
                        System.out.println(clientui.this.result);
                        if (clientui.this.result.equals("0")) {
                            textField1.append("TGS认证失败");
                        } else {
                            textField1.append("从TGS服务器获得票据" + clientui.this.result);
                            System.out.println(clientui.this.result.length() + "   result  " + clientui.this.result);
                            DesDecrypt DesDecrypt = new DesDecrypt(clientui.this.result, clientui.this.kctgs);
                            DesDecrypt.decrypt();
                            String tgs_c = DesDecrypt.message;
                            String Kcv = tgs_c.substring(0, 64);
                            String idv = tgs_c.substring(64, 65);
                            String TS4 = tgs_c.substring(65, 78);
                            String Ticketv = tgs_c.substring(78);
                            System.out.println(Kcv.length() + "   Kcv  " + Kcv);
                            System.out.println(idv.length() + "   idv  " + idv);
                            System.out.println(TS4.length() + "   TS4  " + TS4);
                            System.out.println(Ticketv.length() + "   Ticketv  " + Ticketv);
                            Long startTs4 = System.currentTimeMillis();
                            clientui.this.ts5 = "" + startTs4;
                            String eauthc1 = account + "127127127127" + clientui.this.ts5;
                            DesEncrypt Desencrypt1 = new DesEncrypt(eauthc1, clientui.this.kctgs);
                            Desencrypt1.encrypt();
                            String Authc1 = Desencrypt1.ciphertexts;
                            data = 4 + Ticketv + Authc1;
                            System.out.println(data + "   data  " + data.length());
                            clientui.this.result = registersocket.register("192.168.43.218", 55533, data, account);
                            System.out.println(clientui.this.result);
                            if (!clientui.this.result.equals("0")) {
                                DesDecrypt DesDecrypt1 = new DesDecrypt(clientui.this.result, Kcv);
                                DesDecrypt1.decrypt();
                                String v_c = DesDecrypt1.message;
                                System.out.println("v_c   " + v_c);
                                JButton button2 = new JButton("发送");
                                button2.setBounds(440, 400, 80, 30);
                                button2.setBorderPainted(false);
                                button2.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            String data = textField2.getText();
                                            clientui.this.result = registersocket.register("192.168.43.218", 55533, data, account);
                                        } catch (IOException var3) {
                                            var3.printStackTrace();
                                        }

                                    }
                                });
                                frame2.add(button2);

                                while(true) {
                                    clientui.this.result = registersocket.register("192.168.43.218", 55533, "d", account);
                                    textField1.append("从TGS服务器获得票据" + clientui.this.result);
                                }
                            }

                            textField1.append("认证失败");
                        }
                    }
                } catch (IOException var27) {
                    var27.printStackTrace();
                }

                JButton button1 = new JButton("取消");
                button1.setBounds(40, 400, 80, 30);
                button1.setBorderPainted(false);
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                frame2.add(button1);
                Panel pan = new Panel();
                pan.setSize(100, 100);
                frame2.getContentPane().add(pan);
                frame2.setVisible(true);
                System.out.println(account);
                System.out.println(password);
            }
        });
        btnNewButton_1.setBounds(289, 196, 97, 23);
        this.contentPane.add(btnNewButton_1);
        JLabel lblNewLabel = new JLabel("账号");
        lblNewLabel.setBounds(91, 58, 58, 15);
        this.contentPane.add(lblNewLabel);
        JLabel lblNewLabel_1 = new JLabel("密码");
        lblNewLabel_1.setBounds(91, 113, 58, 15);
        this.contentPane.add(lblNewLabel_1);
        this.textField = new JTextField();
        this.textField.setBounds(129, 55, 203, 18);
        this.contentPane.add(this.textField);
        this.textField.setColumns(10);
        this.textField_1 = new JTextField();
        this.textField_1.setBounds(132, 110, 200, 18);
        this.contentPane.add(this.textField_1);
        this.textField_1.setColumns(10);
    }

    public static char asciitochar(String s) {
        char c = 0;
        System.out.println(s);
        char[] S = s.toCharArray();
        c = (char)((S[0] - 48) * 128 + (S[1] - 48) * 64 + (S[2] - 48) * 32 + (S[3] - 48) * 16 + (S[4] - 48) * 8 + (S[5] - 48) * 4 + (S[6] - 48) * 2 + (S[7] - 48) * 1);
        System.out.println("c   " + c);
        return c;
    }
    public class TextFieldHintListener implements FocusListener {
        private String hintText;
        private TextField textField;

        public TextFieldHintListener(TextField TextField, String hintText) {
            this.textField = TextField;
            this.hintText = hintText;
            TextField.setText(hintText);
        }

        public void focusGained(FocusEvent e) {
            String temp = this.textField.getText();
            if (temp.equals(this.hintText)) {
                this.textField.setText("");
            }

        }

        public void focusLost(FocusEvent e) {
            String temp = this.textField.getText();
            if (temp.equals("")) {
                this.textField.setText(this.hintText);
            }

        }
    }
}
