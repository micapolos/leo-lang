package leo21.token.strings

import leo.ansi
import leo.bold
import leo.cyan
import leo.defaultColor
import leo.italic
import leo.notBold
import leo.notItalic

val String.value get() = this
val String.valueKeyword get() = ansi.italic + ansi.bold + this + ansi.notBold + ansi.notItalic
val String.type get() = ansi.cyan + this + ansi.defaultColor
val String.typeKeyword get() = ansi.cyan + ansi.italic + ansi.bold + this + ansi.notBold + ansi.notItalic + ansi.defaultColor

val applyKeyword = "apply".valueKeyword
val choiceKeyword = "choice".typeKeyword
val defineKeyword = "define".valueKeyword
val doKeyword = "do".valueKeyword
val doesKeyword = "does".valueKeyword
val functionKeyword = "function".valueKeyword
val recurseKeyword = "recurse".typeKeyword
val recursiveKeyword = "recursive".typeKeyword
val typeKeyword = "type".valueKeyword
