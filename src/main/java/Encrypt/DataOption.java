package Encrypt;

public class DataOption {
    static public DataOption INSTANCE=new DataOption();
    private DataOption(){key=getRandomKey(64);}
    public volatile String key="abcdefgh";
    public volatile String tgsKey="abcdefgh";
    public static String getRandomKey(int len) {
        String chars="1234567890";
        int clen = chars.length();
        String randomkey = "";
        for(int i=0;i<len;i++) {
            randomkey+=chars.charAt((int) Math.floor(Math.random()*clen));
        }
        return randomkey;
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
}
