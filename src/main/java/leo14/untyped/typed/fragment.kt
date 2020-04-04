package leo14.untyped.typed

import leo14.Fragment
import leo14.FragmentParent
import leo14.fragment
import leo14.parent

val Leo.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(resolver.typed.script)

val LeoParent.fragmentParent: FragmentParent
	get() =
		leo.fragment.parent(begin)