package leo13.type

import leo13.script.Scriptable
import leo13.script.asScript
import leo13.script.lineTo
import leo13.script.script
import leo13.value.Given
import leo9.*

data class TypeBindings(val stack: Stack<Pattern>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bindings"
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

val Stack<Pattern>.typeBindings get() = TypeBindings(this)
fun TypeBindings.push(pattern: Pattern) = stack.push(pattern).typeBindings
fun typeBindings(vararg patterns: Pattern) = stack(*patterns).typeBindings

val TypeBindings.asScript get() = stack.map { "bind" lineTo scriptableBody }.script

fun TypeBindings.unsafeType(given: Given): Pattern =
	stack.drop(given.previousStack)!!.linkOrNull!!.value
