package example

import example.ExampleContainer.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class ExampleViewModel {

    val viewModelScope = CoroutineScope(Job())

    val mvi = ExampleContainer.create(
        state = State.Default,
        scope = viewModelScope
    ) // create "store"
}