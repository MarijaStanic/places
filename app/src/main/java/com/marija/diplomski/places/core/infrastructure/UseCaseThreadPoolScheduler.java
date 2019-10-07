package com.marija.diplomski.places.core.infrastructure;

import android.os.Handler;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.executor.UseCaseScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UseCaseThreadPoolScheduler implements UseCaseScheduler {

    private final Handler handler = new Handler();

    private ExecutorService executorService;

    private static UseCaseThreadPoolScheduler INSTANCE;

    private UseCaseThreadPoolScheduler() {
        executorService = Executors.newCachedThreadPool();
    }

    public static UseCaseThreadPoolScheduler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UseCaseThreadPoolScheduler();
        }
        return INSTANCE;
    }

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public <V extends UseCase.ResponseValue> void notifyResponse(final V response,
                                                                 final UseCase.UseCaseCallback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    @Override
    public <V extends UseCase.ResponseValue> void onError(final UseCase.UseCaseCallback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onError();
            }
        });
    }

}