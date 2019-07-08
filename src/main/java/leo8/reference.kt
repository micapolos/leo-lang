package leo8

data class Reference(val getList: ListOf<Get>)

fun reference(vararg gets: Get): Reference = Reference(listOf(*gets))
