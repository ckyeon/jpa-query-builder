package persistence.sql.ddl.generator;

import persistence.sql.ddl.dialect.ColumnType;
import persistence.sql.ddl.schema.TableMeta;

public class DropDDLQueryGenerator {

    public static final String DROP_TABLE_FORMAT = "DROP TABLE %s;";

    public DropDDLQueryGenerator(ColumnType columnType) {
    }

    public String drop(Class<?> entityClazz) {
        return String.format(DROP_TABLE_FORMAT, TableMeta.of(entityClazz).getTableName());
    }
}