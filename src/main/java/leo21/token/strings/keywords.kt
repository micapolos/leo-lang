package leo21.token.strings

import leo.ansi
import leo.bold
import leo.brightBlue
import leo.normal
import leo.white

val String.value get() = this
val String.valueKeyword get() = this//ansi.brightBlue + ansi.bold + this + ansi.normal + ansi.white
val String.type get() = this//ansi.brightBlue + this + ansi.white
val String.typeKeyword get() = this//ansi.brightBlue + ansi.bold + this + ansi.normal + ansi.white

val applyKeyword = "apply".valueKeyword
val defineKeyword = "define".valueKeyword
val doKeyword = "do".valueKeyword
val doesKeyword = "does".valueKeyword
val functionKeyword = "function".valueKeyword
val typeKeyword = "type".valueKeyword
