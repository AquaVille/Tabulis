package sk.aquaville.tabulis.universal.explorer;

import com.github.bsideup.jabel.Desugar;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.abstraction.SchemaExplorer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Desugar
public record SQLSchemaExplorer(DatabaseConnection db) implements SchemaExplorer {

    public String getPrimaryKey(String tableName) throws SQLException {
        Connection conn = db.getRaw();
        ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, tableName);
        if (rs.next())
            return rs.getString("COLUMN_NAME");
        return null;
    }

    public String getForeignKey(String childTable, String parentTable) throws SQLException {
        Connection conn = db.getRaw();
        ResultSet rs = conn.getMetaData().getImportedKeys(null, null, childTable);
        while (rs.next()) {
            if (parentTable.equalsIgnoreCase(rs.getString("PKTABLE_NAME")))
                return rs.getString("FKCOLUMN_NAME");
        }
        return null;
    }
}
