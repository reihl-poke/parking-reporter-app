package io.github.reihlinstinct.reporter.live

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.reihlinstinct.reporter.FlowDriver

/**
 * Live runs only. Called right after a confirmed submission: leaves the
 * submitted screen through its "back to reports" button, waits for the
 * history section ("היסטוריית דיווחים") and at least one sent-report
 * identifier (testTag "history-identifier") to be visible, waits for the
 * UI to go idle, then captures the screenshot into the encrypted bundle.
 *
 * Privacy: only fixed marker names are passed to [marker]. The case number
 * shown on screen is never logged; it exists only in the screenshot.
 */
internal fun showReportHistoryAfterSubmit(
    compose: ComposeTestRule,
    driver: FlowDriver,
    marker: (String) -> Unit,
) {
    compose.waitUntil(15_000) {
        compose.onAllNodesWithText("חזרה לדיווחים").fetchSemanticsNodes().isNotEmpty()
    }
    compose.onNodeWithText("חזרה לדיווחים").performClick()
    try {
        compose.waitUntil(15_000) {
            compose.onAllNodesWithText("היסטוריית דיווחים").fetchSemanticsNodes().isNotEmpty() &&
                compose.onAllNodesWithTag("history-identifier").fetchSemanticsNodes().isNotEmpty()
        }
    } catch (e: ComposeTimeoutException) {
        runCatching { driver.screenshot("live-e2e-history-missing.png") }
        marker("HISTORY_NOT_VISIBLE")
        throw AssertionError("report history did not show the sent report")
    }
    compose.waitForIdle()
    driver.screenshot("live-e2e-history.png")
    marker("HISTORY_VISIBLE")
}
