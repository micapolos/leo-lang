package leo14.untyped

data class Fragment(
	val parentOrNull: FragmentParent?,
	val program: Program)

data class FragmentParent(
	val fragment: Fragment,
	val name: String)

fun Fragment.begin(name: String): Fragment =
	Fragment(FragmentParent(this, name), program())

fun Fragment.append(value: Value): Fragment =
	Fragment(parentOrNull, program.plus(value))

fun Fragment.end(): Fragment? =
	parentOrNull?.let { parent ->
		Fragment(
			parent.fragment.parentOrNull,
			parent.fragment.program.plus(parent.name valueTo program))
	}

fun Fragment.updateProgram(fn: Program.() -> Program) =
	copy(program = program.fn())