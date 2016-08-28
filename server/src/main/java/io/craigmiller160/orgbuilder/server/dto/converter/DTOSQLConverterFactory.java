package io.craigmiller160.orgbuilder.server.dto.converter;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/28/16.
 */
public class DTOSQLConverterFactory {

    private static final Map<Class,DTOSQLConverter> converterMap;

    //TODO find a way to combine this map with the entity/dao map, to reduce mapping code
    static{
        Map<Class<?>,DTOSQLConverter<?>> map = new HashMap<>();
        map.put(MemberDTO.class, new MemberDTOSQLConverter());
        map.put(AddressDTO.class, new AddressDTOSQLConverter());
        map.put(EmailDTO.class, new EmailDTOSQLConverter());
        map.put(OrgDTO.class, new OrgDTOSQLConverter());
        map.put(PhoneDTO.class, new PhoneDTOSQLConverter());

        converterMap = Collections.unmodifiableMap(map);
    }

    public static DTOSQLConverterFactory newInstance(){
        return new DTOSQLConverterFactory();
    }

    private DTOSQLConverterFactory(){}

    public <E> DTOSQLConverter<E> getDTOSQLConverter(Class<E> dtoType){
        return converterMap.get(dtoType);
    }

}
