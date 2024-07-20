/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib

import dev.wanderia.netlib.payload.api.SerializedPayloadConfiguration
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
public fun interface NetLibEntrypoint {
    public fun register(register: (List<SerializedPayloadConfiguration<*>>) -> Unit)
}
