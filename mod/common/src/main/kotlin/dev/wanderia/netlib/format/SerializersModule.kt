/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.format

import dev.wanderia.netlib.serializers.IdentifierSerializer
import dev.wanderia.netlib.serializers.UUIDSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private val module: SerializersModule = SerializersModule {
    include(EmptySerializersModule())

    contextual(UUIDSerializer)
    contextual(IdentifierSerializer)
}

/**
 * A [SerializersModule] containing contextual serializers for Minecraft related classes.
 *
 * @see PacketByteBufDecoder
 * @see PacketByteBufEncoder
 */
public fun NetlibSerializersModule(): SerializersModule = module

@Deprecated(
    "Renamed",
    replaceWith = ReplaceWith("dev.wanderia.netlib.format.NetlibSerializersModule()"),
)
public fun WanderiaSerializersModule(): SerializersModule = NetlibSerializersModule()
