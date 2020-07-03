package com.dlet.cartrack.challenge.common_android.mvi

typealias Reducer<S, C> = (state: S, change: C) -> S