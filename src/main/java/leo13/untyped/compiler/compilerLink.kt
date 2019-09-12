package leo13.untyped.compiler

data class CompilerLink(val compiler: Compiler, val name: String)

infix fun Compiler.linkTo(name: String) =
	CompilerLink(this, name)

fun CompilerLink.compiler(compiled: Compiled): Compiler? =
	compiler.plus(name lineTo compiled)