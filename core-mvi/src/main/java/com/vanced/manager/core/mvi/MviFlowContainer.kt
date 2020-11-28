package com.vanced.manager.core.mvi

import kotlinx.coroutines.CoroutineScope

abstract class MviFlowContainer<State, Action, Modification, SideEffect> {

    protected abstract val handler: Handler<State, Action, Modification, SideEffect>

    protected abstract val reducer: Reducer<State, Modification>

    fun create(
        state: State,
        scope: CoroutineScope
    ): MviFlow<State, Action, SideEffect> =
        MviFlow(
            initialState = state,
            reducer = reducer,
            handler = handler,
            scope = scope
        )
}