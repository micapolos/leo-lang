package leo25

data class Context(
	val publicResolver: Resolver,
	val privateResolver: Resolver
)

fun context(publicResolver: Resolver, privateResolver: Resolver) =
	Context(publicResolver, privateResolver)

val Resolver.context
	get() =
		context(resolver(), this)

fun context() = context(resolver(), resolver())

fun Context.plus(definition: Definition): Context =
	context(publicResolver.plus(definition), privateResolver.plus(definition))

fun Context.plusPrivate(resolver: Resolver): Context =
	context(publicResolver, privateResolver.plus(resolver))

val Context.private: Context
	get() =
		context(resolver(), privateResolver)