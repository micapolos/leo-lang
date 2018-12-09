package leo.lab.v2

sealed class Match

data class BodyMatch(
	val body: Body) : Match()

data class FunctionMatch(
	val function: Function) : Match()

val Function.match: Match
	get() =
		FunctionMatch(this)

val Body.match: Match
	get() =
		BodyMatch(this)

fun match(body: Body): Match =
	BodyMatch(body)

fun match(function: Function): Match =
	FunctionMatch(function)

val Match.bodyOrNull: Body?
	get() =
		(this as? BodyMatch)?.body

val Match.functionOrNull: Function?
	get() =
		(this as? FunctionMatch)?.function
