package leo3

import leo.base.*

data class Script(
	val parentOrNull: ScriptParent?,
	val termOrNull: Term?) {
	override fun toString() = appendableString { it.append(this) }
}

data class ScriptParent(
	val script: Script,
	val begin: Begin) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.script
	get() = Script(null, null)

fun script(termOrNull: Term?) =
	Script(null, termOrNull)

fun Script.plus(token: Token): Script? =
	when (token) {
		is BeginToken -> Script(ScriptParent(this, token.begin), null)
		is EndToken -> parentOrNull?.let { parent ->
			Script(
				parent.script.parentOrNull,
				parent.script.termOrNull.plus(parent.begin.word, termOrNull))
		}
	}

fun Script.plus(termOrNull: Term?): Script =
	orNullFold(termOrNull.tokenSeq, Script::plus)!!

val Script.resultOrNull: Result?
	get() = parentOrNull.ifNull { result(termOrNull) }

fun Appendable.append(script: Script) =
	this
		.ifNotNull(script.parentOrNull) { append(it) }
		.ifNotNull(script.termOrNull) { append(it) }

fun Appendable.append(scriptParent: ScriptParent): Appendable =
	this
		.append(scriptParent.script)
		.append(scriptParent.begin)
