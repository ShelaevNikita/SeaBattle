enum class Ships(val count: Int, val value: Int, val kill: Int) {
    FIRST(1, 4, 1 * 4),
    SECOND(2, 3, 2 * 3),
    THIRD(3, 2, 3 * 2),
    FOURTH(4, 1, 4 * 1),
    NO(0, Int.MAX_VALUE, Int.MAX_VALUE);
}

