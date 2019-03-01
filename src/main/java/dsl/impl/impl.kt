package dsl.impl

import leo.*
import leo.script.Script
import leo.script.plus

data class Term(
	val name: String,
	val args: List<Any?>)

fun _term(name: String, vararg args: Any?): Any? =
	Term(name, listOf(*args))

data class Field(
	val word: Word,
	val rhs: Script? = null)

val List<Any?>.script
	get() =
		fold(leo.script.script) { script, arg ->
			arg.field.let {
				script.plus(it.word, it.rhs)
			}
		}

val Term.field
	get() =
		Field(name.wordOrNull!!, args.script)

val Any?.field: Field
	get() =
		when (this) {
			is Term -> field
			is Boolean -> if (this) Field(trueWord) else Field(falseWord)
			is Int -> Field(numberWord)
			else -> error(this.toString())
		}

val Any?.script
	get() =
		field.let { leo.script.script(it.word, it.rhs) }
