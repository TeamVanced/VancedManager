package com.vanced.manager.core.mvi.subject

// "single events"
sealed class SideEffect {

    data class ShowToast<testData>(val message: testData) : SideEffect()
}

// view state
sealed class State {

    object Default : State()

    data class SetTitle<testData>(val text: testData) : State()

    data class SetDescription<testData>(val text: testData) : State()
}

// view actions
sealed class Action {

    object Click : Action()

    object Tap : Action()
}

// Modification for view
sealed class Modification {

    data class ChangeTitle<testData>(val text: testData) : Modification()

    data class ChangeDescription<testData>(val text: testData) : Modification()
}