package leo32.term

import leo.base.empty

data class TemplateField(
	val name: String,
	val value: Template)

infix fun String.fieldTo(template: Template) =
	TemplateField(this, template)

val String.templateField get() =
	this fieldTo empty.template

fun TemplateField.invoke(term: Term): TermField =
	name fieldTo value.invoke(term)
