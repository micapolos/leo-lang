package leo14.dispatching

import leo14.*

val Leo.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(resolver.typed.script)

val LeoParent.fragmentParent: FragmentParent
	get() =
		leo.fragment.parent(begin(name))