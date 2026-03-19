package com.philiprehberger.csvkit

/**
 * Convenience object for quick CSV reading and writing.
 *
 * ```
 * val rows = Csv.read("name,age\nAlice,30\nBob,25")
 * val csv = Csv.write(rows.map { it.values }, listOf("name", "age"))
 * ```
 */
public object Csv {

    /**
     * Reads a CSV string with optional configuration.
     *
     * @param input the CSV string to parse
     * @param config optional configuration block applied to a [MutableCsvConfig]
     * @return a list of [CsvRow] objects
     */
    public fun read(input: String, config: MutableCsvConfig.() -> Unit = {}): List<CsvRow> {
        val cfg = MutableCsvConfig().apply(config).toConfig()
        return CsvReader(cfg).read(input)
    }

    /**
     * Writes a list of maps to a CSV string with optional configuration.
     *
     * @param rows the data as a list of column-name-to-value maps
     * @param headers the column headers to include
     * @param config optional configuration block applied to a [MutableCsvConfig]
     * @return the formatted CSV string
     */
    public fun write(rows: List<Map<String, String>>, headers: List<String>, config: MutableCsvConfig.() -> Unit = {}): String {
        val cfg = MutableCsvConfig().apply(config).toConfig()
        val csvRows = rows.mapIndexed { index, values ->
            CsvRow(values, index)
        }
        return CsvWriter(cfg).write(csvRows, headers)
    }
}

/**
 * Mutable configuration for use with the [Csv] DSL.
 */
public class MutableCsvConfig {
    /** The field delimiter character. */
    public var delimiter: Char = ','
    /** The quote character for escaping fields. */
    public var quote: Char = '"'
    /** Whether the first row contains column headers. */
    public var hasHeader: Boolean = true
    /** Whether to skip blank lines during parsing. */
    public var skipEmptyLines: Boolean = true

    internal fun toConfig(): CsvConfig = CsvConfig(delimiter, quote, hasHeader, skipEmptyLines)
}
