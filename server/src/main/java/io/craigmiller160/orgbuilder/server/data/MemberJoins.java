package io.craigmiller160.orgbuilder.server.data;

import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public interface MemberJoins<E,I> {

    List<E> getAllByMember(I id) throws OrgApiDataException;

    List<E> getAllByMember(I id, long offset, long size) throws OrgApiDataException;

    long getCountByMember(I id) throws OrgApiDataException;

}
