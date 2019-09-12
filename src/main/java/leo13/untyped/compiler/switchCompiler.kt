package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.script.plus
import leo13.script.script
import leo13.untyped.TokenReader
import leo13.untyped.caseName
import leo9.EmptyStack
import leo9.LinkStack
import leo9.any

data class SwitchCompiler(
	val parentCompiler: Compiler,
	val compiled: Compiled) : TokenReader {

	override fun begin(name: String): CaseCompiler? =
		notNullIf(name == caseName) {
			caseCompiler()
		}

	override val end
		get() =
			parentCompiler.plusSwitch(compiled)
}

fun Compiler.switchCompiler(compiled: Compiled = compiled()) =
	SwitchCompiler(this, compiled)

fun SwitchCompiler.plusCase(line: CompiledLine): SwitchCompiler? =
	when (compiled.script.lineStack) {
		is EmptyStack -> set(compiled(script(line.scriptLine), line.rhs.pattern))
		is LinkStack -> notNullIf(line.rhs.pattern == compiled.pattern) {
			set(compiled(compiled.script.plus(line.scriptLine), line.rhs.pattern))
		}
	}

fun SwitchCompiler.set(compiled: Compiled) =
	copy(compiled = compiled)

fun SwitchCompiler.containsCase(caseName: String): Boolean =
	compiled.script.lineStack.any { name == caseName }