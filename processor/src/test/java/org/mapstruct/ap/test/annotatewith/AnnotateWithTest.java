/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.annotatewith;

import org.mapstruct.Mapper;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.compilation.annotation.CompilationResult;
import org.mapstruct.ap.testutil.compilation.annotation.Diagnostic;
import org.mapstruct.ap.testutil.compilation.annotation.ExpectedCompilationOutcome;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ben Zegveld
 */
@IssueKey( "1574" )
public class AnnotateWithTest {

    @ProcessorTest
    @WithClasses( { DeprecateMapper.class } )
    public void mapperBecomesDeprecated() {
        DeprecateMapper mapper = Mappers.getMapper( DeprecateMapper.class );

        assertThat( mapper.getClass().getAnnotation( Deprecated.class ) ).isNotNull();
    }

    @ProcessorTest
    @WithClasses( { DeprecateAndCustomMapper.class, CustomAnnotation.class } )
    public void mapperBecomesDeprecatedAndGetsCustomAnnotation() {
        DeprecateAndCustomMapper mapper = Mappers.getMapper( DeprecateAndCustomMapper.class );

        assertThat( mapper.getClass().getAnnotation( Deprecated.class ) ).isNotNull();
        assertThat( mapper.getClass().getAnnotation( CustomAnnotation.class ) ).isNotNull();
    }

    @ProcessorTest
    @WithClasses( { CustomNamedMapper.class, CustomAnnotationWithParams.class } )
    public void annotationWithValue() {
        CustomNamedMapper mapper = Mappers.getMapper( CustomNamedMapper.class );

        CustomAnnotationWithParams annotation = mapper.getClass().getAnnotation( CustomAnnotationWithParams.class );
        assertThat( annotation ).isNotNull();
        assertThat( annotation.booleanParam() ).isEqualTo( true );
        assertThat( annotation.byteParam() ).isEqualTo( (byte) 0x13 );
        assertThat( annotation.charParam() ).isEqualTo( 'a' );
        assertThat( annotation.doubleParam() ).isEqualTo( 1.2 );
        assertThat( annotation.floatParam() ).isEqualTo( 1.2f );
        assertThat( annotation.intParam() ).isEqualTo( 1 );
        assertThat( annotation.longParam() ).isEqualTo( 1L );
        assertThat( annotation.shortParam() ).isEqualTo( (short) 1 );
        assertThat( annotation.stringParam() ).isEqualTo( "test" );
        assertThat( annotation.booleanArray() ).containsExactly( true );
        assertThat( annotation.byteArray() ).containsExactly( (byte) 0x10 );
        assertThat( annotation.charArray() ).containsExactly( 'd' );
        assertThat( annotation.doubleArray() ).containsExactly( 0.3 );
        assertThat( annotation.floatArray() ).containsExactly( 0.3f );
        assertThat( annotation.intArray() ).containsExactly( 3 );
        assertThat( annotation.longArray() ).containsExactly( 3L );
        assertThat( annotation.shortArray() ).containsExactly( (short) 3 );
        assertThat( annotation.stringArray() ).containsExactly( "test" );
        assertThat( annotation.genericTypedClass() ).isEqualTo( CustomAnnotationWithParams.class );
    }

    @ProcessorTest
    @WithClasses( { MultipleArrayValuesMapper.class, CustomAnnotationWithParams.class } )
    public void annotationWithMultipleValues() {
        MultipleArrayValuesMapper mapper = Mappers.getMapper( MultipleArrayValuesMapper.class );

        CustomAnnotationWithParams annotation = mapper.getClass().getAnnotation( CustomAnnotationWithParams.class );
        assertThat( annotation ).isNotNull();
        assertThat( annotation.booleanArray() ).containsExactly( false, true );
        assertThat( annotation.byteArray() ).containsExactly( (byte) 0x08, (byte) 0x1f );
        assertThat( annotation.charArray() ).containsExactly( 'b', 'c' );
        assertThat( annotation.doubleArray() ).containsExactly( 1.2, 3.4 );
        assertThat( annotation.floatArray() ).containsExactly( 1.2f, 3.4f );
        assertThat( annotation.intArray() ).containsExactly( 12, 34 );
        assertThat( annotation.longArray() ).containsExactly( 12L, 34L );
        assertThat( annotation.shortArray() ).containsExactly( (short) 12, (short) 34 );
        assertThat( annotation.stringArray() ).containsExactly( "test1", "test2" );
        assertThat( annotation.classArray() ).containsExactly( Mapper.class, CustomAnnotationWithParams.class );
    }

    @ProcessorTest
    @WithClasses( { CustomNamedGenericClassMapper.class, CustomAnnotationWithParams.class } )
    public void annotationWithcorrectGenericClassValue() {
        CustomNamedGenericClassMapper mapper = Mappers.getMapper( CustomNamedGenericClassMapper.class );

        CustomAnnotationWithParams annotation = mapper.getClass().getAnnotation( CustomAnnotationWithParams.class );
        assertThat( annotation ).isNotNull();
        assertThat( annotation.stringParam() ).isEqualTo( "test" );
        assertThat( annotation.genericTypedClass() ).isEqualTo( Mapper.class );
    }

    @ProcessorTest
    @WithClasses( { MetaAnnotatedMapper.class, ClassMetaAnnotation.class, CustomClassOnlyAnnotation.class } )
    public void metaAnnotationWorks() {
        MetaAnnotatedMapper mapper = Mappers.getMapper( MetaAnnotatedMapper.class );

        CustomClassOnlyAnnotation annotation = mapper.getClass().getAnnotation( CustomClassOnlyAnnotation.class );
        assertThat( annotation ).isNotNull();
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithMissingParameter.class,
                line = 16,
                message = "Parameter \"required\" is required for annotation "
                    + "\"org.mapstruct.ap.test.annotatewith.AnnotationWithRequiredParameter\"."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithMissingParameter.class, AnnotationWithRequiredParameter.class } )
    public void erroneousMapperWithMissingParameter() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithMethodOnInterface.class,
                line = 16,
                message = "Annotation \"org.mapstruct.ap.test.annotatewith.CustomMethodOnlyAnnotation\" "
                    + "is not allowed on classes."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithMethodOnInterface.class, CustomMethodOnlyAnnotation.class } )
    public void erroneousMapperWithMethodOnInterface() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithMethodOnClass.class,
                line = 16,
                message = "Annotation \"org.mapstruct.ap.test.annotatewith.CustomMethodOnlyAnnotation\" "
                    + "is not allowed on classes."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithMethodOnClass.class, CustomMethodOnlyAnnotation.class } )
    public void erroneousMapperWithMethodOnClass() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithClassOnMethod.class,
                line = 18,
                message = "Annotation \"org.mapstruct.ap.test.annotatewith.CustomClassOnlyAnnotation\" "
                    + "is not allowed on methods."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithClassOnMethod.class, CustomClassOnlyAnnotation.class } )
    public void erroneousMapperWithClassOnMethod() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithUnknownParameter.class,
                line = 18,
                message = "Parameter \"unknownParameter\" is not present in annotation "
                    + "\"org.mapstruct.ap.test.annotatewith.CustomAnnotation\"."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithUnknownParameter.class, CustomAnnotation.class } )
    public void erroneousMapperWithUnknownParameter() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 17,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"boolean\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 18,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"byte\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 19,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"char\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 20,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type "
                    + "\"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\""
                    + " but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 21,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"double\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 22,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"float\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 23,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"int\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 24,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"long\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 25,
                alternativeLine = 31,
                message = "Parameter \"stringParam\" is not of type \"short\" but of type \"java.lang.String\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 27,
                alternativeLine = 31,
                message = "Parameter \"genericTypedClass\" is not of type \"java.lang.String\" "
                    + "but of type \"java.lang.Class<? extends java.lang.annotation.Annotation>\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMapperWithWrongParameter.class,
                line = 28,
                alternativeLine = 31,
                message = "Parameter \"genericTypedClass\" is not of type "
                    + "\"org.mapstruct.ap.test.annotatewith.ErroneousMapperWithWrongParameter\" "
                    + "but of type \"java.lang.Class<? extends java.lang.annotation.Annotation>\" "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            )
        }
    )
    @WithClasses( { ErroneousMapperWithWrongParameter.class, CustomAnnotationWithParams.class } )
    public void erroneousMapperWithWrongParameter() {
    }

    @ProcessorTest
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 16,
                alternativeLine = 27,
                message = "Parameter \"stringParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 17,
                alternativeLine = 27,
                message = "Parameter \"booleanParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 18,
                alternativeLine = 27,
                message = "Parameter \"byteParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 19,
                alternativeLine = 27,
                message = "Parameter \"charParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 20,
                alternativeLine = 27,
                message = "Parameter \"doubleParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 21,
                alternativeLine = 27,
                message = "Parameter \"floatParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 22,
                alternativeLine = 27,
                message = "Parameter \"intParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 23,
                alternativeLine = 27,
                message = "Parameter \"longParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 24,
                alternativeLine = 27,
                message = "Parameter \"shortParam\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            ),
            @Diagnostic(
                kind = javax.tools.Diagnostic.Kind.ERROR,
                type = ErroneousMultipleArrayValuesMapper.class,
                line = 25,
                alternativeLine = 27,
                message = "Parameter \"genericTypedClass\" does not accept multiple values "
                    + "for annotation \"org.mapstruct.ap.test.annotatewith.CustomAnnotationWithParams\"."
            )
        }
    )
    @WithClasses( { ErroneousMultipleArrayValuesMapper.class, CustomAnnotationWithParams.class } )
    public void erroneousMapperUsingMultipleValuesInsteadOfSingle() {
    }
}