package leo13.type

import leo.base.notNullIf
import leo.base.orNull
import leo13.fold
import leo13.isEmpty
import leo13.reverse
import leo13.value.Switch
import leo13.value.plus
import leo13.value.switch

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
