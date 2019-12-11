package leo14.lib

import leo14.lambda.invoke
import leo14.lambda.native
import leo14.lambda.nativeEval
import leo14.lambda.term
import leo14.native.*
import leo14.typed.numberLine

data class Number(override val term: Term) : Obj() {
	override val typeLine get() = numberLine
	override fun toString() = super.toString()
	val string get() = term.native.string
	val increment
		get() =
			term(numberIncNative).invoke(term).nativeEval.number
	val decrement
		get() =
			term(numberDecNative).invoke(term).nativeEval.number

	operator fun plus(number: Number) =
		term(numberPlusNumberNative).invoke(term).invoke(number.term).nativeEval.number

	operator fun minus(number: Number) =
		term(numberMinusNumberNative).invoke(term).invoke(number.term).nativeEval.number

	operator fun times(number: Number) =
		term(numberTimesNumberNative).invoke(term).invoke(number.term).nativeEval.number

	operator fun unaryMinus() = 0.number - this
}

val Term.number get() = Number(this)
val Int.number get() = term(native(this)).number
val Double.number get() = term(native(this)).number
