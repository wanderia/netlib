/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.testmod

import dev.wanderia.testmod.TestMod.dummyPayload
import dev.wanderia.testmod.payloads.TestModPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object TestModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientConfigurationConnectionEvents.START.register { _, _ ->
            ClientConfigurationNetworking.registerReceiver(TestModPayload.payloadId) { payload, _ ->
                println(payload.toString())
            }

            if (ClientConfigurationNetworking.canSend(TestModPayload.payloadId)) {
                ClientConfigurationNetworking.send(dummyPayload)
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
    }
}
