/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi

import com.intellij.lang.ASTNode
import org.jetbrains.kotlin.KtStubBasedElementTypes
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.psi.psiUtil.ClassIdCalculator
import org.jetbrains.kotlin.psi.stubs.KotlinRefinementStub

public class KtRefinement : KtTypeParameterListOwnerStub<KotlinRefinementStub>, KtNamedDeclaration, KtClassLikeDeclaration {
    constructor(node: ASTNode) : super(node)
    constructor(stub: KotlinRefinementStub) : super(stub, KtStubBasedElementTypes.REFINEMENT)

    override fun getClassId(): ClassId? {
        greenStub?.let { return it.getClassId() }
        return ClassIdCalculator.calculateClassId(this)
    }
}
