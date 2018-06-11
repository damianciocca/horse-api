package com.etermax.spacehorse.core.status;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StatusResourceTest {

    @Test
    public void testPing() {
        StatusResource statusResource = new StatusResource();
        Response response = statusResource.ping();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

}
