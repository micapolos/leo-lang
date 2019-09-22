package leo13.base

import leo.base.fold
import leo13.Character
import leo13.character

fun Writer<Character>.write(char: Char): Writer<Character> =
	write(character(char))

fun Writer<Character>.write(string: String): Writer<Character> =
	fold(string) { write(it) }
