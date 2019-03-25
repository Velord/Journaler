package com.journaler.model

enum class MODE(val mode: Int) {
    CREATE(0),
    EDIT(1),
    VIEW(2);

    companion object {
        val EXTRAS_KEY = "MODE"

        fun getByValue(value: Int): MODE {
            values().forEach {
                if (it.mode == value)
                    return it
            }
            return VIEW
        }
    }
}