package vm3.dsl

import vm3.Data

val Boolean.data: Data get() = Data.Bool(this)
val Int.data: Data get() = Data.I32(this)
val Float.data: Data get() = Data.F32(this)
