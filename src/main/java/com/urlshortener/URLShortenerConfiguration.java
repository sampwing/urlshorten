package com.urlshortener;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import com.urlshortener.client.JedisPoolFactory;

public class URLShortenerConfiguration extends Configuration {

    private JedisPoolFactory jedisPoolFactory = new JedisPoolFactory();

    @JsonProperty("redis")
    public JedisPoolFactory getJedisManaged(){
        return jedisPoolFactory;
    }

    @JsonProperty("redis")
    public void setJedisManaged(JedisPoolFactory factory) {
        this.jedisPoolFactory = factory;
    }

}