package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.untyped.TokenReader
import leo13.untyped.caseName

data class SwitchCompiler(
	val parentCompiler: Compiler,
	val compiledSwitch: CompiledSwitch) : TokenReader {

	override fun begin(name: String): CaseCompiler? =
		notNullIf(name == caseName) {
			caseInterpreter(this)
		}

	override val end get() = parentCompiler.plus(compiledSwitch)
}

fun Compiler.switchInterpreter(compiledSwitch: CompiledSwitch = compiledSwitch()) =
	SwitchCompiler(this, compiledSwitch)

fun SwitchCompiler.begin(name: String): CaseCompiler? =
	notNullIf(name == caseName) {
		caseInterpreter(this)
	}

fun SwitchCompiler.plus(compiledCase: CompiledCase) =
	compiledSwitch
		.plus(compiledCase)
		?.let { copy(compiledSwitch = it) }


fun SwitchCompiler.containsCase(name: String): Boolean =
	compiledSwitch.containsCase(name)