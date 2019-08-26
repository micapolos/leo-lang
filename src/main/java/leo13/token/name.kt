package leo13.token

import leo13.script.lineTo
import leo13.script.script

data class Name(val string: String)

fun name(string: String) = Name(string)
val String.nameAsScriptLine get() = "name" lineTo script(this lineTo script())