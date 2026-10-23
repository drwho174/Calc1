# Правила R8 для release-сборки.

# Markwon и LaTeX-рендерер используют отражение и ServiceLoader-подобные
# механизмы внутри плагинов, поэтому их классы не обфусцируем.
-keep class io.noties.markwon.** { *; }
-keep class ru.noties.jlatexmath.** { *; }
-keep class org.scilab.forge.jlatexmath.** { *; }
-dontwarn io.noties.markwon.**
-dontwarn ru.noties.**
-dontwarn org.scilab.**

# Сохраняем имена ключей навигации для kotlinx.serialization.
-keepclassmembers class com.drwho174.calc1.navigation.** {
    *** Companion;
}
-keepclasseswithmembers class com.drwho174.calc1.navigation.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Полезно для читаемых стектрейсов из release-сборки.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
