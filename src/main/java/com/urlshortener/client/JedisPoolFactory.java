package com.urlshortener.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import io.dropwizard.lifecycle.Managed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolFactory {

    @NotNull
    @Size(min=1)
    private String host;

    @Min(1)
    @Max(65535)
    private int port;

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public String getHost() { return host; }

    @JsonProperty
    public int getPort() { return port; }

    public JedisPool build(Environment environment){
        final JedisPool pool = new JedisPool(new JedisPoolConfig(), this.host, this.port);
        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                // force attempt on creating a connection
                try (Jedis jedis = pool.getResource()) {}
            }

            @Override
            public void stop() throws Exception {
                pool.destroy();
            }
        });
        return pool;
    }

}
