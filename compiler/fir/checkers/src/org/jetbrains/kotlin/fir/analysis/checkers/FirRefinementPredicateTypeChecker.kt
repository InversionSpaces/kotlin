/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.checkers

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirRefinementChecker
import org.jetbrains.kotlin.fir.declarations.FirRefinement
import org.jetbrains.kotlin.fir.resolve.fullyExpandedType
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.createPredicate
import org.jetbrains.kotlin.fir.types.resolvedType

object FirRefinementPredicateTypeChecker : FirRefinementChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirRefinement) {
        val underlyingType = declaration.underlyingTypeRef.coneType.fullyExpandedType()
        val expectedPredicateType = underlyingType.createPredicate(context.session)
        checkTypeMismatch(
            expectedPredicateType,
            assignment = null,
            declaration.predicate,
            declaration.predicate.source!!,
            isInitializer = false,
            isRefinementPredicate = true,
        )
    }
}