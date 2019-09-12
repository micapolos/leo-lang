package leo13.untyped.compiler

data class SetCompilerLink(val setCompiler: SetCompiler, val name: String)

infix fun SetCompiler.linkTo(name: String) = SetCompilerLink(this, name)

fun SetCompilerLink.setCompiler(compiled: Compiled): SetCompiler? =
	setCompiler.plus(name lineTo compiled)