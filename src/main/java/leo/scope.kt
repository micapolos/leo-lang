package leo

import leo.base.string

data class Scope(
  val parentWord: Word,
  val function: Function,
  val scriptOrNull: Script?
) {
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
    ?: parseRule(argument)
    ?: invokeFunction(argument)

fun Scope.parseRule(argument: Script): Scope? =
  argument.parseRule?.let(this::push)

fun Scope.invokeFunction(argument: Script): Scope =
  function.invoke(argument).let { result ->
    copy(scriptOrNull = result)
  }

fun Scope.push(rule: Rule) =
  copy(function = function.push(rule), scriptOrNull = null)

// === reflect ===

val Scope.reflect: Field<Nothing>
  get() =
    scopeWord fieldTo term(
      leo.parentWord fieldTo term(parentWord.reflect),
      function.reflect,
      scriptOrNull?.reflect.orNullField(scriptWord)
    )