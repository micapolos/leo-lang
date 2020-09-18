package leo18

import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.StackLink
import leo13.fold
import leo13.push
import leo13.reverse
import leo13.stack
import leo13.stackLink
import leo14.End
import leo14.Processor
import leo14.Token
import leo14.begin
import leo14.end
import leo14.parser.NameParser
import leo14.parser.nameOrNull
import leo14.parser.newNameParser
import leo14.parser.parse
import leo14.process
import leo14.token

data class Tokenizer(
	val endStackLinkStack: Stack<StackLink<End>>,
	val lineState: LineState,
	val tokenProcessor: Processor<Token>
) {

	sealed class LineState {
		data class Lead(val endStackLinkStack: Stack<StackLink<End>>) : LineState()
		data class Name(val parser: NameParser) : LineState()
		object Colon : LineState()
	}

	sealed class Result {
		data class Success(val tokenizer: Tokenizer) : Result()
		object End : Result()
	}
}

fun Tokenizer.consume(char: Char): Tokenizer? =
	when (lineState) {
		is Tokenizer.LineState.Lead -> when (char) {
			'\t' -> when (lineState.endStackLinkStack) {
				is EmptyStack -> Tokenizer(
					endStackLinkStack.push(stackLink(end)),
					Tokenizer.LineState.Name(newNameParser),
					tokenProcessor)
				is LinkStack -> Tokenizer(
					endStackLinkStack.push(lineState.endStackLinkStack.link.value),
					Tokenizer.LineState.Lead(lineState.endStackLinkStack.link.stack),
					tokenProcessor)
			}
			else -> newNameParser.parse(char)?.let { nameParser ->
				Tokenizer(
					endStackLinkStack,
					Tokenizer.LineState.Name(nameParser),
					tokenProcessor.fold(lineState.endStackLinkStack) { endStackLink ->
						process(token(end)).fold(endStackLink.stack) { end ->
							process(token(end))
						}
					})
			}
		}
		is Tokenizer.LineState.Name -> lineState.parser.parse(char).let { nameParser ->
			if (nameParser != null) Tokenizer(
				endStackLinkStack,
				Tokenizer.LineState.Name(nameParser),
				tokenProcessor)
			else lineState.parser.nameOrNull?.let { name ->
				when (char) {
					':' -> Tokenizer(
						endStackLinkStack,
						Tokenizer.LineState.Colon,
						tokenProcessor.process(token(begin(name))))
					'\n' -> Tokenizer(
						stack(),
						Tokenizer.LineState.Lead(endStackLinkStack.reverse),
						tokenProcessor.process(token(begin(name))))
					' ' -> Tokenizer(
						stack(),
						Tokenizer.LineState.Lead(endStackLinkStack.reverse),
						tokenProcessor.process(token(begin(name))))
					else -> null
				}
			}
		}
		Tokenizer.LineState.Colon -> when (char) {
			' ' -> TODO()
			else -> null
		}
	}