package leo14.typed.compiler.natives

import leo14.Literal
import leo14.native.Native
import leo14.native.invoke
import leo14.typed.*
import leo14.typed.compiler.Context
import leo14.typed.compiler.defaultDictionary

val context: Context<Native> =
	Context(
		defaultDictionary,
		Typed<Native>::nativeResolve,
		Literal::nativeTypedLine,
		Native::invoke,
		TypedLine<Native>::decompileLiteral)
