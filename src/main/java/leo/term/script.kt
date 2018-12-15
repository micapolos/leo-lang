package leo.term

import leo.Word
import leo.base.fold

data class Script(
	val term: Term<Script>) {
	override fun toString() = scriptType.string(term)
}

val scriptType = Type(Script::isSimple)

val Term<Script>.script: Script
	get() =
		Script(this)

val Word.script: Script
	get() =
		term<Script>().script

fun script(word: Word): Script =
	word.script

fun script(script: Script, vararg applications: Application<Script>): Script =
	script.fold(applications) { apply(it).script }

fun script(application: Application<Script>, vararg applications: Application<Script>): Script =
	script(application.term.script, *applications)

val Script.isSimple: Boolean
	get() =
		term.isSimple

// === matching

fun <R : Any> Script.matchArgument(word: Word, fn: Script?.() -> R?): R? =
	term.matchArgument(word, fn)