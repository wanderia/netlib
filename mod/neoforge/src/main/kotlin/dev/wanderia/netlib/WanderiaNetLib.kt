/*
 * Copyright (C) 2024 Wanderia - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package dev.wanderia.netlib

import kotlinx.serialization.ExperimentalSerializationApi
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

@Mod("netlib")
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
@ExperimentalSerializationApi
@ApiStatus.Internal
public object WanderiaNetLib {

    internal val logger = LogManager.getLogger(WanderiaNetLib::class.java)

    init {
        logger.info("trans rights are human rights!")
    }
}
