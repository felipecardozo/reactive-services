package com.reactive.beta.version;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

public class TestingReactor {

    //Not caring about the order, will come depending of the speed of processing
    public void mergingFluxes2(){
        Flux<String> characters = Flux.just("char1", "char2", "char3");
        Flux<String> food = Flux.just("food1", "food2", "food3");

        Flux<String> merged = characters.mergeWith(food);
        merged.subscribe(f -> System.out.println(f.toUpperCase()));
    }

    /**
     * Zip will interalate flux1 with flux2
     * Will emit new flux with 2 values
     * if a Fourth register exist in flux2or flux1 will be ignored
     * */
    public void zippingFluxes(){
        Flux<String> characters = Flux.just("char1", "char2", "char3", "char4");
        Flux<String> food = Flux.just("food1", "food2", "food3");

        Flux<Tuple2<String, String>> zipped = Flux.zip(characters, food);
        zipped.subscribe( f -> System.out.println(f.getT1() + " like " + f.getT2()) );
    }

    public void zippingFluxes2(){
        Flux<String> characters = Flux.just("char1", "char2", "char3", "char4");
        Flux<String> food = Flux.just("food1", "food2", "food3");

        Flux<String> result = Flux.zip(characters, food, (chars, foods) -> chars + " eats " + foods);
        result.subscribe( f -> System.out.println(f) );
    }

    public void distinctFlux(){
        Flux<String> letters = Flux.just("A", "B", "C", "A", "A").distinct();
        letters.subscribe( f -> System.out.println(f));
    }

    public void filterFlux(){
        Flux<Integer> numbers = Flux.just(1,2,3,4,56,7,23,5,6,1)
                .filter( number -> number%2==0 );
        numbers.subscribe( f -> System.out.println(f) );
    }

    public void mapFlux(){
        Flux<BasketPlayer> players = Flux.just("Michael Jordan", "Scottie Pippen", "Shaquille O'neal")
                .map( p -> {
                    String[] fullName = p.split("\\s");
                    return new BasketPlayer(fullName[0], fullName[1]);
                } );
        players.subscribe( f -> System.out.println(f) );
    }

    public void flatMapFlux(){
        System.out.println("=== flat map ===");
        Flux.just("Michael Jordan", "Scottie Pippen", "Shaquille O'neal", "John Doe")
                .flatMap( mono -> Mono.just(mono)
                    .map( player -> {
                        String [] fullName = player.split("\\s");
                        return new BasketPlayer(fullName[0], fullName[1]);
                    })
                .subscribeOn(Schedulers.parallel()))
        .subscribe( f -> System.out.println(f));

    }

    /**
     * We are buffering a flux of 13 strings values in a new flux of list collections
     * But then you apply flatMap() to that Flux of list collections.
     * This takes each list buffer and cretes a new Flux from its elements, and then
     * applies a map operation on it.
     * Last, each buffered List is further processed in a parallel in individual threads.
     * log only was added in order to understand the sequence under subscribe and onNext.
     * Eventually every time runs this snippet, will change.
     * */
    public void bufferFluxWithLists(){
        System.out.println("=== buffer ===");
        Flux.just("a", "q", "w", "e", "r", "f", "g", "h", "b", "v", "m", "s", "u")
                .buffer(7)
                .flatMap( flatLetter ->
                        Flux.fromIterable(flatLetter)
                            .map( m -> m.toUpperCase() )
                            .subscribeOn(Schedulers.parallel())
                            .log()
                ).subscribe( f -> System.out.println(f) );
    }

    public static void main(String[] args) {
        TestingReactor testingReactor = new TestingReactor();
        testingReactor.mergingFluxes2();
        testingReactor.zippingFluxes();
        testingReactor.zippingFluxes2();
        testingReactor.distinctFlux();
        testingReactor.filterFlux();
        testingReactor.mapFlux();
        testingReactor.flatMapFlux();
        testingReactor.bufferFluxWithLists();
    }
}
