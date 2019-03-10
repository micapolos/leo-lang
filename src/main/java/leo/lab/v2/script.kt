package leo.lab.v2

import leo.*
import leo.base.*

data class Script(
	val lhsOrNull: Script?,
	val word: Word,
	val rhsOrNull: Script?) {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

fun Script?.invoke(word: Word, rhsTermOrNull: Script? = null): Script =
	Script(this, word, rhsTermOrNull)

fun script(word: Word, rhsTermOrNull: Script? = null): Script =
	nullOf<Script>().invoke(word, rhsTermOrNull)

val Word.script: Script
	get() =
		script(this)

val Pair<Word, Script?>.script: Script
	get() =
		script(first, second)

val Field.onlyScript: Script
	get() =
		script(key, value)

fun Script?.invoke(field: Field): Script =
	invoke(field.key, field.value)

fun Script?.invoke(pair: Pair<Word, Script?>): Script =
	invoke(pair.first, pair.second)

fun script(pair: Pair<Word, Script?>, vararg pairs: Pair<Word, Script?>): Script =
	pairs.fold(pair.script) { term, nextPair -> term.invoke(nextPair) }

fun script(script: Script, vararg pairs: Pair<Word, Script?>): Script =
	pairs.fold(script) { term, pair -> term.invoke(pair) }

// === field stream

val Script.fieldStreamOrNull: Stream<Field>?
	get() =
		reversedFieldStreamOrNull?.stack?.stream

val Script.reversedFieldStreamOrNull: Stream<Field>?
	get() =
		if (lhsOrNull == null)
			rhsOrNull?.let { rhs ->
				(word fieldTo rhs).onlyStream
			}
		else
			rhsOrNull?.let { rhs ->
				lhsOrNull.reversedFieldStreamOrNull?.run {
					field(word, rhs).onlyStreamThen { lhsOrNull.reversedFieldStreamOrNull }
				}
			}

val Stream<Field>.fieldScript: Script
	get() =
		first.onlyScript.fold(nextOrNull, Script::invoke)

// === Appendable (pretty-print)

val Script.isSimple: Boolean
	get() =
		lhsOrNull == null

fun Appendable.append(term: Script): Appendable =
	this
		.ifNotNull(term.lhsOrNull) { lhsTerm ->
			append(lhsTerm).append(", ")
		}
		.append(term.word)
		.ifNotNull(term.rhsOrNull) { rhsTerm ->
			if (rhsTerm.isSimple) append(" ").append(rhsTerm)
			else append("(").append(rhsTerm).append(")")
		}

// === stream

val Script.coreString: String
	get() =
		appendableString {
			it.fold(
				tokenStream.mapJoin(Token<Nothing>::characterStream).map(Character::char),
				Appendable::append)
		}

val Script.bitStream: Stream<EnumBit>
	get() =
		tokenStream
			.mapJoin(Token<Nothing>::characterStream)
			.mapJoin(Character::bitStream)

val Script.tokenStream: Stream<Token<Nothing>>
	get() =
		lhsOrNull?.tokenStream.orNullThen {
			word.token<Nothing>().onlyStreamThen {
				begin.control.token<Nothing>().onlyStream.then {
					rhsOrNull?.tokenStream.orNullThen {
						end.control.token<Nothing>().onlyStream
					}
				}
			}
		}

// === access

fun Script.fieldValueStreamOrNull(key: Word): Stream<Script>? =
	fieldStreamOrNull?.filterMap { it.get(key) }

//fun <V> StructureTerm<V>.onlyValueOrNull(key: Word): Term<V>? =
//	valueStreamOrNull(key)?.onlyValueOrNull

//val <V> Script<V>.onlyFieldOrNull: Field<V>?
//	get() =
//		fieldStreamOrNull?.onlyValueOrNull

fun <R : Any> Script.matchWord(fn: Word.() -> R?): R? =
	if (lhsOrNull == null && rhsOrNull == null) word.fn()
	else null

fun <R : Any> Script.matchWord(key: Word, fn: () -> R?): R? =
	if (lhsOrNull == null && rhsOrNull == null && word == key) fn()
	else null

fun <R : Any> Script.matchFieldKey(word: Word, fn: Script.() -> R?): R? =
	fieldStreamOrNull?.matchOne { field ->
		field.matchKey(word, fn)
	}

fun <R : Any> Script.matchFieldKeys(key1: Word, key2: Word, fn: (Script, Script) -> R?): R? =
	fieldStreamOrNull?.matchFirst({ it.key == key1 }) { field1 ->
		this?.matchFirst({ it.key == key2 }) { field2 ->
			matchNull {
				fn(field1.value, field2.value)
			}
		}
	}

// === selecting

fun Script.select(key: Word): Script? =
	if (word == theWord && rhsOrNull != null && lhsOrNull?.isList == true)
		when (key) {
			lastWord -> rhsOrNull
			previousWord -> lhsOrNull
			else -> null
		}
	else fieldValueStreamOrNull(key)?.run {
		nextOrNull.let { nextOrNull ->
			if (nextOrNull == null) first
			else map { value -> theWord fieldTo value }.fieldScript
		}
	}

val Script.isList: Boolean
	get() =
		fieldStreamOrNull.run {
			this != null && all { it.key != theWord } == null
		}

//// === reflect
//
//val Term<Nothing>.reflect: Field<Nothing>
//	get() =
//		termWord fieldTo this
//
//fun <V> Term<V>.reflectMetaTerm(valueReflect: V.() -> Term<Nothing>): Term<Nothing> =
//	when (this) {
//		is MetaTerm -> meta.reflectMeta(valueReflect)
//		is WordTerm -> word.term
//		is StructureTerm -> structure.reflectMetaTerm(valueReflect)
//	}
//
//fun <V> Term<V>.reflectMeta(valueReflect: V.() -> Field<Nothing>): Term<Nothing> =
//	reflectMetaTerm { valueReflect(this).onlyTerm }
//
//val Field<Nothing>.parseTerm: Term<Nothing>?
//	get() =
//		matchKey(termWord) {
//			parseTerm { null }
//		}
//
//fun <V> Term<Nothing>.parseTerm(parseValue: (Term<Nothing>) -> V?): Term<V>? =
//	parseValue(this)?.meta?.term ?: when (this) {
//		is MetaTerm -> fail
//		is WordTerm -> word.term()
//		is StructureTerm -> parseStructure(parseValue)?.term
//	}
//
//// === select
//
//val Term<Nothing>.evaluateSelect: Term<Nothing>
//	get() =
//		onlyFieldOrNull?.run {
//			value.select(key)
//		} ?: this

val Script.commandStream: Stream<Command>
	get() =
		lhsOrNull?.commandStream.orNullThen {
			begin.command(word).onlyStreamThen {
				rhsOrNull?.commandStream.orNullThen {
					end.command.onlyStream
				}
			}
		}
