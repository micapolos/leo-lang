package leo

import leo.base.string

data class Scope(
	val parentWord: Word,
	val function: Function,
	val scriptOrNull: Script?) {
	override fun toString() = reflect.string
}

val Word.scope
	get() =
		Scope(this, identityFunction, null)

fun Scope.beginChild(word: Word): Scope =
	Scope(word, function, null)

fun Scope.push(word: Word) =
	scriptOrNull.push(word)?.let(this::invoke)

fun Scope.push(field: Field<Nothing>) =
	scriptOrNull.push(field)?.let(this::invoke)

fun Scope.invoke(argument: Script): Scope =
	null
		?: parseSelect(argument)
		?: parseRule(argument)
		?: invokeFunction(argument)

fun Scope.parseRule(argument: Script): Scope? =
	argument.parseRule(function)?.let(this::push)

fun Scope.parseSelect(argument: Script): Scope? =
	argument.term.onlyField?.let { field ->
		field.value.select(field.key)?.let { selected ->
			copy(scriptOrNull = selected.script)
		}
	}

fun Scope.invokeFunction(argument: Script): Scope =
	copy(scriptOrNull = function.invoke(argument))

fun Scope.push(rule: Rule) =
	copy(function = function.push(rule), scriptOrNull = null)

// === reflect ===

val Scope.reflect: Field<Nothing>
	get() =
		scopeWord fieldTo term(
			parentWord fieldTo term(parentWord.reflect),
			function.reflect,
			scriptOrNull?.reflect.orNullField(scriptWord))

// === folding bytes

fun <R> R.foldBytes(scope: Scope, fn: R.(Byte) -> R): R =
	foldBytes(scope.parentWord, fn)
		.let { folded ->
			when {
				scope.scriptOrNull != null -> folded.fn('('.toByte()).foldBytes(scope.scriptOrNull, fn)
				else -> folded
			}
		}
