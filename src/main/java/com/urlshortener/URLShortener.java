package com.urlshortener;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.urlshortener.resources.ShortenResource;
import com.urlshortener.resources.RouteResource;
import com.urlshortener.health.JedisHealthCheck;
import com.urlshortener.util.JedisHelper;

import redis.clients.jedis.JedisPool;

public class URLShortener extends Application<URLShortenerConfiguration>{
    public static void main(String[] args) throws Exception {
        new URLShortener().run(args);
    }

    @Override
    public void initialize(Bootstrap<URLShortenerConfiguration> bootstrap){}

    @Override
    public void run(URLShortenerConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {

        JedisPool pool = configuration.getJedisManaged().build(environment);

        JedisHelper.setPool(pool);

        environment.jersey().register(new ShortenResource());
        environment.jersey().register(new RouteResource());

        environment.healthChecks().register("redis-health", new JedisHealthCheck(pool));
    }
}