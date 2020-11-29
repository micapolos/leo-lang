package leo23.compiler.binding

import leo.base.notNullIf
import leo13.map
import leo13.toList
import leo23.term.argExpr
import leo23.term.type.does
import leo23.type.Type
import leo23.type.Types
import leo23.typed.of
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.termType

data class BindingFunction(val types: Types, val doesType: Type)

val BindingFunction.indexCount get() = 1

fun BindingFunction.resolveOrNull(index: Int, stackCompiled: StackCompiled): Compiled? =
	notNullIf(types == stackCompiled.t) {
		argExpr(index, types.map { termType }.toList().does(doesType.termType)).of(doesType)
	}