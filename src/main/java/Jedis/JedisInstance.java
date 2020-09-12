package Jedis;

import redis.clients.jedis.Jedis;

public class JedisInstance {
    private Jedis jedis;
    private JedisInstance(){
        jedis=new Jedis("localhost");
    }
    public static final JedisInstance INSTANCE=new JedisInstance();
    public String getPublicKey(String key){
        return jedis.lrange(key,1,1).get(0);
    }
}
