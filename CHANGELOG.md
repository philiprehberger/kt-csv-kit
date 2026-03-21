# Changelog

## 0.1.4 (2026-03-20)

- Fix README: remove Groovy section, update badge label to "Tests"
- Fix CHANGELOG formatting: split malformed entry, remove preamble

## 0.1.3 (2026-03-20)

- Standardize README: fix title, badges, version sync, remove Requirements section

## 0.1.2 (2026-03-18)

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## 0.1.1 (2026-03-18)

- Fix CI badge and gradlew permissions

## 0.1.0 (2026-03-17)

### Added
- `CsvReader` with `read()`, `readFromFile()`, and `stream()` for lazy parsing
- `CsvWriter` with `write()` and `writeToFile()` for formatted output
- `CsvRow` with column access by name, index, and nullable lookup
- `CsvConfig` for delimiter, quote character, header mode, and empty line handling
- `Csv` convenience object for quick read/write operations
- RFC 4180 compliant parsing: quoted fields, embedded commas, newlines, escaped quotes
- Streaming support via Kotlin sequences for memory-efficient processing
