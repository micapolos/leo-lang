package leo5.gen.kotlin

import leo.base.string
import leo5.script.nonEmptyOrNull

data class PackageName(val package_: Package)

fun name(package_: Package) = PackageName(package_)

fun Appendable.append(name: PackageName) {
	name.package_.path.nonEmptyOrNull?.let { nonEmpty ->
		append(name(package_(nonEmpty.path)))
		nonEmpty.path.nonEmptyOrNull?.run { append(".") }
		append(nonEmpty.path.string)
	}
}
