package net.albertogarrido.githubreposearch.ui.common

import net.albertogarrido.githubreposearch.util.ErrorType

interface GithubRepoFinderView {

    fun showError(errorType: ErrorType, optionalMessage: String?)

    fun toggleLoading(show: Boolean)
}