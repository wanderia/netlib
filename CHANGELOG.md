# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Debug log lines to print payload info when initializing.

### Changed

- Updated `minecraft` from `1.21` to `1.21.1`
- Updated `kotlin` from `2.0.0` to `2.0.20`
- Updated `fabric-api` from `0.100.7+1.21` to `0.103.0+1.21.1`
- Updated `fabric-kotlin` from `1.11.0+kotlin.2.0.0` to `1.12.1+kotlin.2.0.20`
- Updated `fabric-loader` from `0.16.0` to `0.16.3`
- Updated `spotless` from `7.0.0.BETA1` to `7.0.0.BETA2`
- Moved use of client networking and events to a `client` entrypoint. (TestMod)

### Deprecated

### Removed

### Fixed

- Broken custom entrypoint.

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

## [1.0.0] - 2024-02-16

### Added

- Initial release
