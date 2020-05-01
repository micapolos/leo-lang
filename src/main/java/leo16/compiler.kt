package leo16

import leo.base.fold
import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo15.*

data class Compiler(val parentOrNull: CompilerParent?, val compiled: Compiled, val isMeta: Boolean) {
	override fun toString() = asSentence.toString()
}

data class CompilerParent(val compiler: Compiler, val word: String) {
	override fun toString() = asSentence.toString()
}

fun CompilerParent?.evaluator(compiled: Compiled, isMeta: Boolean) = Compiler(this, compiled, isMeta)
infix fun CompilerParent?.evaluator(compiled: Compiled) = Compiler(this, compiled, isMeta = false)
val Compiled.compiler get() = nullOf<CompilerParent>().evaluator(this)
val Library.compiler get() = emptyCompiled.compiler
fun Compiler.parent(word: String) = CompilerParent(this, word)
val emptyCompiler = emptyLibrary.emptyCompiled.compiler

val Compiler.asSentence: Sentence
	get() =
		compilerName(
			parentOrNull?.asSentence.orIfNull { parentName(nothingName()) },
			compiled.asSentence,
			metaName(if (isMeta) trueName() else falseName()))

val CompilerParent.asSentence: Sentence
	get() =
		parentName(compiler.asSentence, wordName(word()))

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
	updateCompiled {
		applyCompiler(line) ?: if (isMeta) plus(line)
		else apply(line)
	}

fun Compiler.applyCompiler(line: Line): Compiled? =
	notNullIf(line == compilerName(value())) {
		compiled.library.compiled(value(asSentence.line))
	}

fun Compiler.updateCompiled(fn: Compiled.() -> Compiled): Compiler =
	copy(compiled = compiled.fn())
