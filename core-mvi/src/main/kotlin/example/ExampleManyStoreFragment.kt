package example

import com.vanced.manager.core.mvi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ExampleManyStoreFragment {

	val lifecycleScope = CoroutineScope(Job())

	private val view1 = getView()
	private val container1 = getStore()

	private val view2 = getView()
	private val container2 = getStore()

	private fun onCreate() {
		lifecycleScope.launch { // bind view for call render
			container1.store.bindView(view = view1, scope = this)
		}
		lifecycleScope.launch { // bind side effects (single events) for catch on view
			container1.store.bindSideEffects(view = view1, scope = this)
		}

		lifecycleScope.launch { // bind view for call render
			container2.store.bindView(view = view2, scope = this)
		}
		lifecycleScope.launch { // bind side effects (single events) for catch on view
			container2.store.bindSideEffects(view = view2, scope = this)
		}
	}
}

// handle actions, generate side effects (single events) and send changes (may be viewModel ane change)
private fun createHandler(): Handler<State, Action, Modification, SideEffect> =
	{ _: State, action: Action, sideEffect: MutableSharedFlow<SideEffect> ->
		when (action) {
			Action.Click -> {
				sideEffect.emit(SideEffect.ShowToast(""))
				delay(10)
				emit(Modification.ChangeText(""))
			}
		}
	}

// handle modifications and current state and generate new state for view\
private fun createReducer(): Reducer<State, Modification> = { _: State, modification: Modification ->
	when (modification) {
		is Modification.ChangeText -> {
			State.Default
		}
	}
}

private fun ExampleManyStoreFragment.getStore() = object : MviFlowStore<State, Action, Modification, SideEffect> {

	override val store: MviFlow<State, Action, Modification, SideEffect>
		get() = MviFlow(
			initialState = State.Default,
			reducer = createReducer(),
			handler = createHandler(),
			scope = lifecycleScope
		)// create "store"
}

private fun getView() =
	object : MviRenderView<State, Action, SideEffect> {
		override fun render(state: State) {
			when (state) {
				State.Default -> {
					// render view
				}
			}
		}

		override fun actionsFlow(): Flow<Action> = callbackFlow {
			//generate actions click and other
			awaitClose()
		}

		override fun sideEffects(sideEffect: SideEffect) {
			when (sideEffect) {
				is SideEffect.ShowToast -> {
					// Toast.show
				}
			}
		}

	}