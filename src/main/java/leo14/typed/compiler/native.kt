package leo14.typed.compiler

import leo14.Literal
import leo14.native.Native
import leo14.typed.Typed
import leo14.typed.native
import leo14.typed.nativeResolve
import leo14.typed.typed

val nativeContext: Context<Native> =
	Context(englishDictionary, memory(), Typed<Native>::nativeResolve, Literal::native)

val nativeCompiler: Compiler<Native> =
	TypedCompiler(null, nativeContext, typed())
