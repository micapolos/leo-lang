package leo13.script

import leo13.TypeBindings
import leo13.Types
import leo13.typeBindings
import leo13.types

data class Context(
	val types: Types,
	val functions: Functions,
	val typeBindings: TypeBindings)

fun context() = Context(types(), functions(), typeBindings())
