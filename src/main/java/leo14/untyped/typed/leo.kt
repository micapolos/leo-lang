package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.asString
import leo14.untyped.*

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
				{ begin -> writeFallback(begin) },
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
							rhsLeo.resolver.typed
								.let { rhsTyped ->
									when (rhsTyped.type) {
										textType ->
											copy(resolver = resolver.copy(typed = string.plus(rhsTyped.value.asString).typed))
										else -> writeFallback(begin)
									}
								}
						}.beginLeo
					else -> writeFallback(begin)
				}
			},
			{ literal -> writeFallback(literal) }))

fun LeoParent?.leo(number: Number): Leo =
	Leo(
		this,
		Resolver(
			number.typed,
			{ begin ->
				when (begin.string) {
					plusName ->
						parent(begin) { rhsLeo ->
							rhsLeo.resolver.typed.let { rhsTyped ->
								when (rhsTyped.type) {
									numberType ->
										copy(
											resolver = resolver.copy(
												typed = number.plus(rhsTyped.value.asNumber).typed))
									else -> writeFallback(begin)
								}
							}
						}.beginLeo
					minusName ->
						parent(begin) { rhsLeo ->
							rhsLeo.resolver.typed.let { rhsTyped ->
								when (rhsTyped.type) {
									numberType ->
										copy(
											resolver = resolver.copy(
												typed = number.minus(rhsTyped.value.asNumber).typed))
									else -> writeFallback(begin)
								}
							}
						}.beginLeo
					timesName ->
						parent(begin) { rhsLeo ->
							rhsLeo.resolver.typed.let { rhsTyped ->
								when (rhsTyped.type) {
									numberType ->
										copy(
											resolver = resolver.copy(
												typed = number.times(rhsTyped.value.asNumber).typed))
									else -> writeFallback(begin)
								}
							}
						}.beginLeo
					else -> TODO()
				}
			},
			{ literal -> writeFallback(literal) }))

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

fun Leo.writeFallback(begin: Begin): Leo =
	Leo(
		LeoParent(this, begin) { rhsLeo ->
			writeFallback(begin, rhsLeo.resolver.typed)
		},
		Resolver(
			emptyTyped,
			{ begin -> writeFallback(begin) },
			{ literal -> writeFallback(literal) }))

fun Leo.writeFallback(begin: Begin, rhs: Typed): Leo =
	Leo(
		parentOrNull,
		Resolver(
			resolver.typed.plus(begin, rhs),
			{ begin -> writeFallback(begin) },
			{ literal -> writeFallback(literal) }))

fun Leo.writeFallback(literal: Literal): Leo =
	Leo(
		parentOrNull,
		Resolver(
			resolver.typed.plus(literal),
			{ begin -> writeFallback(begin) },
			{ literal -> writeFallback(literal) }))

fun Leo.resolveIf(type: Thunk, fn: Value.(Value) -> Typed): Leo =
	TODO()