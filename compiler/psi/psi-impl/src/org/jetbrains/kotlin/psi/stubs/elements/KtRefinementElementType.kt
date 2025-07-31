/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi.stubs.elements

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.jetbrains.kotlin.psi.KtRefinement
import org.jetbrains.kotlin.psi.psiUtil.safeFqNameForLazyResolve
import org.jetbrains.kotlin.psi.stubs.KotlinRefinementStub
import org.jetbrains.kotlin.psi.stubs.StubUtils
import org.jetbrains.kotlin.psi.stubs.impl.KotlinRefinementStubImpl

class KtRefinementElementType(debugName: String) :
    KtStubElementType<KotlinRefinementStub, KtRefinement>(debugName, KtRefinement::class.java, KotlinRefinementStub::class.java) {

    override fun createStub(psi: KtRefinement, parentStub: StubElement<*>): KotlinRefinementStub {
        val name = StringRef.fromString(psi.name)
        val fqName = StringRef.fromString(psi.safeFqNameForLazyResolve()?.asString())
        val classId = StubUtils.createNestedClassId(parentStub, psi)
        return KotlinRefinementStubImpl(parentStub, name, fqName, classId)
    }

    override fun serialize(stub: KotlinRefinementStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeName(stub.getFqName()?.asString())
        StubUtils.serializeClassId(dataStream, stub.getClassId())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): KotlinRefinementStub {
        val name = dataStream.readName()
        val fqName = dataStream.readName()
        val classId = StubUtils.deserializeClassId(dataStream)
        return KotlinRefinementStubImpl(parentStub, name, fqName, classId)
    }

    override fun indexStub(stub: KotlinRefinementStub, sink: IndexSink) {
        StubIndexService.getInstance().indexRefinement(stub, sink)
    }
}
