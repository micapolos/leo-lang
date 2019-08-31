package leo13.compiler

import leo13.fail
import leo13.script.*
import leo13.value.caseTo
import leo13.value.plus
import leo13.value.switch

data class SwitchCompiler(
	val context: Context,
	val compiledOrNull: SwitchCompiled?)

fun compiler(context: Context, compiledOrNull: SwitchCompiled?) =
	SwitchCompiler(context, compiledOrNull)

fun switchCompiler() = compiler(context(), null)

fun SwitchCompiler.push(switch: Switch): SwitchCompiler =
	push(switch.lhsNode).push(switch.case)

fun SwitchCompiler.push(switchNode: SwitchNode): SwitchCompiler =
	when (switchNode) {
		is CaseSwitchNode -> push(switchNode.case)
		is SwitchSwitchNode -> push(switchNode.switch)
	}

fun SwitchCompiler.push(case: Case): SwitchCompiler =
	context
		.unsafeCompile(case.rhs)
		.let { caseCompiled ->
			if (compiledOrNull == null)
				compiler(
					context,
					compiled(
						switch(case.name caseTo caseCompiled.expr),
						caseCompiled.type))
			else if (compiledOrNull.type == caseCompiled.type)
				compiler(
					context,
					compiled(
						compiledOrNull.switch.plus(case.name caseTo caseCompiled.expr),
						caseCompiled.type))
			else
				fail("type")
		}

val SwitchCompiler.compiled: SwitchCompiled
	get() =
		if (compiledOrNull == null) fail("empty")
		else compiledOrNull