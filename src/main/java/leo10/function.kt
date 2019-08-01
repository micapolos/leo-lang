package leo10

import leo.base.Empty
import leo.base.empty

sealed class Function

data class EmptyFunction(
	val empty: Empty) : Function()

data class ArgFunction(
	val arg: Arg) : Function()

data class ApplicationFunction(
	val application: FunctionApplication) : Function()

data class FunctionApplication(
	val function: Function,
	val op: Op)

fun Function.call(script: Script) =
	call(empty.args + script)

fun Function.call(args: Args): Script =
	when (this) {
		is EmptyFunction -> script()
		is ArgFunction -> args.at(arg)
		is ApplicationFunction -> application.function.call(args)
	}

fun function(empty: Empty): Function = EmptyFunction(empty)
fun function(arg: Arg): Function = ArgFunction(arg)
fun function(application: FunctionApplication): Function = ApplicationFunction(application)

fun Function.apply(op: Op) = function(FunctionApplication(this, op))