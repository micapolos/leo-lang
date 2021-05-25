package leo25

import leo14.*

data class ScriptDottedLhs(val lhsScript: Script, val rhsName: String)
data class ScriptDottedLink(val lhs: ScriptDottedLhs, val rhsName: String)

val ScriptLink.dottedLinkOrNull: ScriptDottedLink?
	get() =
		lhs.dottedLhsOrNull?.let { lhs ->
			line.fieldOrNull?.onlyStringOrNull?.let { rhsName ->
				ScriptDottedLink(lhs, rhsName)
			}
		}

val Script.dottedLhsOrNull: ScriptDottedLhs?
	get() =
		linkOrNull?.run {
			line.fieldOrNull?.onlyStringOrNull?.let { rhsName ->
				ScriptDottedLhs(lhs, rhsName)
			}
		}
