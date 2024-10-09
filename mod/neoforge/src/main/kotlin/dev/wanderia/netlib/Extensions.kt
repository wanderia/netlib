/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib

import dev.wanderia.netlib.payload.PayloadChannel.ClientboundConfiguration
import dev.wanderia.netlib.payload.PayloadChannel.ClientboundPlay
import dev.wanderia.netlib.payload.PayloadChannel.ServerboundConfiguration
import dev.wanderia.netlib.payload.PayloadChannel.ServerboundPlay
import dev.wanderia.netlib.payload.SerializedPayload
import dev.wanderia.netlib.payload.SerializedPayloadConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.network.handling.IPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar

/**
 * Used to register [SerializedPayload]s with NeoForge's networking during
 * [net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent]
 *
 * See [NeoForge Networking Documentation](https://docs.neoforged.net/docs/networking/payload)
 */
@Suppress("UNCHECKED_CAST")
@ExperimentalSerializationApi
public fun <T : SerializedPayload<T>> PayloadRegistrar.netlibPayload(
    config: SerializedPayloadConfiguration<T>,
    handler: IPayloadHandler<T>,
): PayloadRegistrar {
    // i hate this ~py
    when (config.channels.size) {
        1 -> {
            when (config.channels.first()) {
                ServerboundConfiguration ->
                    configurationToServer(
                        config.payloadId,
                        config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        handler,
                    )
                ServerboundPlay ->
                    playToServer(
                        config.payloadId,
                        config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        handler,
                    )
                ClientboundConfiguration ->
                    configurationToClient(
                        config.payloadId,
                        config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        handler,
                    )
                ClientboundPlay ->
                    playToClient(
                        config.payloadId,
                        config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        handler,
                    )
            }
        }
        2 ->
            with(config.channels) {
                when {
                    contains(ServerboundConfiguration) && contains(ClientboundConfiguration) ->
                        configurationToServer(
                            config.payloadId,
                            config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                            handler,
                        )
                    contains(ServerboundPlay) && contains(ClientboundPlay) ->
                        playBidirectional(
                            config.payloadId,
                            config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                            handler,
                        )
                    contains(ServerboundPlay) && contains(ServerboundConfiguration) ->
                        commonToServer(
                            config.payloadId,
                            config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                            handler,
                        )
                    contains(ClientboundPlay) && contains(ClientboundConfiguration) ->
                        commonToClient(
                            config.payloadId,
                            config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                            handler,
                        )
                    else ->
                        throw throw IllegalArgumentException(
                            "Payload channels for ${config.payloadId.id} incompatible with NeoForge networking."
                        )
                }
            }
        4 ->
            commonBidirectional(
                config.payloadId,
                config.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                handler,
            )
        else ->
            throw IllegalArgumentException(
                "Payload channels for ${config.payloadId.id} incompatible with NeoForge networking."
            )
    }

    return this
}
