/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.util.stream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 *
 * @author spy
 */
public class Streams {
static final Object NONE = new Object();
    static class StreamBuilderImpl {

        public StreamBuilderImpl() {
        }
    }

    static class IntStreamBuilderImpl implements IntStream.Builder {

        public IntStreamBuilderImpl() {
        }

        IntStreamBuilderImpl(int t) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void accept(int t) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public IntStream build() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        public IntStream.Builder add(int i ){
             throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class RangeIntSpliterator implements Spliterator.OfInt {

        public RangeIntSpliterator(int startInclusive, int endExclusive, boolean b) {
        }

        @Override
        public OfInt trySplit() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean tryAdvance(Consumer<? super Integer> action) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
public void forEachRemaining(IntConsumer action){
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

}
public void forEachRemaining(Consumer action){
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

}
        @Override
        public long estimateSize() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int characteristics() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
}
