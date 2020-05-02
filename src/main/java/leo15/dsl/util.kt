package leo15.dsl

import leo.base.println
import leo.base.runWith
import leo14.*
import leo14.untyped.leoString
import leo15.eval
import leo16.nativeString

fun X.number(int: Int) = x(token(literal(int)))
fun X.number(double: Double) = x(token(literal(double)))
fun X.text(string: String) = x(token(literal(string)))
val String.text: X get() = X.text(this)
val Int.number: X get() = X.number(this)
val Double.number: X get() = X.number(this)
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
	emptyFragment.tokenReducer.read(f).state.script

fun main_(f: F) {
	script_(f).eval.leoString.println
}

fun dsl_(f: F): F = f

val Fragment.tokenReducer: Reducer<Fragment, Token>
	get() =
		reducer { plus(it).tokenReducer }