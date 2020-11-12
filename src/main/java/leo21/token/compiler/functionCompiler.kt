package leo21.token.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.lambda.fn
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.compiled
import leo21.compiled.lineCompiled
import leo21.compiled.of
import leo21.compiler.Compiler
import leo21.compiler.push
import leo21.token.processor.CompilerTokenProcessor
import leo21.token.processor.FunctionTypeCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.token.type.compiler.FunctionTypeCompilerTypeSibling
import leo21.token.type.compiler.TypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type
import leo21.type.arrowTo

sealed class FunctionParent
data class CompilerFunctionParent(val compiler: TokenCompiler) : FunctionParent()

data class FunctionTypeCompiler(
	val parentCompiler: TokenCompiler,
	val typeCompiler: TypeCompiler
)

data class FunctionCompiler(
	val parentOrNull: FunctionParent?,
	val arrowCompiled: ArrowCompiled
)

fun FunctionTypeCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken ->
			FunctionTypeCompilerTokenProcessor(
				copy(typeCompiler = (typeCompiler.plus(token) as TypeCompilerTokenProcessor).typeCompiler))
		is BeginToken ->
			when (token.begin.string) {
				"does" ->
					CompilerTokenProcessor(
						TokenCompiler(
							FunctionDoesCompiledParent(this),
							Compiler(parentCompiler.lineCompiler.bindings.push(typeCompiler.type), compiled())))
				else ->
					typeCompiler
						.copy(siblingOrNull = FunctionTypeCompilerTypeSibling(this))
						.plus(token)
			}
		is EndToken -> null!!
	}

fun FunctionTypeCompiler.process(type: Type): TokenProcessor =
	FunctionTypeCompilerTokenProcessor(copy(typeCompiler = typeCompiler.copy(type = type)))

fun FunctionCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> null!!
		is EndToken -> parentOrNull!!.plus(arrowCompiled)
	}

fun FunctionParent.plus(arrowCompiled: ArrowCompiled): TokenProcessor =
	when (this) {
		is CompilerFunctionParent ->
			CompilerTokenProcessor(compiler.plus(lineCompiled(arrowCompiled)))
	}

fun FunctionTypeCompiler.plusDoes(compiled: Compiled): FunctionCompiler =
	FunctionCompiler(
		CompilerFunctionParent(parentCompiler),
		fn(compiled.term) of (typeCompiler.type arrowTo compiled.type))
