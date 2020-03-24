package leo14.untyped

import leo14.Script

sealed class Compiler

data class ContextCompiler(val context: Context) : Compiler()
data class LinkCompiler(val link: CompilerLink) : Compiler()
data class CompilerLink(val compiler: Compiler, val recursive: Recursive)

fun compiler(context: Context): Compiler = ContextCompiler(context)
fun compiler(link: CompilerLink): Compiler = LinkCompiler(link)
infix fun Compiler.linkTo(recursive: Recursive) = CompilerLink(this, recursive)

fun Compiler.compile(thunk: Thunk): Compiler? =
	when (this) {
		is ContextCompiler -> context.compile(thunk)?.let(::compiler)
		is LinkCompiler -> link.compile(thunk)?.let(::compiler)
	}

fun CompilerLink.compile(thunk: Thunk): CompilerLink? =
	recursive.compile(thunk)?.let { compiler linkTo it }

fun Recursive.compile(thunk: Thunk): Recursive? =
	context.compile(thunk)?.let(::recursive)

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

fun Compiler.evalThunk(script: Script): Thunk =
	resolver().compile(script).thunk
