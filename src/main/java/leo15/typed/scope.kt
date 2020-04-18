package leo15.typed

import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import leo13.*
import leo15.lambda.*

data class Binding(val fromType: Term, val toType: Term)

infix fun Term.bindingTo(to: Term) = Binding(this, to)

val intPlusIntType = intType.append(plusName.valueTerm.append(intType))
val textPlusTextType = textType.append(plusName.valueTerm.append(textType))

val intPlusIntFunction: Term
	get() =
		fn(
			termFn { lhs ->
				termFn { rhs ->
					(lhs.value as Int).plus(rhs.value as Int).valueTerm
				}
			}.invoke(at(0).first).invoke(at(0).second))

val textPlusTextFunction: Term
	get() =
		fn(
			termFn { lhs ->
				termFn { rhs ->
					(lhs.value as String).plus(rhs.value as String).valueTerm
				}
			}.invoke(at(0).first).invoke(at(0).second))

var bindings = stack<Binding>()
var functionThunks = stakOf<Thunk>()

fun define(from: Term, to: Term, fn: Term) {
	bindings = bindings.push(from bindingTo to)
	functionThunks = functionThunks.push(fn.thunk)
}

fun runLeo(fn: () -> Unit) {
	val previousBindings = bindings
	val previousThunks = functionThunks
	define(intPlusIntType, intType, intPlusIntFunction)
	define(textPlusTextType, textType, textPlusTextFunction)
	try {
		fn()
	} finally {
		bindings = previousBindings
		functionThunks = previousThunks
	}
}

fun scopeApply(typed: Typed): Typed? =
	bindings
		.firstIndexed { fromType == typed.type }
		?.let { indexedBinding ->
			(bindings.size - indexedBinding.index - 1).let { bottomIndex ->
				thunkFn { arg ->
					functionThunks
						.top(bottomIndex)!!.let { thunk ->
							thunk.apply(arg)
						}
				}.invoke(typed.term) of indexedBinding.value.toType
			}
		}
