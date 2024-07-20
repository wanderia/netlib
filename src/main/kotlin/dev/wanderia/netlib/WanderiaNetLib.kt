/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib

import dev.wanderia.netlib.payload.api.PayloadChannel
import dev.wanderia.netlib.payload.api.SerializedPayload
import dev.wanderia.netlib.payload.api.SerializedPayloadConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import org.jetbrains.annotations.ApiStatus

@ExperimentalSerializationApi
@ApiStatus.Internal
public object WanderiaNetLib : ModInitializer {

    @Suppress("UNCHECKED_CAST")
    private fun <T : SerializedPayload<T>> register(
        configuration: SerializedPayloadConfiguration<T>
    ) {
        configuration.channels.forEach { channel ->
            when (channel) {
                PayloadChannel.ServerboundConfiguration ->
                    PayloadTypeRegistry.configurationC2S()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>
                        )
                PayloadChannel.ServerboundPlay ->
                    PayloadTypeRegistry.playC2S()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>
                        )
                PayloadChannel.ClientboundConfiguration ->
                    PayloadTypeRegistry.configurationS2C()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>
                        )
                PayloadChannel.ClientboundPlay ->
                    PayloadTypeRegistry.playS2C()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>
                        )
            }
        }
    }

    override fun onInitialize() {
        FabricLoader.getInstance()
            .getEntrypoints("waderia-netlib", NetLibEntrypoint::class.java)
            .forEach { entry ->
                entry.register { payloads: List<SerializedPayloadConfiguration<*>> ->
                    payloads.forEach { payload -> register(payload) }
                }
            }
    }
}
