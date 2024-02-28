package persistence.sql.model;

import jakarta.persistence.Transient;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Columns {

    private final List<Column> columns;

    public Columns(List<Field> fields) {
        this.columns = buildColumns(fields);
    }

    private List<Column> buildColumns(List<Field> fields) {
        return fields.stream()
                .filter(field -> !hasTransientAnnotation(field))
                .map(Column::new)
                .collect(Collectors.toList());
    }

    private boolean hasTransientAnnotation(Field field) {
        return field.isAnnotationPresent(Transient.class);
    }

    public List<String> getColumnNames() {
        return columns.stream()
                .map(Column::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    public Stream<Column> stream() {
        return columns.stream();
    }
}
