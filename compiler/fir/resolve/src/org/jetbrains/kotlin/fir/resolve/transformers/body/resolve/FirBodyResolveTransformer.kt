/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.transformers.body.resolve

import org.jetbrains.kotlin.builtins.functions.FunctionTypeKind
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.resolve.ResolutionMode
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.fir.resolve.transformers.ReturnTypeCalculator
import org.jetbrains.kotlin.fir.resolve.transformers.ReturnTypeCalculatorForFullBodyResolve
import org.jetbrains.kotlin.fir.resolve.withExpectedType
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.FirRefinementTypeRef
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassType
import org.jetbrains.kotlin.fir.types.toLookupTag
import org.jetbrains.kotlin.fir.visitors.FirDefaultTransformer
import org.jetbrains.kotlin.fir.visitors.transformSingle

open class FirBodyResolveTransformer(
    session: FirSession,
    phase: FirResolvePhase,
    implicitTypeOnly: Boolean,
    scopeSession: ScopeSession,
    returnTypeCalculator: ReturnTypeCalculator = ReturnTypeCalculatorForFullBodyResolve.Default,
    outerBodyResolveContext: BodyResolveContext? = null,
) : FirAbstractBodyResolveTransformerDispatcher(
    session,
    phase,
    implicitTypeOnly,
    scopeSession,
    returnTypeCalculator,
    outerBodyResolveContext,
    expandTypeAliases = true,
) {
    final override val expressionsTransformer: FirExpressionsResolveTransformer = FirExpressionsResolveTransformer(this)
    final override val declarationsTransformer: FirDeclarationsResolveTransformer = FirDeclarationsResolveTransformer(this)

    override fun transformResolvedTypeRef(resolvedTypeRef: FirResolvedTypeRef, data: ResolutionMode): FirTypeRef {
        val transformed = transformTypeRef(resolvedTypeRef, data)
        val refinementTransformed = RefinementTypeRefPredicateTransformer()
            .transformResolvedTypeRef(transformed, null)
        return refinementTransformed
    }

    private inner class RefinementTypeRefPredicateTransformer() : FirDefaultTransformer<Nothing?>() {
        @Suppress("UNCHECKED_CAST")
        override fun <E : FirElement> transformElement(element: E, data: Nothing?): E =
            element.transformChildren(this, data) as E // TODO: Is full traverse necessary?

        override fun transformRefinementTypeRef(refinementTypeRef: FirRefinementTypeRef, data: Nothing?): FirTypeRef =
            refinementTypeRef.transformUnderlyingType(this, data).let {
                val underlyingType = it.underlyingType.coneType
                val expectedType = underlyingType.createPredicate()
                val resolutionMode = withExpectedType(expectedType)
                // TODO: Safe the CFG
                dataFlowAnalyzer.enterRefinementTypePredicate()
                it.transformPredicate(this@FirBodyResolveTransformer, resolutionMode).also {
                    dataFlowAnalyzer.exitRefinementTypePredicate()
                }
            }

        override fun transformResolvedTypeRef(resolvedTypeRef: FirResolvedTypeRef, data: Nothing?): FirResolvedTypeRef {
            resolvedTypeRef.delegatedTypeRef?.transformSingle(this@RefinementTypeRefPredicateTransformer, data)
            return resolvedTypeRef
        }
    }

    private fun ConeKotlinType.createPredicate(): ConeKotlinType {
        val classId = FunctionTypeKind.Function.numberedClassId(1)
        val parameters = listOf(this, session.builtinTypes.booleanType.coneType)
        return classId.toLookupTag().constructClassType(parameters.toTypedArray())
    }
}
