/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.wanderia.netlib.packet

import dev.wanderia.netlib.format.decodeFrom
import dev.wanderia.netlib.format.encodeTo
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import dev.wanderia.netlib.sample.SampleSerializedPacket

/**
 * An abstract [FabricPacket] using `kotlinx.serialization` for encoding and decoding.
 *
 * @sample SampleSerializedPacket
 */
@Serializable
public abstract class SerializedPacket<T : SerializedPacket<T>>(
    @Transient
    public val packetId: ResourceLocation = EmptySerializedPacket.ID,
    @Transient
    public val packetType: PacketType<*> = EmptySerializedPacket.TYPE
) : FabricPacket {
    override fun write(buf: FriendlyByteBuf): Unit = encodeTo(buf, this)
    override fun getType(): PacketType<*> = packetType

    public companion object {
        public inline fun <reified T : SerializedPacket<T>> createType(packetId: ResourceLocation): PacketType<T> =
            PacketType.create(packetId) { decodeFrom<T>(it) }
    }
}
