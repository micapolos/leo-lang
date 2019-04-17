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

val String.symbol
	get() =
		symbol(this)

fun Symbol.plus(symbol: Symbol) =
	Symbol(nonEmptyNoZerosByteArray.add(symbol.nonEmptyNoZerosByteArray))

fun Symbol.plus(string: String) =
	plus(symbol(string))

val Symbol.byteSeq
	get() =
		nonEmptyNoZerosByteArray.seq.then { 0.toByte().onlySeq }

val Symbol.bitSeq
	get() =
		byteSeq.map { bitSeq }.flat

val Symbol.string
	get() =
		nonEmptyNoZerosByteArray.byteArray.utf8String

fun Appendable.append(symbol: Symbol): Appendable =
	append(symbol.string)

@Suppress("unused")
fun <V : Any> Empty.symbolDict(): Dict<Symbol, V> =
	emptyTrie<V>().dict { bitSeq }

val actualSymbol = symbol("actual")
val argumentSymbol = symbol("argument")
val bitSymbol = symbol("bit")
val bodySymbol = symbol("body")
val caseSymbol = symbol("case")
val classSymbol = symbol("class")
val defineSymbol = symbol("define")
val describeSymbol = symbol("describe")
val eitherSymbol = symbol("either")
val entrySymbol = symbol("entry")
val equalsSymbol = symbol("equals")
val errorSymbol = symbol("error")
val expectedSymbol = symbol("expected")
val givesSymbol = symbol("gives")
val hasSymbol = symbol("has")
val i32Symbol = symbol("i32")
val intSymbol = symbol("int")
val isSymbol = symbol("is")
val keySymbol = symbol("key")
val lhsSymbol = symbol("lhs")
val oneSymbol = symbol("one")
val quoteSymbol = symbol("quote")
val rhsSymbol = symbol("rhs")
val selfSymbol = symbol("self")
val switchSymbol = symbol("switch")
val testSymbol = symbol("test")
val theSymbol = symbol("the")
val toSymbol = symbol("to")
val unquoteSymbol = symbol("unquote")
val zeroSymbol = symbol("zero")

