package leo32.runtime.v2

import leo32.runtime.Symbol

data class Field(
	val symbol: Symbol,
	val script: Script)

infix fun Symbol.to(script: Script) =
	Field(this, script)