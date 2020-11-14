package leo21.token.processor

import leo14.Fragment
import leo21.token.body.printFragment
import leo21.token.compiler.fragment
import leo21.token.evaluator.fragment
import leo21.token.script.fragment
import leo21.token.type.compiler.fragment

val TokenProcessor.fragment: Fragment
	get() =
		when (this) {
			is CompilerTokenProcessor -> compiler.fragment
			is EvaluatorTokenProcessor -> evaluator.fragment
			is TypeCompilerTokenProcessor -> typeCompiler.fragment
			is ChoiceCompilerTokenProcessor -> choiceCompiler.fragment
			is ArrowCompilerTokenProcessor -> arrowCompiler.fragment
			is ScriptCompilerTokenProcessor -> scriptCompiler.fragment
			is DataCompilerTokenProcessor -> dataCompiler.fragment
			is FunctionTypeCompilerTokenProcessor -> functionTypeCompiler.fragment
			is FunctionCompilerTokenProcessor -> functionCompiler.fragment
			is BodyCompilerTokenProcessor -> bodyCompiler.printFragment
		}
