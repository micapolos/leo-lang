package leo13.type.pattern

import leo.base.Empty
import leo13.script.*

sealed class Type : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "type"
	override val scriptableBody get() = typeScriptableBody
	abstract val typeScriptableName: String
	abstract val typeScriptableBody: Script
}

data class EmptyType(val empty: Empty) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = "empty"
	override val typeScriptableBody get() = script()
}

data class LinkType(val link: TypeLink) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = link.scriptableName
	override val typeScriptableBody get() = link.scriptableBody
}

data class ChoiceType(val choice: Choice) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = choice.scriptableName
	override val typeScriptableBody get() = choice.scriptableBody
}

data class FunctionType(val arrow: TypeArrow) : Type() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = arrow.scriptableName
	override val typeScriptableBody get() = arrow.scriptableBody
}

data class TypeLine(val name: String, val rhs: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithMeta(meta: Boolean): ScriptLine =
		if (meta) "meta" lineTo script(scriptableLine)
		else scriptableLine
}

data class TypeLink(val lhs: Type, val line: TypeLine) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(line.scriptableLineWithMeta(needsMeta(line.name)))
	fun needsMeta(name: String): Boolean =
		when (name) {
			"or" -> lhs is LinkType && lhs.link.lhs is EmptyType
			"to" -> lhs is LinkType
			else -> false
		}
}

data class Choice(val lhsNode: ChoiceNode, val case: Case) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "choice"
	override val scriptableBody get() = lhsNode.scriptableBody.plus(case.scriptableLineWithOr(true))
}

sealed class ChoiceNode : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "node"
	override val scriptableBody get() = nodeScriptableBody
	abstract val nodeScriptableName: String
	abstract val nodeScriptableBody: Script
}

data class CaseChoiceNode(val case: Case) : ChoiceNode() {
	override fun toString() = super.toString()
	override val nodeScriptableName get() = "case"
	override val nodeScriptableBody get() = script(case.scriptableLine)
}

data class ChoiceChoiceNode(val choice: Choice) : ChoiceNode() {
	override fun toString() = super.toString()
	override val nodeScriptableName get() = choice.scriptableName
	override val nodeScriptableBody get() = choice.scriptableBody
}

data class Case(val name: String, val rhs: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithOr(withOr: Boolean): ScriptLine =
		if (withOr) "or" lineTo script(scriptableLine)
		else scriptableLine
}

data class TypeArrow(val lhs: Type, val rhs: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "arrow"
	override val scriptableBody get() = lhs.scriptableBody.plus("to" lineTo rhs.scriptableBody)
}
