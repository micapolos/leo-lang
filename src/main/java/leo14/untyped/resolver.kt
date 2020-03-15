package leo14.untyped

data class Resolver(
	val context: Context,
	val program: Program)

fun Context.resolver(program: Program = program()) =
	Resolver(this, program)

fun resolver(program: Program = program()) =
	context().resolver(program)

fun Resolver.apply(value: Value): Resolver =
	context.resolve(this.program.plus(value))

fun Context.resolve(program: Program): Resolver =
	null
		?: resolveContext(program)
		?: resolveDefinitions(program)
		?: resolveStatic(program)

fun Context.resolveStatic(program: Program): Resolver =
	null
		?: resolveCompile(program)
		?: resolver(program)

fun Context.resolveContext(program: Program): Resolver? =
	apply(program)?.let { resolver(it) }

fun Context.resolveDefinitions(program: Program): Resolver? =
	compile(program)?.resolver()

fun Context.resolveCompile(program: Program) =
	program.matchPostfix("compile") { lhs ->
		resolver().tokenizer().append(lhs).resolver
	}

fun Resolver.set(program: Program): Resolver =
	copy(program = program)

val Resolver.clear
	get() =
		set(program())
