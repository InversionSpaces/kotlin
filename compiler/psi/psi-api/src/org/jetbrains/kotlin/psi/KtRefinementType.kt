/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi

import com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.stubs.KotlinPlaceHolderStub
import org.jetbrains.kotlin.KtStubBasedElementTypes

class KtRefinementType : KtElementImplStub<KotlinPlaceHolderStub<KtRefinementType>>, KtTypeElement {
    constructor(node: ASTNode) : super(node)
    constructor(stub: KotlinPlaceHolderStub<KtRefinementType>) : super(stub, KtStubBasedElementTypes.REFINEMENT_TYPE)

    override fun getTypeArgumentsAsTypes(): List<KtTypeReference?> = emptyList()
}