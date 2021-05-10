package concurrency.lock;

public class MyReadWriteLock {
    //读写锁的共享变量
    private int state = 0;

    // 高16位为读锁
    private int getReadCount() {
        return state >>> 16;  //无符号右移
    }

    // 低16位为写锁
    private int getWriteCount() {
        return state & (1 << 16 - 1);
    }

    public synchronized void lockRead() throws InterruptedException {
        while (getWriteCount() > 0) {
            wait();
        }
        System.out.println("lockRead ---" + Thread.currentThread().getName());
        state += 1 << 16;
    }

    public synchronized void unLockRead() throws InterruptedException {
        if (getReadCount() > 0) {
            state -= 1 << 16;
        }
        System.out.println("unLock Read ---" + Thread.currentThread().getName());
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        while (getWriteCount() > 0 || getReadCount() > 0) {
            wait();
        }
        System.out.println("lockWrite ---" + Thread.currentThread().getName());
        state += 1;
    }

    public synchronized void unLockWrite() {
        if (getWriteCount() > 0) {
            state -= 1;
        }
        System.out.println("unLock write ---" + Thread.currentThread().getName());
        notifyAll();
    }
}
