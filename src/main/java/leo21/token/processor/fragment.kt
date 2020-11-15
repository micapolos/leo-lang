package leo21.token.processor

import leo14.Fragment
import leo21.token.body.printFragment
import leo21.token.script.fragment
import leo21.token.type.compiler.fragment

val TokenProcessor.fragment: Fragment
	get() =
		when (this) {
			is TypeCompilerTokenProcessor -> typeCompiler.fragment
			is ChoiceCompilerTokenProcessor -> choiceCompiler.fragment
			is ArrowCompilerTokenProcessor -> arrowCompiler.fragment
			is ScriptCompilerTokenProcessor -> scriptCompiler.fragment
			is BodyCompilerTokenProcessor -> bodyCompiler.printFragment
			is FunctionCompilerTokenProcessor -> functionCompiler.printFragment
			is FunctionItCompilerTokenProcessor -> functionItCompiler.printFragment
			is FunctionItDoesCompilerTokenProcessor -> functionItDoesCompiler.printFragment
			is DefineCompilerTokenProcessor -> defineCompiler.printFragment
		}
