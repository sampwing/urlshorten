package com.urlshortener.resources;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import org.apache.commons.lang.StringUtils;
import com.urlshortener.core.Stats;
import com.urlshortener.util.JedisHelper;

@Path("/route/{route}")
@Produces(MediaType.APPLICATION_JSON)
public class RouteResource {

    @GET
    @Timed
    public Response redirect(@PathParam("route") String route,
                             @Context HttpHeaders headers) {
        MultivaluedMap<String, String> map = headers.getRequestHeaders();
        for(Object value: map.values()) {
            System.out.println(value);
        }
        System.out.println(headers.toString());
        try (Jedis jedis = JedisHelper.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.incr(JedisHelper.HITS + route);
            System.out.println(route);
            redis.clients.jedis.Response<String> rep = pipeline.get(route);
            pipeline.sync();
            // check if empty and handle correctly
            String url = Preconditions.checkNotNull(rep.get());
            url = StringUtils.substringAfter(url, JedisHelper.AUTHORITY);
            UriBuilder builder = UriBuilder.fromPath("{arg1}");
            URI uri = builder.build(url);
            System.out.println(url);
            return Response
                    .temporaryRedirect(uri)
                    .build();

        } catch (NullPointerException e) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
        } catch (Exception e) {
                return Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .build();
        }
    }

    @GET
    @Timed
    @Path("/stats")
    public Response getStats(@PathParam("route") String route) {
        try (Jedis jedis = JedisHelper.getResource()) {
            String hitCount = jedis.get(JedisHelper.HITS + route);
            hitCount = Preconditions.checkNotNull(hitCount);
            int hits = Integer.parseInt(hitCount);

            Stats stats = new Stats();
            stats.setHits(hits);

            return Response
                    .ok(stats)
                    .build();

        } catch (NullPointerException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();

        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }}

}
