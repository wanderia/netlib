/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.format

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import net.minecraft.network.FriendlyByteBuf

@ExperimentalSerializationApi
public class PacketByteBufEncoder(private val buf: FriendlyByteBuf) : AbstractEncoder() {
    override val serializersModule: SerializersModule = WanderiaSerializersModule()

    override fun encodeBoolean(value: Boolean) {
        buf.writeBoolean(value)
    }

    override fun encodeByte(value: Byte) {
        buf.writeByte(value.toInt())
    }

    override fun encodeChar(value: Char) {
        buf.writeChar(value.code)
    }

    override fun encodeDouble(value: Double) {
        buf.writeDouble(value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        buf.writeInt(index)
    }

    override fun encodeFloat(value: Float) {
        buf.writeFloat(value)
    }

    override fun encodeInt(value: Int) {
        buf.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        buf.writeLong(value)
    }

    override fun encodeShort(value: Short) {
        buf.writeShort(value.toInt())
    }

    override fun encodeString(value: String) {
        buf.writeUtf(value)
    }

    override fun encodeNull(): Unit = encodeBoolean(false)

    override fun encodeNotNullMark(): Unit = encodeBoolean(true)

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder {
        encodeInt(collectionSize)
        return this
    }
}

@ExperimentalSerializationApi
public fun <T> encodeTo(buf: FriendlyByteBuf, serializer: SerializationStrategy<T>, value: T) {
    val encoder = PacketByteBufEncoder(buf)
    encoder.encodeSerializableValue(serializer, value)
}

@ExperimentalSerializationApi
public inline fun <reified T> encodeTo(buf: FriendlyByteBuf, value: T): Unit =
    encodeTo(buf, serializer(), value)
