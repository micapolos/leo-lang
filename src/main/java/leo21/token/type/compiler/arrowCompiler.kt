package leo21.token.type.compiler

import leo13.Either
import leo13.firstEither
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

data class ArrowCompiler(
	val parentOrNull: ArrowParent?,
	val typeOrArrow: Either<Type, Arrow>
)

fun ArrowCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> error("$token")
		is BeginToken -> typeOrArrow.select(
			{ type ->
				when (token.begin.string) {
					"doing" -> TypeCompilerTokenProcessor(
						TypeCompiler(
							ArrowDoingTypeParent(this, type),
							type()))
					else ->
						TypeCompilerTokenProcessor(
							TypeCompiler(
								ArrowNameTypeParent(this, type, token.begin.string),
								type()))
				}
			},
			{ arrow -> error("$arrow") })
		is EndToken -> parentOrNull!!.plus(typeOrArrow.second)
	}

fun ArrowCompiler.plusDoing(lhs: Type, rhs: Type): ArrowCompiler =
	copy(typeOrArrow = lhs.arrowTo(rhs).secondEither())

fun ArrowCompiler.set(type: Type): ArrowCompiler =
	copy(typeOrArrow = type.firstEither())