package persistence.sql.dialect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import persistence.sql.model.SqlConstraint;
import persistence.sql.model.SqlType;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class H2DialectTest {

    private final Dialect dialect = new H2Dialect();

    @DisplayName("H2 타입 방언 확인하기")
    @ParameterizedTest
    @MethodSource
    void getType(SqlType type, String h2Type) {
        String result = dialect.getType(type);

        assertThat(result).isEqualTo(h2Type);
    }

    private static Stream<Arguments> getType() {
        return Stream.of(
                Arguments.arguments(SqlType.VARCHAR, "varchar"),
                Arguments.arguments(SqlType.VARCHAR, "varchar"),
                Arguments.arguments(SqlType.VARCHAR, "varchar")
        );
    }

    @DisplayName("H2 제약조건 방언 확인하기")
    @ParameterizedTest
    @MethodSource
    void getConstraint(SqlConstraint constraint, String h2Constraint) {
        String result = dialect.getConstraint(constraint);

        assertThat(result).isEqualTo(h2Constraint);
    }

    private static Stream<Arguments> getConstraint() {
        return Stream.of(
                Arguments.arguments(SqlConstraint.PRIMARY_KEY, "primary key"),
                Arguments.arguments(SqlConstraint.IDENTITY, "auto_increment"),
                Arguments.arguments(SqlConstraint.NOT_NULL, "not null")
        );
    }
}
