package io.github.sashirestela.openai.test.captors;

import org.mockito.ArgumentCaptor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow;

public class CapturedValues {

    public static String getRequestBodyAsString(ArgumentCaptor<HttpRequest> httpRequest) {
        // be very sure that nobody else is concurrently
        // subscribed to the body publisher when executing this code
        return httpRequest.getValue().bodyPublisher().map(p -> {
            var bodySubscriber = BodySubscribers.ofString(StandardCharsets.UTF_8);
            var flowSubscriber = new StringSubscriberAdapter(bodySubscriber);
            p.subscribe(flowSubscriber);
            return bodySubscriber.getBody().toCompletableFuture().join();
        }).orElse(null);
    }

    private static final class StringSubscriberAdapter implements Flow.Subscriber<ByteBuffer> {

        final BodySubscriber<String> wrapped;

        StringSubscriberAdapter(BodySubscriber<String> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            wrapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(ByteBuffer item) {
            wrapped.onNext(List.of(item));
        }

        @Override
        public void onError(Throwable throwable) {
            wrapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            wrapped.onComplete();
        }

    }

}
