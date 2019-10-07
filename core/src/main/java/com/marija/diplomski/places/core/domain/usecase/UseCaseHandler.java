package com.marija.diplomski.places.core.domain.usecase;


import com.marija.diplomski.places.core.domain.executor.UseCaseScheduler;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;

    private final UseCaseScheduler useCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        this.useCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance(UseCaseScheduler useCaseScheduler) {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(useCaseScheduler);
        }
        return INSTANCE;
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(final V response, final UseCase.UseCaseCallback<V> useCaseCallback) {
        useCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue> void notifyError(final UseCase.UseCaseCallback<V> useCaseCallback) {
        useCaseScheduler.onError(useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValue> implements UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> callback;
        private final UseCaseHandler useCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback, UseCaseHandler useCaseHandler) {
            this.callback = callback;
            this.useCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            useCaseHandler.notifyResponse(response, callback);
        }

        @Override
        public void onError() {
            useCaseHandler.notifyError(callback);
        }
    }

}
