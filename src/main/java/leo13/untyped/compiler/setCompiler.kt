package leo13.untyped.compiler

import leo13.untyped.TokenReader

data class SetCompiler(
	val parentCompiler: Compiler,
	val compiledSet: CompiledSet) : TokenReader {

	override fun begin(name: String) =
		SetCompilerParent(linkTo(name)).compiler(parentCompiler.context)

	override val end
		get() =
			parentCompiler.plus(compiledSet)
}

data class SetCompilerLink(val setCompiler: SetCompiler, val name: String)

infix fun SetCompiler.linkTo(name: String) = SetCompilerLink(this, name)

fun SetCompiler.plus(line: CompiledLine) =
	copy(compiledSet = compiledSet.plus(line))

fun Compiler.setCompiler() = SetCompiler(this, compiledSet())
