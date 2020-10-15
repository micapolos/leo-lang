package leo19

import leo14.invoke
import leo14.script
import leo16.names.*

val bitScript = script(bitScriptLine)

val Boolean.script
	get() =
		script(
			if (this) _boolean(_choice(_yes(_true()), _no(_false())))
			else _boolean(_choice(_no(_true()), _yes(_false()))))