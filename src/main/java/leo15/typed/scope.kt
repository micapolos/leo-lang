package leo15.typed

import leo.stak.bottom
import leo.stak.stakOf
import leo13.firstIndexed
import leo13.plusName
import leo13.size
import leo13.stack
import leo15.lambda.*

data class Binding(val fromType: Term, val toType: Term)

infix fun Term.bindingTo(to: Term) = Binding(this, to)

val intPlusIntType = intType.append(plusName.valueTerm.append(intType))
val textPlusTextType = textType.append(plusName.valueTerm.append(textType))

val intPlusIntFunction: Term
	get() =
		fn(
			fn { lhs ->
				fn { rhs ->
					(lhs.value as Int).plus(rhs.value as Int).valueTerm
				}
			}.invoke(at(0).first).invoke(at(0).second))

val textPlusTextFunction: Term
	get() =
		fn(
			fn { lhs ->
				fn { rhs ->
					(lhs.value as String).plus(rhs.value as String).valueTerm
				}
			}.invoke(at(0).first).invoke(at(0).second))

var bindings = stack(
	//intPlusIntType,
	textPlusTextType bindingTo textType
)

var functions = stakOf(
	//intPlusIntFunction,
	textPlusTextFunction
)

fun scopeApply(typed: Typed): Typed? =
	bindings
		.firstIndexed { fromType == typed.type }
		?.let { indexedBinding ->
			(bindings.size - indexedBinding.index - 1).let { bottomIndex ->
				fn { arg ->
					functions
						.bottom(bottomIndex)!!.let { function ->
							function.invoke(arg)
						}
				}.invoke(typed.term) of indexedBinding.value.toType
			}
		}
