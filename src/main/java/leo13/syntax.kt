package leo13

sealed class Syntax

data class StartSyntax(val start: SyntaxStart) : Syntax()
data class LinkSyntax(val link: SyntaxLink) : Syntax()

fun syntax(start: SyntaxStart): Syntax = StartSyntax(start)
fun syntax(link: SyntaxLink): Syntax = LinkSyntax(link)

val Syntax.sentence
	get() =
		when (this) {
			is StartSyntax -> sentence(start.sentenceStart)
			is LinkSyntax -> sentence(link.sentenceLink)
		}
