package leo21.token.typer

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.type.script

val TokenTyper.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(type.script)

val TypeParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TyperNameTypeParent -> typer.fragment.parent(begin(name))
		}

