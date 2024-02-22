package persistence.sql.model;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.HashMap;

public enum SqlConstraint {
    NOT_NULL("not null"),
    UNIQUE("unique"),
    PRIMARY_KEY("primary key"),
    FOREIGN_KEY("foreign key"),
    CHECK("check"),
    IDENTITY("auto_increment");

    private static final HashMap<Class<?>, SqlConstraint> javaClassToJdbcConstraintCodeMap = buildJavaClassToJdbcConstraintCodeMappings();

    private final String query;

    SqlConstraint(String query) {
        this.query = query;
    }

    private static HashMap<Class<?>, SqlConstraint> buildJavaClassToJdbcConstraintCodeMappings() {
        final HashMap<Class<?>, SqlConstraint> workMap = new HashMap<>();

        workMap.put(Id.class, SqlConstraint.PRIMARY_KEY);

        return workMap;
    }

    public static SqlConstraint of(Class<?> clazz) {
        SqlConstraint constraint = javaClassToJdbcConstraintCodeMap.get(clazz);

        if (constraint == null) {
            throw new IllegalArgumentException("No mapping for jdbc constraint: " + clazz.getSimpleName());
        }

        return constraint;
    }

    public static SqlConstraint of(GenerationType type) {
        if (type == GenerationType.IDENTITY) {
            return SqlConstraint.IDENTITY;
        }
        throw new IllegalArgumentException("No mapping for jdbc constraint: " + type.name());
    }

    public String query() {
        return this.query;
    }
}