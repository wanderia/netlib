# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- NeoForge runtime target
- (NeoForge) `PayloadRegistrar.netlibPayload` extension function for registering SerializedPayloads with NeoForge networking.
- Created typealiases in `dev.wanderia.netlib.payload.api` for moved classes.

### Changed

- Renamed Marven artifacts to match targets: `netlib-common`, `netlib-fabric`, `netlib-neoforge`
- Moved `dev.wanderia.netlib.payload.api` classes to `dev.wanderia.netlib.payload`
- Updated `dokka` from `1.9.20` to `2.0.0-Beta`
- Updated `fabric-api` from `0.103.0+1.21.1` to `0.105.0+1.21.1`
- Updated `fabric-kotlin` from `1.12.1+kotlin.2.0.20` to `1.12.2+kotlin.2.0.20`
- Updated `fabric-loader` from `0.16.3` to `0.16.5`
- Updated `kotlinx-serialization` from `1.7.1` to `1.7.3`

### Deprecated

- Typealiases in `dev.wanderia.netlib.payload.api`, replace with `dev.wanderia.netlib.payload`.

### Removed

- Testmod, to be added back for each runtime target.

### Fixed

- Broken dokka versioning plugin configuration.

### Security

## [1.2.0] - 2024-08-28

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

### Fixed

- Broken custom entrypoint.

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
