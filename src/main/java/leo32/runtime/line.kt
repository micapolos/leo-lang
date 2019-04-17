package leo32.runtime

import leo.base.appendableString

data class Line(val name: String, val value: Script) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(name: String, vararg xs: Line): Line = Line(name, script(*xs))
infix fun String.to(script: Script) = Line(this, script)

fun Appendable.append(line: Line): Appendable =
	this
		.append(line.name)
		.append('(')
		.append(line.value)
		.append(')')
