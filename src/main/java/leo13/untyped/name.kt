package leo13.untyped

import leo13.LeoObject
import leo13.script.script

data class name(val string: String) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "name"
	override val scriptableBody get() = script(string)
}
