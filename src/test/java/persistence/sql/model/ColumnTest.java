package persistence.sql.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import persistence.entity.EntityBinder;
import persistence.study.sql.ddl.Person2;
import persistence.study.sql.ddl.Person3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ColumnTest {

    @DisplayName("올바른 컬럼이 생성되는지 확인")
    @ParameterizedTest
    @MethodSource
    void newColumn(Column column, String name, SqlType type, List<SqlConstraint> constraints) throws NoSuchFieldException {
        assertSoftly(softly -> {
            softly.assertThat(column.getName()).isEqualTo(name);
            softly.assertThat(column.getType()).isEqualTo(type);
            softly.assertThat(column.getConstraints()).isEqualTo(constraints);
        });
    }

    private static Stream<Arguments> newColumn() throws NoSuchFieldException {
        Class<?> clazz = Person3.class;
        Field idField = clazz.getDeclaredField("id");
        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        Field emailField = clazz.getDeclaredField("email");
        return Stream.of(
                Arguments.arguments(new Column(idField), "id", SqlType.BIGINT, List.of(SqlConstraint.PRIMARY_KEY)),
                Arguments.arguments(new Column(nameField), "nick_name", SqlType.VARCHAR, List.of()),
                Arguments.arguments(new Column(ageField), "old", SqlType.INTEGER, List.of()),
                Arguments.arguments(new Column(emailField), "email", SqlType.VARCHAR, List.of(SqlConstraint.NOT_NULL))
        );
    }

    @DisplayName("@Transient 어노테이션이 달린 필드를 넣었을 경우 IllegarArgumentException을 던진다.")
    @Test
    void newColumnWithException() throws NoSuchFieldException {
        Class<Person3> clazz = Person3.class;
        Field field = clazz.getDeclaredField("index");

        assertThatThrownBy(() -> new Column(field))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This field is not a column: index");
    }

    @DisplayName("올바르게 인스턴스의 값을 반환하는지 확인")
    @Test
    void getValue() throws NoSuchFieldException {
        Column column = createPerson3NameColumn();
        Person3 person = new Person3("a", 1, "email@email.com");

        EntityBinder entityBinder = new EntityBinder(person);
        Object result = entityBinder.getValue(column);

        assertThat(result).isEqualTo("a");
    }

    @DisplayName("해당 필드가 없는 instance를 넣었을 경우 IllegalArgumentException을 던진다.")
    @Test
    void getValueWithException() throws NoSuchFieldException {
//        Column column = createPerson3NameColumn();
//        Person2 person = new Person2("a", 1, "email@email.com");
//
//        assertThatThrownBy(() -> column.getValue(person))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("This instance does not have any of the fields in that column.");
    }

    @DisplayName("인스턴스의 필드에 값을 정상적으로 할당하는지 확인")
    @Test
    void setValue() throws Exception {
        Person3 person = new Person3("qweqwe", 123, "e");
        Column column = createPerson3NameColumn();

//        column.setValue(person, "aaa");
//
//        String result = person.getName();
//        assertThat(result).isEqualTo("aaa");
    }

    @DisplayName("필드가 없는 instance에 값을 넣으려는 경우 IllegalArgumentException을 던진다.")
    @Test
    void setValueWithWrongInstance() throws Exception {
//        Person2 person = new Person2("a", 1, "email@email.com");
//        Column column = createPerson3NameColumn();
//
//        assertThatThrownBy(() -> column.setValue(person, "some name"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Invalid instance or value: ");
    }

    @DisplayName("필드에 잘못된 값을 넣으려는 경우 IllegalArgumentException을 던진다.")
    @Test
    void setValueWithWrongType() throws Exception {
//        Person3 person = new Person3("aa", 2, "email@email.com");
//        Column column = createPerson3NameColumn();
//
//        assertThatThrownBy(() -> column.setValue(person, 112323))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Invalid instance or value: ");
    }

    private Column createPerson3NameColumn() throws NoSuchFieldException {
        Class<Person3> clazz = Person3.class;
        Field field = clazz.getDeclaredField("name");
        return new Column(field);
    }
}
