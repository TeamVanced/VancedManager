package example

// "single events"
sealed class SideEffect {

	data class ShowToast(val message: String) : SideEffect()
}

// view state
sealed class State {

	object Default : State()
}

// view actions
sealed class Action {

	object Click : Action()
}

// Modification for view
sealed class Modification {

	data class ChangeText(val text: String) : Modification()
}