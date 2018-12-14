package leo.term

import leo.Word
import leo.base.fold
import leo.base.string

data class Script(
	val term: Term<Script>) {
	override fun toString() = scriptType.string(term)
}

val scriptType = Type<Script>({ string }, { term.isSimple })

val Term<Script>.script: Script
	get() =
		Script(this)

val Word.script: Script
	get() =
		term<Script>().script

fun script(script: Script, vararg applications: Application<Script>): Script =
	script.fold(applications) { apply(it).script }

fun script(application: Application<Script>, vararg applications: Application<Script>): Script =
	script(application.term.script, *applications)
