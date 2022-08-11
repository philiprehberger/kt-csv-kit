package com.philiprehberger.csvkit

/**
 * Represents a single row in a CSV file.
 *
 * Values can be accessed by column name (if headers are present) or by column index.
 *
 * @property values a map of column names to their values
 * @property index the zero-based row index in the CSV data (excluding the header row)
 */
public class CsvRow(
    public val values: Map<String, String>,
    public val index: Int
) {
    private val indexedValues: List<String> = values.values.toList()

    /**
     * Gets the value for the given column name.
     *
     * @param column the column header name
     * @return the field value
     * @throws NoSuchElementException if the column does not exist
     */
    public operator fun get(column: String): String {
        return values[column] ?: throw NoSuchElementException("Column '$column' not found")
    }

    /**
     * Gets the value at the given column index.
     *
     * @param index the zero-based column index
     * @return the field value
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public operator fun get(index: Int): String {
        return indexedValues[index]
    }

    /**
     * Gets the value for the given column name, or null if not present.
     *
     * @param column the column header name
     * @return the field value or null
     */
    public fun getOrNull(column: String): String? = values[column]

    /**
     * Gets the value for the given column name as an [Int], or null if not parseable.
     *
     * @param column the column header name
     * @return the int value, or null
     */
    public fun getInt(column: String): Int? = values[column]?.toIntOrNull()

    /**
     * Gets the value for the given column name as a [Double], or null if not parseable.
     *
     * @param column the column header name
     * @return the double value, or null
     */
    public fun getDouble(column: String): Double? = values[column]?.toDoubleOrNull()

    /**
     * Gets the value for the given column name as a [Boolean].
     *
     * Recognized values: "true", "1", "yes" (case-insensitive) for true; everything else for false.
     *
     * @param column the column header name
     * @return the boolean value, or null if the column is not found
     */
    public fun getBoolean(column: String): Boolean? {
        val raw = values[column] ?: return null
        return raw.lowercase() in setOf("true", "1", "yes")
    }

    override fun toString(): String = "CsvRow($index, $values)"

}
