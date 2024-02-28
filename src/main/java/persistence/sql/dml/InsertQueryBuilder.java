package persistence.sql.dml;

import persistence.entity.EntityBinder;
import persistence.sql.model.Column;
import persistence.sql.model.Columns;
import persistence.sql.model.Table;

import java.util.List;

public class InsertQueryBuilder {

    private final static String INSERT_QUERY_FORMAT = "INSERT INTO %s (%s) values (%s);";

    private final Table table;
    private final Object instance;

    public InsertQueryBuilder(Table table, Object instance) {
        this.table = table;
        this.instance = instance;
    }

    public String build() {
        String tableName = table.getName();
        String columnsClause = buildColumnsClause();
        String valueClause = buildColumnsValueClause();
        return String.format(INSERT_QUERY_FORMAT, tableName, columnsClause, valueClause);
    }

    private String buildColumnsClause() {
        StringBuilder columnsClauseBuilder = new StringBuilder();

        List<String> columnNames = table.getAllColumnNames();
        String joinedColumnNames = String.join(",", columnNames);
        columnsClauseBuilder.append(joinedColumnNames);

        return columnsClauseBuilder.toString();
    }

    private String buildColumnsValueClause() {
        StringBuilder valueClauseBuilder = new StringBuilder();

        valueClauseBuilder.append("null");

        Columns columns = table.getColumns();
        columns.stream()
                .forEach(column -> {
                    String columnValueClause = buildColumnValueClause(column, instance);
                    valueClauseBuilder
                            .append(',')
                            .append(columnValueClause);
                });

        return valueClauseBuilder.toString();
    }

    private String buildColumnValueClause(Column column, Object instance) {
        StringBuilder valueClauseBuilder = new StringBuilder();
        EntityBinder entityBinder = new EntityBinder(instance);

        Object value = entityBinder.getValue(column);
        if (column.isType(String.class)) {
            return valueClauseBuilder.append('\'')
                    .append(value)
                    .append('\'')
                    .toString();
        }

        valueClauseBuilder.append(value);
        return valueClauseBuilder.toString();
    }
}
