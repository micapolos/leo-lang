package dsl.impl

import leo.*
import leo.script.*
import leo.script.Script
import leo.script.Term

fun term(word: Word, args: List<Any?>): Term =
	ScriptTerm(
		word,
		args.fold(nullScript) { script, arg ->
			script.plus(arg.term)
		})

val List<Any?>.script: Script?
	get() =
		fold(nullScript) { script, arg ->
			script.plus(arg.term)
		}

val List<Any?>.term: Term
	get() =
		when (size) {
			0 -> literalTerm
			else -> (this[0] as? String)?.wordOrNull?.let {
				term(it, slice(1 until size))
			} ?: literalTerm
		}

val Any?.term: Term
	get() =
		when (this) {
			is Boolean -> if (this) ScriptTerm(trueWord) else ScriptTerm(falseWord)
			is Int -> IntTerm(this)
			is Float -> FloatTerm(this)
			is String -> StringTerm(this)
			is List<Any?> -> term
			else -> literalTerm
		}

val Any?.literalTerm: Term
	get() =
		ScriptTerm(literalWord, Script(null, StringTerm(toString())))

val Any?.script
	get() =
		term.let { nullScript.plus(term) }
