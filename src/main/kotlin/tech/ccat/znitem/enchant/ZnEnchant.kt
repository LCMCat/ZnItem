package tech.ccat.znitem.enchant

import tech.ccat.znitem.model.ItemType

abstract class ZnEnchant(
    val id: String,
    val displayName: String,
    val applicableTypes: Set<ItemType>
) {
    abstract fun getEffectValue(level: Int): Double

    abstract fun getDescription(level: Int): String

    open fun formatLore(level: Int): String {
        return "§9$displayName ${toRoman(level)}"
    }

    companion object {
        fun toRoman(level: Int): String {
            if (level <= 0) return "0"
            if (level >= 4000) return level.toString()
            val thousands = level / 1000
            val remainder = level % 1000
            val result = StringBuilder()
            repeat(thousands) { result.append("M") }
            result.append(toRomanUnder1000(remainder))
            return result.toString()
        }

        private fun toRomanUnder1000(num: Int): String {
            if (num <= 0) return ""
            val values = intArrayOf(900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
            val symbols = arrayOf("CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
            val result = StringBuilder()
            var n = num
            for (i in values.indices) {
                while (n >= values[i]) {
                    result.append(symbols[i])
                    n -= values[i]
                }
            }
            return result.toString()
        }
    }
}
