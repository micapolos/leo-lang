package leo13.untyped.compiler

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.caseName
import leo13.untyped.duplicateName
import leo13.untyped.mismatchName
import leo13.untyped.pattern.scriptLine
import leo13.untyped.switchName
import leo9.*

data class CompiledSwitch(val caseStack: Stack<CompiledCase>)

fun compiledSwitch() = CompiledSwitch(stack())

fun compileSwitch(caseStack: Stack<CompiledCase>) =
	compiledSwitch().fold(caseStack.reverse) { compiledPlus(it) }

fun CompiledSwitch.compiledPlus(case: CompiledCase): CompiledSwitch =
	compile(switchName) {
		when (caseStack) {
			is EmptyStack ->
				CompiledSwitch(stack(case))
			is LinkStack ->
				if (caseStack.any { name == case.name })
					compileError(duplicateName lineTo script(caseName lineTo script(case.name)))
				else if (caseStack.link.value.rhs.pattern != case.rhs.pattern)
					compileError(mismatchName lineTo script(case.rhs.pattern.scriptLine))
				else CompiledSwitch(caseStack.push(case))
		}
	}
