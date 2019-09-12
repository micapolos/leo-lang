package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.untyped.TokenReader

data class CaseCompiler(
	val parentSwitchCompiler: SwitchCompiler,
	val compiledLineOrNull: CompiledLine?) : TokenReader {

	override fun begin(name: String) =
		notNullIf(compiledLineOrNull == null && parentSwitchCompiler.containsCase(name)) {
			linkTo(name).parent.compiler(parentSwitchCompiler.parentCompiler.context, compiled())
		}

	override val end
		get() =
			compiledLineOrNull?.let { parentSwitchCompiler.plusCase(it) }
}

fun SwitchCompiler.caseCompiler(compiledLineOrNull: CompiledLine? = null) =
	CaseCompiler(this, compiledLineOrNull)

fun CaseCompiler.plus(compiledLine: CompiledLine): CaseCompiler? =
	notNullIf(compiledLineOrNull == null) {
		copy(compiledLineOrNull = compiledLine)
	}
