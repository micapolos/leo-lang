package leo32.runtime.v2

data class Entry(
	val lhs: Script,
	val rhs: Script)

infix fun Script.to(script: Script) =
	Entry(this, script)