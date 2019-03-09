package leo32

import leo.binary.Array32
import leo.binary.array32
import leo.binary.at

data class Function(
	val matchArray: Array32<Match>)

val emptyFunction =
	Function(pushMatch.array32)

fun Function.invoke(arg: Int, runtime: Runtime) =
	matchArray.at(arg).invoke(arg, runtime)
