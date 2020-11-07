package leo21.token.processor

import leo14.Script
import leo21.evaluator.script
import leo21.token.compiler.script
import leo21.type.script

val TokenProcessor.script: Script
	get() =
		when (this) {
			is CompilerTokenProcessor -> compiler.script
			is EvaluatorTokenProcessor -> evaluator.evaluator.evaluated.script
			is TypeCompilerTokenProcessor -> typeCompiler.type.script
			is ChoiceCompilerTokenProcessor -> choiceCompiler.choice.script
		}