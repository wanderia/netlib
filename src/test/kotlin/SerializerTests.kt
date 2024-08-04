/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@file:OptIn(ExperimentalSerializationApi::class)

import dev.wanderia.netlib.format.PacketByteBufDecoder
import dev.wanderia.netlib.format.PacketByteBufEncoder
import dev.wanderia.netlib.serializers.IdentifierSerializer
import dev.wanderia.netlib.serializers.UUIDSerializer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.resources.ResourceLocation

class SerializerTests {

    @Test
    fun `identifiers should serialize and deserialize`() {
        // given
        val buf = PacketByteBufs.create()
        val id = ResourceLocation.parse("test")

        // when
        IdentifierSerializer.serialize(PacketByteBufEncoder(buf), id)

        // then
        assertEquals(id, IdentifierSerializer.deserialize(PacketByteBufDecoder(buf)))
    }

    @Test
    fun `uuids should serialize and deserialize`() {
        // given
        val buf = PacketByteBufs.create()
        val uuid = UUID.randomUUID()

        // when
        UUIDSerializer.serialize(PacketByteBufEncoder(buf), uuid)

        // then
        assertEquals(uuid, UUIDSerializer.deserialize(PacketByteBufDecoder(buf)))
    }
}
