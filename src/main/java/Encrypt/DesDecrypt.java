package Encrypt;

import java.io.IOException;

public class DesDecrypt {
    private String key = "";
    public static char[][] keybox = new char[16][48];
    public String message = "";
    public String ciphertexts = "";

    public DesDecrypt(String ciphertexts, String key) {
        this.key = key;
        this.ciphertexts = ciphertexts;
    }

    public static String[] ciphertextsplit(String ciphertext, int length) {
        int len = ciphertext.length();
        int section = len / length;
        String[] ciphertextbox = new String[section];
        int i = 0;

        for(int j = 0; i < section; j += length) {
            ciphertextbox[i] = ciphertext.substring(j, j + length);
            ++i;
        }

        return ciphertextbox;
    }

    public static char asciitochar(String s) {
        char c = 0;
        char[] S = s.toCharArray();
        c = (char)((S[0] - 48) * 128 + (S[1] - 48) * 64 + (S[2] - 48) * 32 + (S[3] - 48) * 16 + (S[4] - 48) * 8 + (S[5] - 48) * 4 + (S[6] - 48) * 2 + (S[7] - 48) * 1);
        return c;
    }

    public void decrypt() throws IOException {
        char[] ciphertextIP_1 = new char[64];
        char[] massages = new char[64];
        char[] ciphertextL = new char[32];
        char[] ciphertextR = new char[32];
        String[] keys = null;
        String messagestr = "";
        String ciphertextf = null;
        DesEncrypt desencrypt = new DesEncrypt();
        desencrypt.init();
        keys = DesEncrypt.messagesplit(this.key);
        keybox = desencrypt.createkey(keys[0].toCharArray());
        String[] ciphertextbox = ciphertextsplit(this.ciphertexts, 64);
        String[] var13 = ciphertextbox;
        int var12 = ciphertextbox.length;

        for(int var11 = 0; var11 < var12; ++var11) {
            String c = var13[var11];
            DesEncrypt.replacement(c.toCharArray(), ciphertextIP_1, desencrypt.IP);
            DesEncrypt.split(ciphertextL, ciphertextR, ciphertextIP_1);
            DesEncrypt.Feistel(ciphertextL, ciphertextR, keybox, 1);
            ciphertextf = String.valueOf(ciphertextR) + String.valueOf(ciphertextL);
            DesEncrypt.replacement(ciphertextf.toCharArray(), massages, desencrypt.IP_1);
            messagestr = messagestr + String.valueOf(massages);
        }

        String[] messagebox = ciphertextsplit(messagestr, 8);
        StringBuilder m = new StringBuilder(messagebox.length);
        String[] var15 = messagebox;
        int var14 = messagebox.length;

        for(int var19 = 0; var19 < var14; ++var19) {
            String w = var15[var19];
            if (w.equals("00000000")) {
                break;
            }

            m.append(asciitochar(w));
        }

        this.message = m.toString();
    }
}
