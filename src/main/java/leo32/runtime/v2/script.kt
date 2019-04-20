package leo32.runtime.v2

import leo.base.*
import leo32.runtime.Symbol
import leo32.runtime.byteSeq

data class Script(
	val symbol: Symbol,
	val arguments: Arguments,
	val symbolTreeOrNull: Dict<Symbol, The<Script?>>?)

val Script.byteSeq: Seq<Byte>
	get() =
		seq(
			arguments.lhsOrNull?.byteSeq.orIfNull { seq() },
			symbol.byteSeq,
			arguments.rhsOrNull?.byteSeq.orIfNull { seq() },
			0.toByte().onlySeq).flat

fun script(symbol: Symbol) =
	Script(
		symbol,
		empty.arguments,
		null)

fun Script.plus(symbol: Symbol) =
	Script(
		symbol,
		arguments(this),
		empty.symbolDict<The<Script?>>().put(symbol, the(this)))

fun Script.plus(field: Field) =
	Script(
		field.symbol,
		arguments(this to field.script),
		symbolTreeOrNull?.update(field.symbol) {
			if (this == null) the(field.script)
			else the(null)
		})

fun Script.at(symbol: Symbol): Script? =
	symbolTreeOrNull?.at(symbol)?.value