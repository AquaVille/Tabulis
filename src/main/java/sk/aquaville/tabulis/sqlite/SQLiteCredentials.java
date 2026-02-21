package sk.aquaville.tabulis.sqlite;

import com.github.bsideup.jabel.Desugar;
import com.google.common.io.Files;
import lombok.SneakyThrows;
import sk.aquaville.tabulis.abstraction.DatabaseCredentials;

import java.io.File;
import java.sql.SQLException;

/**
 * Represents credentials/configuration for a SQLite database.
 *
 * <p>
 * Encapsulates the database file and validates that it exists, is a file,
 * and has the `.sqlite` extension.
 * </p>
 *
 * Implements {@link DatabaseCredentials} for compatibility with database connection APIs.
 */
@Desugar
public record SQLiteCredentials(File databaseFile) implements DatabaseCredentials {

    /**
     * Constructs a new {@code SQLiteCredentials} instance.
     * Validates that the provided file exists, is a regular file, and has a `.sqlite` extension.
     *
     * @param databaseFile The SQLite database file.
     */
    @SneakyThrows
    public SQLiteCredentials {
        if (databaseFile == null || !databaseFile.exists() || !databaseFile.isFile() || !Files.getFileExtension(databaseFile.getName()).equals("sqlite")) {
            throw new SQLException("Database file is not valid SQLite file!");
        }
    }
}
