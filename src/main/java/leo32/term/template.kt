@file:Suppress("unused")

package leo32.term

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo32.base.List
import leo32.base.add
import leo32.base.list
import leo32.base.seq

data class Template(
	val selector: Selector?,
	val fieldList: List<TemplateField>)

val Selector.template get() =
	Template(this, list())

val Empty.template get() =
	Template(null, list())

fun Template.plus(field: TemplateField) =
	copy(fieldList = fieldList.add(field))

fun Template.plus(name: String) =
	plus(name fieldTo empty.template)

fun template(string: String) =
	empty.template.plus(string)

fun template(vararg fields: TemplateField) =
	empty.template.fold(fields) { plus(it) }

fun template(selector: Selector, vararg fields: TemplateField) =
	selector.template.fold(fields) { plus(it) }

fun Template.invoke(term: Term): Term =
	(selector?.let { term.invoke(it) } ?: empty.term)
		.fold(fieldList.seq) { plus(it.invoke(term)) }
