package leo14.typed.compiler.js

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.code.code
import leo14.js.ast.open
import leo14.js.ast.print
import leo14.js.ast.show
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.Value
import leo14.lambda.evaluator
import leo14.lambda.js.expr.*
import leo14.typed.*
import leo14.typed.compiler.Compiled
import leo14.typed.compiler.typedForEnd
import leo14.typed.compiler.updateTyped

val Compiled<Expr>.resolve: Compiled<Expr>?
	get() =
		null
			?: resolveOpen
			?: resolvePrint
			?: resolveShow
			?: typed.resolve?.let { updateTyped { it } }

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		null
			?: resolveNative
			?: resolveNativeRhs
			?: resolveOp(numberType, "plus", "+")
			?: resolveOp(numberType, "minus", "-")
			?: resolveOp(numberType, "times", "*")
			?: resolveOp(textType, "plus", "+")
			?: resolveInvoke
			?: resolveGet
			?: resolveSet
			?: resolveText

fun Typed<Expr>.resolveOp(type: Type, name: String, op: String): Typed<Expr>? =
	decompileLinkOrNull?.let { link ->
		notNullIf(link.tail.type == type && link.head.resolveFieldOrNull?.field == name fieldTo type) {
			link.tail.term.op(op, link.head.term).expr.term of type
		}
	}

val Typed<Expr>.resolveInvoke: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			notNullIf(link.tail.type == nativeType && link.head.resolveFieldOrNull?.field?.string == "invoke") {
				link.tail.term.invoke(link.head.term).expr.term of nativeType
			}
		}

val Typed<Expr>.resolveNative: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			if (link.tail.type == textType &&
				term is NativeTerm &&
				term.native is LiteralExpr &&
				term.native.literal is StringLiteral &&
				link.head.line.fieldOrNull == "native" fieldTo type())
				term.native.literal.string.code.expr.term of nativeType
			else null
		}

val Compiled<Expr>.resolveOpen: Compiled<Expr>?
	get() =
		typed.decompileLinkOrNull?.let { link ->
			notNullIf(link.head.line.fieldOrNull == "open" fieldTo type()) {
				updateTyped { link.tail }.apply { typedForEnd.term.astExpr.open }
			}
		}

val Compiled<Expr>.resolvePrint: Compiled<Expr>?
	get() =
		typed.decompileLinkOrNull?.let { link ->
			notNullIf(link.head.line.fieldOrNull == "print" fieldTo type()) {
				updateTyped { link.tail }.apply { typedForEnd.term.astExpr.print }
			}
		}

val Compiled<Expr>.resolveShow: Compiled<Expr>?
	get() =
		typed.decompileLinkOrNull?.let { link ->
			notNullIf(link.head.line.fieldOrNull == "show" fieldTo type()) {
				updateTyped { link.tail }.apply { typedForEnd.term.astExpr.show }
			}
		}

val Typed<Expr>.resolveGet: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			if (link.tail.type == nativeType &&
				link.head.line.fieldOrNull == ("get" fieldTo textType) &&
				link.head.term is NativeTerm &&
				link.head.term.native is LiteralExpr &&
				link.head.term.native.literal is StringLiteral
			)
				link.tail.term.get(link.head.term.native.literal.string).expr.term of nativeType
			else null
		}

val Typed<Expr>.resolveSet: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			ifOrNull(link.tail.type == nativeType) {
				link.head.resolveFieldOrNull?.let { fieldTyped ->
					ifOrNull(fieldTyped.field.string == "set") {
						fieldTyped.resolveRhs.decompileLinkOrNull?.let { setLink ->
							if (setLink.tail.type == textType &&
								setLink.tail.term is NativeTerm &&
								setLink.tail.term.native is LiteralExpr &&
								setLink.tail.term.native.literal is StringLiteral)
								link.tail.term.set(setLink.tail.term.native.literal.string, setLink.head.term).expr.term of nativeType
							else null
						}
					}
				}
			}
		}

val Typed<Expr>.resolveNativeRhs: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			if (link.tail.type == type() &&
				link.head.line.fieldOrNull == ("native" fieldTo textType) &&
				link.head.term is NativeTerm &&
				link.head.term.native is LiteralExpr &&
				link.head.term.native.literal is StringLiteral)
				link.head.term.native.literal.string.code.expr.term of nativeType
			else null
		}

val Typed<Expr>.resolveText: Typed<Expr>?
	get() =
		decompileLinkOrNull?.let { link ->
			if (link.tail.type == nativeType &&
				link.head.line.fieldOrNull == ("text" fieldTo type()))
				term of textType
			else null
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(value: Value<Expr>): Value<Expr>? = null

val exprEvaluator = evaluator(Expr::invoke)

val Term<Expr>.termDecompile: ScriptLine
	get() =
		"javascript" lineTo script(literal(code.string))

// No need for decompiling, since we don't evaluate.
val TypedLine<Expr>.decompileLiteral: Literal?
	get() =
		null