package com.droidcon.voices.data.preference

/**
 * Enum class used for changing sort order of displayed voices
 */
enum class SortOrder {
    ASCENDING,
    DESCENDING
}

/**
 * Enum class used for sorting items differently based on type of sort
 */
enum class SortBy {
    DEFAULT,
    DATE,
    NAME,
    SIZE,
    DURATION
}