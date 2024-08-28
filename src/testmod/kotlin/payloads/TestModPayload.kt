/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.testmod.payloads

import dev.wanderia.netlib.payload.api.PayloadChannel
import dev.wanderia.netlib.payload.api.SerializedPayload
import dev.wanderia.netlib.payload.api.SerializedPayloadConfiguration
import java.util.UUID
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TestModPayload(
    val testBoolean: Boolean,
    val testByte: Byte,
    val testChar: Char,
    val testDouble: Double,
    val testFloat: Float,
    val testInt: Int,
    val testLong: Long,
    val testShort: Short,
    val testString: String,
    val testEnum: PayloadChannel,
    val testCollection: Collection<PayloadChannel>,
    @Contextual val testId: ResourceLocation,
    @Contextual val testUUID: UUID,
) : SerializedPayload<TestModPayload>() {
    override fun codec(): StreamCodec<RegistryFriendlyByteBuf, TestModPayload> = payloadCodec

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = payloadId

    companion object Configuration : SerializedPayloadConfiguration<TestModPayload> {
        override val payloadId: CustomPacketPayload.Type<TestModPayload> =
            CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath("testmod", "payload"))
        override val payloadCodec: StreamCodec<RegistryFriendlyByteBuf, TestModPayload> =
            createCodec()
        override val channels: Set<PayloadChannel> = PayloadChannel.entries.toSet()
    }
}
