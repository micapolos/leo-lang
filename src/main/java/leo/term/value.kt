package leo.term

import leo.Word
import leo.base.fold

sealed class Value

data class UniqueValue(
	val unique: Unique<Value>) : Value()

data class TermValue(
	val term: Term<Value>) : Value()

// === constructors

val Term<Value>.value: Value
	get() =
		TermValue(this)

fun value(unique: Unique<Value>): Value =
	unique.value

val Word.value: Value
	get() =
		term<Value>().value

fun value(word: Word): Value =
	word.value

fun value(value: Value, vararg applications: Application<Value>): Value =
	value.fold(applications) { apply(it).value }

fun value(application: Application<Value>, vararg applications: Application<Value>): Value =
	value(application.term.value, *applications)

// === parsing

val Script.value: Value
	get() = null
		?: parseUnique { this?.value }?.value
		?: term.receiverOrNull?.value.apply(term.application.word, term.application.argumentOrNull?.value).value