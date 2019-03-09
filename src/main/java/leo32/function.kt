package leo32

import leo.binary.Array32
import leo.binary.at
import leo.binary.nullArray32

data class Function(
	val matchArray: Array32<Match>)

val emptyFunction =
	Function(nullArray32())

fun Function.invoke(arg: Int, runtime: Runtime) =
	(matchArray.at(arg) ?: pushMatch).invoke(arg, runtime)
