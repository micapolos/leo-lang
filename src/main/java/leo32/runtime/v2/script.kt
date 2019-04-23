package leo32.runtime.v2

import leo.base.*
import leo.binary.zero
import leo32.base.*
import leo32.base.List
import leo32.runtime.Symbol
import leo32.runtime.byteSeq

data class Script(
	val symbol: Symbol,
	val arguments: Arguments,
	val symbolDictOrNull: Dict<Symbol, The<Script?>>?,
	val firstSymbolOrNull: Symbol?,
	val fieldList: List<Field>,
	val block: Block)

val Script.byteSeq: Seq<Byte>
	get() =
		seq(
			arguments.lhsOrNull?.byteSeq.orIfNull { seq() },
			symbol.byteSeq,
			arguments.rhsOrNull?.byteSeq.orIfNull { seq() },
			0.toByte().onlySeq).flat

fun script(symbol: Symbol, vararg fields: Field) =
	Script(
		symbol,
		empty.arguments,
		null,
		symbol,
		list(),
		block(primitive(symbol)))
		.fold(fields) { plus(it) }

fun script(field: Field, vararg fields: Field) =
	Script(
		field.symbol,
		arguments(field.script),
		empty.symbolDict<The<Script?>>().put(field.symbol, the(field.script)),
		null,
		list<Field>().add(field),
		block(primitive(field)))
		.fold(fields) { plus(it) }

fun Script.plus(symbol: Symbol) =
	Script(
		symbol,
		arguments(this),
		empty.symbolDict<The<Script?>>().put(symbol, the(this)),
		null,
		list(symbol to this),
		block(primitive(symbol to this)))

fun Script.plus(field: Field) =
	Script(
		field.symbol,
		arguments(this to field.script),
		symbolDictOrNull?.update(field.symbol) {
			if (this == null) the(field.script)
			else the(null)
		},
		firstSymbolOrNull,
		fieldList.add(field),
		block.plus(field))

fun Script.at(symbol: Symbol): Script? =
	symbolDictOrNull?.at(symbol)?.value

fun Script.simpleAt(symbol: Symbol): Script? =
	ifOrNull(firstSymbolOrNull == null) {
		fieldList.onlyOrNull?.let { field ->
			ifOrNull(field.symbol == symbol) {
				field.script
			}
		}
	}

val Script.simpleSymbolOrNull: Symbol?
	get() =
		ifOrNull(fieldList.isEmpty) {
			firstSymbolOrNull
		}

val Script.simpleFieldOrNull: Field?
	get() =
		ifOrNull(firstSymbolOrNull == null && fieldList.size.int == 1) {
			fieldList.at(zero.i32)
		}

val Script.isSimple
	get() =
		if (simpleSymbolOrNull != null) fieldList.isEmpty
		else fieldList.isSingleton
