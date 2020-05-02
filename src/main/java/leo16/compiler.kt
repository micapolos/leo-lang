package leo16

import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo15.*

data class Compiler(val parentOrNull: CompilerParent?, val compiled: Compiled, val isMeta: Boolean) {
	override fun toString() = asField.toString()
}

data class CompilerParent(val compiler: Compiler, val word: String) {
	override fun toString() = asField.toString()
}

fun CompilerParent?.evaluator(compiled: Compiled, isMeta: Boolean) = Compiler(this, compiled, isMeta)
infix fun CompilerParent?.evaluator(compiled: Compiled) = Compiler(this, compiled, isMeta = false)
val Compiled.compiler get() = nullOf<CompilerParent>().evaluator(this)
val Scope.compiler get() = emptyCompiled.compiler
fun Compiler.parent(word: String) = CompilerParent(this, word)
val emptyCompiler = emptyScope.emptyCompiled.compiler

val Compiler.asField: Field
	get() =
		compilerName(
			parentOrNull?.asField.orIfNull { parentName(nothingName()) },
			compiled.asSentence,
			metaName(if (isMeta) trueName() else falseName()))

val CompilerParent.asField: Field
	get() =
		parentName(compiler.asField, wordName(word()))

operator fun Compiler.plus(token: Token): Compiler? =
	when (token) {
		is LiteralToken -> append(token.literal.field)
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
		is LiteralField -> append(field)
	}

operator fun Compiler.plus(sentence: Sentence): Compiler =
	begin(sentence.word).plus(sentence.value).end!!

operator fun Compiler.plus(literal: Literal): Compiler =
	plus(token(literal))!!

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
	notNullIf(field == compilerName(value())) {
		compiled.scope.compiled(value(asField))
	}

fun Compiler.updateCompiled(fn: Compiled.() -> Compiled): Compiler =
	copy(compiled = compiled.fn())
