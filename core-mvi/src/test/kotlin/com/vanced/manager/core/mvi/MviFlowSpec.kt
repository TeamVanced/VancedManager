package com.vanced.manager.core.mvi

import com.vanced.manager.core.mvi.subject.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope

@ExperimentalCoroutinesApi
class MviFlowSpec : ShouldSpec() {

    data class Test(val msg: String)

    init {
        context("Various events") {
            should("return Modifications") {
                val testData = "testText"
                val store = SubjectStore<String>(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Tap)
                        emit(Action.Click)
                    }
                )
                delay(600)
                store.modifications shouldBe listOf(
                    Modification.ChangeDescription(testData),
                    Modification.ChangeTitle(testData),
                    Modification.ChangeDescription(testData),
                )
            }
            should("return States") {
                val testData = 4545454545
                val store = SubjectStore<Long>(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                val view = SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Tap)
                        emit(Action.Click)
                    }
                )
                delay(600)
                view.states shouldBe listOf(
                    State.Default,
                    State.SetDescription(testData),
                    State.Default,
                    State.SetTitle(testData),
                    State.Default,
                    State.SetDescription(testData),
                    State.Default,
                )
            }
            should("return SideEffects") {
                val testData = Test("test")
                val store = SubjectStore<Test>(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                val view = SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Tap)
                        emit(Action.Click)
                    }
                )
                delay(600)
                view.sideEffects shouldBe listOf(
                    SideEffect.ShowToast(testData),
                    SideEffect.ShowToast(testData),
                    SideEffect.ShowToast(testData),
                )
            }
        }

        context("The same event") {
            should("return Modifications") {
                val testData = Test("test")
                val store = SubjectStore(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Click)
                        emit(Action.Click)
                    }
                )
                delay(600)
                store.modifications shouldBe listOf(
                    Modification.ChangeDescription(testData),
                    Modification.ChangeDescription(testData),
                    Modification.ChangeDescription(testData),
                )
            }
            should("return States") {
                val testData = Test("test")
                val store = SubjectStore(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                val view = SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Click)
                        emit(Action.Click)
                    }
                )
                delay(600)
                view.states shouldBe listOf(
                    State.Default,
                    State.SetDescription(testData),
                    State.Default,
                    State.SetDescription(testData),
                    State.Default,
                    State.SetDescription(testData),
                    State.Default
                )
            }
            should("return SideEffects") {
                val testData = Test("test")
                val store = SubjectStore(
                    scope = TestCoroutineScope(),
                    defaultState = State.Default,
                    testData = testData
                )
                val view = SubjectView(
                    scope = TestCoroutineScope(),
                    store = store,
                    actions = flow {
                        emit(Action.Click)
                        emit(Action.Click)
                        emit(Action.Click)
                    }
                )
                delay(600)
                view.sideEffects shouldBe listOf(
                    SideEffect.ShowToast(testData),
                    SideEffect.ShowToast(testData),
                    SideEffect.ShowToast(testData),
                )
            }
        }
    }
}