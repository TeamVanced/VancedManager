package example

import com.vanced.manager.core.mvi.Handler
import com.vanced.manager.core.mvi.MviFlowContainer
import com.vanced.manager.core.mvi.Reducer
import example.ExampleContainer.Action
import example.ExampleContainer.Modification
import example.ExampleContainer.SideEffect
import example.ExampleContainer.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

object ExampleContainer : MviFlowContainer<State, Action, Modification, SideEffect>() {

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

    // handle actions, generate side effects (single events) and send changes (may be viewModel ane change)
    override val handler: Handler<State, Action, Modification, SideEffect> =
        { _: State, action: Action, sideEffect: MutableSharedFlow<SideEffect> ->
            when (action) {
                Action.Click -> {
                    sideEffect.emit(SideEffect.ShowToast(""))
                    delay(10)
                    emit(Modification.ChangeText(""))
                }
            }
        }

    // handle modifications and current state and generate new state for view
    override val reducer: Reducer<State, Modification> = { _: State, modification: Modification ->
        when (modification) {
            is Modification.ChangeText -> {
                State.Default
            }
        }
    }
}