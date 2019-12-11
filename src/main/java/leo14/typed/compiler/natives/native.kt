package leo14.typed.compiler.natives

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.java.lang.sendMail
import leo13.linkOrNull
import leo14.*
import leo14.Number
import leo14.js.ast.code
import leo14.lambda.*
import leo14.lambda.js.expr.astExpr
import leo14.native.*
import leo14.typed.*
import leo14.typed.compiler.Compiled
import leo14.typed.compiler.js.compileTyped
import leo14.typed.compiler.js.expr
import leo14.typed.compiler.js.open
import leo14.typed.compiler.js.show
import leo14.typed.compiler.updateTyped

val Literal.nativeTypedLine: TypedLine<Native>
	get() =
		when (this) {
			is StringLiteral -> term(native(string)) of textLine
			is NumberLiteral -> number.nativeTypedLine
		}

val Number.nativeTypedLine: TypedLine<Native>
	get() =
		term(native(this)) of numberLine

val Typed<Native>.decompile
	get() =
		decompile(TypedLine<Native>::decompileLiteral, Term<Native>::termDecompile)

val TypedLine<Native>.decompile
	get() =
		decompileLine(TypedLine<Native>::decompileLiteral, Term<Native>::termDecompile)

val TypedLine<Native>.decompileLiteral: Literal?
	get() =
		when (line) {
			textLine -> term.native.literal
			numberLine -> term.native.literal
			else -> null
		}

val Compiled<Native>.nativeResolve: Compiled<Native>?
	get() =
		typed.nativeResolve?.let { updateTyped { it } }

val Typed<Native>.nativeResolve: Typed<Native>?
	get() =
		null
			?: resolveBinaryOpNew(numberLine, "plus", numberPlusNumberNative)
			?: resolveBinaryOpNew(numberLine, "minus", numberMinusNumberNative)
			?: resolveBinaryOpNew(numberLine, "times", numberTimesNumberNative)
			?: resolveBinaryOpNew(textLine, "plus", stringPlusStringNative)
			?: resolveLinkOrNull?.let { link ->
			when (type) {
				type(
					"mail" lineTo type(
						"to" lineTo textType,
						"subject" lineTo textType,
						"message" lineTo textType),
					"send" lineTo type()) ->
					term.pair().let { pair ->
						pair.first.pair().let { pair2 ->
							pair2.first.native.string.let { to ->
								pair2.second.native.string.let { subject ->
									pair.second.native.string.let { message ->
										sendMail(to = to, subject = subject, message = message)
										typed<Native>("done")
									}
								}
							}
						}
					}
				else ->
					type.lineStack.linkOrNull?.let { link1 ->
						link1.value.fieldOrNull?.let { field ->
							ifOrNull(field.rhs.isEmpty) {
								when (field.string) {
									"open" -> link.tail.decompile.open.run { typed<Native>() }
									"show" -> link.tail.decompile.show.run { typed<Native>() }
									"js" -> typed(native(link.tail.decompile.compileTyped.expr.astExpr.code))
									else -> null
								}
							}
						}
					}
			}
		}

fun Typed<Native>.resolveBinaryOpNew(argLine: Line, name: String, opNative: Native): Typed<Native>? =
	resolveLink { link ->
		notNullIf(this.type == type(argLine, name lineTo type(argLine))) {
			term(opNative)
				.invoke(link.tail.term)
				.invoke(link.head.term) of type(argLine)
		}
	}


fun typedLine(native: Native): TypedLine<Native> =
	when (native) {
		is StringNative -> term(native) of textLine
		is NumberNative -> term(native) of numberLine
		is BooleanNative -> line("boolean" fieldTo typed("$native.boolean"))
		else -> error("$native.typedLine")
	}

fun typed(native: Native): Typed<Native> =
	typed(typedLine(native))

val Typed<Native>.eval get() = eval(nativeEvaluator)
