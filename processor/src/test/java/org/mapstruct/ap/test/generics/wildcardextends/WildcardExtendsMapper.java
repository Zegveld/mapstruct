/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.generics.wildcardextends;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Ben Zegveld
 */
@Mapper
public abstract class WildcardExtendsMapper {
    public static final WildcardExtendsMapper INSTANCE = Mappers.getMapper( WildcardExtendsMapper.class );

    @Mapping( target = "object", source = "object" )
    public abstract Target map(Source<?> action);

    String mapToString(WildcardedInterface rawData) {
        return rawData.getContents();
    }
}
