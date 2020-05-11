package leo16.processor

sealed class StringParser
data class TextParserStringParser(val textParser: TextParser) : StringParser()
data class StringStringParser(val string: String) : StringParser()
