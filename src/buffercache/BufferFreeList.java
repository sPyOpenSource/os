package buffercache;

import jx.buffer.BufferList;

/**
 * Requirements: 
 *  -every BufferHead is part of at most one list 
 */

final class BufferFreeList extends BufferList {

    boolean contains(BufferHead bh) {
	return bh.inlist;
    }
    
}
