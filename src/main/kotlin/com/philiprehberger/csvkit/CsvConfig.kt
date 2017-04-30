package com.philiprehberger.csvkit

/**
 * Configuration for CSV reading and writing.
 *
 * @property delimiter the field delimiter character (default: ',')
 * @property quote the quote character for escaping fields (default: '"')
 * @property hasHeader whether the first row contains column headers (default: true)
 * @property skipEmptyLines whether to skip blank lines during parsing (default: true)
 */
public data class CsvConfig(
    public val delimiter: Char = ',',
    public val quote: Char = '"',
    public val hasHeader: Boolean = true,
    public val skipEmptyLines: Boolean = true
)
