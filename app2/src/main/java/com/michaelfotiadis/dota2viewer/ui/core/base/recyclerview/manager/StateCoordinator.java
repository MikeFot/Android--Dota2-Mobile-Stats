package com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.manager;

import com.michaelfotiadis.dota2viewer.ui.core.base.error.errorpage.QuoteOnClickListenerWrapper;
import com.michaelfotiadis.dota2viewer.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.dota2viewer.ui.core.base.viewmanagement.UiStateKeeper;

/**
 *
 */
/*protected*/ public class StateCoordinator {

    private final BaseRecyclerViewAdapter<?, ?> adapter;
    private final CharSequence emptyMessage;
    private final UiStateKeeper uiStateKeeper;
    private State currentState = null;
    private boolean error;
    private CharSequence errorMessage;
    private QuoteOnClickListenerWrapper onClickListenerWrapper;

    public StateCoordinator(final BaseRecyclerViewAdapter<?, ?> adapter,
                            final UiStateKeeper uiStateKeeper,
                            final CharSequence emptyMessage) {

        this.adapter = adapter;
        this.uiStateKeeper = uiStateKeeper;
        this.emptyMessage = emptyMessage;
    }

    public void clearError() {
        error = false;
        errorMessage = null;
        onClickListenerWrapper = null;
        updateUiState();
    }

    public void updateUiState() {

        final State state;

        if (error) {
            state = State.ERROR;
        } else {
            if (adapter.getItemCount() == 0) {
                state = adapter.hasAttemptedDataAddition() ? State.EMPTY : State.PROGRESS;
            } else {
                state = State.CONTENT;
            }
        }

        this.updateUiState(state);
    }

    public void updateUiState(final State state) {
        if (currentState != state || currentState.equals(State.ERROR)) {
            currentState = state;

            if (uiStateKeeper != null) {
                switch (state) {
                    case ERROR:
                        uiStateKeeper.showError(errorMessage, onClickListenerWrapper);
                        break;
                    case PROGRESS:
                        uiStateKeeper.showProgress();
                        break;
                    case EMPTY:
                        uiStateKeeper.showEmpty(emptyMessage);
                        break;
                    case CONTENT:
                        uiStateKeeper.showContent();
                        break;
                }
            }
        }
    }

    public void setError(final CharSequence errorMessage,
                         final QuoteOnClickListenerWrapper listenerWrapper) {

        this.error = true;
        this.errorMessage = errorMessage;
        this.onClickListenerWrapper = listenerWrapper;
        updateUiState();
    }


}
