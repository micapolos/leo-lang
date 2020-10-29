package leo20.tokenizer

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Script
import leo14.ScriptLine
import leo14.Token
import leo14.line
import leo14.plus
import leo14.script

data class Writer(
	val parentOrNull: WriterParent?,
	val script: Script
)

fun Writer.push(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> copy(script = script.plus(line(token.literal))).run(::WriterTokenizer)
		is BeginToken -> Writer(PlusWriterParent(token.begin.string, this), script()).run(::WriterTokenizer)
		is EndToken -> parentOrNull?.end(script)
	}

fun Writer.push(scriptLine: ScriptLine): Writer =
	copy(script = script.plus(scriptLine))