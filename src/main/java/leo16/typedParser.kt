package leo16

import leo.base.fold
import leo.base.reverse
import leo14.BeginToken
import leo14.EndToken
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.LiteralToken
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.Token
import leo14.lineSeq
import leo16.names.*

data class TypedParser(val parentOrNull: TypedParserParent?, val evaluator: TypedEvaluator)
data class TypedParserParent(val parser: TypedParser, val word: String)

fun TypedParserParent?.parser(evaluator: TypedEvaluator) = TypedParser(this, evaluator)
fun TypedParser.parent(word: String) = TypedParserParent(this, word)

fun TypedParser.begin(word: String): TypedParser =
	parent(word).parser(evaluator.begin)

fun TypedParser.plusNative(native: Any?): TypedParser =
	set(evaluator.set(evaluator.typed.plusNativeOrNull(native)!!))

fun TypedParser.plus(literal: Literal): TypedParser =
	when (literal) {
		is StringLiteral -> begin(_text).plusNative(literal.string).end
		is NumberLiteral -> begin(_number).plusNative(literal.number.bigDecimal).end
	}

fun TypedParser.plus(token: Token): TypedParser =
	when (token) {
		is LiteralToken -> plus(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end
	}

fun TypedParser.plus(script: Script): TypedParser =
	fold(script.lineSeq.reverse) { plus(it) }

fun TypedParser.plus(scriptLine: ScriptLine): TypedParser =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun TypedParser.plus(scriptField: ScriptField): TypedParser =
	begin(scriptField.string).plus(scriptField.rhs).end

val TypedParser.end: TypedParser get() = TODO()

fun TypedParser.set(evaluator: TypedEvaluator) = copy(evaluator = evaluator)
