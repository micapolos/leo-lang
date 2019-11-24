package leo14.typed.compiler.natives

import leo14.Literal
import leo14.defaultDictionary
import leo14.native.Native
import leo14.native.invoke
import leo14.typed.*
import leo14.typed.compiler.Context

val context: Context<Native> =
	Context(
		defaultDictionary,
		Typed<Native>::nativeResolve,
		Literal::nativeTypedLine,
		Native::invoke,
		TypedLine<Native>::decompileLiteral)
