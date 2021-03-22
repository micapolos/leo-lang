package leo23.compiler.type

import leo13.Stack
import leo13.stack
import leo23.type.Types

typealias TypeBindings = Stack<TypeBinding>

val emptyTypeBindings: TypeBindings get() = stack()

fun TypeBindings.resolve(types: Types): Types =
	TODO()