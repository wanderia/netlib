# netlib
Kotlinx Serialization for Minecraft Payloads.

### Features
- Kotlinx Serialization encoder/decoder for Minecraft's FriendlyByteBuf/PacketByteBuf.
- Abstract `SerializedPayload` for easy packet creation.

### Developer Usage
This project is intended to be included via Jar-in-Jar.
netlib is available on the wanderia maven.
```kotlin
repositories {
    maven("https://maven.wanderia.dev/releases") { name = "Wanderia" }
}
dependencies {
    include("dev.wanderia:netlib:$version")
    modImplementation("dev.wanderia:netlib:$version")
}
```
See the [testmod](src/testmod/) for example usage.

## Attribution
Logo icon by [Iconoir](https://iconoir.com/) which is licensed under MIT.
