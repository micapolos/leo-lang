package leo

import leo.base.fail

data class Script(
	val term: Term<Nothing>) {
	override fun toString() = term.toString()
}

val Term<Nothing>.script
	get() =
		Script(this)

fun script(term: Term<Nothing>) =
	term.script

fun Script?.push(word: Word) =
	this?.term.push(word)?.script

fun Script?.push(field: Field<Nothing>) =
	this?.term.push(field)?.script

fun <R> Script.match(key: Word, fn: (Script) -> R): R? =
	term.match(key) { term ->
		fn(term.script)
	}

fun <R> Script.match(key1: Word, key2: Word, fn: (Script, Script) -> R): R? =
	term.match(key1, key2) { term1, term2 ->
		fn(term1.script, term2.script)
	}

// === reflect

val Script.reflect: Field<Nothing>
	get() =
		scriptWord fieldTo term.map { fail }

// === folding bytes

fun <R> R.foldBytes(script: Script, fn: R.(Byte) -> R): R =
	foldBytes(script.term, { fail }, fn)
