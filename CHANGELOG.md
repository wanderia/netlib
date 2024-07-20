# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## [1.1.0] - 2024-07-20

### Added

- `wanderia-netlib` entrypoint as new standard for registering payload types.
- `WanderiaSerializersModule` used by default in payload serialization.
- `UUIDSerializer` as a default contextual serializer.
- `IdentifierSerialzier` as a default contextual serializer.
- Spotless formatting and checking.

### Changed

- Updated to Minecraft `1.21`
- Updated to Kotlin `2.0.0`
- Updated to Kotlinx Serialization `1.7.1`
- Updated to Fabric Loom `1.7-SNAPSHOT`
- Updated to Fabric Loader `0.16.0`
- Updated to Fabric API `0.100.7+1.21`

### Removed

- `dev.wanderia.netlib.samples` has been replaced with a full example test mod in `src/testmod`.
- Yumi licenser (replaced by spotless)

## 1.0.0 - 2024-02-16

### Added

- Initial release
