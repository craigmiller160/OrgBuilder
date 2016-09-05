package io.craigmiller160.orgbuilder.server.dto;

/**
 * Created by craig on 9/4/16.
 */
public interface DTO<I> {

    I getElementId();

    void setElementId(I id);

}
