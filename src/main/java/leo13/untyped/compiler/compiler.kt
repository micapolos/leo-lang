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

	override val end
		get() = when (parent) {
			is SelfCompilerParent -> parent.link.compiler.plus(parent.link.name lineTo compiled)
			is CaseCompilerParent -> parent.link.caseCompiler.plus(parent.link.name lineTo compiled)
			is SetCompilerParent -> parent.link.setCompiler.plus(parent.link.name lineTo compiled)
			is EvaluatorCompilerParent -> TODO()//parent.link.evaluator.plus(parent.link.name lineTo compiled)
		}
}

data class CompilerLink(val compiler: Compiler, val name: String)

infix fun Compiler.linkTo(name: String) = CompilerLink(this, name)

fun CompilerParent.compiler(context: Context, compiled: Compiled = compiled()) =
	Compiler(this, context, compiled)

fun Compiler.plusSwitch(switch: Compiled): TokenReader? =
	set(
		compiled(
			compiled.script.plus("switch" lineTo switch.script),
			switch.pattern))

fun Compiler.plusSet(compiled: Compiled): TokenReader? =
	TODO()

fun Compiler.plus(line: CompiledLine): TokenReader? =
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