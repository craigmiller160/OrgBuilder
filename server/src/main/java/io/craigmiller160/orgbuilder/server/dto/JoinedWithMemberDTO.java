package io.craigmiller160.orgbuilder.server.dto;

/**
 * Created by craig on 8/28/16.
 */
public interface JoinedWithMemberDTO<I> extends DTO<I>{

    void setPreferred(boolean preferred);

    boolean isPreferred();

    void setMemberId(long memberId);

    long getMemberId();

}
