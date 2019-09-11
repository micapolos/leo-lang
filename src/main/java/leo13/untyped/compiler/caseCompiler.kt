package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.untyped.TokenReader

data class CaseCompiler(
	val parentSwitchCompiler: SwitchCompiler,
	val compiledCaseOrNull: CompiledCase?) : TokenReader {

	override fun begin(name: String) =
		notNullIf(compiledCaseOrNull == null && parentSwitchCompiler.containsCase(name)) {
			CaseCompilerParent(CaseCompilerLink(this, name))
				.compiler(parentSwitchCompiler.parentCompiler.context, value())
		}

	override val end
		get() =
			compiledCaseOrNull
				?.let { parentSwitchCompiler.plus(it) }
}

data class CaseCompilerLink(
	val case: CaseCompiler,
	val name: String)

fun caseInterpreter(switchCompiler: SwitchCompiler, compiledCaseOrNull: CompiledCase? = null) =
	CaseCompiler(switchCompiler, compiledCaseOrNull)

fun CaseCompiler.plus(compiledCase: CompiledCase): CaseCompiler? =
	notNullIf(compiledCaseOrNull == null) {
		copy(compiledCaseOrNull = compiledCase)
	}
