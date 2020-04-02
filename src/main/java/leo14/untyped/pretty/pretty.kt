package leo14.untyped.pretty

import leo14.Script

val prettyColumns = 80

val Script.prettyString: String get() = indentString(0)