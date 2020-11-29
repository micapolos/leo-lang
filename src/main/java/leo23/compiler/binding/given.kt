package leo23.compiler.binding

import leo23.term.argExpr
import leo23.type.Types
import leo23.type.exprCount
import leo23.type.indexedTypeOrNull
import leo23.typed.of
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.nameOrNull
import leo23.typed.term.termType

data class Given(val types: Types)

val Given.indexCount get() = types.exprCount

fun Given.resolveOrNull(index: Int, stackCompiled: StackCompiled): Compiled? =
	stackCompiled.nameOrNull?.let { name ->
		types.indexedTypeOrNull(name)?.let { indexedType ->
			argExpr(index + indexedType.index, indexedType.value.termType).of(indexedType.value)
		}
	}
