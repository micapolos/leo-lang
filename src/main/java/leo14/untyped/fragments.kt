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
		opOrNull?.fragmentParent.fragment(resolver.printScript)

val Quoted.fragment: Fragment
	get() =
		opOrNull?.fragmentParent.fragment(thunk.script)

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
			is UnquotedDoingCodeOp -> unquoted.fragment.parent(begin(doingName))
			is UnquotedGetCodeOp -> unquoted.fragment.parent(begin(getName))
			is UnquotedAssertCodeOp -> unquoted.fragment.parent(begin(assertName))
			is UnquotedLazyCodeOp -> unquoted.fragment.parent(begin(lazyName))
			is UnquotedMatchCodeOp -> unquoted.fragment.parent(begin(matchName))
			is UnquotedExpandsCodeOp -> unquoted.fragment.parent(begin(expandsName))
			is UnquotedDoCodeOp -> unquoted.fragment.parent(begin(doName))
			is UnquotedDoesCodeOp -> unquoted.fragment.parent(begin(doesName))
		}
