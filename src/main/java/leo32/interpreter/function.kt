package leo32.interpreter

import leo32.runtime.Term
import leo32.runtime.invoke
import leo32.runtime.parameter

data class Function(
	val typeResolver: TypeResolver,
	val templateResolver: TemplateResolver)

fun Function.invoke(term: Term) =
	templateResolver.resolve(typeResolver.resolve(term)).invoke(parameter(term))
