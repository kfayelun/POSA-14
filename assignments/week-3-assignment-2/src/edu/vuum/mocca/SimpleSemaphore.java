package edu.vuum.mocca;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject (which is accessed via a Condition). It must
 *        implement both "Fair" and "NonFair" semaphore semantics,
 *        just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
    // doesn't need final
    private final ReentrantLock mRLock;

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
    private final Condition mPermitCondition;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
    private volatile int numberOfAvailablePermits;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.

        mRLock = new ReentrantLock(fair);
        mPermitCondition = mRLock.newCondition();
        this.numberOfAvailablePermits = permits;

    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.

        try {
            mRLock.lockInterruptibly();
            while (numberOfAvailablePermits==0){
                mPermitCondition.await();
            }
            numberOfAvailablePermits--;
        } finally {
            mRLock.unlock();
        }
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.

        try {
            mRLock.lock();
            while (numberOfAvailablePermits==0){
                mPermitCondition.awaitUninterruptibly();
            }
            numberOfAvailablePermits--;
        } finally {
            mRLock.unlock();
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.

        try {
            mRLock.lock();
            numberOfAvailablePermits++;
            mPermitCondition.signalAll();
        } finally {
            mRLock.unlock();
        }
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate

        return numberOfAvailablePermits;
    }
}
