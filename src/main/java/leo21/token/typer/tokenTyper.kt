package leo21.token.typer

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TyperTokenProcessor
import leo21.type.Type
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.type

data class TokenTyper(
	val parentOrNull: TypeParent?,
	val type: Type
)

val emptyTokenTyper = TokenTyper(null, type())

fun TokenTyper.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> TyperTokenProcessor(plusBegin(token.begin.string))
		is EndToken -> parentOrNull!!.plus(type)
	}

fun TokenTyper.plusBegin(name: String): TokenTyper =
	when (name) {
		"choice" -> TODO()
		else -> TokenTyper(
			TyperNameTypeParent(this, name),
			type())
	}

fun TokenTyper.plus(name: String, rhs: Type): TokenTyper =
	when (name) {
		"number" -> notNullIf(rhs == type()) { set(type.plus(doubleLine)) }
		"string" -> notNullIf(rhs == type()) { set(type.plus(stringLine)) }
		"function" -> TODO()
		else -> null
	} ?: set(type.plus(name lineTo rhs))

fun TokenTyper.set(type: Type): TokenTyper =
	copy(type = type)