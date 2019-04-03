@file:Suppress("unused")

package leo32.term

import leo.base.Empty
import leo.base.empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class TemplateResolver(
	val typeToTemplateDict: Dict<Type, Template>)

val Dict<Type, Template>.templateResolver get() =
	TemplateResolver(this)

val Empty.templateResolver get() =
	empty.typeDict<Template>().templateResolver

fun TemplateResolver.put(type: Type, template: Template) =
	copy(typeToTemplateDict = typeToTemplateDict.put(type, template))

fun TemplateResolver.resolve(type: Type): Template =
	typeToTemplateDict.at(type)?:template(selector())