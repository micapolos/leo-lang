package leo16

import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo13.fold
import leo13.reverse
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
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
val Scope.compiler get() = emptyCompiled.compiler
fun Compiler.parent(word: String) = CompilerParent(this, word)
val emptyCompiler = emptyScope.emptyCompiled.compiler

val Compiler.asSentence: Sentence
	get() =
		compilerName(
			parentOrNull?.asSentence.orIfNull { parentName(nothingName()) },
			compiled.asSentence,
			metaName(if (isMeta) trueName() else falseName()))

val CompilerParent.asSentence: Sentence
	get() =
		parentName(compiler.asSentence, wordName(word()))

operator fun Compiler.plus(token: Token): Compiler? =
	when (token) {
		is LiteralToken -> TODO()
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end
	}

operator fun Compiler.plus(value: Value): Compiler =
	fold(value.fieldStack.reverse) { plus(it) }

operator fun Compiler.plus(field: Field): Compiler =
	when (field) {
		is SentenceField -> plus(field.sentence)
		is FunctionField -> append(field)
		is LibraryField -> append(field)
	}

operator fun Compiler.plus(sentence: Sentence): Compiler =
	begin(sentence.word).plus(sentence.value).end!!

fun Compiler.begin(word: String): Compiler =
	parent(word).evaluator(compiled.begin, isMeta || word.wordIsMeta)

val Compiler.end: Compiler?
	get() =
		parentOrNull?.endEvaluator(compiled)

fun CompilerParent.endEvaluator(compiled: Compiled): Compiler =
	compiler.append(word.invoke(compiled.value))

fun Compiler.append(field: Field): Compiler =
	updateCompiled {
		applyCompiler(field) ?: if (isMeta) plus(field)
		else apply(field)
	}

fun Compiler.append(sentence: Sentence): Compiler =
	append(sentence.field)

fun Compiler.applyCompiler(field: Field): Compiled? =
	notNullIf(field == compilerName(value()).field) {
		compiled.scope.compiled(value(asSentence.field))
	}

fun Compiler.updateCompiled(fn: Compiled.() -> Compiled): Compiler =
	copy(compiled = compiled.fn())
