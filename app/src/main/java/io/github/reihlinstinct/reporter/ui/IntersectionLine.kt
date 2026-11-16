package io.github.reihlinstinct.reporter.ui

import io.github.reihlinstinct.reporter.i18n.Strings

/**
 * The description line for a spot at a street crossing (owner steer 2026-11-16). It is Hebrew in every UI
 * language because the description goes to the city as typed, and it is built from the Hebrew template by
 * plain replacement so no bidi isolate characters end up in the sent text.
 */
internal fun intersectionDescriptionLine(strings: Strings, streetA: String, streetB: String): String =
    strings.catalog.t("he", "review.location.intersection_line")
        .replace("{street_a}", streetA).replace("{street_b}", streetB)

/** Adds [line] after the existing description, once: the same line is never added twice. */
internal fun withIntersectionLine(text: String, line: String): String = when {
    text.contains(line) -> text
    text.isBlank() -> line
    else -> text.trimEnd() + "\n" + line
}
