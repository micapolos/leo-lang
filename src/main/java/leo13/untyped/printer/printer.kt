package leo13.untyped.printer

import leo13.script.Script
import leo13.untyped.TokenReader

data class Printer(val script: Script) : TokenReader {
	override fun begin(name: String) = TODO()
	override val end get() = TODO()
}
