package leo13.type

import leo13.script.Switch
import leo9.Stack
import leo9.stack

data class ChoiceMatch(val caseMatchStack: Stack<CaseMatch>)

val Stack<CaseMatch>.choiceMatch get() = ChoiceMatch(this)
fun choiceMatch(vararg caseMatches: CaseMatch) = stack(*caseMatches).choiceMatch


fun Switch.choiceMatchOrNull(choice: Choice): ChoiceMatch? =
	TODO()
//	distinctCaseStack
//		.mapOrNull {
//			choice
//				.rhsOrNull(name)
//				?.let { either -> match(either, rhs) }
//		}
//		?.choiceMatch
//		?.orNullIf { !(choice.caseStack.drop(caseMatchStack)?.isEmpty ?: true) }