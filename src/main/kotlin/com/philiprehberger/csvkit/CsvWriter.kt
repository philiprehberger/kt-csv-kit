package com.philiprehberger.csvkit

import java.io.File

/**
 * Writes CSV data to strings or files.
 *
 * Handles proper quoting of fields that contain the delimiter,
 * quote characters, or newlines.
 *
 * @property config the CSV configuration
 */
class CsvWriter(val config: CsvConfig = CsvConfig()) {

    /**
     * Writes the given rows to a CSV string.
     *
     * @param rows the list of [CsvRow] objects to write
     * @param headers the column headers to include
     * @return the formatted CSV string
     */
    fun write(rows: List<CsvRow>, headers: List<String>): String {
        val sb = StringBuilder()

        if (config.hasHeader) {
            sb.appendLine(headers.joinToString(config.delimiter.toString()) { escapeField(it) })
        }

        for (row in rows) {
            val fields = headers.map { header -> escapeField(row.getOrNull(header) ?: "") }
            sb.appendLine(fields.joinToString(config.delimiter.toString()))
        }

        return sb.toString().trimEnd('\n', '\r')
    }

    /**
     * Writes the given rows to a file.
     *
     * @param path the output file path
     * @param rows the list of [CsvRow] objects to write
     * @param headers the column headers
     */
    fun writeToFile(path: String, rows: List<CsvRow>, headers: List<String>) {
        File(path).writeText(write(rows, headers))
    }

    private fun escapeField(value: String): String {
        val needsQuoting = value.contains(config.delimiter) ||
            value.contains(config.quote) ||
            value.contains('\n') ||
            value.contains('\r')

        return if (needsQuoting) {
            val escaped = value.replace(
                config.quote.toString(),
                "${config.quote}${config.quote}"
            )
            "${config.quote}$escaped${config.quote}"
        } else {
            value
        }
    }
}
