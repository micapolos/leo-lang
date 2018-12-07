package leo.lab.v2

import leo.Begin
import leo.End
import leo.Word

sealed class Command

data class WordBeginCommand(
	val word: Word,
	val begin: Begin) : Command()

data class EndCommand(
	val end: End) : Command()

fun Begin.command(word: Word): Command =
	WordBeginCommand(word, this)

val End.command: Command
	get() =
		EndCommand(this)
