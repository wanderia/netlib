/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.payload

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@ExperimentalSerializationApi
public interface SerializedPayloadConfiguration<T : SerializedPayload<T>> {
    public val payloadId: CustomPacketPayload.Type<T>
    public val payloadCodec: StreamCodec<RegistryFriendlyByteBuf, T>
    public val channels: Set<PayloadChannel>
}
