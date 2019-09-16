package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.caseTo
import leo13.untyped.expression.expression
import leo13.untyped.pattern.Either

data class CaseCompiler(
	val converter: Converter<CaseCompiled, Token>,
	val context: Context,
	val either: Either,
	val compiledOrNull: CaseCompiled?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				either.scriptingLine,
				compiledOrNull
					?.scriptingLine
					?: "compiled" lineTo script("case" lineTo script("null")))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		if (compiledOrNull != null) tracedError("single" lineTo script("line"))
		else if (name != either.name) tracedError("expected" lineTo script(either.name))
		else compiler(
			converter {
				plus(
					compiled(
						name caseTo it.expression,
						it.pattern))
			},
			context,
			compiled(expression(), either.rhs))

	val end
		get() =
			if (compiledOrNull == null) tracedError("empty" lineTo script("case"))
			else converter.convert(compiledOrNull)

	fun plus(compiled: CaseCompiled) =
		set(compiled)

	fun set(compiledOrNull: CaseCompiled?) =
		copy(compiledOrNull = compiledOrNull)
}

fun caseCompiler(
	converter: Converter<CaseCompiled, Token>,
	context: Context,
	either: Either,
	compiledOrNull: CaseCompiled? = null) =
	CaseCompiler(converter, context, either, compiledOrNull)

