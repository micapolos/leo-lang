package leo13.type

import leo.base.notNullIf
import leo.base.orNull
import leo13.value.Switch
import leo13.value.plus
import leo13.value.switch
import leo9.fold
import leo9.isEmpty
import leo9.reverse

data class SwitchTyped(val switch: Switch, val pattern: Pattern)

fun typed(switch: Switch, pattern: Pattern) = SwitchTyped(switch, pattern)

fun switchTyped() = SwitchTyped(switch(), pattern())

val TypedSwitch.switchTypedOrNull: SwitchTyped?
	get() =
		switchTyped().orNull.fold(caseTypedStack.reverse) {
			this?.plusOrNull(it)
		}

fun SwitchTyped.plusOrNull(caseTyped: CaseTyped): SwitchTyped? =
	if (switch.caseStack.isEmpty) typed(switch(caseTyped.case), caseTyped.pattern)
	else notNullIf(caseTyped.pattern == pattern) {
		typed(switch.plus(caseTyped.case), pattern)
	}
