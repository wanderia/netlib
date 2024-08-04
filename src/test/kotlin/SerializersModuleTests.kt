/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@file:OptIn(ExperimentalSerializationApi::class)

import dev.wanderia.netlib.format.decodeFrom
import dev.wanderia.netlib.format.encodeTo
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.resources.ResourceLocation

class SerializersModuleTests {

    @Serializable
    data class TestData(@Contextual val identifier: ResourceLocation, @Contextual val uuid: UUID)

    @Test
    fun `should use contextual serializers`() {
        // given
        val buf = PacketByteBufs.create()
        val testData = TestData(ResourceLocation.parse("test"), UUID.randomUUID())

        // when
        encodeTo(buf, testData)

        // then
        assertEquals(testData, decodeFrom<TestData>(buf))
    }
}
