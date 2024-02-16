/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.wanderia.netlib.packet

import kotlinx.serialization.Serializable
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.resources.ResourceLocation

/**
 * An empty [SerializedPacket].
 */
@Serializable
public sealed class EmptySerializedPacket : SerializedPacket<EmptySerializedPacket>(ID, TYPE) {
    public companion object {
        public val ID: ResourceLocation = ResourceLocation("empty")
        public val TYPE: PacketType<EmptySerializedPacket> = createType(ID)
    }
}
