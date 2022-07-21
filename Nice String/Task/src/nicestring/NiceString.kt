package nicestring

fun String.isNice(): Boolean {
    var count = 0
    val notHasSubstrings = !this.contains("bu") &&
            !this.contains("ba") &&
            !this.contains("be")

    if (notHasSubstrings) count++
    if (this.containsDoubleLetter()) count++
    if (this.containsVowels()) count++

    if (count > 1) return true

    return false
}

fun String.containsVowels(): Boolean {
    val vowels = listOf('a', 'e', 'i', 'o', 'u')

    return this.filter { vowels.contains(it) }.length >= 3
}

fun String.containsDoubleLetter(): Boolean {

    this.forEachIndexed { index, _ ->
        if (index > 0) {
            if (this[index] == this[index - 1]) {
                return true
            }
        }
    }
    return false
}