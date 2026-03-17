package com.philiprehberger.csvkit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CsvKitTest {

    @Test
    fun `read simple CSV with headers`() {
        val csv = "name,age\nAlice,30\nBob,25"
        val rows = CsvReader().read(csv)

        assertEquals(2, rows.size)
        assertEquals("Alice", rows[0]["name"])
        assertEquals("30", rows[0]["age"])
        assertEquals("Bob", rows[1]["name"])
        assertEquals("25", rows[1]["age"])
    }

    @Test
    fun `read CSV and access by index`() {
        val csv = "name,age\nAlice,30"
        val rows = CsvReader().read(csv)

        assertEquals("Alice", rows[0][0])
        assertEquals("30", rows[0][1])
    }

    @Test
    fun `quoted fields with commas`() {
        val csv = "name,description\nAlice,\"Has a comma, here\""
        val rows = CsvReader().read(csv)

        assertEquals(1, rows.size)
        assertEquals("Has a comma, here", rows[0]["description"])
    }

    @Test
    fun `quoted fields with embedded newlines`() {
        val csv = "name,bio\nAlice,\"Line 1\nLine 2\""
        val rows = CsvReader().read(csv)

        assertEquals(1, rows.size)
        assertEquals("Line 1\nLine 2", rows[0]["bio"])
    }

    @Test
    fun `escaped quotes within quoted fields`() {
        val csv = "name,quote\nAlice,\"She said \"\"hello\"\"\""
        val rows = CsvReader().read(csv)

        assertEquals(1, rows.size)
        assertEquals("She said \"hello\"", rows[0]["quote"])
    }

    @Test
    fun `write and read round-trip`() {
        val headers = listOf("name", "age", "city")
        val data = listOf(
            mapOf("name" to "Alice", "age" to "30", "city" to "New York"),
            mapOf("name" to "Bob", "age" to "25", "city" to "London")
        )

        val csvRows = data.mapIndexed { i, v -> CsvRow(v, i) }
        val writer = CsvWriter()
        val output = writer.write(csvRows, headers)

        val reader = CsvReader()
        val rows = reader.read(output)

        assertEquals(2, rows.size)
        assertEquals("Alice", rows[0]["name"])
        assertEquals("30", rows[0]["age"])
        assertEquals("New York", rows[0]["city"])
        assertEquals("Bob", rows[1]["name"])
    }

    @Test
    fun `custom delimiter`() {
        val csv = "name;age\nAlice;30\nBob;25"
        val reader = CsvReader(CsvConfig(delimiter = ';'))
        val rows = reader.read(csv)

        assertEquals(2, rows.size)
        assertEquals("Alice", rows[0]["name"])
        assertEquals("30", rows[0]["age"])
    }

    @Test
    fun `skip empty lines`() {
        val csv = "name,age\n\nAlice,30\n\nBob,25\n"
        val rows = CsvReader(CsvConfig(skipEmptyLines = true)).read(csv)

        assertEquals(2, rows.size)
        assertEquals("Alice", rows[0]["name"])
        assertEquals("Bob", rows[1]["name"])
    }

    @Test
    fun `missing column returns null via getOrNull`() {
        val csv = "name,age\nAlice,30"
        val rows = CsvReader().read(csv)

        assertNull(rows[0].getOrNull("email"))
    }

    @Test
    fun `missing column throws via get`() {
        val csv = "name,age\nAlice,30"
        val rows = CsvReader().read(csv)

        assertFailsWith<NoSuchElementException> {
            rows[0]["email"]
        }
    }

    @Test
    fun `stream processes rows lazily`() {
        val csv = "id,value\n1,a\n2,b\n3,c\n4,d\n5,e"
        val reader = CsvReader()
        val first3 = reader.stream(csv).take(3).toList()

        assertEquals(3, first3.size)
        assertEquals("1", first3[0]["id"])
        assertEquals("3", first3[2]["id"])
    }

    @Test
    fun `Csv convenience object read`() {
        val rows = Csv.read("name,age\nAlice,30")
        assertEquals(1, rows.size)
        assertEquals("Alice", rows[0]["name"])
    }

    @Test
    fun `Csv convenience object write`() {
        val data = listOf(
            mapOf("name" to "Alice", "age" to "30")
        )
        val output = Csv.write(data, listOf("name", "age"))
        assertEquals("name,age\nAlice,30", output)
    }

    @Test
    fun `writer quotes fields containing delimiter`() {
        val rows = listOf(CsvRow(mapOf("name" to "Smith, John", "age" to "40"), 0))
        val output = CsvWriter().write(rows, listOf("name", "age"))
        assertEquals("name,age\n\"Smith, John\",40", output)
    }

    @Test
    fun `read without headers uses generated column names`() {
        val csv = "Alice,30\nBob,25"
        val reader = CsvReader(CsvConfig(hasHeader = false))
        val rows = reader.read(csv)

        assertEquals(2, rows.size)
        assertEquals("Alice", rows[0]["column0"])
        assertEquals("30", rows[0]["column1"])
    }
}
