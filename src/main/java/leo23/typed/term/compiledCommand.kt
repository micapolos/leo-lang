package leo23.typed.term

import leo14.Begin
import leo14.begin

sealed class CompiledCommand
data class BeginCompiledCommand(val begin: Begin) : CompiledCommand()
data class DoCompiledCommand(var stackCompiled: StackCompiled) : CompiledCommand()
object FunctionCompiledCommand : CompiledCommand()
data class ApplyCompiledCommand(val functionCompiled: FunctionCompiled) : CompiledCommand()
data class SwitchCompiledCommand(val choiceCompiled: ChoiceCompiled) : CompiledCommand()
object DefineCompiledCommand : CompiledCommand()
data class RepeatCompiledCommand(val stackCompiled: StackCompiled) : CompiledCommand()

fun StackCompiled.command(name: String): CompiledCommand =
	when (name) {
		"do" -> DoCompiledCommand(this)
		"function" -> FunctionCompiledCommand
		"apply" -> ApplyCompiledCommand(functionCompiled)
		"switch" -> SwitchCompiledCommand(choiceCompiled)
		"define" -> DefineCompiledCommand
		"repeat" -> RepeatCompiledCommand(this)
		else -> BeginCompiledCommand(begin(name))
	}
