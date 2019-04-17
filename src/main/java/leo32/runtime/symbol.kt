package leo32.runtime

import leo.base.*
import leo.binary.bitSeq
import leo.binary.utf8ByteSeq
import leo32.base.*
import leo32.base.List

data class Symbol(
	val nonEmptyNoZerosByteArray: List<Byte>) {
	override fun toString() = string
}

fun symbol(string: String): Symbol =
	list<Byte>()
		.fold(string.utf8ByteSeq) { failIfOr(it == 0.toByte()) { add(it) } }
		.run { failIfOr(isEmpty) { Symbol(this) } }

val Symbol.byteSeq
	get() =
		string.utf8ByteSeq.then { 0.toByte().onlySeq }

val Symbol.bitSeq
	get() =
		byteSeq.map { bitSeq }.flat

val Symbol.string
	get() =
		nonEmptyNoZerosByteArray.byteArray.utf8String

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

