package leo13.base

import leo.base.fold
import leo13.script.CharLeo
import leo13.script.leo

fun Writer<CharLeo>.write(char: Char): Writer<CharLeo> =
	write(leo(char))

fun Writer<CharLeo>.write(string: String): Writer<CharLeo> =
	fold(string) { write(it) }
