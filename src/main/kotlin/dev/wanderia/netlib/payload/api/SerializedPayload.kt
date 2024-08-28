/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.payload.api

import dev.wanderia.netlib.format.decodeFrom
import dev.wanderia.netlib.format.encodeTo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Serializable
@ExperimentalSerializationApi
public abstract class SerializedPayload<T : SerializedPayload<T>> : CustomPacketPayload {
    public abstract fun codec(): StreamCodec<RegistryFriendlyByteBuf, T>

    public companion object {
        public inline fun <reified T : SerializedPayload<T>> createCodec():
            StreamCodec<RegistryFriendlyByteBuf, T> =
            CustomPacketPayload.codec(
                { value, buffer -> encodeTo<T>(buffer, value) },
                { buffer -> decodeFrom<T>(buffer) },
            )
    }
}
