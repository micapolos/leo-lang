package leo13.untyped.compiler

import leo13.script.lineTo
import leo13.script.plus
import leo13.untyped.Context
import leo13.untyped.TokenReader
import leo13.untyped.setName
import leo13.untyped.switchName

data class Compiler(
	val parent: CompilerParent,
	val context: Context,
	val compiled: Compiled) : TokenReader {


	override fun begin(name: String) =
		when (name) {
			setName -> setCompiler()
			switchName -> switchCompiler()
			else -> SelfCompilerParent(linkTo(name)).compiler(context)
		}

	override val end get() = parent.plus(compiled)
}

fun CompilerParent.compiler(context: Context, compiled: Compiled = compiled()) =
	Compiler(this, context, compiled)

fun Compiler.plusSwitch(switch: Compiled): TokenReader? =
	set(
		compiled(
			compiled.script.plus(switchName lineTo switch.script),
			switch.pattern))

fun Compiler.plusSet(compiledSet: Compiled): TokenReader? =
	set(
		compiled(
			compiled.script.plus(compiledSet.script),
			compiledSet.pattern))

fun Compiler.plus(line: CompiledLine): Compiler? =
	set(compiled.plus(line))

fun Compiler.set(compiled: Compiled) =
	copy(compiled = compiled)