package leo32.term

data class Function(
	val typeResolver: TypeResolver,
	val templateResolver: TemplateResolver)

fun Function.invoke(term: Term) =
	templateResolver.resolve(typeResolver.resolve(term)).invoke(term)
