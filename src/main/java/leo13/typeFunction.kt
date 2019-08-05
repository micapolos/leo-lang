package leo13

import leo.base.notNullIf
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class TypeFunctions(val functionStack: Stack<TypeFunction>)
data class TypeFunction(val typeLink: TypeLink, val type: Type)

val Stack<TypeFunction>.types get() = TypeFunctions(this)
fun TypeFunctions.plus(function: TypeFunction) = functionStack.push(function).types
fun types(vararg functions: TypeFunction) = stack(*functions).types
fun function(link: TypeLink, type: Type) = TypeFunction(link, type)

fun TypeFunctions.type(typeLink: TypeLink): Type =
	typeOrNull(typeLink) ?: typeLink.type

fun TypeFunctions.typeOrNull(typeLink: TypeLink): Type? =
	typeLink.scriptLinkOrNull?.let { typeOrNull(it) }

fun TypeFunctions.typeOrNull(scriptLink: ScriptLink): Type? =
	functionStack.mapFirst { typeOrNull(scriptLink) }

fun TypeFunction.typeOrNull(scriptLink: ScriptLink): Type? =
	notNullIf(typeLink.matches(scriptLink)) { type }
