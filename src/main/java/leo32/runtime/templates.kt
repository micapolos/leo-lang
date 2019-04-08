package leo32.runtime

import leo.base.Empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class Templates(
	val typeToTemplateDict: Dict<Term, Template>)

val Dict<Term, Template>.templates get() =
	Templates(this)

val Empty.templates get() =
	termDict<Template>().templates

fun Templates.put(type: Term, template: Template) =
	copy(typeToTemplateDict = typeToTemplateDict.put(type, template))

fun Templates.at(type: Term): Template? =
	typeToTemplateDict.at(type)
