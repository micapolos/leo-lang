package leo13

import leo13.script.Scriptable
import leo13.script.script

object Append : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Resolve : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}


object Lhs : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Rhs : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object RhsLine : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Meta : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Quote : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

object Evaluator : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "append"
	override val scriptableBody get() = script()
}

val append = Append
val lhs = Lhs
val rhs = Rhs
val rhsLine = RhsLine
val meta = Meta
val resolve = Resolve
val quote = Quote
val evaluator = Evaluator