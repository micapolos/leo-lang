package leo23.compiler.binding

import leo.base.notNullIf
import leo23.term.argExpr
import leo23.type.Type
import leo23.type.Types
import leo23.typed.of
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.termType

data class BindingConstant(val keyTypes: Types, val valueType: Type)

val BindingConstant.indexCount get() = 1

fun BindingConstant.resolveOrNull(index: Int, stackCompiled: StackCompiled): Compiled? =
	notNullIf(keyTypes == stackCompiled.t) {
		argExpr(index, valueType.termType).of(valueType)
	}