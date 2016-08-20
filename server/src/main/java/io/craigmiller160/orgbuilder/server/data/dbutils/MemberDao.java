package io.craigmiller160.orgbuilder.server.data.dbutils;

import io.craigmiller160.orgbuilder.server.dto.MemberDTO;

import java.util.List;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractDbUtilsDao<MemberDTO,Long> {

    @Override
    public Class<MemberDTO> getEntityType() {
        return null;
    }

    @Override
    public MemberDTO insert(MemberDTO element) {
        return null;
    }

    @Override
    public List<MemberDTO> insertAll(List<MemberDTO> elements) {
        return null;
    }

    @Override
    public MemberDTO update(MemberDTO element) {
        return null;
    }

    @Override
    public List<MemberDTO> updateAll(List<MemberDTO> elements) {
        return null;
    }

    @Override
    public MemberDTO delete(Long id) {
        return null;
    }

    @Override
    public List<MemberDTO> deleteAll(List<Long> ids) {
        return null;
    }

    @Override
    public MemberDTO get(Long id) {
        return null;
    }

    @Override
    public MemberDTO getWithJoins(Long id) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public List<MemberDTO> getAll() {
        return null;
    }

    @Override
    public List<MemberDTO> getAllWithJoins() {
        return null;
    }

    @Override
    public List<MemberDTO> getAll(int offset, int limit) {
        return null;
    }

    @Override
    public List<MemberDTO> getAllWithJoins(int offset, int limit) {
        return null;
    }
}
