package persistence.sql.model;

import jakarta.persistence.Id;
import util.CaseConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Column {

    private final Field field;

    private final String name;

    private final SqlType type;

    private final List<SqlConstraint> constraints;

    public Column(Field field) {
        this.field = field;
        this.name = buildName();
        this.type = buildType();
        this.constraints = buildConstraints();
    }

    private String buildName() {
        jakarta.persistence.Column column = field.getDeclaredAnnotation(jakarta.persistence.Column.class);

        if (column != null && hasName(column)) {
            return column.name();
        }

        String name = field.getName();
        return CaseConverter.camelToSnake(name);
    }

    private boolean hasName(jakarta.persistence.Column column) {
        String name = column.name();
        return !name.isEmpty();
    }

    private SqlType buildType() {
        Class<?> type = field.getType();
        return SqlType.of(type);
    }

    private List<SqlConstraint> buildConstraints() {
        List<SqlConstraint> constraints = new ArrayList<>();

        jakarta.persistence.Column column = field.getDeclaredAnnotation(jakarta.persistence.Column.class);
        if (column != null) {
            constraints.addAll(SqlConstraint.of(column));
        }

        Id id = field.getDeclaredAnnotation(Id.class);

        if (id != null) {
            constraints.addAll(SqlConstraint.of(id));
        }

        return constraints;
    }

    public String getName() {
        return name;
    }

    public SqlType getType() {
        return type;
    }

    public List<SqlConstraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public Object getValue(Object instance) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("This instance does not have any of the fields in that column.");
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Column column = (Column) object;

        if (!name.equals(column.name)) return false;
        if (type != column.type) return false;
        return constraints.equals(column.constraints);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + constraints.hashCode();
        return result;
    }
}
