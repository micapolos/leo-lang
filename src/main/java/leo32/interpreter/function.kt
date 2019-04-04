package leo32.interpreter

import leo32.runtime.Term
import leo32.runtime.invoke
import leo32.runtime.parameter

data class Function(
	val types: Types,
	val templateResolver: TemplateResolver)

fun Function.invoke(term: Term) =
	templateResolver.resolve(types.typeOf(term)).invoke(parameter(term))
