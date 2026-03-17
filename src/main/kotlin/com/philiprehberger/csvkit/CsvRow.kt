package com.philiprehberger.csvkit

/**
 * Represents a single row in a CSV file.
 *
 * Values can be accessed by column name (if headers are present) or by column index.
 *
 * @property values a map of column names to their values
 * @property index the zero-based row index in the CSV data (excluding the header row)
 */
class CsvRow(
    val values: Map<String, String>,
    val index: Int
) {
    private val indexedValues: List<String> = values.values.toList()

    /**
     * Gets the value for the given column name.
     *
     * @param column the column header name
     * @return the field value
     * @throws NoSuchElementException if the column does not exist
     */
    operator fun get(column: String): String {
        return values[column] ?: throw NoSuchElementException("Column '$column' not found")
    }

    /**
     * Gets the value at the given column index.
     *
     * @param index the zero-based column index
     * @return the field value
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    operator fun get(index: Int): String {
        return indexedValues[index]
    }

    /**
     * Gets the value for the given column name, or null if not present.
     *
     * @param column the column header name
     * @return the field value or null
     */
    fun getOrNull(column: String): String? = values[column]

    override fun toString(): String = "CsvRow($index, $values)"
}
