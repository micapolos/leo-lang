package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.asString
import leo14.untyped.plusName
import leo14.untyped.textName
import leo14.untyped.thunk
import leo14.untyped.value

data class Leo(
	val parentOrNull: LeoParent?,
	val resolver: Resolver)

data class LeoParent(
	val leo: Leo,
	val begin: Begin,
	val endFn: (Leo) -> Leo)

val LeoParent?.beginLeo: Leo
	get() =
		Leo(
			this,
			Resolver(
				emptyTyped,
				{ begin -> TODO() },
				{ literal -> leo(literal) }))

fun LeoParent?.leo(literal: Literal): Leo =
	when (literal) {
		is StringLiteral -> leo(literal.string)
		is NumberLiteral -> leo(literal.number)
	}

fun LeoParent?.leo(string: String): Leo =
	Leo(
		this,
		Resolver(
			string.typed,
			{ begin ->
				when (begin.string) {
					plusName ->
						parent(begin) { rhsLeo ->
							rhsLeo.resolver.typed.let { rhsTyped ->
								when (rhsTyped.type) {
									thunk(value(textName)) ->
										copy(resolver = resolver.copy(typed = string.plus(rhsTyped.value.asString).typed))
									else -> null
								}
							} ?: TODO()
						}.beginLeo
					else -> TODO()
				}
			},
			{ literal -> TODO() }))

fun LeoParent?.leo(number: Number): Leo =
	Leo(
		this,
		Resolver(
			number.typed,
			{ begin -> TODO() },
			{ literal -> TODO() }))

val emptyLeo =
	null.beginLeo

fun Leo.write(token: Token): Leo =
	when (token) {
		is LiteralToken -> resolver.literalFn.invoke(this, token.literal)
		is BeginToken -> resolver.beginFn.invoke(this, token.begin)
		is EndToken -> parentOrNull!!.endFn(this)
	}

fun Leo.parent(begin: Begin, endFn: (Leo) -> Leo): LeoParent =
	LeoParent(this, begin, endFn)