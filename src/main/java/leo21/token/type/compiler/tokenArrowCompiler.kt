package leo21.token.type.compiler

import leo13.Either
import leo13.second
import leo13.secondEither
import leo13.select
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type

data class TokenArrowCompiler(
	val parentOrNull: ArrowParent?,
	val typeOrArrow: Either<Type, Arrow>
)

fun TokenArrowCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> error("$token")
		is BeginToken -> typeOrArrow.select(
			{ type ->
				when (token.begin.string) {
					"doing" -> TypeCompilerTokenProcessor(
						TokenTypeCompiler(
							ArrowDoingTypeParent(this, type),
							type()))
					else -> TODO()
				}
			},
			{ arrow -> error("$arrow") })
		is EndToken -> parentOrNull!!.plus(typeOrArrow.second)
	}

fun TokenArrowCompiler.plusDoing(lhs: Type, rhs: Type): TokenArrowCompiler =
	copy(typeOrArrow = lhs.arrowTo(rhs).secondEither())
