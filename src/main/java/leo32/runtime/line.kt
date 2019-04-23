package leo32.runtime

data class Line(val name: Symbol, val value: Script)

fun line(name: Symbol, vararg xs: Line): Line = Line(name, script(*xs))
infix fun Symbol.to(script: Script) = Line(this, script)
