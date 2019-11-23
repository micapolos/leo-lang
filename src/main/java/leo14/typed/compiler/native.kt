package leo14.typed.compiler

import leo14.Literal
import leo14.native.Native
import leo14.native.invoke
import leo14.typed.*

val nativeContext: Context<Native> =
	Context(
		defaultDictionary,
		Typed<Native>::nativeResolve,
		Literal::nativeTypedLine,
		Native::invoke,
		TypedLine<Native>::decompileLiteral)
