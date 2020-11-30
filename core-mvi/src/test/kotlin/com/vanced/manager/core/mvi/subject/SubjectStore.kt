package com.vanced.manager.core.mvi.subject

import com.vanced.manager.core.mvi.Handler
import com.vanced.manager.core.mvi.MviFlow
import com.vanced.manager.core.mvi.MviFlowStore
import com.vanced.manager.core.mvi.Reducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

class SubjectStore<TD>(
    scope: CoroutineScope,
    defaultState: State,
    private val testData: TD
) : MviFlowStore<State, Action, Modification, SideEffect> {

    val modifications = mutableListOf<Modification>()

    private val handler: Handler<State, Action, Modification, SideEffect> =
        { state: State, action: Action, sideEffectsFlow: MutableSharedFlow<SideEffect> ->
            when (action) {
                Action.Click -> {
                    emit(Modification.ChangeDescription(testData))
                    sideEffectsFlow.emit(SideEffect.ShowToast(testData))
                }
                Action.Tap -> {
                    emit(Modification.ChangeTitle(testData))
                    sideEffectsFlow.emit(SideEffect.ShowToast(testData))
                }
            }
        }

    private val reducer: Reducer<State, Modification> =
        { state: State, modification: Modification ->
            modifications.add(modification)
            when (modification) {
                is Modification.ChangeDescription<*> -> {
                    emit(State.SetDescription(modification.text))
                    emit(State.Default)
                }
                is Modification.ChangeTitle<*> -> {
                    emit(State.SetTitle(modification.text))
                    emit(State.Default)
                }
            }
        }

    override val store: MviFlow<State, Action, Modification, SideEffect> =
        MviFlow(
            scope = scope,
            initialState = defaultState,
            handler = handler,
            reducer = reducer
        )
}