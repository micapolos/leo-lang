package leo32.compiled

import leo.binary.Bit
import leo32.base.Branch
import leo32.base.at
import leo32.base.branch

sealed class Term

data class BitTerm(val bit: Bit) : Term()
data class BranchTerm(val branch: Branch<Term>) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class SelectTerm(val cases: Term, val selector: Term) : Term()
object ArgumentTerm : Term()

fun Term.invoke(argument: Term): Term =
	when (this) {
		is ArgumentTerm -> argument
		is BitTerm -> this
		is BranchTerm -> BranchTerm(branch(branch.at0.invoke(argument), branch.at1.invoke(argument)))
		is InvokeTerm -> function.invoke(param.invoke(argument))
		is SelectTerm -> (cases.invoke(argument) as BranchTerm).branch.at((selector.invoke(argument) as BitTerm).bit)
	}
