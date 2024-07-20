/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.testmod

import dev.wanderia.netlib.NetLibEntrypoint
import dev.wanderia.netlib.payload.api.SerializedPayloadConfiguration
import dev.wanderia.testmod.payloads.TestModPayload
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
object NetLibInit : NetLibEntrypoint {
    override fun register(register: (List<SerializedPayloadConfiguration<*>>) -> Unit) {
        register(listOf(TestModPayload.Configuration))
    }
}
