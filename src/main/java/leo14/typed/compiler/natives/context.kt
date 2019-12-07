package leo14.typed.compiler.natives

import leo14.Literal
import leo14.defaultLanguage
import leo14.lambda.Term
import leo14.native.Native
import leo14.native.nativeEvaluator
import leo14.script
import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Context

val emptyContext: Context<Native> =
	Context(
		defaultLanguage,
		Typed<Native>::nativeResolve,
		Literal::nativeTypedLine,
		nativeEvaluator,
		nativeTypeContext,
		TypedLine<Native>::decompileLiteral,
		Term<Native>::termDecompile,
		script("native"))
