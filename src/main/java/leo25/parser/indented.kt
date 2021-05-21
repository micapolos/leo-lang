package leo25.parser

data class Indented<T>(val item: T, val tailIndent: Int)

fun <T> Parser<T>.indented(indent: Int): Parser<Indented<T>> =
	indentedWithTail(indent, indent)

fun <T> Parser<T>.indentedWithTail(indent: Int, tailIndent: Int): Parser<Indented<T>> =
	if (tailIndent == 0) indentedBody(indent)
	else Parser(
		{ char -> tabUnitParser.plus(char)?.let { indentedWithTail(indent, tailIndent, it) } },
		{ parsedOrNull?.let { Indented(it, tailIndent) } })

fun <T> Parser<T>.indentedBody(indent: Int): Parser<Indented<T>> =
	Parser({ char ->
		if (char == '\n') plus(char)?.indentedWithTail(indent, indent)
		else plus(char)?.indentedBody(indent)
	}, { parsedOrNull?.let { Indented(it, 0) } })

fun <T> Parser<T>.indentedWithTail(indent: Int, tailIndent: Int, tabUnitParser: Parser<Unit>): Parser<Indented<T>> =
	partialParser { char ->
		tabUnitParser.plus(char)?.let { newTabParser ->
			newTabParser.parsedOrNull.let { tabUnitOrNull ->
				if (tabUnitOrNull == null) indentedWithTail(indent, tailIndent, newTabParser)
				else indentedWithTail(indent, tailIndent.dec())
			}
		}
	}
