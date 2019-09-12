package leo13.untyped.compiler

import leo13.script.lineTo
import leo13.script.plus
import leo13.untyped.Context
import leo13.untyped.TokenReader

data class Compiler(
	val parent: CompilerParent,
	val context: Context,
	val compiled: Compiled) : TokenReader {


	override fun begin(name: String) =
		when (name) {
			"set" -> setCompiler()
			"switch" -> switchCompiler()
			else -> SelfCompilerParent(linkTo(name)).compiler(context)
		}

	override val end get() = parent.plus(compiled)
}

fun CompilerParent.compiler(context: Context, compiled: Compiled = compiled()) =
	Compiler(this, context, compiled)

fun Compiler.plusSwitch(switch: Compiled): TokenReader? =
	set(
		compiled(
			compiled.script.plus("switch" lineTo switch.script),
			switch.pattern))

fun Compiler.plusSet(compiled: Compiled): TokenReader? =
	TODO()

fun Compiler.plus(line: CompiledLine): Compiler? =
	TODO()
//	if (line.rhs.expression.isEmpty) copy(compiled = value()).plusResolved(line.name lineTo compiled)
//	else resolve(line)

fun Compiler.resolve(line: CompiledLine): TokenReader? =
	when (line.name) {
		"set" -> setCompiler()
		else -> TODO()
	}

fun Compiler.plusResolved(line: CompiledLine): TokenReader? =
	TODO()

fun Compiler.set(compiled: Compiled) =
	copy(compiled = compiled)