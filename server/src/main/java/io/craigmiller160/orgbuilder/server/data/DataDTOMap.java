package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.data.jdbc.AddressDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.EmailDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.MemberDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.OrgDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.PhoneDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.RefreshTokenDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.UserDao;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.RefreshTokenDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.UserDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.AddressDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.EmailDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.MemberDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.OrgDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.PhoneDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/30/16.
 */
public class DataDTOMap<T> {

    public static Map<Class,DataDTOMap> generateDataDTOMap(){
        Map<Class,DataDTOMap> map = new HashMap<>();
        map.put(MemberDTO.class, new DataDTOMap<>(MemberDTO.class, new MemberDTOSQLConverter(), MemberDao.class));
        map.put(AddressDTO.class, new DataDTOMap<>(AddressDTO.class, new AddressDTOSQLConverter(), AddressDao.class));
        map.put(EmailDTO.class, new DataDTOMap<>(EmailDTO.class, new EmailDTOSQLConverter(), EmailDao.class));
        map.put(OrgDTO.class, new DataDTOMap<>(OrgDTO.class, new OrgDTOSQLConverter(), OrgDao.class));
        map.put(PhoneDTO.class, new DataDTOMap<>(PhoneDTO.class, new PhoneDTOSQLConverter(), PhoneDao.class));
        map.put(UserDTO.class, new DataDTOMap<>(UserDTO.class, new UserDTOSQLConverter(), UserDao.class));
        map.put(RefreshTokenDTO.class, new DataDTOMap<>(RefreshTokenDTO.class, new RefreshTokenDTOSQLConverter(), RefreshTokenDao.class));

        return Collections.unmodifiableMap(map);
    }

    private final DTOSQLConverter<T> converter;
    private final Class<T> dtoType;
    private final Class<? extends Dao<T,?>> daoType;

    private DataDTOMap(Class<T> dtoType, DTOSQLConverter<T> converter, Class<? extends Dao<T,?>> daoType){
        this.dtoType = dtoType;
        this.converter = converter;
        this.daoType = daoType;
    }

    public Class<T> getDTOType(){
        return dtoType;
    }

    public DTOSQLConverter<T> getDTOSQLConverter(){
        return converter;
    }

    public Class<? extends Dao<T,?>> getDaoType(){
        return daoType;
    }

}
