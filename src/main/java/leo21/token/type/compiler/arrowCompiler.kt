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
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type

data class ArrowCompiler(
	val parentOrNull: ArrowParent?,
	val lines: Lines,
	val typeOrArrow: Either<Type, Arrow>
)

fun ArrowCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> error("$token")
		is BeginToken -> typeOrArrow.select(
			{ type ->
				when (token.begin.string) {
					"doing" -> TypeCompilerProcessor(
						TypeCompiler(
							ArrowDoingTypeParent(this, type),
							lines,
							type()))
					else ->
						TypeCompilerProcessor(
							TypeCompiler(
								ArrowNameTypeParent(this, type, token.begin.string),
								lines,
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