package leo14.typed

import leo14.*
import leo14.Number
import leo14.lambda.invoke
import leo14.lambda.nativeEval
import leo14.lambda.term
import leo14.native.Native
import leo14.native.intPlusIntNative
import leo14.native.native
import leo14.typed.compiler.Compiler
import leo14.typed.compiler.compile
import leo14.typed.compiler.nativeCompiler
import leo14.typed.compiler.typed

val Typed<Native>.nativeEval
	get() =
		term.nativeEval of type

val Literal.native: Native
	get() =
		when (this) {
			is StringLiteral -> native(string)
			is NumberLiteral -> number.native
		}

val Number.native: Native
	get() =
		when (this) {
			is IntNumber -> native(int)
			is DoubleNumber -> error("$double.native")
		}

val Script.nativeCompile: Compiler<Native>
	get() =
		nativeCompiler.compile(this)

val Typed<Native>.nativeDecompile
	get() =
		decompile(Native::scriptLine)

val Script.nativeEval: Script
	get() =
		nativeCompile.typed.nativeEval.nativeDecompile

val Typed<Native>.nativeResolve: Typed<Native>?
	get() =
		when (type) {
			nativeType.plus("plus" lineTo nativeType) ->
				lineLink.run {
					term(intPlusIntNative).invoke(tail.term).invoke(head.term) of nativeType
				}
			else -> null
		}