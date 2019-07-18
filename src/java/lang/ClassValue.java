/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.lang;

import java.lang.invoke.MethodHandle;

/**
 *
 * @author spy
 */
public class ClassValue<T> {

    public T get(Class<?> type) {
        // non-racing this.hashCodeForCache : final int
        //Entry<?>[] cache;
        //Entry<T> e = probeHomeLocation(cache = getCacheCarefully(type), this);
        // racing e : current value <=> stale value from current cache or from stale cache
        // invariant:  e is null or an Entry with readable Entry.version and Entry.value
        //if (match(e))
            // invariant:  No false positive matches.  False negatives are OK if rare.
            // The key fact that makes this work: if this.version == e.version,
            // then this thread has a right to observe (final) e.value.
        //    return e.value();
        // The fast path can fail for any of these reasons:
        // 1. no entry has been computed yet
        // 2. hash code collision (before or after reduction mod cache.length)
        // 3. an entry has been removed (either on this type or another)
        // 4. the GC has somehow managed to delete e.version and clear the reference
        //return getFromBackup(cache, type);
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
