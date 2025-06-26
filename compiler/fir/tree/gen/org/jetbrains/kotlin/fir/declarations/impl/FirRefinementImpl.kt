/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// This file was generated automatically. See compiler/fir/tree/tree-generator/Readme.md.
// DO NOT MODIFY IT MANUALLY.

@file:Suppress("DuplicatedCode")

package org.jetbrains.kotlin.fir.declarations.impl

import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.FirImplementationDetail
import org.jetbrains.kotlin.fir.FirModuleData
import org.jetbrains.kotlin.fir.MutableOrEmptyList
import org.jetbrains.kotlin.fir.builder.toMutableOrEmpty
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirAnonymousFunctionExpression
import org.jetbrains.kotlin.fir.scopes.FirScopeProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.fir.visitors.FirVisitor
import org.jetbrains.kotlin.fir.visitors.transformInplace
import org.jetbrains.kotlin.name.Name

@OptIn(FirImplementationDetail::class, ResolveStateAccess::class)
internal class FirRefinementImpl(
    override val source: KtSourceElement?,
    resolvePhase: FirResolvePhase,
    override var annotations: MutableOrEmptyList<FirAnnotation>,
    override val moduleData: FirModuleData,
    override val origin: FirDeclarationOrigin,
    override val attributes: FirDeclarationAttributes,
    override val typeParameters: MutableList<FirTypeParameterRef>,
    override var status: FirDeclarationStatus,
    override val symbol: FirClassLikeSymbol<FirClassLikeDeclaration>,
    override var deprecationsProvider: DeprecationsProvider,
    override val scopeProvider: FirScopeProvider,
    override val name: Name,
    override var underlyingType: FirTypeRef,
    override var predicate: FirAnonymousFunctionExpression,
) : FirRefinement() {

    init {
        symbol.bind(this)
        resolveState = resolvePhase.asResolveState()
        @Suppress("SENSELESS_COMPARISON")
        require(source != null || origin != FirDeclarationOrigin.Source) { "${this::class.simpleName} with Source origin was instantiated without a source element." }
    }

    override fun <R, D> acceptChildren(visitor: FirVisitor<R, D>, data: D) {
        annotations.forEach { it.accept(visitor, data) }
        typeParameters.forEach { it.accept(visitor, data) }
        status.accept(visitor, data)
        underlyingType.accept(visitor, data)
        predicate.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        transformAnnotations(transformer, data)
        transformTypeParameters(transformer, data)
        transformStatus(transformer, data)
        transformUnderlyingType(transformer, data)
        transformPredicate(transformer, data)
        return this
    }

    override fun <D> transformAnnotations(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        annotations.transformInplace(transformer, data)
        return this
    }

    override fun <D> transformTypeParameters(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        typeParameters.transformInplace(transformer, data)
        return this
    }

    override fun <D> transformStatus(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        status = status.transform(transformer, data)
        return this
    }

    override fun <D> transformUnderlyingType(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        underlyingType = underlyingType.transform(transformer, data)
        return this
    }

    override fun <D> transformPredicate(transformer: FirTransformer<D>, data: D): FirRefinementImpl {
        predicate = predicate.transform(transformer, data)
        return this
    }

    override fun replaceAnnotations(newAnnotations: List<FirAnnotation>) {
        annotations = newAnnotations.toMutableOrEmpty()
    }

    override fun replaceStatus(newStatus: FirDeclarationStatus) {
        status = newStatus
    }

    override fun replaceDeprecationsProvider(newDeprecationsProvider: DeprecationsProvider) {
        deprecationsProvider = newDeprecationsProvider
    }

    override fun replaceUnderlyingType(newUnderlyingType: FirTypeRef) {
        underlyingType = newUnderlyingType
    }
}
