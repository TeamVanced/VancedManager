package com.vanced.manager.core.mvi

interface MviFlowStore<State, Action, Modification, SideEffect> {

	val store: MviFlow<State, Action, Modification, SideEffect>
}