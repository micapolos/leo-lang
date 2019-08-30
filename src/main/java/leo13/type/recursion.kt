package leo13.type

import leo.base.fold
import leo13.script.Scriptable
import leo13.script.asScript
import leo9.Back
import leo9.Stack
import leo9.push
import leo9.stack

data class Recursion(val backStack: Stack<Back>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "recursion"
	override val scriptableBody get() = backStack.asScript { TODO() }
}

val Stack<Back>.recursion get() = Recursion(this)
fun Recursion.plus(back: Back) = backStack.push(back).recursion
fun recursion(vararg backs: Back) = stack<Back>().recursion.fold(backs) { plus(it) }
