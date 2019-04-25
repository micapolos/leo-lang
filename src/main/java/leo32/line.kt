package leo32

import leo.base.string

data class Line(val name: Symbol, val value: Script) {
	override fun toString() = code.string
}

fun line(name: Symbol, vararg xs: Line): Line = Line(name, script(*xs))
infix fun Symbol.to(script: Script) = Line(this, script)
