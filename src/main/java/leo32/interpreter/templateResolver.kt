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

data class TemplateResolver(
	val typeToFunctionDict: Dict<Type, Function>)

val Dict<Type, Function>.templateResolver get() =
	TemplateResolver(this)

val Empty.templateResolver get() =
	empty.typeDict<Function>().templateResolver

fun TemplateResolver.put(type: Type, function: Function) =
	copy(typeToFunctionDict = typeToFunctionDict.put(type, function))

fun TemplateResolver.resolve(type: Type): Function =
	typeToFunctionDict
		.at(type)
		?: argument.function