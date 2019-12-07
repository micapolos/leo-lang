package leo14.typed.compiler.natives

import leo14.Literal
import leo14.defaultLanguage
import leo14.lambda.Term
import leo14.native.Native
import leo14.native.nativeEvaluator
import leo14.script
import leo14.scriptLine
import leo14.typed.TypedLine
import leo14.typed.compiler.Compiled
import leo14.typed.compiler.Context

val emptyContext: Context<Native> =
	Context(
		defaultLanguage,
		Compiled<Native>::nativeResolve,
		Literal::nativeTypedLine,
		nativeEvaluator,
		nativeTypeContext,
		TypedLine<Native>::decompileLiteral,
		Term<Native>::termDecompile,
		Native::scriptLine,
		script("native"))
