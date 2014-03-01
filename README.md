# kt-csv-kit

[![CI](https://github.com/philiprehberger/kt-csv-kit/actions/workflows/ci.yml/badge.svg)](https://github.com/philiprehberger/kt-csv-kit/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/csv-kit)](https://central.sonatype.com/artifact/com.philiprehberger/csv-kit)

CSV reading and writing for Kotlin with streaming support.

## Requirements

- Kotlin 1.9+ / Java 17+

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.philiprehberger:csv-kit:0.1.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'com.philiprehberger:csv-kit:0.1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>csv-kit</artifactId>
    <version>0.1.0</version>
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

## API

| Class / Function | Description |
|------------------|-------------|
| `Csv.read(input, config)` | Convenience method to parse a CSV string |
| `Csv.write(rows, headers, config)` | Convenience method to write maps to CSV |
| `CsvReader` | Full-featured CSV parser with `read()`, `readFromFile()`, `stream()` |
| `CsvWriter` | CSV writer with `write()` and `writeToFile()` |
| `CsvRow` | A single row with `get(column)`, `get(index)`, `getOrNull(column)` |
| `CsvConfig` | Configuration: delimiter, quote char, header mode, empty line handling |

## Development

```bash
./gradlew test       # Run tests
./gradlew check      # Run all checks
./gradlew build      # Build JAR
```

## License

MIT
