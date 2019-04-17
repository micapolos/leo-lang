package leo32.runtime

import leo.base.appendableString

data class Line(val name: Symbol, val value: Script) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(name: Symbol, vararg xs: Line): Line = Line(name, script(*xs))
infix fun Symbol.to(script: Script) = Line(this, script)

fun Appendable.append(line: Line): Appendable =
	this
		.append(line.name)
		.append('(')
		.append(line.value)
		.append(')')
