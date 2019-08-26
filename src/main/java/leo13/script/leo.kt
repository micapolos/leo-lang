package leo13.script

import lambda.indexed.Quote
import leo.base.Empty
import leo.base.ifOrNull
import leo13.*
import leo13.compiler.Context
import leo13.compiler.context
import leo13.type.type
import leo13.value.expr
import leo13.value.isEmpty
import leo9.Stack
import leo9.isEmpty
import leo9.stack

data class Leo(
	val parentOrNull: LeoParent?,
	val context: Context,
	val typed: Typed,
	val isMeta: Boolean,
	val quoteStack: Stack<Quote>) {
	val asScript get() = script(toString() lineTo script()) // TODO
}

data class LeoParent(
	val leo: Leo,
	val name: String)

sealed class LeoOp
data class NameLeoOp(val name: String) : LeoOp()
data class EmptyLeoOp(val empty: Empty) : LeoOp()

val Empty.leo
	get() =
		Leo(null as LeoParent?, context(), expr() of type(), false, stack())

fun Leo.push(token: Token): Interpreter =
	when (token) {
		is OpeningToken -> begin(token.opening.name)
		is ClosingToken -> end
	}

fun Leo.begin(name: String): Interpreter =
	if (isMeta) metaBegin(name)
	else nonMetaBegin(name)

fun Leo.metaBegin(name: String): Interpreter =
	interpreter(
		Leo(
			LeoParent(this, name),
			context,
			expr() of type(),
			false,
			quoteStack))

fun Leo.nonMetaBegin(name: String): Interpreter =
	if (name == "meta") beginMeta
	else interpreter(
		Leo(
			LeoParent(this, name),
			context,
			expr() of type(),
			name == "meta",
			quoteStack))

val Leo.beginMeta: Interpreter
	get() =
		interpreter(
			Leo(
				parentOrNull,
				context,
				typed,
				false,
				quoteStack))

fun Leo.beginNonMeta(name: String): Interpreter =
	interpreter(
		Leo(
			parentOrNull,
			context,
			expr() of type(),
			false,
			quoteStack))

val Leo.end: Interpreter
	get() =
		if (isMeta) metaEnd
		else nonMetaEnd

val Leo.metaEnd: Interpreter
	get() =
		interpreter(
			Leo(
				parentOrNull,
				context,
				typed,
				false,
				quoteStack))

val Leo.nonMetaEnd: Interpreter
	get() =
		if (parentOrNull == null) interpreter(error("unexpected end"))
		else parentOrNull.leo.resolveOrAppend(parentOrNull.name lineTo typed)

fun Leo.resolveOrAppend(typedLine: TypedLine): Interpreter =
	if (isMeta || !quoteStack.isEmpty) append(typedLine)
	else resolve(typedLine)

fun Leo.resolve(typedLine: TypedLine): Interpreter =
	when (typedLine.name) {
		"quote" -> TODO()
		"unquote" -> TODO()
		"exists" -> TODO()
		"gives" -> TODO()
		"of" -> TODO()
		else -> TODO()
	}

fun Leo.resolveQuote(typed: Typed): Interpreter =
	interpreter(
		Leo(
			parentOrNull,
			context,
			typed,
			false,
			quoteStack))

fun Leo.resolveAccessOrNull(typedLine: TypedLine): Leo? =
	ifOrNull(typed.expr.isEmpty) {
		typedLine.rhs.accessOrNull(typedLine.name)?.let { accessTypedExpr ->
			copy(typed = accessTypedExpr)
		}
	}

fun Leo.resolveCastOrNull(typedLine: TypedLine): Leo? =
	typed
		.linkTo(typedLine)
		.ofTypedExprOrNull
		?.let { ofTypedExpr ->
			copy(typed = ofTypedExpr)
		}

fun Leo.append(typedLine: TypedLine): Interpreter =
	interpreter(
		Leo(
			parentOrNull,
			context,
			typed.plus(typedLine),
			isMeta,
			quoteStack))