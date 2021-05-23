package leo25

data class Context(
	val resolver: Resolver,
	val library: Library
)

fun context(resolver: Resolver, library: Library) = Context(resolver, library)

fun context() = context(resolver(), library())
val nativeContext get() = context(nativeResolver, library())

fun Context.plus(definition: Definition): Context =
	context(resolver.plus(definition), library.plus(definition))