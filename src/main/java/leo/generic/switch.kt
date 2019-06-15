package leo.parser

data class Switch<out I, out O>(val caseList: List<Case<I, O>>)

fun <I, O> switch(vararg cases: Case<I, O>) = Switch(cases.toList())

operator fun <I, O : Any> Switch<I, O>.get(input: I): O? =
	caseList.find { it.contains(input) }?.output
