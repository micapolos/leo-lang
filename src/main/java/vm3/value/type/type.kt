package vm3.value.type

import vm3.value.Value

val Value.type get() = emptyTyper.typeEffect(this).value