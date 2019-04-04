@file:Suppress("unused")

package leo32.interpreter

import leo.base.Empty
import leo.base.empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put
import leo32.runtime.Function
import leo32.runtime.argument
import leo32.runtime.function

data class Functions(
	val typeToFunctionDict: Dict<Type, Function>)

val Dict<Type, Function>.functions get() =
	Functions(this)

val Empty.functions get() =
	empty.typeDict<Function>().functions

fun Functions.put(type: Type, function: Function) =
	copy(typeToFunctionDict = typeToFunctionDict.put(type, function))

fun Functions.at(type: Type): Function =
	typeToFunctionDict.at(type) ?: argument.function