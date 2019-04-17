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
val anythingSymbol = symbol("anything")
val argumentSymbol = symbol("argument")
val barSymbol = symbol("bar")
val bitSymbol = symbol("bit")
val blueSymbol = symbol("blue")
val bodySymbol = symbol("body")
val booleanSymbol = symbol("boolean")
val caseSymbol = symbol("case")
val centerSymbol = symbol("center")
val circleSymbol = symbol("circle")
val classSymbol = symbol("class")
val colorSymbol = symbol("color")
val defineSymbol = symbol("define")
val describeSymbol = symbol("describe")
val dwaSymbol = symbol("dwa")
val eitherSymbol = symbol("either")
val entrySymbol = symbol("entry")
val equalsSymbol = symbol("equals")
val errorSymbol = symbol("error")
val expectedSymbol = symbol("expected")
val falseSymbol = symbol("false")
val fooSymbol = symbol("foo")
val fourSymbol = symbol("four")
val givesSymbol = symbol("gives")
val greenSymbol = symbol("green")
val hasSymbol = symbol("has")
val i32Symbol = symbol("i32")
val intSymbol = symbol("int")
val jedenSymbol = symbol("jeden")
val isSymbol = symbol("is")
val itSymbol = symbol("it")
val keySymbol = symbol("key")
val lhsSymbol = symbol("lhs")
val notSymbol = symbol("not")
val negateSymbol = symbol("negate")
val oneSymbol = symbol("one")
val pencilSymbol = symbol("pencil")
val quoteSymbol = symbol("quote")
val radiusSymbol = symbol("radius")
val redSymbol = symbol("red")
val resolvedSymbol = symbol("resolved")
val rhsSymbol = symbol("rhs")
val selfSymbol = symbol("self")
val switchSymbol = symbol("switch")
val testSymbol = symbol("test")
val theSymbol = symbol("the")
val threeSymbol = symbol("three")
val toSymbol = symbol("to")
val trueSymbol = symbol("true")
val twoSymbol = symbol("two")
val unquoteSymbol = symbol("unquote")
val vecSymbol = symbol("vec")
val xSymbol = symbol("x")
val ySymbol = symbol("y")
val zeroSymbol = symbol("zero")

