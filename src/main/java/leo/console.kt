package leo

val consoleWidth: Int = System.getenv("COLUMNS").toIntOrNull() ?: 80
val consoleHeight: Int = System.getenv("LINES").toIntOrNull() ?: 25
