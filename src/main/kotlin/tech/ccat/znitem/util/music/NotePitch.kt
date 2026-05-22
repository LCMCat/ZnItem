package tech.ccat.znitem.util.music

import org.bukkit.Sound

enum class NotePitch(val note: Int, val pitch: Float) {
    NOTE_0(0, 0.5F),
    NOTE_1(1, 0.53F),
    NOTE_2(2, 0.56F),
    NOTE_3(3, 0.6F),
    NOTE_4(4, 0.63F),
    NOTE_5(5, 0.67F),
    NOTE_6(6, 0.7F),
    NOTE_7(7, 0.76F),
    NOTE_8(8, 0.8F),
    NOTE_9(9, 0.84F),
    NOTE_10(10, 0.9F),
    NOTE_11(11, 0.94F),
    NOTE_12(12, 1.0F),
    NOTE_13(13, 1.06F),
    NOTE_14(14, 1.12F),
    NOTE_15(15, 1.18F),
    NOTE_16(16, 1.26F),
    NOTE_17(17, 1.34F),
    NOTE_18(18, 1.42F),
    NOTE_19(19, 1.5F),
    NOTE_20(20, 1.6F),
    NOTE_21(21, 1.68F),
    NOTE_22(22, 1.78F),
    NOTE_23(23, 1.88F),
    NOTE_24(24, 2.0F);

    companion object {
        fun getPitch(note: Int): Float = entries.find { it.note == note }?.pitch ?: 0.0F
    }
}
