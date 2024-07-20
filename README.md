![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/wanderia/netlib/build.yml?style=flat-square&logo=github&logoColor=white)
![Modrinth Version](https://img.shields.io/modrinth/v/netlib?style=flat-square&logo=modrinth&logoColor=white)
![Discord](https://img.shields.io/discord/1264019749701161054?style=flat-square&logo=discord&logoColor=white)

# netlib
Kotlinx Serialization for Minecraft Payloads.

### Features
- Kotlinx Serialization encoder/decoder for Minecraft's FriendlyByteBuf/PacketByteBuf.
- Abstract `SerializedPayload` for easy packet creation.

### Developer Usage
This project is intended to be included via Jar-in-Jar.\
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
See the [testmod](https://github.com/wanderia/netlib/tree/main/src/testmod) for example usage.

## Attribution
Logo icon by [Iconoir](https://iconoir.com/) which is licensed under MIT.
