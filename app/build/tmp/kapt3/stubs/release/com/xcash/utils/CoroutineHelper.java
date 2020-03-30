package com.xcash.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u000bJ\u0006\u0010\f\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/xcash/utils/CoroutineHelper;", "", "()V", "job", "Lkotlinx/coroutines/Job;", "uiScope", "Lkotlinx/coroutines/CoroutineScope;", "launch", "", "T", "onCoroutineListener", "Lcom/xcash/utils/CoroutineHelper$OnCoroutineListener;", "onDestroy", "OnCoroutineListener", "app_release"})
public final class CoroutineHelper {
    private final kotlinx.coroutines.Job job = null;
    private final kotlinx.coroutines.CoroutineScope uiScope = null;
    
    public final <T extends java.lang.Object>void launch(@org.jetbrains.annotations.NotNull()
    com.xcash.utils.CoroutineHelper.OnCoroutineListener<T> onCoroutineListener) {
    }
    
    public final void onDestroy() {
    }
    
    public CoroutineHelper() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\u0015\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\u0006J\r\u0010\u0007\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/xcash/utils/CoroutineHelper$OnCoroutineListener;", "T", "", "overRunOnMain", "", "t", "(Ljava/lang/Object;)V", "runOnIo", "()Ljava/lang/Object;", "app_release"})
    public static abstract interface OnCoroutineListener<T extends java.lang.Object> {
        
        public abstract T runOnIo();
        
        public abstract void overRunOnMain(T t);
    }
}