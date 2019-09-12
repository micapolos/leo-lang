package leo13.untyped.compiler

import leo13.script.plus
import leo13.untyped.TokenReader
import leo13.untyped.setOrNull

data class SetCompiler(
	val parentCompiler: Compiler,
	val compiled: Compiled) : TokenReader {

	override fun begin(name: String) =
		linkTo(name).parent.compiler(parentCompiler.context)

	override val end
		get() =
			parentCompiler.plusSet(compiled)
}

fun SetCompiler.plus(line: CompiledLine) =
	compiled.pattern.setOrNull(line.patternLine)?.let { pattern ->
		set(
			compiled(
				compiled.script.plus(line.scriptLine),
				pattern))
	}

fun SetCompiler.set(compiled: Compiled) =
	copy(compiled = compiled)

fun Compiler.setCompiler() = SetCompiler(this, compiled())
