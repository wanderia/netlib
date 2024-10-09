/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib

import dev.wanderia.netlib.payload.PayloadChannel
import dev.wanderia.netlib.payload.SerializedPayload
import dev.wanderia.netlib.payload.SerializedPayloadConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import org.jetbrains.annotations.ApiStatus
import org.slf4j.LoggerFactory

@ExperimentalSerializationApi
@ApiStatus.Internal
public object WanderiaNetLib : ModInitializer {

    private val logger = LoggerFactory.getLogger("netlib")

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
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        )
                PayloadChannel.ServerboundPlay ->
                    PayloadTypeRegistry.playC2S()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        )
                PayloadChannel.ClientboundConfiguration ->
                    PayloadTypeRegistry.configurationS2C()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        )
                PayloadChannel.ClientboundPlay ->
                    PayloadTypeRegistry.playS2C()
                        .register(
                            configuration.payloadId,
                            configuration.payloadCodec as StreamCodec<in FriendlyByteBuf, T>,
                        )
            }
        }
    }

    override fun onInitialize() {
        logger.info("[netlib] trans rights are human rights!")
        val debug = System.getProperty("dev.wanderia.netlib.debug", "false") == "true"
        val entrypoints =
            FabricLoader.getInstance().getEntrypoints(ENTRYPOINT_NAME, NetLibEntrypoint::class.java)
        if (debug) {
            logger.info("[netlib] Found ${entrypoints.size} entrypoints.")
        }

        entrypoints.forEach { entry ->
            entry.register { payloads: List<SerializedPayloadConfiguration<*>> ->
                payloads.forEach { payload ->
                    if (debug) {
                        logger.info(
                            "[netlib] Registering ${payload::class.qualifiedName} on ${payload.channels}."
                        )
                    }
                    register(payload)
                }
            }
        }
    }
}
