package leo14.untyped.dsl2

import leo.base.println
import leo.base.runWith
import leo14.*
import leo14.untyped.*
import leo15.eval

fun X.number(int: Int) = x(leo14.token(leo14.literal(int)))
fun X.number(double: Double) = x(leo14.token(leo14.literal(double)))
fun X.text(string: String) = x(leo14.token(leo14.literal(string)))
val nothing_ = X

fun run_(f: F): Unit = readerParameter.runWith(emptyReader) { X.f() }
fun resolver_(f: F): Resolver = run_(f).run { readerParameter.value.rootResolverOrNull!! }
fun library_(f: F) = f
fun Reader.read(f: F): Reader = readerParameter.runWith(this) { X.f(); readerParameter.value }

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

val Fragment.tokenReducer: Reducer<Fragment, Token>
	get() =
		reducer { plus(it).tokenReducer }