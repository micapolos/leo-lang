package leo13.untyped.compiler

import leo.base.notNullIf
import leo9.*

data class CompiledSwitch(val caseStack: Stack<CompiledCase>)

fun compiledSwitch() = CompiledSwitch(stack())

fun CompiledSwitch.plus(compiledCase: CompiledCase): CompiledSwitch? =
	when (caseStack) {
		is EmptyStack -> CompiledSwitch(stack(compiledCase))
		is LinkStack -> notNullIf(compiledCase.rhs.pattern == caseStack.link.value.rhs.pattern) {
			copy(caseStack = caseStack.push(compiledCase))
		}
	}

fun CompiledSwitch.containsCase(name: String): Boolean =
	caseStack.any { this.name == name }
