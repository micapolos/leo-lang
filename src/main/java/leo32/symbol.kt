package leo32

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

val Byte.symbolOrNull
	get() =
		notNullIf(this != 0.toByte()) {
			Symbol(list<Byte>().add(this))
		}

fun Symbol.plus(byte: Byte): Symbol? =
	notNullIf(byte != 0.toByte()) {
		Symbol(nonEmptyNoZerosByteArray.add(byte))
	}

fun Symbol?.orNullPlus(byte: Byte): Symbol? =
	if (this == null) byte.symbolOrNull
	else plus(byte)

fun Symbol.plus(symbol: Symbol): Symbol? =
	Symbol(nonEmptyNoZerosByteArray.add(symbol.nonEmptyNoZerosByteArray))

fun Symbol.plus(string: String) =
	plus(symbol(string))

val Symbol.byteSeq
	get() =
		noTrailingZeroByteSeq.then { 0.toByte().onlySeq }

val Symbol.noTrailingZeroByteSeq
	get() =
		nonEmptyNoZerosByteArray.seq

val Symbol.bitSeq
	get() =
		byteSeq.map { bitSeq }.flat

val Symbol.string
	get() =
		nonEmptyNoZerosByteArray.byteArray.utf8String

fun Appendable.append(symbol: Symbol): Appendable =
	append(symbol.string)

val Symbol.stringField
	get() =
		stringSymbol to term(this)

fun Symbol.fieldNameContains(symbol: Symbol): Boolean =
	this == symbol

fun Symbol.fieldNameUnion(symbol: Symbol): Symbol? =
	notNullIf(this == symbol) { this }

val actualSymbol = symbol("actual")
val andSymbol = symbol("and")
val anythingSymbol = symbol("anything")
val arraySymbol = symbol("array")
val argumentSymbol = symbol("argument")
val barSymbol = symbol("bar")
val bitSymbol = symbol("bit")
val blueSymbol = symbol("blue")
val bodySymbol = symbol("body")
val booleanSymbol = symbol("boolean")
val caseSymbol = symbol("case")
val centerSymbol = symbol("center")
val circleSymbol = symbol("circle")
val charSymbol = symbol("char")
val classSymbol = symbol("class")
val colorSymbol = symbol("color")
val commentSymbol = symbol("comment")
val defineSymbol = symbol("define")
val describeSymbol = symbol("describe")
val dispatchSymbol = symbol("dispatch")
val doSymbol = symbol("do")
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
val isSymbol = symbol("is")
val itSymbol = symbol("it")
val jedenSymbol = symbol("jeden")
val keySymbol = symbol("key")
val lhsSymbol = symbol("lhs")
val lineSymbol = symbol("line")
val maybeSymbol = symbol("maybe")
val notSymbol = symbol("not")
val negateSymbol = symbol("negate")
val oneSymbol = symbol("one")
val orSymbol = symbol("or")
val pencilSymbol = symbol("pencil")
val pingSymbol = symbol("ping")
val plusSymbol = symbol("plus")
val pongSymbol = symbol("pong")
val printSymbol = symbol("print")
val quoteSymbol = symbol("quote")
val radiusSymbol = symbol("radius")
val redSymbol = symbol("red")
val resolvedSymbol = symbol("resolved")
val rhsSymbol = symbol("rhs")
val scriptSymbol = symbol("script")
val selfSymbol = symbol("self")
val stringSymbol = symbol("string")
val switchSymbol = symbol("switch")
val testSymbol = symbol("test")
val theSymbol = symbol("the")
val threeSymbol = symbol("three")
val toSymbol = symbol("to")
val trueSymbol = symbol("true")
val twoSymbol = symbol("two")
val unquoteSymbol = symbol("unquote")
val vecSymbol = symbol("vec")
val withSymbol = symbol("with")
val xSymbol = symbol("x")
val ySymbol = symbol("y")
val zSymbol = symbol("z")
val zeroSymbol = symbol("zero")

