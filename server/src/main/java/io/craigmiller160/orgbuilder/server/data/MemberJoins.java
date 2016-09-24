package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.dto.JoinedWithMemberDTO;

import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public interface MemberJoins<E extends JoinedWithMemberDTO<I>,I> {

    List<E> getAllByMember(long memberId) throws OrgApiDataException;

    List<E> getAllByMember(long memberId, long offset, long size) throws OrgApiDataException;

    long getCountByMember(long memberId) throws OrgApiDataException;

    E getPreferredForMember(long memberId) throws OrgApiDataException;

    List<E> deleteByMember(long memberId) throws OrgApiDataException;

    E getByIdAndMember(I id, long memberId) throws OrgApiDataException;

}
