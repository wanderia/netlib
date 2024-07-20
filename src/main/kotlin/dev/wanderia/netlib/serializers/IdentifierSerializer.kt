/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.resources.ResourceLocation

public object IdentifierSerializer : KSerializer<ResourceLocation> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("net.minecraft.util.Identifier", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ResourceLocation =
        ResourceLocation.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ResourceLocation) {
        encoder.encodeString(value.toString())
    }
}
