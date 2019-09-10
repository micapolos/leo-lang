package leo13

data class GivenSyntax(val given: Given)

fun syntax(given: Given) = GivenSyntax(given)

val GivenSyntax.sentenceStart get() = given.sentenceStart
