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

data class Argument(val backStack: Stack<Back>)

object Back
object Lhs
object Rhs

data class FunctionLine(
	val name: String,
	val function: Function)

data class FunctionSwitch(
	val map: Map<String, Function>)

data class FunctionCall(
	val function: Function)

data class Trace(
	val scriptStack: Stack<Script>)

// ---------------

val Stack<Back>.argument get() = Argument(this)
val Stack<Script>.trace get() = Trace(this)

fun argument(vararg backs: Back) = stack(*backs).argument
fun trace(vararg scripts: Script) = stack(*scripts).trace

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

fun Function.call(script: Script) =
	call(trace(script))

fun Function.call(trace: Trace): Script =
	when (this) {
		is EmptyFunction -> empty.call(trace)
		is ArgumentFunction -> argument.call(trace)
		is ApplicationFunction -> application.call(trace)
	}

fun Empty.call(trace: Trace) = script()

tailrec fun Argument.call(trace: Trace): Script =
	when (backStack) {
		is EmptyStack -> trace.scriptStack.top
		is LinkStack -> backStack.pop.argument.call(trace.scriptStack.pop.trace)
	}

fun FunctionApplication.call(trace: Trace): Script =
	operation.call(target.call(trace), trace)

fun Operation.call(target: Script, trace: Trace): Script =
	when (this) {
		is LineOperation -> line.call(target, trace)
		is LhsOperation -> lhs.call(target, trace)
		is RhsOperation -> rhs.call(target, trace)
		is SwitchOperation -> switch.call(target, trace)
		is CallOperation -> call.call(target, trace)
	}

fun FunctionLine.call(target: Script, trace: Trace): Script =
	target.push(name lineTo function.call(trace))

fun Lhs.call(target: Script, trace: Trace): Script =
	target.lhs

fun Rhs.call(target: Script, trace: Trace): Script =
	target.rhs

fun FunctionSwitch.call(target: Script, trace: Trace): Script =
	map.getValue(target.name).call(trace)

fun FunctionCall.call(target: Script, trace: Trace): Script =
	function.call(trace.scriptStack.push(target).trace)
