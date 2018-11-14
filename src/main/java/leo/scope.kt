package leo

import leo.base.Stream
import leo.base.onlyStream
import leo.base.string
import leo.base.then

data class Scope(
	val parentWord: Word,
	val function: Function,
	val valueTermOrNull: Term<Value>?) {
	override fun toString() = reflect.string
}

val Word.scope
	get() =
		Scope(this, identityFunction, null)

fun Scope.beginChild(word: Word): Scope =
	Scope(word, function, null)

fun Scope.push(word: Word) =
	valueTermOrNull.push(word)?.let(this::invoke)

fun Scope.push(field: Field<Value>) =
	valueTermOrNull.push(field)?.let(this::invoke)

fun Scope.invoke(argument: Term<Value>): Scope =
	null
		?: parseSelect(argument)
		?: parseRule(argument)
		?: invokeFunction(argument)

fun Scope.parseRule(argument: Term<Value>): Scope? =
	argument.parseRule(function)?.let(this::push)

fun Scope.parseSelect(argument: Term<Value>): Scope? =
	argument.onlyField?.let { field ->
		field.value.select(field.key)?.let { selected ->
			copy(valueTermOrNull = selected)
		}
	}

fun Scope.invokeFunction(argument: Term<Value>): Scope =
	copy(valueTermOrNull = function.invoke(argument))

fun Scope.push(rule: Rule) =
	copy(function = function.push(rule), valueTermOrNull = null)

// === reflect ===

val Scope.reflect: Field<Value>
	get() =
		scopeWord fieldTo term(
			parentWord fieldTo term(parentWord.reflect),
			function.reflect,
			valueTermOrNull.orNullField(valueWord))

// === folding bytes

val Scope.byteStream: Stream<Byte>
	get() =
		parentWord.byteStream
			.then(valueTermOrNull?.let { valueTerm ->
				'('.toByte().onlyStream
					.then(valueTerm.byteStream)
			})
