package leo14.typed.compiler.natives

import leo14.Literal
import leo14.defaultLanguage
import leo14.native.Native
import leo14.native.nativeEvaluator
import leo14.script
import leo14.typed.*
import leo14.typed.compiler.Context

val context: Context<Native> =
	Context(
		defaultLanguage,
		Typed<Native>::nativeResolve,
		Literal::nativeTypedLine,
		nativeEvaluator,
		nativeTypeContext,
		TypedLine<Native>::decompileLiteral,
		script("native"))
