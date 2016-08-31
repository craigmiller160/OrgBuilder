package io.craigmiller160.orgbuilder.server.dto.converter;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.util.DataDTOMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 8/28/16.
 */
public class DTOSQLConverterFactory {

    public static DTOSQLConverterFactory newInstance(){
        return new DTOSQLConverterFactory();
    }

    private final Map<Class,DataDTOMap> dataDtoMap;

    private DTOSQLConverterFactory(){
        this.dataDtoMap = ServerCore.getDataDTOMap();
    }

    public <E> DTOSQLConverter<E> getDTOSQLConverter(Class<E> dtoType){
        DataDTOMap<E> mapping = dataDtoMap.get(dtoType);
        if(mapping == null){
            throw new IllegalArgumentException("No Data DTO mapping exists for provided DTO class type. DTO Type: " + dtoType.getName());
        }
        return mapping.getDTOSQLConverter();
    }

}
