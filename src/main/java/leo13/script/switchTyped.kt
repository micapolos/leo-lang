package leo13.script

import leo.base.notNullIf
import leo.base.orNull
import leo13.Type
import leo13.type
import leo9.fold
import leo9.isEmpty
import leo9.reverse

data class SwitchTyped(val switch: Switch, val type: Type)

fun typed(switch: Switch, type: Type) = SwitchTyped(switch, type)

fun switchTyped() = SwitchTyped(switch(), type())

val TypedSwitch.switchTypedOrNull: SwitchTyped?
	get() =
		switchTyped().orNull.fold(caseTypedStack.reverse) {
			this?.plusOrNull(it)
		}

fun SwitchTyped.plusOrNull(caseTyped: CaseTyped): SwitchTyped? =
	if (switch.caseStack.isEmpty) typed(switch(caseTyped.case), caseTyped.type)
	else notNullIf(caseTyped.type == type) {
		typed(switch.plus(caseTyped.case), type)
	}
