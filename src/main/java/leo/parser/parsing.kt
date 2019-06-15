package leo.parser

data class Parsing<in I, out O>(val fn: (I) -> Parser<I, O>)

fun <I, O> parsing(fn: (I) -> Parser<I, O>) = Parsing(fn)
fun <I, O> Parsing<I, O>.parser(value: I) = fn(value)
