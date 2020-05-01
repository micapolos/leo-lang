package leo16

import leo.base.fold
import leo.base.nullOf

data class Compiler(val parentOrNull: CompilerParent?, val compiled: Compiled, val isMeta: Boolean)
data class CompilerParent(val compiler: Compiler, val word: String)

fun CompilerParent?.evaluator(compiled: Compiled, isMeta: Boolean) = Compiler(this, compiled, isMeta)
infix fun CompilerParent?.evaluator(compiled: Compiled) = Compiler(this, compiled, isMeta = false)
val Compiled.compiler get() = nullOf<CompilerParent>().evaluator(this)
val Scope.compiler get() = compiled(value()).compiler
fun Compiler.parent(word: String) = CompilerParent(this, word)
val emptyCompiler = emptyScope.compiled(value()).compiler

operator fun Compiler.plus(script: Script): Compiler =
	fold(script.tokenSeq) { plus(it) }

operator fun Compiler.plus(token: Token): Compiler =
	when (token) {
		is BeginToken -> begin(token.word)
		EndToken -> end
	}

fun Compiler.begin(word: String): Compiler =
	parent(word).evaluator(compiled.begin, isMeta || word.wordIsMeta)

val Compiler.end: Compiler
	get() =
		parentOrNull!!.endEvaluator(compiled)

fun CompilerParent.endEvaluator(compiled: Compiled): Compiler =
	compiler.plus(word.invoke(compiled.value))

operator fun Compiler.plus(line: Line): Compiler =
	updateEvaluated {
		if (isMeta) plus(line)
		else apply(line)
	}

fun Compiler.updateEvaluated(fn: Compiled.() -> Compiled): Compiler =
	copy(compiled = compiled.fn())
