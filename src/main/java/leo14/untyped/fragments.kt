package leo14.untyped

import leo14.*

val Reader.fragment: Fragment
	get() =
		when (this) {
			is QuotedReader -> quoted.fragment
			is UnquotedReader -> unquoted.fragment
			is CodeReader -> code.fragment
		}

val Unquoted.fragment: Fragment
	get() =
		opOrNull?.fragmentParent.fragment(resolver.program.script)

val Quoted.fragment: Fragment
	get() =
		opOrNull?.fragmentParent.fragment(program.script)

val Code.fragment: Fragment
	get() =
		opOrNull?.fragmentParent.fragment(script)

val UnquotedOp.fragmentParent: FragmentParent
	get() =
		when (this) {
			is UnquotedResolveUnquotedOp -> unquoted.fragment.parent(begin)
			is QuotedPlusUnquotedOp -> quoted.fragment.parent(begin(unquoteName))
		}

val QuotedOp.fragmentParent: FragmentParent
	get() =
		when (this) {
			is QuotedAppendQuotedOp -> quoted.fragment.parent(begin)
			is UnquotedPlusQuotedOp -> unquoted.fragment.parent(begin(quoteName))
		}

val CodeOp.fragmentParent: FragmentParent
	get() =
		when (this) {
			is CodeAppendCodeOp -> code.fragment.parent(begin)
			is UnquotedFunctionCodeOp -> unquoted.fragment.parent(begin(functionName))
			is UnquotedGivesCodeOp -> unquoted.fragment.parent(begin(doesName))
			is UnquotedGetCodeOp -> unquoted.fragment.parent(begin(getName))
		}