package leo

import leo.base.*

data class TokenEvaluator(
	val entryStackOrNull: Stack<Entry>?,
	val scope: Scope,
	val wordOrNull: Word?) {
	override fun toString() = reflect.string

	data class Entry(
		val scope: Scope,
		val word: Word) {
		override fun toString() = reflect.string
	}
}

val emptyTokenEvaluator =
	TokenEvaluator(null, emptyScope, null)

val TokenEvaluator.entryStreamOrNull: Stream<TokenEvaluator.Entry>?
	get() =
		entryStackOrNull?.reverse?.stream

fun TokenEvaluator.evaluate(token: Token<Nothing>): TokenEvaluator? =
	when (token) {
		is MetaToken -> fail
		is WordToken -> evaluate(token)
		is ControlToken -> evaluate(token)
	}

fun TokenEvaluator.evaluateInternal(token: Token<Nothing>): Evaluator<Token<Nothing>>? =
	evaluate(token)?.evaluator

fun TokenEvaluator.apply(term: Term<Nothing>): Match? =
	function.get(term)

fun TokenEvaluator.evaluate(controlToken: ControlToken<Nothing>): TokenEvaluator? =
	when (controlToken.control) {
		is BeginControl -> evaluateBegin
		is EndControl -> evaluateEnd
	}

fun TokenEvaluator.evaluate(wordToken: WordToken<Nothing>): TokenEvaluator? =
	wordOrNull.ifNull {
		copy(wordOrNull = wordToken.word)
	}

val TokenEvaluator.evaluateBegin: TokenEvaluator?
	get() =
		wordOrNull?.let { word ->
			copy(
				entryStackOrNull = entryStackOrNull.push(TokenEvaluator.Entry(scope, word)),
				scope = Scope(scope.function, null),
				wordOrNull = null)
		}

val TokenEvaluator.evaluateEnd: TokenEvaluator?
	get() =
		wordOrNull.ifNull {
			entryStackOrNull?.let { entryStack ->
				if (scope.termOrNull == null)
					entryStack.head.scope.push(entryStack.head.word)?.let { pushedScope ->
						copy(
							entryStackOrNull = entryStack.tail,
							scope = pushedScope.evaluate)
					}
				else
					entryStack.head.scope.push(entryStack.head.word fieldTo scope.termOrNull)?.let { pushedScope ->
						copy(
							entryStackOrNull = entryStack.tail,
							scope = pushedScope.evaluate)
					}
			}
		}

// === byte stream

val TokenEvaluator.bitStreamOrNull: Stream<EnumBit>?
	get() =
		entryStreamOrNull
			?.map { it.coreBitStream.then { beginBitStream } }
			?.join
			.orNullThenIfNotNull { scope.bitStreamOrNull(wordOrNull != null) }

val TokenEvaluator.Entry.coreBitStream: Stream<EnumBit>
	get() =
		scope.bitStreamOrNull(true)?.then { word.bitStream } ?: word.bitStream

val TokenEvaluator.function: Function
	get() =
		scope.function

val TokenEvaluator.theEvaluatedTermOrNull: The<Term<Nothing>?>?
	get() =
		entryStackOrNull.ifNull { scope.termOrNull.the }

val TokenEvaluator.evaluator: Evaluator<Token<Nothing>>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)

// === reflect

val TokenEvaluator.reflect: Field<Nothing>
	get() =
		evaluatorWord fieldTo term(
			entryWord fieldTo term(
				stackWord fieldTo
					(entryStackOrNull?.stream?.reflect(TokenEvaluator.Entry::reflect) ?: nullWord.term)),
			scope.reflect,
			wordOrNull.orNullReflect(wordWord, Word::reflect)
		)

val TokenEvaluator.Entry.reflect: Field<Nothing>
	get() =
		entryWord fieldTo term(
			scope.reflect,
			word.reflect)
