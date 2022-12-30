/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.model.source;

import javax.lang.model.element.ExecutableElement;

import org.mapstruct.SubclassMappingInheritanceStrategy;
import org.mapstruct.ap.internal.gem.InheritConfigurationGem;
import org.mapstruct.ap.internal.gem.InheritInverseConfigurationGem;

/**
 * @author Ben Zegveld
 */
public class InheritConfigurationOptions {

    private SubclassMappingInheritanceStrategy normalStrategy;
    private SubclassMappingInheritanceStrategy inverseStrategy;

    public InheritConfigurationOptions(SubclassMappingInheritanceStrategy normalStrategy,
                                       SubclassMappingInheritanceStrategy inverseStrategy) {
        this.normalStrategy = normalStrategy;
        this.inverseStrategy = inverseStrategy;
    }

    public static InheritConfigurationOptions getInstanceOn(ExecutableElement method) {
        InheritConfigurationGem inheritConfigurationGem = InheritConfigurationGem.instanceOn( method );
        InheritInverseConfigurationGem inheritInverseConfigurationGem =
                        InheritInverseConfigurationGem.instanceOn( method );
        if ( inheritConfigurationGem != null ) {
            SubclassMappingInheritanceStrategy.valueOf(
                                                  inheritConfigurationGem.subclassMappingInheritanceStrategy().get() );
        }
        if ( inheritInverseConfigurationGem != null ) {
            SubclassMappingInheritanceStrategy.valueOf(
                                           inheritInverseConfigurationGem.subclassMappingInheritanceStrategy().get() );
        }
        return new InheritConfigurationOptions(
            inheritConfigurationGem != null ?
                            SubclassMappingInheritanceStrategy.valueOf(
                                inheritConfigurationGem.subclassMappingInheritanceStrategy().get() ) : null,
            inheritInverseConfigurationGem != null ?
                            SubclassMappingInheritanceStrategy.valueOf(
                                inheritInverseConfigurationGem.subclassMappingInheritanceStrategy().get() ) : null
                        );
    }

    public SubclassMappingInheritanceStrategy getNormalStrategy() {
        return normalStrategy;
    }

    public SubclassMappingInheritanceStrategy getInverseStrategy() {
        return inverseStrategy;
    }
}
