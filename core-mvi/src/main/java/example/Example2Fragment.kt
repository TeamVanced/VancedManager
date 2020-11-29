package example

import com.vanced.manager.core.mvi.MviRenderView
import example.ExampleContainer.Action
import example.ExampleContainer.SideEffect
import example.ExampleContainer.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class Example2Fragment : MviRenderView<State, Action, SideEffect> {

    val lifecycleScope = CoroutineScope(Job())

    val mvi = ExampleContainer.create(
        state = State.Default,
        scope = lifecycleScope
    )  // create "store"

    private fun onCreate() {
        lifecycleScope.launch { // bind view for call render
            mvi.bindView(view = this@Example2Fragment, scope = this)
        }
        lifecycleScope.launch { // bind side effects (single events) for catch on view
            mvi.bindSideEffects(view = this@Example2Fragment, scope = this)
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