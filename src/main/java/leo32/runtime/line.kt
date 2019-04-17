package leo32.runtime

import leo.base.appendableString

data class Line(val name: Symbol, val value: Script) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(name: Symbol, vararg xs: Line): Line = Line(name, script(*xs))
fun line(name: String, vararg xs: Line): Line = line(symbol(name), *xs)
infix fun Symbol.to(script: Script) = Line(this, script)
infix fun String.to(script: Script) = symbol(this) to script

fun Appendable.append(line: Line): Appendable =
	this
		.append(line.name)
		.append('(')
		.append(line.value)
		.append(')')
