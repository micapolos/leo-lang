@file:Suppress("unused")

package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Template

data class EmptyTemplate(
	val empty: Empty): Template()

data class ArgumentTemplate(
	val argument: Argument): Template()

data class ApplicationTemplate(
	val application: Application): Template()

val Empty.template get() =
	EmptyTemplate(this) as Template

val Argument.template get() =
	ArgumentTemplate(this) as Template

val Application.template get() =
	ApplicationTemplate(this) as Template

fun Template.plus(op: Op) =
	application(op).template

fun Template.plus(field: FunctionField) =
	plus(op(field))

fun Template.plus(name: String) =
	plus(name to template())

fun template(vararg ops: Op) =
	empty.template.fold(ops) { plus(it) }

fun Template.plus(field: TermField): Template =
	plus(field.functionField)

fun template(term: Term): Template =
	template().fold(term.fieldSeq) { plus(it) }

fun template(argument: Argument, vararg ops: Op) =
	argument.template.fold(ops) { plus(it) }

fun template(string: String) =
	empty.template.plus(string)

fun Template.invoke(parameter: Parameter): Term =
	when (this) {
		is EmptyTemplate -> empty.term
		is ArgumentTemplate -> argument.invoke(parameter)
		is ApplicationTemplate -> application.invoke(parameter)
	}
