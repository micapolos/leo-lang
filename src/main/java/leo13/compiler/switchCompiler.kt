package leo13.compiler

import leo13.fail
import leo13.script.*
import leo13.type.*
import leo13.value.caseTo
import leo13.value.plus
import leo13.value.switch

data class SwitchCompiler(
	val context: Context,
	val compiledOrNull: SwitchCompiled?)

fun compiler(context: Context, compiledOrNull: SwitchCompiled?) =
	SwitchCompiler(context, compiledOrNull)

fun SwitchCompiler.push(choice: Choice, switch: Switch): SwitchCompiler =
	push(choice.lhsNode, switch.lhsNode).push(choice.case, switch.case)

fun SwitchCompiler.push(choiceNode: ChoiceNode, switchNode: SwitchNode): SwitchCompiler =
	when (switchNode) {
		is CaseSwitchNode -> push((choiceNode as CaseChoiceNode).case, switchNode.case)
		is SwitchSwitchNode -> push((choiceNode as ChoiceChoiceNode).choice, switchNode.switch)
	}

fun SwitchCompiler.push(choiceCase: leo13.type.Case, case: leo13.script.Case): SwitchCompiler =
	if (choiceCase.name != case.name) fail(
		"mismatch" lineTo script(
			"choice" lineTo script(choiceCase.name),
			"case" lineTo script(case.name)))
	else context
		.bind(type(choiceCase.name lineTo choiceCase.rhs))
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
				fail(
					"mismatch" lineTo script(
						compiledOrNull.type.scriptableLine,
						caseCompiled.type.scriptableLine))
		}

val SwitchCompiler.compiled: SwitchCompiled
	get() =
		if (compiledOrNull == null) fail("empty")
		else compiledOrNull