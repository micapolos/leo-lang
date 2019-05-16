package leo3

import leo.base.Empty
import leo.base.appendableString
import leo.base.ifNotNull
import leo.base.orNullFold

data class Script(
	val parentOrNull: ScriptParent?,
	val nodeOrNull: Node?) {
	override fun toString() = appendableString { it.append(this) }
}

data class ScriptParent(
	val script: Script,
	val begin: Begin) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.script
	get() = Script(null, null)

fun script(nodeOrNull: Node?) =
	Script(null, nodeOrNull)

fun Script.plus(token: Token): Script? =
	when (token) {
		is BeginToken -> Script(ScriptParent(this, token.begin), null)
		is EndToken -> parentOrNull?.let { parent ->
			Script(
				parent.script.parentOrNull,
				parent.script.nodeOrNull.plus(parent.begin.word, nodeOrNull))
		}
	}

fun Script.plus(nodeOrNull: Node?): Script =
	orNullFold(nodeOrNull.tokenSeq, Script::plus)!!

fun Appendable.append(script: Script) =
	this
		.ifNotNull(script.parentOrNull) { append(it) }
		.ifNotNull(script.nodeOrNull) { append(it) }

fun Appendable.append(scriptParent: ScriptParent): Appendable =
	this
		.append(scriptParent.script)
		.append(scriptParent.begin)
