package leo13.value

import leo13.script.Scriptable
import leo13.script.script

data class Get(val name: String) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "get"
	override val scriptableBody get() = script(name)
}

fun get(name: String) = Get(name)