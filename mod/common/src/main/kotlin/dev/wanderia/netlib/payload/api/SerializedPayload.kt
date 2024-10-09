/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.payload.api

import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Deprecated(
    replaceWith = ReplaceWith("dev.wanderia.netlib.payload.SerializedPayload"),
    message = "Moved Package",
)
public typealias SerializedPayload<T> = dev.wanderia.netlib.payload.SerializedPayload<T>