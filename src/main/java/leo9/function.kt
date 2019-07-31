package leo9

import leo.base.Empty

sealed class Function

data class EmptyFunction(
	val empty: Empty) : Function()

data class ArgumentFunction(
	val argument: Argument) : Function()

data class ApplicationFunction(
	val application: FunctionApplication) : Function()

data class FunctionApplication(
	val target: Function,
	val operation: Operation)

sealed class Operation

data class LineOperation(
	val line: FunctionLine) : Operation()

data class LhsOperation(
	val lhs: Lhs) : Operation()

data class RhsOperation(
	val rhs: Rhs) : Operation()

data class SwitchOperation(
	val switch: FunctionSwitch) : Operation()

data class CallOperation(
	val call: FunctionCall) : Operation()

object Argument
object Lhs
object Rhs

data class FunctionLine(
	val name: String,
	val function: Function)

data class FunctionSwitch(
	val map: Map<String, Function>)

data class FunctionCall(
	val function: Function)

// ---------------

val argument = Argument
val lhs = Lhs
val rhs = Rhs

fun function(empty: Empty): Function = EmptyFunction(empty)
fun function(argument: Argument): Function = ArgumentFunction(argument)
fun function(application: FunctionApplication): Function = ApplicationFunction(application)

fun operation(line: FunctionLine): Operation = LineOperation(line)
fun operation(lhs: Lhs): Operation = LhsOperation(lhs)
fun operation(rhs: Rhs): Operation = RhsOperation(Rhs)
fun operation(switch: FunctionSwitch): Operation = SwitchOperation(switch)
fun operation(call: FunctionCall): Operation = CallOperation(call)

fun line(name: String, function: Function) = FunctionLine(name, function)
fun call(function: Function) = FunctionCall(function)
fun switch(vararg pairs: Pair<String, Function>) = FunctionSwitch(mapOf(*pairs))

// ---------------

fun Function.call(parameter: Script): Script =
	when (this) {
		is EmptyFunction -> empty.call(parameter)
		is ArgumentFunction -> argument.call(parameter)
		is ApplicationFunction -> application.call(parameter)
	}

fun Empty.call(parameter: Script) = script(this)

fun Argument.call(parameter: Script) = parameter

fun FunctionApplication.call(parameter: Script): Script =
	operation.call(target.call(parameter), parameter)

fun Operation.call(target: Script, parameter: Script): Script =
	when (this) {
		is LineOperation -> line.call(target, parameter)
		is LhsOperation -> lhs.call(target, parameter)
		is RhsOperation -> rhs.call(target, parameter)
		is SwitchOperation -> switch.call(target, parameter)
		is CallOperation -> call.call(target, parameter)
	}

fun FunctionLine.call(target: Script, parameter: Script): Script =
	script(
		application(
			target,
			line(name, function.call(parameter))))

fun Lhs.call(target: Script, parameter: Script): Script =
	target.application.script

fun Rhs.call(target: Script, parameter: Script): Script =
	target.application.line.script

fun FunctionSwitch.call(target: Script, parameter: Script): Script =
	map.getValue(target.application.line.name).call(parameter)

fun FunctionCall.call(target: Script, parameter: Script): Script =
	function.call(target)
