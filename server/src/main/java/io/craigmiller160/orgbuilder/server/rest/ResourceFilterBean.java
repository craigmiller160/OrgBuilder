package io.craigmiller160.orgbuilder.server.rest;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * Created by craigmiller on 9/25/16.
 */
public class ResourceFilterBean {

    @ApiParam(value = "The optional offset value to use for pagination")
    @QueryParam(QueryParamName.OFFSET)
    @DefaultValue("-1")
    private long offset;

    @ApiParam(value = "The optional size value to use for pagination")
    @QueryParam(QueryParamName.SIZE)
    @DefaultValue("-1")
    private long size;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void validateFilterParams() throws OrgApiInvalidRequestException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }
    }
}
