package example

import com.vanced.manager.core.mvi.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow

class ExampleStoreFragment :
	MviRenderView<State, Action, SideEffect>,
	MviFlowStore<State, Action, Modification, SideEffect> {

	private val lifecycleScope = CoroutineScope(Job())

	// handle actions, generate side effects (single events) and send changes (may be viewModel ane change)
	private val handler: Handler<State, Action, Modification, SideEffect> =
		{ _: State, action: Action, sideEffectsFlow: MutableSharedFlow<SideEffect> ->
			when (action) {
				Action.Click -> {
					sideEffectsFlow.emit(SideEffect.ShowToast(""))
					delay(10)
					emit(Modification.ChangeText(""))
				}
			}
		}

	// handle modifications and current state and generate new state for view
	private val reducer: Reducer<State, Modification> = { _: State, modification: Modification ->
		when (modification) {
            is Modification.ChangeText -> {
                State.Default
            }
		}
	}

	override val store: MviFlow<State, Action, Modification, SideEffect> =
		MviFlow(
            initialState = State.Default,
            reducer = reducer,
            handler = handler,
            scope = lifecycleScope
        )// create "store"

	private fun onCreate() {
		lifecycleScope.launch { // bind view for call render
			store.bindView(view = this@ExampleStoreFragment, scope = this)
		}
		lifecycleScope.launch { // bind side effects (single events) for catch on view
			store.bindSideEffects(view = this@ExampleStoreFragment, scope = this)
		}
	}

	override fun render(state: State) {
		when (state) {
            State.Default -> {
                // render view
            }
		}
	}

	@ExperimentalCoroutinesApi
	override fun actionsFlow(): Flow<Action> = callbackFlow {
		//generate actions click and other
		awaitClose()
	}

	override fun sideEffects(sideEffect: SideEffect) { // single events
		when (sideEffect) {
            is SideEffect.ShowToast -> {
                // Toast.show
            }
		}
	}
}