package sk.aquaville.tabulis.universal.query;

import lombok.experimental.UtilityClass;

import java.sql.*;
import java.util.List;

@UtilityClass
public final class SQLQueryBinder {


    /**
     * Binds the given parameters to the specified {@link PreparedStatement}, automatically
     * mapping common Java types to appropriate SQL types.
     * <p>This utility handles:</p>
     * <ul>
     *     <li>{@link java.time.LocalDate} → SQL {@link java.sql.Date}</li>
     *     <li>{@link java.time.LocalDateTime} → SQL {@link java.sql.Timestamp}</li>
     *     <li>{@code null} values → SQL NULL (with best-effort type detection)</li>
     *     <li>Other objects → {@link PreparedStatement#setObject(int, Object)}</li>
     * </ul>
     *
     * <p>Useful for safely preparing statements without manually converting types.</p>
     *
     * @param ps     the {@link PreparedStatement} to bind parameters to
     * @param params the list of parameter values to bind
     * @throws SQLException if a parameter cannot be converted or bound
     */
    public static void bind(PreparedStatement ps, List<Object> params) throws SQLException {
        if (params == null || params.isEmpty()) return;
        ParameterMetaData pmd = ps.getParameterMetaData();
        for (int i = 0; i < params.size(); i++) {
            Object val = params.get(i);
            int idx = i + 1;
            if (val == null) {
                int sqlType = Types.NULL;
                try {
                    sqlType = pmd.getParameterType(idx);
                } catch (SQLException ignore) {}
                ps.setNull(idx, sqlType == 0 ? Types.NULL : sqlType);
            } else {
                // simple typed mapping for common cases; adjust as needed
                if (val instanceof java.time.LocalDate) ps.setDate(idx, Date.valueOf((java.time.LocalDate) val));
                else if (val instanceof java.time.LocalDateTime) ps.setTimestamp(idx, Timestamp.valueOf((java.time.LocalDateTime) val));
                else ps.setObject(idx, val);
            }
        }
    }

}
