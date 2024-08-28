/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.format

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import net.minecraft.network.FriendlyByteBuf

@ExperimentalSerializationApi
public class PacketByteBufDecoder(
    private val buf: FriendlyByteBuf,
    private var elementsCount: Int = 0,
) : AbstractDecoder() {
    private var elementIndex: Int = 0
    override val serializersModule: SerializersModule = WanderiaSerializersModule()

    override fun decodeBoolean(): Boolean = buf.readBoolean()

    override fun decodeByte(): Byte = buf.readByte()

    override fun decodeChar(): Char = buf.readChar()

    override fun decodeDouble(): Double = buf.readDouble()

    override fun decodeFloat(): Float = buf.readFloat()

    override fun decodeInt(): Int = buf.readInt()

    override fun decodeLong(): Long = buf.readLong()

    override fun decodeShort(): Short = buf.readShort()

    override fun decodeString(): String = buf.readUtf()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = buf.readInt()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        PacketByteBufDecoder(buf, descriptor.elementsCount)

    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
        decodeInt().also { elementsCount = it }

    override fun decodeNotNullMark(): Boolean = decodeBoolean()
}

@ExperimentalSerializationApi
public fun <T> decodeFrom(buf: FriendlyByteBuf, deserializer: DeserializationStrategy<T>): T {
    val decoder = PacketByteBufDecoder(buf)
    return decoder.decodeSerializableValue(deserializer)
}

@ExperimentalSerializationApi
public inline fun <reified T> decodeFrom(buf: FriendlyByteBuf): T = decodeFrom(buf, serializer())
