package leo21.token.strings

import leo.ansi
import leo.blue
import leo.bold
import leo.magenta
import leo.normal
import leo.white

val String.keyword get() = this//ansi.magenta + ansi.bold + this + ansi.normal + ansi.white
val String.type get() = this//ansi.blue + this + ansi.white
