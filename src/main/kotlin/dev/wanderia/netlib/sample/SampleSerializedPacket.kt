/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.wanderia.netlib.sample

import dev.wanderia.netlib.packet.SerializedPacket
import kotlinx.serialization.Serializable
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.resources.ResourceLocation

/**
 * A simple [SerializedPacket] sample.
 */
@Serializable
public data class SampleSerializedPacket(
    val myBoolean: Boolean,
) : SerializedPacket<SampleSerializedPacket>(ID, TYPE) {
    public companion object {
        public val ID: ResourceLocation = ResourceLocation("sample")
        public val TYPE: PacketType<SampleSerializedPacket> = createType(ID)
    }
}
