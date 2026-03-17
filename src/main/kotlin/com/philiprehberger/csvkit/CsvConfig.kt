package com.philiprehberger.csvkit

/**
 * Configuration for CSV reading and writing.
 *
 * @property delimiter the field delimiter character (default: ',')
 * @property quote the quote character for escaping fields (default: '"')
 * @property hasHeader whether the first row contains column headers (default: true)
 * @property skipEmptyLines whether to skip blank lines during parsing (default: true)
 */
data class CsvConfig(
    val delimiter: Char = ',',
    val quote: Char = '"',
    val hasHeader: Boolean = true,
    val skipEmptyLines: Boolean = true
)
