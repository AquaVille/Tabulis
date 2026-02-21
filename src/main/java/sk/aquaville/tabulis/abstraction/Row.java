package sk.aquaville.tabulis.abstraction;

import com.github.bsideup.jabel.Desugar;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a single row of a database query result.
 * <p>
 * Internally stores a mapping of column names to their corresponding values.
 * Provides type-safe access methods to retrieve values by column name.
 * </p>
 * Instances are immutable: the underlying map is unmodifiable.
 */
@Desugar
public record Row(Map<String, Object> values) {

    /**
     * Constructs a Row with the given values.
     *
     * @param values A map of column names to their values. Will be wrapped as unmodifiable.
     */
    public Row(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    /**
     * Returns the raw value for the given column.
     *
     * @param column Column name.
     * @return Value associated with the column, or null if not present.
     */
    public Object get(String column) {
        return values.get(column);
    }

    /**
     * Returns the value of the specified column as a String.
     *
     * @param column Column name.
     * @return String value or null if not present.
     */
    public String getString(String column) {
        return (String) values.get(column);
    }

    /**
     * Returns the value of the specified column as an int.
     *
     * @param column Column name.
     * @return Integer value if the underlying object is a Number, otherwise 0.
     */
    public int getInt(String column) {
        Object v = values.get(column);
        return v instanceof Number ? ((Number) v).intValue() : 0;
    }

    /**
     * Returns the value of the specified column as a boolean.
     *
     * @param column Column name.
     * @return Boolean value if the underlying object is a Boolean, otherwise false.
     */
    public boolean getBoolean(String column) {
        Object v = values.get(column);
        return v instanceof Boolean ? (Boolean) v : false;
    }

    /**
     * Returns the value of the specified column cast to the given type.
     *
     * @param column Column name.
     * @param type   Target type class.
     * @param <T>    Type parameter.
     * @return Value cast to the specified type.
     */
    public <T> T getAs(String column, Class<T> type) {
        return type.cast(values.get(column));
    }

    /**
     * Returns the number of columns in this row.
     *
     * @return Number of column-value mappings.
     */
    public Integer length() {
        return values.size();
    }
}
