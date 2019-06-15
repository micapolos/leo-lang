package leo.parser

import leo.base.ifOrNull

data class Case<out I, out O>(
	val input: I,
	val output: O)

infix fun <I, O> I.caseTo(output: O) =
	Case(this, output)

fun <I> Case<I, *>.contains(input: I) =
	this.input == input

fun <I, O : Any> Case<I, O>.at(input: I): O? =
	ifOrNull(contains(input)) { output }