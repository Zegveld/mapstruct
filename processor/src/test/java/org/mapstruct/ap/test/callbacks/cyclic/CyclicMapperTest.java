/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.callbacks.cyclic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;

/**
 * @author Ben Zegveld
 */
@WithClasses( { Student.class, StudentDto.class, Teacher.class, TeacherDto.class } )
public class CyclicMapperTest {

    @WithClasses( { StackOverflowMapper.class } )
    @ProcessorTest
    void throwsStackOverflowDuringMapping() {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        StackOverflowMapper mapper = StackOverflowMapper.INSTANCE;

        assertThatThrownBy( () -> mapper.map( teacher ) ).isInstanceOf( StackOverflowError.class );
    }

    @WithClasses( { StackOverflowUpdateMapper.class } )
    @ProcessorTest
    void throwsStackOverflowDuringUpdateMapping() {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        StackOverflowUpdateMapper mapper = StackOverflowUpdateMapper.INSTANCE;
        TeacherDto target = new TeacherDto();

        assertThatThrownBy( () -> mapper.map( teacher, target ) ).isInstanceOf( StackOverflowError.class );
    }

    @WithClasses( { CyclicMapperWithClassContext.class, PreventCyclicContext.class } )
    @ProcessorTest
    void shouldMapCyclesWithClassContext() {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        CyclicMapperWithClassContext mapper = CyclicMapperWithClassContext.INSTANCE;

        assertThatNoException().isThrownBy( () -> mapper.map( teacher ) );
        TeacherDto target = mapper.map( teacher );
        assertThat( target.getStudents().get( 0 ).getTeacher() ).isSameAs( target );
    }

    @WithClasses( { CyclicUpdateMapperWithClassContext.class, PreventCyclicContext.class } )
    @ProcessorTest
    void shouldMapUpdateCyclesWithClassContext() {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        CyclicUpdateMapperWithClassContext mapper = CyclicUpdateMapperWithClassContext.INSTANCE;
        TeacherDto target = new TeacherDto();

        assertThatNoException().isThrownBy( () -> mapper.map( teacher, target ) );
        assertThat( target.getStudents().get( 0 ).getTeacher() ).isSameAs( target );
    }

    @WithClasses( { CyclicMapperWithMethodContext.class, PreventCyclicContext.class } )
    @ProcessorTest
    void shouldMapCyclesWithMethodContext() {
        PreventCyclicContext context = new PreventCyclicContext();
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        CyclicMapperWithMethodContext mapper = CyclicMapperWithMethodContext.INSTANCE;

        assertThatNoException().isThrownBy( () -> mapper.map( teacher, context ) );
        TeacherDto target = mapper.map( teacher, context );
        assertThat( target.getStudents().get( 0 ).getTeacher() ).isSameAs( target );
    }

    @WithClasses( { CyclicUpdateMapperWithMethodContext.class, PreventCyclicContext.class } )
    @ProcessorTest
    void shouldMapUpdateCyclesWithMethodContext() {
        PreventCyclicContext context = new PreventCyclicContext();
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.addStudent( student );
        student.setTeacher( teacher );

        CyclicUpdateMapperWithMethodContext mapper = CyclicUpdateMapperWithMethodContext.INSTANCE;
        TeacherDto target = new TeacherDto();

        assertThatNoException().isThrownBy( () -> mapper.map( teacher, target, context ) );
        assertThat( target.getStudents().get( 0 ).getTeacher() ).isSameAs( target );
    }

}
