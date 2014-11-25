package com.urlshortener.health;

import com.codahale.metrics.health.HealthCheck;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisHealthCheck extends HealthCheck{

    private final JedisPool pool;

    public JedisHealthCheck(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    protected Result check() throws Exception {
        try (Jedis jedis = pool.getResource()) {
            return Result.healthy();
        } catch (JedisConnectionException e) {
            return Result.unhealthy("Unable to grab a redis connection from the pool.");
        } catch (Exception e) {
            return Result.unhealthy(e.toString());
        }
    }

}
