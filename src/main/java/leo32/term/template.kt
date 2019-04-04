@file:Suppress("unused")

package leo32.term

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Template

data class EmptyTemplate(
	val empty: Empty): Template()

data class ApTemplate(
	val lhs: Template,
	val ap: Ap): Template()

val Empty.template get() =
	EmptyTemplate(this) as Template

fun Template.plus(ap: Ap) =
	ApTemplate(this, ap) as Template

fun Template.plus(field: TemplateField) =
	plus(field.ap)

fun Template.plus(name: String) =
	plus(name.templateField)

fun template(string: String) =
	template(string.templateField.ap)

fun template(vararg aps: Ap) =
	empty.template.fold(aps) { plus(it) }

fun Template.invoke(term: Term): Term =
	when (this) {
		is EmptyTemplate -> empty.term
		is ApTemplate -> ap.invoke(???, term)
	}
