/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.lower

import org.jetbrains.kotlin.backend.common.*
import org.jetbrains.kotlin.backend.common.phaser.PhaseDescription
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.expressions.impl.IrTypeOperatorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.symbols.IrRefinementSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.Name

@PhaseDescription(name = "RefinementTypesLowering")
class RefinementTypesLowering(private val context: LoweringContext) : ModuleLoweringPass {
    private val phases = listOf(
        RefinementTypeOperationLowering(context),
        RefinementTypeErasureLowering(context),
        RefinementPredicateLowering(context),
    )

    override fun lower(irModule: IrModuleFragment) {
        for (phase in phases) {
            phase.lower(irModule)
        }
    }
}

private class RefinementTypeOperationLowering(private val context: LoweringContext) : BodyLoweringPass {
    override fun lower(irBody: IrBody, container: IrDeclaration) {
        irBody.transformChildrenVoid(RefinementTypeOperationTransformer(context, container))
    }
}

private class RefinementTypeOperationTransformer(
    val context: LoweringContext,
    val container: IrSymbolOwner
) : IrElementTransformerVoidWithContext() {
    private val throwTypeCastException = context.symbols.throwTypeCastException

    override fun visitTypeOperator(expression: IrTypeOperatorCall): IrExpression {
        val transformed = super.visitTypeOperator(expression) as IrTypeOperatorCall
        val isRefinement = expression.typeOperand.classifierOrNull?.let { it is IrRefinementSymbol } == true
        if (!isRefinement) return transformed

        val result = context.createIrBuilder(
            container.symbol,
            expression.startOffset,
            expression.endOffset
        ).buildTypeOperator(
            expression.operator,
            expression.type,
            expression.argument,
            expression.typeOperand
        )

        return result
    }

    private fun IrBuilderWithScope.buildTypeOperator(
        operator: IrTypeOperator,
        type: IrType,
        argument: IrExpression,
        typeOperand: IrType,
    ): IrExpression {
        val refinementSymbol = typeOperand.classifierOrNull as? IrRefinementSymbol
        if (refinementSymbol == null) return IrTypeOperatorCallImpl(
            startOffset,
            endOffset,
            type,
            operator,
            typeOperand,
            argument
        )

        return when (operator) {
            IrTypeOperator.CAST -> {
                val newType = refinementSymbol.underlyingType
                irLetS(
                    buildTypeOperator(
                        operator,
                        newType,
                        argument,
                        refinementSymbol.underlyingType
                    )
                ) { op ->
                    irIfThenElse(
                        type,
                        irCall(refinementSymbol.predicateSymbol).apply { arguments[0] = irGet(op.owner) },
                        irGet(op.owner),
                        irCall(throwTypeCastException).apply {
                            arguments[0] = irString("Refinement check for ${typeOperand.render()} failed")
                        }
                    )
                }
            }
            IrTypeOperator.SAFE_CAST -> {
                val newType = refinementSymbol.underlyingType.makeNullable()
                irLetS(
                    buildTypeOperator(
                        operator,
                        newType,
                        argument,
                        refinementSymbol.underlyingType
                    )
                ) { op ->
                    irWhen(
                        type, listOf(
                            irBranch(irEqualsNull(irGet(op.owner)), irNull()),
                            irBranch(
                                irCall(refinementSymbol.predicateSymbol).apply { arguments[0] = irGet(op.owner) },
                                irGet(op.owner)
                            ),
                            irElseBranch(irNull())
                        )
                    )
                }
            }
            IrTypeOperator.INSTANCEOF, IrTypeOperator.NOT_INSTANCEOF -> irLetS(argument) { arg ->
                val predicateCall = irCall(refinementSymbol.predicateSymbol).apply { arguments[0] = irGet(arg.owner) }
                val (thenBranch, elseBranch) = when (operator) {
                    IrTypeOperator.INSTANCEOF -> predicateCall to irFalse()
                    IrTypeOperator.NOT_INSTANCEOF -> irFalse() to irNot(predicateCall)
                    else -> error("Unreachable")
                }

                irIfThenElse(
                    type,
                    buildTypeOperator(operator, type, irGet(arg.owner), refinementSymbol.underlyingType),
                    thenBranch,
                    elseBranch,
                )
            }
            else -> TODO()
        }
    }

}

private class RefinementTypeErasureLowering(private val context: LoweringContext) : FileLoweringPass {
    override fun lower(irFile: IrFile) {
        val symbolRemapper = object : SymbolRemapper.Empty() {
            override fun getReferencedRefinement(symbol: IrRefinementSymbol): IrClassifierSymbol =
                symbol.representationType.classifierOrFail
        }
        irFile.remapTypes(SimpleTypeRemapper(symbolRemapper))
    }
}

private class RefinementPredicateLowering(private val context: LoweringContext) : DeclarationTransformer {
    override fun transformFlat(declaration: IrDeclaration): List<IrDeclaration>? {
        if (declaration !is IrRefinement) return null

        val function = declaration.predicate.function
        val parent = declaration.parent

        function.parent = parent
        function.name = Name.identifier($$"$${declaration.name}$predicate")

        return listOf(function)
    }
}