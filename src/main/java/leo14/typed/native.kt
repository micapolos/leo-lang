package leo14.typed

import leo14.*
import leo14.Number
import leo14.lambda.invoke
import leo14.lambda.native
import leo14.lambda.term
import leo14.native.*

val Literal.nativeTypedLine: TypedLine<Native>
	get() =
		when (this) {
			is StringLiteral -> line(textName fieldTo (term(native(string)) of nativeType))
			is NumberLiteral -> number.nativeTypedLine
		}

val Number.nativeTypedLine: TypedLine<Native>
	get() =
		line(numberName fieldTo (term(native(this)) of nativeType))

val Typed<Native>.decompile
	get() =
		decompile(TypedLine<Native>::decompileLiteral)

val TypedLine<Native>.decompileLiteral: Literal?
	get() =
		when (line) {
			line(textName fieldTo nativeType) -> term.native.literal
			line(numberName fieldTo nativeType) -> term.native.literal
			else -> null
		}

val Typed<Native>.nativeResolve: Typed<Native>?
	get() =
		resolveLinkOrNull?.let { link ->
			when (type) {
				type(
					numberLine,
					"plus" lineTo numberType) ->
					term(numberPlusDoubleNative)
						.invoke(link.tail.term)
						.invoke(link.head.term) of numberType
				else -> null
			}
		}

fun typedLine(native: Native): TypedLine<Native> =
	when (native) {
		is StringNative -> line(textName fieldTo nativeTyped<Native>(native))
		is NumberNative -> line(numberName fieldTo nativeTyped<Native>(native))
		is BooleanNative -> line("boolean" fieldTo typed("$native.boolean"))
		else -> error("$native.typedLine")
	}

fun typed(native: Native): Typed<Native> =
	typed(typedLine(native))

val Typed<Native>.eval get() = eval(Native::invoke)
