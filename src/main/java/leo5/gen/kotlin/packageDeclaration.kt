package leo5.gen.kotlin

import leo5.script.nonEmptyOrNull

data class PackageDeclaration(val package_: Package)

fun declaration(package_: Package) = PackageDeclaration(package_)

fun Appendable.append(declaration: PackageDeclaration) {
	declaration.package_.path.nonEmptyOrNull?.run {
		append("package ")
		append(name(declaration.package_))
		append("\n\n")
	}
}
