package leo14.untyped

sealed class Compiler

data class ContextCompiler(val context: Context) : Compiler()
data class LinkCompiler(val link: CompilerLink) : Compiler()
data class CompilerLink(val compiler: Compiler, val recursive: Recursive)

fun compiler(context: Context): Compiler = ContextCompiler(context)
fun compiler(link: CompilerLink): Compiler = LinkCompiler(link)
infix fun Compiler.linkTo(recursive: Recursive) = CompilerLink(this, recursive)

fun Compiler.compile(program: Program): Compiler? =
	when (this) {
		is ContextCompiler -> context.compile(program)?.let(::compiler)
		is LinkCompiler -> link.compile(program)?.let(::compiler)
	}

fun CompilerLink.compile(program: Program): CompilerLink? =
	recursive.compile(program)?.let { compiler linkTo it }

fun Recursive.compile(program: Program): Recursive? =
	context.compile(program)?.let(::recursive)

val Compiler.applyContext: Context
	get() =
		when (this) {
			is ContextCompiler -> context
			is LinkCompiler -> link.applyContext
		}

val CompilerLink.applyContext: Context
	get() =
		compiler.applyContext

fun Compiler.push(definition: Definition): Compiler =
	when (this) {
		is ContextCompiler -> compiler(context.push(definition))
		is LinkCompiler -> compiler(link.push(definition))
	}

fun CompilerLink.push(definition: Definition) =
	compiler linkTo recursive.push(definition)

fun Recursive.push(definition: Definition): Recursive =
	recursive(context.push(definition))
