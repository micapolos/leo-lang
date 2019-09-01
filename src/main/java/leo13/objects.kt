package leo13

import leo13.script.script

object Append : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Resolve : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "resolve"
	override val scriptableBody get() = script()
}

object Lhs : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "lhs"
	override val scriptableBody get() = script()
}

object Back : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "back"
	override val scriptableBody get() = script()
}

object Wrap : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "wrap"
	override val scriptableBody get() = script()
}

object Rhs : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "rhs"
	override val scriptableBody get() = script()
}

object RhsLine : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script()
}

object Meta : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "meta"
	override val scriptableBody get() = script()
}

object Quote : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "quote"
	override val scriptableBody get() = script()
}

object Evaluator : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "evaluator"
	override val scriptableBody get() = script()
}

object Space : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "space"
	override val scriptableBody get() = script()
}

object Tab : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "tab"
	override val scriptableBody get() = script()
}

val append = Append
val lhs = Lhs
val rhs = Rhs
val rhsLine = RhsLine
val meta = Meta
val resolve = Resolve
val wrap = Wrap
val quote = Quote
val evaluator = Evaluator
val space = Space
val tab = Tab