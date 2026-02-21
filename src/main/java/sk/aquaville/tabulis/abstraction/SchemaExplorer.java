package sk.aquaville.tabulis.abstraction;

import java.sql.SQLException;

/**
 * Provides an abstraction for exploring database schema information.
 *
 * <p>
 * Implementations of this interface allow querying metadata about database tables,
 * including primary keys and foreign key relationships.
 * </p>
 *
 * <p>
 * Useful for systems that need dynamic schema inspection, automated ORM mapping,
 * or migration tooling.
 * </p>
 */
public interface SchemaExplorer {

    /**
     * Returns the primary key column name for the specified table.
     *
     * @param tableName the name of the database table
     * @return the name of the primary key column
     * @throws SQLException if there is an error querying the database metadata
     */
    String getPrimaryKey(String tableName) throws SQLException;

    /**
     * Returns the foreign key column name in the child table that references the parent table.
     *
     * @param childTable the name of the table containing the foreign key
     * @param parentTable the name of the table referenced by the foreign key
     * @return the name of the foreign key column in the child table
     * @throws SQLException if there is an error querying the database metadata
     */
    String getForeignKey(String childTable, String parentTable) throws SQLException;
}
