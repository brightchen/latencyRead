package com.example;

import redis.clients.jedis.Jedis;

/**
 * Created by sandesh on 2/24/16.
 */
public class RedisHelper {

    private String host ;
    private Jedis jedis ;

    public void init( String host ) {
        this.host = host ;
        jedis = new Jedis(host) ;
    }

    public void clear( ) {
        jedis.flushDB() ;
    }


}
