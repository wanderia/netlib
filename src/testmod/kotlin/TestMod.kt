/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.testmod

import dev.wanderia.netlib.payload.api.PayloadChannel
import dev.wanderia.testmod.payloads.TestModPayload
import java.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object TestMod : ModInitializer {
    override fun onInitialize() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            ServerPlayNetworking.registerReceiver(handler, TestModPayload.payloadId) { payload, _ ->
                println(payload.toString())
            }

            if (ServerPlayNetworking.canSend(handler.player, TestModPayload.payloadId)) {
                ServerPlayNetworking.send(handler.player, dummyPayload)
            }
        }

        ServerConfigurationConnectionEvents.CONFIGURE.register { handler, _ ->
            ServerConfigurationNetworking.registerReceiver(handler, TestModPayload.payloadId) {
                payload,
                _ ->
                println(payload.toString())
            }

            if (ServerConfigurationNetworking.canSend(handler, TestModPayload.payloadId)) {
                ServerConfigurationNetworking.send(handler, dummyPayload)
            }
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            ClientPlayNetworking.registerReceiver(TestModPayload.payloadId) { payload, _ ->
                println(payload.toString())
            }

            if (ClientPlayNetworking.canSend(TestModPayload.payloadId)) {
                ClientPlayNetworking.send(dummyPayload)
            }
        }

        ClientConfigurationConnectionEvents.START.register { _, _ ->
            ClientConfigurationNetworking.registerReceiver(TestModPayload.payloadId) { payload, _ ->
                println(payload.toString())
            }

            if (ClientConfigurationNetworking.canSend(TestModPayload.payloadId)) {
                ClientConfigurationNetworking.send(dummyPayload)
            }
        }
    }

    private val dummyPayload: TestModPayload =
        TestModPayload(
            testBoolean = true,
            testByte = 0b0,
            testChar = 'a',
            testDouble = 0.0,
            testFloat = 0f,
            testInt = 1,
            testLong = 1L,
            testShort = 1.toShort(),
            testString = "test",
            testEnum = PayloadChannel.ClientboundPlay,
            testCollection = PayloadChannel.entries,
            testId = TestModPayload.payloadId.id,
            testUUID = UUID.randomUUID()
        )
}
