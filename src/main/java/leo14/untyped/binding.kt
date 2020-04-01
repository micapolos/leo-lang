package leo14.untyped

import leo.base.notNullIf

data class Binding(val key: Thunk, val value: Thunk)

fun binding(key: Thunk, value: Thunk) = Binding(key, value)
fun Thunk.bindingTo(value: Thunk) = Binding(this, value)
fun Binding.apply(thunk: Thunk): Thunk? = notNullIf(key == thunk) { value }

val Thunk.parseBinding: Binding?
	get() =
		null
			?: parseIsBinding
			?: parseAsBinding

val Thunk.parseIsBinding: Binding?
	get() =
		value.sequenceOrNull?.matchInfixOrPrefix(isName) { lhs, rhs ->
			lhs.bindingTo(rhs)
		}

val Thunk.parseAsBinding: Binding?
	get() =
		matchInfix(asName) { lhs, rhs ->
			rhs.bindingTo(lhs)
		}
