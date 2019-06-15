package leo.parser

sealed class Parser<in I, out O>

data class ParsedParser<I, O>(val parsed: Parsed<O>) : Parser<I, O>()
data class ParsingParser<I, O>(val parsing: Parsing<I, O>) : Parser<I, O>()

fun <I, O> parser(parsed: Parsed<O>): Parser<I, O> = ParsedParser(parsed)
fun <I, O> parser(parsing: Parsing<I, O>): Parser<I, O> = ParsingParser(parsing)
