package leo14.typed

import leo14.*
import leo14.Number
import leo14.lambda.invoke
import leo14.lambda.native
import leo14.lambda.nativeEval
import leo14.lambda.term
import leo14.native.*
import leo14.typed.compiler.Compiler
import leo14.typed.compiler.compile
import leo14.typed.compiler.nativeCompiler
import leo14.typed.compiler.typed

val Typed<Native>.nativeEval
	get() =
		term.nativeEval of type

val Literal.nativeTypedLine: TypedLine<Native>
	get() =
		when (this) {
			is StringLiteral -> line("string" fieldTo (term(native(string)) of nativeType))
			is NumberLiteral -> number.nativeTypedLine
		}

val Number.nativeTypedLine: TypedLine<Native>
	get() =
		when (this) {
			is IntNumber -> line("int" fieldTo (term(native(int)) of nativeType))
			is DoubleNumber -> line("double" fieldTo (term(native(double)) of nativeType))
		}

val Script.nativeCompile: Compiler<Native>
	get() =
		nativeCompiler.compile(this)

val Typed<Native>.nativeDecompile
	get() =
		decompile(TypedLine<Native>::decompileScriptLine)

val TypedLine<Native>.decompileScriptLine: Literal?
	get() =
		when (line) {
			line("int" fieldTo nativeType) -> term.native.literal
			line("string" fieldTo nativeType) -> term.native.literal
			line("double" fieldTo nativeType) -> term.native.literal
			else -> null
		}

val Script.nativeEval: Script
	get() =
		nativeCompile.typed.nativeEval.nativeDecompile

val Typed<Native>.nativeResolve: Typed<Native>?
	get() =
		resolveLinkOrNull?.let { link ->
			when (type) {
				type(
					"int" fieldTo nativeType,
					"plus" fieldTo type(
						"int" fieldTo nativeType)) ->
					term(intPlusIntNative)
						.invoke(link.tail.term)
						.invoke(link.head.term) of type("int" fieldTo nativeType)
				type(
					"double" fieldTo nativeType,
					"plus" fieldTo type(
						"double" fieldTo nativeType)) ->
					term(doublePlusDoubleNative)
						.invoke(link.tail.term)
						.invoke(link.head.term) of type("double" fieldTo nativeType)
				else -> null
			}
		}

fun typedLine(native: Native): TypedLine<Native> =
	when (native) {
		is StringNative -> line("string" fieldTo nativeTyped<Native>(native))
		is IntNative -> line("int" fieldTo nativeTyped<Native>(native))
		is DoubleNative -> line("double" fieldTo nativeTyped<Native>(native))
		is BooleanNative -> line("boolean" fieldTo typed("$native.boolean"))
		else -> error("$native.typedLine")
	}

fun typed(native: Native): Typed<Native> =
	typed(typedLine(native))