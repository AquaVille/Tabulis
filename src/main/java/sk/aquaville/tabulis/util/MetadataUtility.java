package sk.aquaville.tabulis.util;

import lombok.experimental.UtilityClass;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class MetadataUtility {

    public static boolean tableExists(DatabaseConnection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getRaw().getMetaData();

        try (ResultSet resultSet = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            return resultSet.next();
        }
    }

}
