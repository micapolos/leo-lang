package leo13.script

import leo13.LeoObject

data class Case(val name: String, val rhs: Script) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName = "case"
	override val scriptableBody = script(name lineTo script(), "gives" lineTo rhs.scriptableBody)
}

infix fun String.caseTo(rhs: Script) = Case(this, rhs)

