package leo13.js.compiler

import leo.base.notNullIf
import leo14.Compiler

data class Choice(
	val string: String,
	val rhsCompiler: Compiler)

fun choice(string: String, rhsCompiler: Compiler) =
	Choice(string, rhsCompiler)

fun Choice.compile(string: String): Compiler? =
	notNullIf(string == this.string) {
		rhsCompiler
	}
