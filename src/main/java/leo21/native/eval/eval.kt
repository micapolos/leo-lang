package leo21.native.eval

import leo.stak.Stak
import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import leo14.Number
import leo14.minus
import leo14.number
import leo14.plus
import leo14.string
import leo14.times
import leo21.native.ApplyNative
import leo21.native.BooleanNative
import leo21.native.ConditionalNative
import leo21.native.EqualsNative
import leo21.native.FunctionNative
import leo21.native.IsNilNative
import leo21.native.LhsNative
import leo21.native.MinusNative
import leo21.native.Native
import leo21.native.NilNative
import leo21.native.NumberNative
import leo21.native.NumberStringNative
import leo21.native.PairNative
import leo21.native.PlusNative
import leo21.native.RhsNative
import leo21.native.StringAppendNative
import leo21.native.StringEqualsNative
import leo21.native.StringNative
import leo21.native.StringNumberOrNilNative
import leo21.native.TimesNative
import leo21.native.VariableNative
import leo21.native.VectorAtNative
import leo21.native.VectorNative

typealias Value = Any
typealias Scope = Stak<Value>

data class Fn(val scope: Scope, val body: Native)

fun Fn.apply(params: List<Value>): Value = params.fold(scope) { acc, value -> acc.push(value) }.eval(body)

val Native.eval: Value get() = stakOf<Value>().eval(this)

// TODO: Tail recursion!!!
fun Scope.eval(native: Native): Value =
	when (native) {
		NilNative -> Unit
		is BooleanNative -> native.boolean
		is StringNative -> native.string
		is NumberNative -> native.number
		is IsNilNative -> eval(native.lhs) == Unit
		is PlusNative -> (eval(native.lhs) as Number).plus(eval(native.rhs) as Number)
		is MinusNative -> (eval(native.lhs) as Number).minus(eval(native.rhs) as Number)
		is TimesNative -> (eval(native.lhs) as Number).times(eval(native.rhs) as Number)
		is EqualsNative -> (eval(native.lhs) as Number) == (eval(native.rhs) as Number)
		is NumberStringNative -> (eval(native.number) as Number).string
		is StringAppendNative -> (eval(native.lhs) as String).plus(eval(native.rhs) as String)
		is StringEqualsNative -> (eval(native.lhs) as String) == (eval(native.rhs) as String)
		is StringNumberOrNilNative -> (eval(native.string) as String).toIntOrNull()?.number ?: Unit
		is PairNative -> eval(native.lhs) to eval(native.rhs)
		is LhsNative -> (eval(native.pair) as Pair<Value, Value>).first
		is RhsNative -> (eval(native.pair) as Pair<Value, Value>).second
		is VectorNative -> (native.list.map { eval(it) })
		is VectorAtNative -> (eval(native.vector) as List<Value>).get((eval(native.index) as Number).bigDecimal.intValueExact())
		is ConditionalNative -> if (eval(native.cond) as Boolean) eval(native.caseTrue) else eval(native.caseFalse)
		is FunctionNative -> Fn(this, native.body)
		is ApplyNative -> (eval(native.function) as Fn).apply(native.paramList.map { eval(it) })
		is VariableNative -> top(native.index)!!
	}
