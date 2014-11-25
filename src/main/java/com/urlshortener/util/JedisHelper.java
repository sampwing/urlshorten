package com.urlshortener.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHelper {

    public static final String HITS = "hits:";

    public static final String AUTHORITY = "auth:";

    public static final String GETORMAKEID = "" +
            "local link_id = redis.call('GET', ARGV[1])\n" +
            "if not link_id then\n" +
            "  link_id = redis.call('INCR', ':total:')\n" +
            "  redis.call('SET', ARGV[1], link_id)\n" +
            "  redis.call('SET', link_id, ARGV[1])\n" +
            "end\n" +
            "return link_id\n";

    private static JedisPool jedisPool;

    public static JedisPool getPool() { return jedisPool; }

    public static void setPool(JedisPool pool) { jedisPool = pool; }

    public static Jedis getResource() { return jedisPool.getResource(); }
}
