package services;


import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;

import java.util.LinkedHashSet;
import java.util.Set;

public class Publisher<T> {
    private final int queueCapacity;
    private final Set<ActorRef> actorRefs;

    public Publisher(final int queueCapacity) {
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.queueCapacity = queueCapacity;
        this.actorRefs = new LinkedHashSet<>();
    }

    public Source<T, ?> register() {
        Source<T, ?> source = Source.<T>actorRef(this.queueCapacity, OverflowStrategy.dropHead())
                .mapMaterializedValue(actorRef -> {
                    Publisher.this.actorRefs.add(actorRef);
                    return actorRef;
                })
                .watchTermination((actorRef, termination) -> {
                    termination.whenComplete((done, cause) -> {
                        Publisher.this.actorRefs.remove(actorRef);
                    });
                    return null;
                });
        return source;
    }

    public void broadcast(final T message) {
        for (ActorRef actorRef: this.actorRefs) {
            actorRef.tell(message, ActorRef.noSender());
        }
    }

    public void clear() {
        for (ActorRef actorRef: this.actorRefs) {
            actorRef.tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
        this.actorRefs.clear();
    }
}