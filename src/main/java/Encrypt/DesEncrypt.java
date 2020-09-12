package Encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DesEncrypt {
    private String key = "";
    public char[][] keybox = new char[16][48];
    private String message = "";
    public String ciphertexts = "";
    public int[] IP = new int[64];
    public int[] PC_1 = new int[56];
    static int[] RoundTable = new int[16];
    static int[] PC_2 = new int[48];
    static int[] E = new int[48];
    static int[][][] S = new int[8][4][16];
    static int[] P = new int[32];
    public int[] IP_1 = new int[64];

    public DesEncrypt() {
    }

    public DesEncrypt(String message, String keystr) {
        this.key = keystr;
        this.message = message;
    }

    public static void tableprepare(String filename, int[] table) throws IOException {
        String line = "";
        int len = table.length;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        line = reader.readLine();
        String[] words = line.split(", ");

        for(int i = 0; i < len; ++i) {
            table[i] = Integer.parseInt(words[i]);
        }

    }

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

    public static void replacement(char[] oldchar, char[] newchar, int[] table) {
        for(int i = 0; i < newchar.length; ++i) {
            newchar[i] = oldchar[table[i] - 1];
        }

    }

    public static void split(char[] left, char[] right, char[] origin) {
        int len = origin.length;

        for(int i = 0; i < len; ++i) {
            if (i < len / 2) {
                left[i] = origin[i];
            } else {
                right[i - len / 2] = origin[i];
            }
        }

    }

    public static String desshift(char[] C, char[] D, int round) {
        int rotations = RoundTable[round];
        int len = C.length;
        String tmp = null;
        char[] tmp1 = new char[len];
        char[] tmp2 = new char[len];

        int i;
        for(i = 0; i < len; ++i) {
            tmp1[i] = C[(i + rotations) % len];
            tmp2[i] = D[(i + rotations) % len];
        }

        for(i = 0; i < C.length; ++i) {
            C[i] = tmp1[i];
            D[i] = tmp2[i];
        }

        tmp = String.valueOf(tmp1) + String.valueOf(tmp2);
        return tmp;
    }

    public char[][] createkey(char[] key0) {
        char[][] keytable = new char[16][48];
        char[] key1 = new char[56];
        replacement(key0, key1, this.PC_1);
        char[] C = new char[28];
        char[] D = new char[28];
        split(C, D, key1);
        String temp = null;

        for(int i = 0; i < 16; ++i) {
            temp = desshift(C, D, i);
            replacement(temp.toCharArray(), keytable[i], PC_2);
        }

        return keytable;
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

    public static String[] messagesplit(String messages) {
        int len = messages.length();
        int section = len / 8;
        if (len % 8 != 0) {
            ++section;
        }

        String[] messagebox = new String[section];
        char[] words = messages.toCharArray();
        StringBuilder m = new StringBuilder(64);
        String wordstr = "";

        for(int i = 0; i < 8 * section; ++i) {
            if (i <= len - 1) {
                wordstr = chartoascii(words[i]);
                m.append(wordstr);
                if ((i + 1) % 8 == 0) {
                    messagebox[i / 8] = m.toString();
                    m.delete(0, 64);
                }
            } else {
                wordstr = "00000000";
                m.append(wordstr);
                if ((i + 1) % 8 == 0) {
                    messagebox[i / 8] = m.toString();
                    m.delete(0, 64);
                }
            }
        }

        return messagebox;
    }

    public static char[] xor(char[] messageRE, char[] k, char[] messageRExor) {
        int len = messageRE.length;

        for(int i = 0; i < len; ++i) {
            messageRExor[i] = String.valueOf(messageRE[i] ^ k[i]).toCharArray()[0];
        }

        return messageRExor;
    }

    public static void replacementS(char[] oldmessage, char[] newmessage) {
        char[] half1 = new char[24];
        char[] half2 = new char[24];
        split(half1, half2, oldmessage);
        char[] quarter1 = new char[12];
        char[] quarter2 = new char[12];
        char[] quarter3 = new char[12];
        char[] quarter4 = new char[12];
        split(quarter1, quarter2, half1);
        split(quarter3, quarter4, half2);
        char[][] messagepiece = new char[8][6];
        split(messagepiece[0], messagepiece[1], quarter1);
        split(messagepiece[2], messagepiece[3], quarter2);
        split(messagepiece[4], messagepiece[5], quarter3);
        split(messagepiece[6], messagepiece[7], quarter4);
        String numpiece = "";

        for(int i = 0; i < 8; ++i) {
            int x = (messagepiece[i][0] - 48) * 2 + (messagepiece[i][5] - 48);
            int y = (messagepiece[i][1] - 48) * 8 + (messagepiece[i][2] - 48) * 4 + (messagepiece[i][3] - 48) * 2 + (messagepiece[i][4] - 48);
            int t = S[i][x][y];
            String numstr = "";
            numstr = decimaltoBinary(t, 1);
            numpiece = numpiece + numstr;
        }

        System.arraycopy(numpiece.toCharArray(), 0, newmessage, 0, newmessage.length);
    }

    public static void Feistel(char[] messageL, char[] messageR, char[][] keybox, int flag) {
        char[] temp = new char[32];
        char[] messageRExor = new char[48];
        char[] messageRExorS = new char[32];
        char[] messageRExorSP = new char[32];
        char[] k = new char[48];

        for(int i = 0; i < 16; ++i) {
            temp = (char[])messageR.clone();
            if (flag == 0) {
                k = keybox[i];
            } else if (flag == 1) {
                k = keybox[(15 - i) % 16];
            }

            char[] messageRE = new char[48];
            replacement(messageR, messageRE, E);
            xor(messageRE, k, messageRExor);
            replacementS(messageRExor, messageRExorS);
            replacement(messageRExorS, messageRExorSP, P);
            xor(messageRExorSP, messageL, messageR);
            System.arraycopy(temp, 0, messageL, 0, 32);
        }

    }

    public static void Stableprepare(String filename, int[][][] table) throws IOException {
        String line = "";
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 4; ++j) {
                int len = table[i][j].length;
                line = reader.readLine();
                String[] words = line.split(", ");

                for(int n = 0; n < len; ++n) {
                    table[i][j][n] = Integer.parseInt(words[n]);
                }
            }
        }

    }

    public void init() throws IOException {
        tableprepare("src/main/resources/E.txt", E);
        tableprepare("src/main/resources/IP.txt", this.IP);
        tableprepare("src/main/resources/IP_1.txt", this.IP_1);
        tableprepare("src/main/resources/P.txt", P);
        tableprepare("src/main/resources/PC_1.txt", this.PC_1);
        tableprepare("src/main/resources/PC_2.txt", PC_2);
        tableprepare("src/main/resources/RoundTable.txt", RoundTable);
        Stableprepare("src/main/resources/S.txt", S);
    }

    public void encrypt() throws IOException {
        char[] ciphertext = new char[64];
        char[] messageIP = new char[64];
        char[] messageL = new char[32];
        char[] messageR = new char[32];
        String[] keys = null;
        String messagef = null;
        this.init();
        keys = messagesplit(this.key);
        this.keybox = this.createkey(keys[0].toCharArray());
        String[] messagebox = messagesplit(this.message);
        String[] var11 = messagebox;
        int var10 = messagebox.length;

        for(int var9 = 0; var9 < var10; ++var9) {
            String m = var11[var9];
            replacement(m.toCharArray(), messageIP, this.IP);
            split(messageL, messageR, messageIP);
            Feistel(messageL, messageR, this.keybox, 0);
            messagef = String.valueOf(messageR) + String.valueOf(messageL);
            replacement(messagef.toCharArray(), ciphertext, this.IP_1);
            this.ciphertexts = this.ciphertexts + String.valueOf(ciphertext);
        }

    }
}
