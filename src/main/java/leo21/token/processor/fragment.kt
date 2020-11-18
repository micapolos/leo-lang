package leo21.token.processor

import leo.base.runIf
import leo14.Fragment
import leo14.debugEnabled
import leo14.prepend
import leo14.script
import leo21.token.body.printFragment
import leo21.token.evaluator.printFragment
import leo21.token.script.printFragment
import leo21.token.type.compiler.printFragment

val Processor.printFragment: Fragment
	get() =
		when (this) {
			is TypeCompilerProcessor -> typeCompiler.printFragment
			is ChoiceCompilerProcessor -> choiceCompiler.printFragment
			is ArrowCompilerProcessor -> arrowCompiler.printFragment
			is ScriptCompilerProcessor -> scriptCompiler.printFragment
			is BodyCompilerProcessor -> bodyCompiler.printFragment
			is FunctionCompilerProcessor -> functionCompiler.printFragment
			is FunctionItCompilerProcessor -> functionItCompiler.printFragment
			is FunctionItDoesCompilerProcessor -> functionItDoesCompiler.printFragment
			is DefineCompilerProcessor -> defineCompiler.printFragment
			is SwitchCompilerProcessor -> switchCompiler.printFragment
			is EvaluatorProcessor -> evaluatorNode.printFragment
		}.runIf(debugEnabled) { prepend(script(reflectScriptLine)) }
