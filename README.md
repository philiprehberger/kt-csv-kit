# csv-kit

[![Tests](https://github.com/philiprehberger/kt-csv-kit/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-csv-kit/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/csv-kit.svg)](https://central.sonatype.com/artifact/com.philiprehberger/csv-kit)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-csv-kit)](https://github.com/philiprehberger/kt-csv-kit/commits/main)

CSV reading and writing for Kotlin with streaming support.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:csv-kit:0.2.0")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>csv-kit</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Usage

### Quick Read

```kotlin
import com.philiprehberger.csvkit.*

val rows = Csv.read("name,age\nAlice,30\nBob,25")
rows.forEach { row ->
    println("${row["name"]} is ${row["age"]} years old")
}
```

### CsvReader with Configuration

```kotlin
val reader = CsvReader(CsvConfig(
    delimiter = ';',
    skipEmptyLines = true
))

val rows = reader.read("name;age\nAlice;30\n\nBob;25")
```

### Streaming Large Data

```kotlin
val reader = CsvReader()
reader.stream(largeCsvString)
    .filter { it["status"] == "active" }
    .take(100)
    .forEach { row -> process(row) }
```

### Writing CSV

```kotlin
val data = listOf(
    mapOf("name" to "Alice", "age" to "30"),
    mapOf("name" to "Bob", "age" to "25")
)

val output = Csv.write(data, listOf("name", "age"))
// name,age
// Alice,30
// Bob,25
```

### Quoted Fields

The parser handles RFC 4180 compliant quoting: embedded commas, newlines, and escaped quotes.

```kotlin
val csv = """name,bio
Alice,"Loves coding, hiking"
Bob,"Said ""hello"""
"""
val rows = Csv.read(csv)
println(rows[0]["bio"]) // Loves coding, hiking
println(rows[1]["bio"]) // Said "hello"
```

### Typed Accessors

```kotlin
val rows = Csv.read("name,age,active\nAlice,30,true")
val age: Int? = rows[0].getInt("age")        // 30
val active: Boolean? = rows[0].getBoolean("active") // true
val score: Double? = rows[0].getDouble("score")     // null
```

### Trim Fields

```kotlin
val reader = CsvReader(CsvConfig(trimFields = true))
val rows = reader.read("name , age \n Alice , 30 ")
println(rows[0]["name"]) // "Alice" (trimmed)
```

### Header Validation

```kotlin
val reader = CsvReader()
reader.validateHeaders(csvInput, listOf("name", "email", "age"))
// Throws IllegalArgumentException if any header is missing
```

## API

| Class / Function | Description |
|------------------|-------------|
| `Csv.read(input, config)` | Convenience method to parse a CSV string |
| `Csv.write(rows, headers, config)` | Convenience method to write maps to CSV |
| `CsvReader` | Full-featured CSV parser with `read()`, `readFromFile()`, `stream()` |
| `CsvWriter` | CSV writer with `write()` and `writeToFile()` |
| `CsvRow` | A single row with `get(column)`, `get(index)`, `getOrNull(column)` |
| `CsvConfig` | Configuration: delimiter, quote char, header mode, empty line handling |
| `CsvRow.getInt(column)` | Get field as Int or null |
| `CsvRow.getDouble(column)` | Get field as Double or null |
| `CsvRow.getBoolean(column)` | Get field as Boolean or null |
| `CsvReader.validateHeaders()` | Validate expected columns exist |
| `CsvConfig.trimFields` | Auto-trim whitespace from field values |

## Development

```bash
./gradlew test       # Run tests
./gradlew check      # Run all checks
./gradlew build      # Build JAR
```

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-csv-kit)

🐛 [Report issues](https://github.com/philiprehberger/kt-csv-kit/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-csv-kit/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
