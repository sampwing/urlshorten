package com.urlshortener.resources;

import java.net.URL;
import java.net.MalformedURLException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.urlshortener.core.Error;
import com.urlshortener.util.JedisHelper;

@Path("/short")
@Produces(MediaType.APPLICATION_JSON)
public class ShortenResource{

    @POST
    @Timed
    public Response shorten(@DefaultValue("http://www.example.com") @QueryParam("url") String url) {
        try {
            URL parsedUrl = new URL(url);
            String authority = parsedUrl.getAuthority();
            Preconditions.checkNotNull(authority);
            Jedis jedis = JedisHelper.getResource();
            Object id = jedis.eval(JedisHelper.GETORMAKEID, 0, JedisHelper.AUTHORITY + url);


//            String path = parsedUrl.getPath();
//            String[] parts = StringUtils.split(path, "/");
//            if (parts.length > 0) {
//                for (int idx = 0; idx < parts.length; ++idx) {
//                    String partial = authority + "/" + StringUtils.join(parts, "/", 0, idx + 1);
////                    pipeline.incr(AUTHORITY + partial);
//                    System.out.println(partial);
//                }
//            }
//            Jedis jedis = pool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//            // add a counter for the authority
//            pipeline.incr(AUTHORITY + url);
//            pipeline.set(uuid, url);
//            pipeline.sync();
            return Response
                    .ok(id)
                    .build();

        } catch (MalformedURLException e) {
            Error error = new Error("unable to parse url");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();

        } catch (Exception e) {
            // should be able to generalize this and catch errors somewhere else that are common
            System.err.println(e.toString());
            // relating to connecting to redis etc
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
