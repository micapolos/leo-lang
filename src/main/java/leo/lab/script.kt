package leo.lab

import leo.*
import leo.base.*

sealed class Script<out V>

data class MetaScript<V>(
	val meta: Meta<V>) : Script<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class InvokeScript<V>(
	val lhsOrNull: Script<V>?,
	val word: Word,
	val rhsOrNull: Script<V>?) : Script<V>() {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Meta<V>.script: Script<V>
	get() =
		MetaScript(this)

fun <V> Script<V>?.invoke(word: Word, rhsTermOrNull: Script<V>? = null): Script<V> =
	InvokeScript(this, word, rhsTermOrNull)

fun <V> script(word: Word, rhsTermOrNull: Script<V>? = null): Script<V> =
	nullOf<Script<V>>().invoke(word, rhsTermOrNull)

fun <V> Word.script(): Script<V> =
	script(this)

val Word.script: Script<Nothing>
	get() =
		script()

fun <V> metaScript(value: V): Script<V> =
	value.meta.script

val <V> Pair<Word, Script<V>?>.script: Script<V>
	get() =
		script(first, second)

val <V> Field<V>.onlyScript: Script<V>
	get() =
		script(key, value)

fun <V> Script<V>?.invoke(field: Field<V>): Script<V> =
	invoke(field.key, field.value)

fun <V> Script<V>?.invoke(pair: Pair<Word, Script<V>?>): Script<V> =
	invoke(pair.first, pair.second)

fun <V> script(pair: Pair<Word, Script<V>?>, vararg pairs: Pair<Word, Script<V>?>): Script<V> =
	pairs.fold(pair.script) { term, nextPair -> term.invoke(nextPair) }

fun <V> script(script: Script<V>, vararg pairs: Pair<Word, Script<V>?>): Script<V> =
	pairs.fold(script) { term, pair -> term.invoke(pair) }

// === casting

val <V> Script<V>.metaScriptOrNull
	get() =
		this as? MetaScript

val <V> Script<V>.invokeScriptOrNull
	get() =
		this as? InvokeScript

val <V : Any> Script<V>.valueOrNull: V?
	get() =
		metaScriptOrNull?.meta?.value

// === field stream

val <V> Script<V>.fieldStreamOrNull: Stream<Field<V>>?
	get() =
		reversedFieldStreamOrNull?.stack?.stream

val <V> Script<V>.reversedFieldStreamOrNull: Stream<Field<V>>?
	get() =
		when (this) {
			is MetaScript -> null
			is InvokeScript ->
				if (lhsOrNull == null)
					rhsOrNull?.let { rhs ->
						(word fieldTo rhs).onlyStream
					}
				else
					rhsOrNull?.let { rhs ->
						lhsOrNull.reversedFieldStreamOrNull?.run {
							field(word, rhs).then { lhsOrNull.reversedFieldStreamOrNull }
						}
					}
		}

val <V> Stream<Field<V>>.fieldScript: Script<V>
	get() =
		first.onlyScript.fold(nextOrNull, Script<V>::invoke)

// === Appendable (pretty-print)

val <V> Script<V>.isSimple: Boolean
	get() =
		when (this) {
			is MetaScript -> true
			is InvokeScript -> lhsOrNull == null
		}

fun <V> Appendable.append(term: Script<V>): Appendable =
	when (term) {
		is MetaScript -> append(term.meta)
		is InvokeScript ->
			this
				.ifNotNull(term.lhsOrNull) { lhsTerm ->
					append(lhsTerm).append(", ")
				}
				.append(term.word)
				.ifNotNull(term.rhsOrNull) { rhsTerm ->
					if (rhsTerm.isSimple) append(" ").append(rhsTerm)
					else append("(").append(rhsTerm).append(")")
				}
	}

// === stream

val Script<Nothing>.coreString: String
	get() =
		appendableString {
			it.fold(
				tokenStream.mapJoin(Token<Nothing>::characterStream).map(Character::char),
				Appendable::append)
		}

val Script<Nothing>.bitStream: Stream<Bit>
	get() =
		tokenStream
			.mapJoin(Token<Nothing>::characterStream)
			.mapJoin(Character::bitStream)

val <V> Script<V>.tokenStream: Stream<Token<V>>
	get() =
		when (this) {
			is MetaScript -> meta.token.onlyStream
			is InvokeScript -> lhsOrNull?.tokenStream.orNullThen {
				word.token<V>().then {
					begin.control.token<V>().onlyStream.then {
						rhsOrNull?.tokenStream.orNullThen {
							end.control.token<V>().onlyStream
						}
					}
				}
			}
		}

// === access

fun <V> Script<V>.fieldValueStreamOrNull(key: Word): Stream<Script<V>>? =
	fieldStreamOrNull?.filterMap { it.get(key) }

//fun <V> StructureTerm<V>.onlyValueOrNull(key: Word): Term<V>? =
//	valueStreamOrNull(key)?.onlyValueOrNull

//val <V> Script<V>.onlyFieldOrNull: Field<V>?
//	get() =
//		fieldStreamOrNull?.onlyValueOrNull

fun <V, R : Any> Script<V>.matchWord(fn: Word.() -> R?): R? =
	invokeScriptOrNull?.run {
		if (lhsOrNull == null && rhsOrNull == null) word.fn()
		else null
	}

fun <V, R : Any> Script<V>.matchWord(key: Word, fn: () -> R?): R? =
	invokeScriptOrNull?.run {
		if (lhsOrNull == null && rhsOrNull == null && word == key) fn()
		else null
	}

fun <V, R : Any> Script<V>.matchFieldKey(word: Word, fn: Script<V>.() -> R?): R? =
	fieldStreamOrNull?.matchOne { field ->
		field.matchKey(word, fn)
	}

fun <V, R : Any> Script<V>.matchFieldKeys(key1: Word, key2: Word, fn: (Script<V>, Script<V>) -> R?): R? =
	fieldStreamOrNull?.matchFirst({ it.key == key1 }) { field1 ->
		this?.matchFirst({ it.key == key2 }) { field2 ->
			matchNull {
				fn(field1.value, field2.value)
			}
		}
	}

// === selecting

fun <V> Script<V>.select(key: Word): Script<V>? =
	invokeScriptOrNull?.select(key)

fun <V> InvokeScript<V>.select(key: Word): Script<V>? =
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

val Script<*>.isList: Boolean
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

// === map

fun <V, R> Script<V>.map(fn: (V) -> R): Script<R> =
	when (this) {
		is MetaScript -> meta.map(fn).script
		is InvokeScript -> lhsOrNull?.map(fn).invoke(word, rhsOrNull?.map(fn))
	}

