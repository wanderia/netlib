/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.serializers

import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

public object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("java.util.UUID") {
            element<Long>("mostSignificantBits")
            element<Long>("leastSignificantBits")
        }

    override fun deserialize(decoder: Decoder): UUID =
        decoder.decodeStructure(descriptor) {
            var mostSignificantBits: Long? = null
            var leastSignificantBits: Long? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> mostSignificantBits = decodeLongElement(descriptor, 0)
                    1 -> leastSignificantBits = decodeLongElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw SerializationException("Unknown index: $index")
                }
            }
            require(mostSignificantBits != null && leastSignificantBits != null)
            UUID(mostSignificantBits, leastSignificantBits)
        }

    override fun serialize(encoder: Encoder, value: UUID): Unit =
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.mostSignificantBits)
            encodeLongElement(descriptor, 1, value.leastSignificantBits)
        }
}
