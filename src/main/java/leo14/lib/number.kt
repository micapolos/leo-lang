package leo14.lib

import leo14.lambda.invoke
import leo14.lambda.nativeEval
import leo14.lambda.term
import leo14.native.*
import leo14.typed.numberLine

data class Number(override val term: Term) : Obj() {
	override val typeLine get() = numberLine
	override fun toString() = super.toString()
}

val Term.number get() = Number(this)
val Int.number get() = term(native(this)).number
val Double.number get() = term(native(this)).number

val Number.increment
	get() =
		term(numberIncNative).invoke(term).nativeEval.number

val Number.decrement
	get() =
		term(numberDecNative).invoke(term).nativeEval.number

operator fun Number.plus(number: Number) =
	term(numberPlusNumberNative).invoke(term).invoke(number.term).nativeEval.number

operator fun Number.minus(number: Number) =
	term(numberMinusNumberNative).invoke(term).invoke(number.term).nativeEval.number

operator fun Number.times(number: Number) =
	term(numberTimesNumberNative).invoke(term).invoke(number.term).nativeEval.number

operator fun Number.unaryMinus() = 0.number - this
