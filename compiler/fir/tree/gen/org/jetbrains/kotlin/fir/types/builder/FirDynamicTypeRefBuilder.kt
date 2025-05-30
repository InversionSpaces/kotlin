/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// This file was generated automatically. See compiler/fir/tree/tree-generator/Readme.md.
// DO NOT MODIFY IT MANUALLY.

@file:Suppress("DuplicatedCode", "unused")

package org.jetbrains.kotlin.fir.types.builder

import kotlin.contracts.*
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.builder.FirAnnotationContainerBuilder
import org.jetbrains.kotlin.fir.builder.FirBuilderDsl
import org.jetbrains.kotlin.fir.builder.toMutableOrEmpty
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.types.FirDynamicTypeRef
import org.jetbrains.kotlin.fir.types.impl.FirDynamicTypeRefImpl

@FirBuilderDsl
class FirDynamicTypeRefBuilder : FirAnnotationContainerBuilder {
    override val annotations: MutableList<FirAnnotation> = mutableListOf()
    lateinit var source: KtSourceElement
    var isMarkedNullable: Boolean by kotlin.properties.Delegates.notNull<Boolean>()

    override fun build(): FirDynamicTypeRef {
        return FirDynamicTypeRefImpl(
            annotations.toMutableOrEmpty(),
            source,
            isMarkedNullable,
        )
    }
}

@OptIn(ExperimentalContracts::class)
inline fun buildDynamicTypeRef(init: FirDynamicTypeRefBuilder.() -> Unit): FirDynamicTypeRef {
    contract {
        callsInPlace(init, InvocationKind.EXACTLY_ONCE)
    }
    return FirDynamicTypeRefBuilder().apply(init).build()
}
