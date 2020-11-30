package com.vanced.manager.core.mvi.subject

import com.vanced.manager.core.mvi.MviRenderView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SubjectView<TD>(
    scope: CoroutineScope,
    private val actions: Flow<Action>,
    store: SubjectStore<TD>
) : MviRenderView<State, Action, SideEffect> {

    val states = mutableListOf<State>()
    val sideEffects = mutableListOf<SideEffect>()

    init {
        scope.launch {
            store.store.bindSideEffects(
                view = this@SubjectView,
                scope = this
            )
        }
        scope.launch {
            store.store.bindView(
                view = this@SubjectView,
                scope = this
            )
        }
    }

    override fun render(state: State) {
        states.add(state)
    }

    @ExperimentalCoroutinesApi
    override fun actionsFlow(): Flow<Action> = actions

    override fun sideEffects(sideEffect: SideEffect) {
        sideEffects.add(sideEffect)
    }
}