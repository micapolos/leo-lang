package leo14.untyped

import leo.base.ifNotNull
import leo14.*

data class Tokenizer(
	val parentOrNull: TokenizerParent?,
	val evaluator: Evaluator)

data class TokenizerParent(
	val tokenizer: Tokenizer,
	val begin: Begin)

val emptyTokenizer
	get() =
		(null as TokenizerParent?).tokenizer(emptyEvaluator)

fun TokenizerParent?.tokenizer(evaluator: Evaluator) =
	Tokenizer(this, evaluator)

fun Tokenizer.parent(begin: Begin) =
	TokenizerParent(this, begin)

fun Tokenizer.write(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> write(token.literal)
		is BeginToken -> write(token.begin)
		is EndToken -> write(token.end)
	}

fun Tokenizer.write(begin: Begin): Tokenizer? =
	ifNotNull(evaluator.write(begin)) {
		parent(begin).tokenizer(it)
	}

fun Tokenizer.write(literal: Literal): Tokenizer? =
	evaluator.write(value(literal)).let {
		parentOrNull.tokenizer(it)
	}

fun Tokenizer.write(end: End): Tokenizer? =
	parentOrNull?.write(evaluator.program)

fun TokenizerParent.write(program: Program) =
	tokenizer.write(begin.string fieldTo program)

fun Tokenizer.write(field: Field): Tokenizer? =
	evaluator.write(field)?.let {
		parentOrNull.tokenizer(it)
	}
