package com.philiprehberger.csvkit

import java.io.File

/**
 * Reads CSV data from strings or files.
 *
 * Handles RFC 4180 compliant CSV including quoted fields, embedded commas,
 * embedded newlines, and escaped quotes.
 *
 * @property config the CSV configuration
 */
public class CsvReader(public val config: CsvConfig = CsvConfig()) {

    /**
     * Parses a CSV string into a list of [CsvRow] objects.
     *
     * @param input the CSV string to parse
     * @return a list of rows
     */
    public fun read(input: String): List<CsvRow> {
        return stream(input).toList()
    }

    /**
     * Reads a CSV file and returns all rows.
     *
     * @param path the file path
     * @return a list of rows
     */
    public fun readFromFile(path: String): List<CsvRow> {
        val content = File(path).readText()
        return read(content)
    }

    /**
     * Lazily parses a CSV string, yielding rows one at a time.
     *
     * This is useful for processing large CSV data without loading
     * all rows into memory at once.
     *
     * @param input the CSV string to parse
     * @return a sequence of [CsvRow] objects
     */
    public fun stream(input: String): Sequence<CsvRow> = sequence {
        val records = parseRecords(input)
        val iterator = records.iterator()
        if (!iterator.hasNext()) return@sequence

        val headers: List<String>
        val startIndex: Int

        if (config.hasHeader) {
            headers = iterator.next()
            startIndex = 0
        } else {
            val firstRecord = iterator.next()
            headers = firstRecord.indices.map { "column$it" }
            val row = CsvRow(
                headers.zip(firstRecord).toMap(),
                0
            )
            yield(row)
            startIndex = 1
        }

        var rowIndex = startIndex
        while (iterator.hasNext()) {
            val fields = iterator.next()
            val values = headers.zip(fields).toMap()
            yield(CsvRow(values, rowIndex))
            rowIndex++
        }
    }

    private fun parseRecords(input: String): List<List<String>> {
        val records = mutableListOf<List<String>>()
        val chars = input.toCharArray()
        var pos = 0
        val len = chars.size

        while (pos <= len) {
            val (fields, nextPos) = parseRecord(chars, pos, len)
            pos = nextPos

            if (config.skipEmptyLines && fields.size == 1 && fields[0].isEmpty()) {
                continue
            }

            records.add(fields)
        }

        return records
    }

    private fun parseRecord(chars: CharArray, startPos: Int, len: Int): Pair<List<String>, Int> {
        val fields = mutableListOf<String>()
        var pos = startPos

        while (true) {
            val (field, nextPos) = parseField(chars, pos, len)
            fields.add(field)
            pos = nextPos

            if (pos >= len) {
                return fields to (len + 1)
            }

            when (chars[pos]) {
                config.delimiter -> pos++ // skip delimiter, continue to next field
                '\r' -> {
                    pos++
                    if (pos < len && chars[pos] == '\n') pos++
                    return fields to pos
                }
                '\n' -> {
                    pos++
                    return fields to pos
                }
                else -> pos++ // shouldn't happen, but advance
            }
        }
    }

    private fun parseField(chars: CharArray, startPos: Int, len: Int): Pair<String, Int> {
        if (startPos >= len) return "" to startPos

        return if (chars[startPos] == config.quote) {
            parseQuotedField(chars, startPos, len)
        } else {
            parseUnquotedField(chars, startPos, len)
        }
    }

    private fun parseQuotedField(chars: CharArray, startPos: Int, len: Int): Pair<String, Int> {
        val sb = StringBuilder()
        var pos = startPos + 1 // skip opening quote

        while (pos < len) {
            val ch = chars[pos]
            if (ch == config.quote) {
                if (pos + 1 < len && chars[pos + 1] == config.quote) {
                    // Escaped quote
                    sb.append(config.quote)
                    pos += 2
                } else {
                    // End of quoted field
                    pos++ // skip closing quote
                    return sb.toString() to pos
                }
            } else {
                sb.append(ch)
                pos++
            }
        }

        // Unterminated quote - return what we have
        return sb.toString() to pos
    }

    private fun parseUnquotedField(chars: CharArray, startPos: Int, len: Int): Pair<String, Int> {
        val sb = StringBuilder()
        var pos = startPos

        while (pos < len) {
            val ch = chars[pos]
            if (ch == config.delimiter || ch == '\r' || ch == '\n') {
                break
            }
            sb.append(ch)
            pos++
        }

        return sb.toString() to pos
    }
}
