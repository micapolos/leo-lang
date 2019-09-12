package leo13.untyped.compiler

import leo13.untyped.TokenReader

data class SetCompiler(
	val parentCompiler: Compiler,
	val compiled: Compiled) : TokenReader {

	override fun begin(name: String) =
		SetCompilerParent(linkTo(name)).compiler(parentCompiler.context)

	override val end
		get() =
			parentCompiler.plusSet(compiled)
}

data class SetCompilerLink(val setCompiler: SetCompiler, val name: String)

infix fun SetCompiler.linkTo(name: String) = SetCompilerLink(this, name)

fun SetCompiler.plus(line: CompiledLine) =
	copy(compiled = compiled.plus(line))

fun Compiler.setCompiler() = SetCompiler(this, compiled())
