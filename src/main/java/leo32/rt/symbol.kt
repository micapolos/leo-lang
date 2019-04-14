package leo32.rt

import leo.base.*
import leo.binary.bitSeq
import leo.binary.utf8ByteSeq
import leo.binary.zero

data class Symbol(
	val string: String)

fun symbol(string: String): Symbol =
	if (string.isEmpty() || string.contains('\u0000')) fail()
	else Symbol(string)

val Symbol.byteSeq
	get() =
		string.utf8ByteSeq.then { 0.toByte().onlySeq }

val Symbol.bitSeq
	get() =
		byteSeq.map { bitSeq }.flat

fun Scope.at(symbol: Symbol, key: Symbol) =
	notNullIf(symbol == key) { emptyValue }

fun Scope.field(symbol: Symbol) =
	symbol to emptyValue

fun Scope.fieldSeq(symbol: Symbol) =
	seq(field(symbol))

fun Symbol.fieldByteSeq(fn: () -> Seq<Byte>) =
	byteSeq.then {
		fn().then {
			seq(zero.byte)
		}
	}

val argumentSymbol = symbol("argument")
val bitSymbol = symbol("bit")
val entrySymbol = symbol("entry")
val errorSymbol = symbol("error")
val i32Symbol = symbol("i32")
val lhsSymbol = symbol("lhs")
val oneSymbol = symbol("one")
val quoteSymbol = symbol("quote")
val rhsSymbol = symbol("rhs")
val switchSymbol = symbol("switch")
val toSymbol = symbol("to")
val zeroSymbol = symbol("zero")

