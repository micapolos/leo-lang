package leo15.dsl

import leo.base.println
import leo.base.runWith
import leo14.*
import leo14.untyped.leoString
import leo15.eval
import leo16.nativeString
import java.math.BigDecimal

fun X.number(bigDecimal: BigDecimal) = x(token(literal(leo14.number(bigDecimal))))
fun X.number(int: Int) = x(token(literal(int)))
fun X.number(double: Double) = x(token(literal(double)))
fun X.text(string: String) = x(token(literal(string)))
val Boolean.boolean: X get() = X.boolean { if (this@boolean) true_ else false_ }
val BigDecimal.number: X get() = X.number(this)
val Int.number get() = toBigDecimal().number
val Float.number get() = toBigDecimal().number
val Double.number get() = toBigDecimal().number
val String.text: X get() = X.text(this)
val String.number get() = toBigDecimal().number
val Byte.byte: X get() = X.byte { toInt().toBigDecimal().number }
val Short.short: X get() = X.short { toInt().toBigDecimal().number }
val Int.int: X get() = X.int { toBigDecimal().number }
val Long.long: X get() = X.long { toBigDecimal().number }
val Double.double: X get() = X.double { toBigDecimal().number }
val Float.float: X get() = X.float { toBigDecimal().number }
val String.word_: X get() = X.x(this)
val Any?.native_: X get() = nativeString.word_
val nothing_ = X

fun <T> Reducer<T, Token>.read(f: F): Reducer<T, Token> =
	tokenReducerParameter
		.runWith(this) {
			X.f()
			@Suppress("UNCHECKED_CAST")
			tokenReducerParameter.value as Reducer<T, Token>
		}


fun script_(f: F): Script =
	emptyFragment.tokenReducer.read(f).reduced.script

fun main_(f: F) {
	script_(f).eval.leoString.println
}

fun dsl_(f: F): F = f

val Fragment.tokenReducer: Reducer<Fragment, Token>
	get() =
		reducer { plus(it).tokenReducer }