package leo21.token.processor

import leo14.Fragment
import leo21.token.body.printFragment
import leo21.token.evaluator.printFragment
import leo21.token.script.fragment
import leo21.token.type.compiler.fragment

val Processor.fragment: Fragment
	get() =
		when (this) {
			is TypeCompilerProcessor -> typeCompiler.fragment
			is ChoiceCompilerProcessor -> choiceCompiler.fragment
			is ArrowCompilerProcessor -> arrowCompiler.fragment
			is ScriptCompilerProcessor -> scriptCompiler.fragment
			is BodyCompilerProcessor -> bodyCompiler.printFragment
			is FunctionCompilerProcessor -> functionCompiler.printFragment
			is FunctionItCompilerProcessor -> functionItCompiler.printFragment
			is FunctionItDoesCompilerProcessor -> functionItDoesCompiler.printFragment
			is DefineCompilerProcessor -> defineCompiler.printFragment
			is SwitchCompilerProcessor -> switchCompiler.printFragment
			is EvaluatorProcessor -> evaluatorNode.printFragment
		}
