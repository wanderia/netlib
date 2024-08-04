/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@file:OptIn(ExperimentalSerializationApi::class)

import dev.wanderia.netlib.format.decodeFrom
import dev.wanderia.netlib.format.encodeTo
import dev.wanderia.netlib.payload.api.PayloadChannel
import java.util.UUID
import java.util.stream.Stream
import kotlin.random.Random
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class EncodeDecodeTests {

    @Serializable
    data class TestDataA(
        val boolean: Boolean?,
        val byte: Byte?,
        val char: Char?,
        val double: Double?,
        val float: Float?,
        val int: Int?,
        val long: Long?,
        val short: Short?,
        val string: String?,
        val enum: PayloadChannel?
    )

    @Serializable
    data class TestDataB(
        val booleanCollection: List<Boolean>,
        val byteCollection: List<Byte>,
        val charCollection: List<Char>,
        val doubleCollection: List<Double>,
        val floatCollection: List<Float>,
        val intCollection: List<Int>,
        val longCollection: List<Long>,
        val shortCollection: List<Short>,
        val stringCollection: List<String>,
        val enumCollection: List<PayloadChannel>
    )

    @Serializable
    data class TestDataC(
        val structureA: TestDataA,
        val structureB: TestDataB,
        val collectionA: List<TestDataA>,
        val collectionB: List<TestDataB>,
    )

    @ParameterizedTest
    @MethodSource("testDataAProvider")
    fun `should encode and decode values`(data: TestDataA) {
        // given
        val buf = PacketByteBufs.create()

        // when
        encodeTo(buf, data)

        // then
        assertEquals(data, decodeFrom<TestDataA>(buf))
    }

    @ParameterizedTest
    @MethodSource("testDataBProvider")
    fun `should encode and decode collections`(data: TestDataB) {
        // given
        val buf = PacketByteBufs.create()

        // when
        encodeTo(buf, data)

        // then
        assertEquals(data, decodeFrom<TestDataB>(buf))
    }

    @ParameterizedTest
    @MethodSource("testDataCProvider")
    fun `should encode and decode structures`(data: TestDataC) {
        // given
        val buf = PacketByteBufs.create()

        // when
        encodeTo(buf, data)

        // then
        assertEquals(data, decodeFrom<TestDataC>(buf))
    }

    companion object {
        @JvmStatic
        fun testDataAProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(randomDataA(0)),
                Arguments.of(randomDataA(1)),
                Arguments.of(randomDataA(2)),
                Arguments.of(randomDataA(3)),
                Arguments.of(randomDataA(4)),
            )

        @JvmStatic
        fun testDataBProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(randomDataB()),
                Arguments.of(randomDataB()),
                Arguments.of(randomDataB()),
                Arguments.of(randomDataB()),
                Arguments.of(randomDataB()),
            )

        @JvmStatic
        fun testDataCProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(randomDataC()),
                Arguments.of(randomDataC()),
                Arguments.of(randomDataC()),
                Arguments.of(randomDataC()),
                Arguments.of(randomDataC()),
            )

        @JvmStatic
        fun randomDataA(index: Int): TestDataA =
            TestDataA(
                boolean = listOf(null, true, false)[index.coerceIn(0, 2)],
                byte =
                    listOf(null, 0b0, 0b1, (Byte.MIN_VALUE..Byte.MAX_VALUE).random().toByte())[
                        index.coerceIn(0, 3)],
                char =
                    listOf(null, ('a'..'z').random(), (Char.MIN_VALUE..Char.MAX_VALUE).random())[
                        index.coerceIn(0, 2)],
                double = listOf(null, 0.0, -1.0, 1.0, Random.nextDouble())[index.coerceIn(0, 4)],
                float = listOf(null, 0f, -1f, 1f, Random.nextFloat())[index.coerceIn(0, 4)],
                int = listOf(null, 0, -1, 1, Random.nextInt())[index.coerceIn(0, 4)],
                long = listOf(null, 0L, -1L, 1L, Random.nextLong())[index.coerceIn(0, 4)],
                short =
                    listOf(
                        null,
                        0.toShort(),
                        (-1).toShort(),
                        1.toShort(),
                        (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort()
                    )[index.coerceIn(0, 4)],
                string = listOf(null, "", UUID.randomUUID().toString())[index.coerceIn(0, 2)],
                enum =
                    listOf(
                        null,
                        PayloadChannel.ClientboundPlay,
                        PayloadChannel.ServerboundPlay,
                        PayloadChannel.ClientboundConfiguration,
                        PayloadChannel.ServerboundConfiguration
                    )[index.coerceIn(0, 4)],
            )

        @JvmStatic
        fun randomDataB(): TestDataB =
            TestDataB(
                booleanCollection = listOf(true, false),
                byteCollection =
                    listOf(0b0, 0b1, (Byte.MIN_VALUE..Byte.MAX_VALUE).random().toByte()),
                charCollection =
                    listOf(('a'..'z').random(), (Char.MIN_VALUE..Char.MAX_VALUE).random()),
                doubleCollection = listOf(0.0, -1.0, 1.0, Random.nextDouble()),
                floatCollection = listOf(0f, -1f, 1f, Random.nextFloat()),
                intCollection = listOf(0, -1, 1, Random.nextInt()),
                longCollection = listOf(0L, -1L, 1L, Random.nextLong()),
                shortCollection =
                    listOf(
                        0.toShort(),
                        (-1).toShort(),
                        1.toShort(),
                        (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort()
                    ),
                stringCollection = listOf("", UUID.randomUUID().toString()),
                enumCollection =
                    listOf(
                        PayloadChannel.ClientboundPlay,
                        PayloadChannel.ServerboundPlay,
                        PayloadChannel.ClientboundConfiguration,
                        PayloadChannel.ServerboundConfiguration
                    ),
            )

        @JvmStatic
        fun randomDataC(): TestDataC =
            TestDataC(
                structureA = randomDataA(Random.nextInt(5)),
                structureB = randomDataB(),
                collectionA =
                    listOf(
                        randomDataA(Random.nextInt(5)),
                        randomDataA(Random.nextInt(5)),
                        randomDataA(Random.nextInt(5)),
                        randomDataA(Random.nextInt(5)),
                        randomDataA(Random.nextInt(5))
                    ),
                collectionB =
                    listOf(
                        randomDataB(),
                        randomDataB(),
                        randomDataB(),
                        randomDataB(),
                        randomDataB()
                    ),
            )
    }
}
