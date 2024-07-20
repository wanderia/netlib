/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.payload.api

public enum class PayloadChannel {
    /** C2S during the configuration phase. */
    ServerboundConfiguration,

    /** C2S during the place phase. */
    ServerboundPlay,

    /** S2C during the configuration phase. */
    ClientboundConfiguration,

    /** S2C during the play phase. */
    ClientboundPlay
}
