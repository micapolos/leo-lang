package leo13.js.compiler

import leo.base.notNullIf

data class CompilerChoice(
	val string: String,
	val rhsCompiler: Compiler)

fun choice(string: String, rhsCompiler: Compiler) =
	CompilerChoice(string, rhsCompiler)

fun CompilerChoice.compile(string: String): Compiler? =
	notNullIf(string == this.string) {
		rhsCompiler
	}
