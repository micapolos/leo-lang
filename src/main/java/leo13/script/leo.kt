package leo13.script

import leo.base.Empty
import leo.base.ifOrNull
import leo13.*

data class Leo(
	val parentOrNull: LeoParent?,
	val types: Types,
	val functions: Functions,
	val typeBindings: TypeBindings,
	val typedExpr: TypedExpr,
	val errorOrNull: StringError?,
	val isMeta: Boolean)

data class LeoParent(
	val leo: Leo,
	val name: String)

data class StringError(
	val string: String)

val Empty.leo
	get() =
		Leo(null as LeoParent?, types(), functions(), typeBindings(), expr() of type(), null, false)

fun Leo.begin(name: String): Leo =
	if (errorOrNull != null) this
	else noErrorBegin(name)

val Leo.end
	get(): Leo =
		if (errorOrNull != null) this
		else noErrorEnd

fun Leo.noErrorBegin(name: String): Leo =
	if (isMeta) metaBegin(name)
	else nonMetaBegin(name)

fun Leo.metaBegin(name: String) =
	Leo(
		LeoParent(this, name),
		types,
		functions,
		typeBindings,
		expr() of type(),
		null,
		false)

fun Leo.nonMetaBegin(name: String) =
	if (name == "meta") beginMeta
	else Leo(
		LeoParent(this, name),
		types,
		functions,
		typeBindings,
		expr() of type(),
		null,
		false)

val Leo.beginMeta
	get() =
		copy(isMeta = true)

val Leo.noErrorEnd
	get(): Leo =
		if (isMeta) metaEnd
		else nonMetaEnd

val Leo.metaEnd
	get(): Leo =
		copy(isMeta = false)

val Leo.nonMetaEnd
	get(): Leo =
		if (parentOrNull == null) endError("unexpected end")
		else parentOrNull.leo.resolve(parentOrNull.name lineTo typedExpr)

fun Leo.resolve(typedExprLine: TypedExprLine): Leo =
	if (!typedExpr.expr.isEmpty && typedExprLine.rhs.expr.isEmpty) normalize(typedExprLine.name)
	else normalizedResolve(typedExprLine)

fun Leo.normalize(name: String) =
	copy(typedExpr = expr() of type()).resolve(name lineTo typedExpr)

fun Leo.normalizedResolve(typedExprLine: TypedExprLine): Leo =
	null
		?: resolveAccessOrNull(typedExprLine)
		?: resolveCastOrNull(typedExprLine)
		?: copy(typedExpr = typedExpr.plus(typedExprLine))

fun Leo.resolveAccessOrNull(typedExprLine: TypedExprLine): Leo? =
	ifOrNull(typedExpr.expr.isEmpty) {
		typedExprLine.rhs.accessOrNull(typedExprLine.name)?.let { accessTypedExpr ->
			copy(typedExpr = accessTypedExpr)
		}
	}

fun Leo.resolveCastOrNull(typedExprLine: TypedExprLine): Leo? =
	typedExpr
		.linkTo(typedExprLine)
		.ofTypedExprOrNull
		?.let { ofTypedExpr ->
			copy(typedExpr = ofTypedExpr)
		}

fun Leo.endError(string: String): Leo =
	copy(errorOrNull = StringError(string))
