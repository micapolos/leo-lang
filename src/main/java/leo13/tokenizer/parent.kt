package leo13.tokenizer

import leo13.LeoObject
import leo13.Space
import leo13.orNullAsScriptLine
import leo13.script.script

data class Parent(val indentOrNull: Indent?) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "parent"
	override val scriptableBody get() = script(indentOrNull.orNullAsScriptLine("indent"))
}

fun parent(indent: Indent? = null) = Parent(indent)

val Parent.isEmpty: Boolean get() = indentOrNull == null

fun Parent.plus(tab: SpacesTab): Parent =
	if (indentOrNull == null) parent(indent(tab))
	else parent(indentOrNull.plus(tab))

fun Parent.plus(space: Space): Parent =
	if (indentOrNull == null) parent(indent(tab(space)))
	else parent(indentOrNull.plus(space))
