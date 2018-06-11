package com.etermax.spacehorse.core.status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.configuration.NewRelicIgnoreTransaction;

@Path("/v1")
@Api(value = "/status", tags = "Status")
public class StatusResource {

    @GET
    @Path("/status/ping")
    @ApiOperation("Checks if the server is up")
    @NewRelicIgnoreTransaction
    public Response ping() {
        return Response.ok().build();
    }

}
