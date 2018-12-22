package v.alice.service3;

import android.app.Service;
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;

public class LocalService extends Service {

    private final IBinder binder = new PrimesBinder();
    private long prime = 2;
    private int index = 0;
    private boolean running = false;
    @Override
    public IBinder onBind(Intent intent) {
        start();
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        running = false;
        return true;
    }

    public int getIndex()
    {
        return index;
    }

    public long getPrime()
    {
        return prime;
    }

    public void pause() {
        running = false;
    }

    public void start()
    {
        if (!running) {
            running = true;
            Thread th = new Thread(new Generator());
            th.start();
        }
    }

    public class PrimesBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    private boolean isPrime(long n)
    {
        if (n == 2 || n == 3 || n == 5 || n == 7) return true;
        if (n < 11 || n % 2 == 0) return false;
        for (long t = 3, m = (long)Math.sqrt(n) + 1; t <= m; t += 2) if (n % t == 0) return false;
        return true;
    }

    private class Generator implements Runnable
    {
        @Override
        public void run() {
            long t = index == 0 ? 3 : prime + 2;
            for ( ; running ; t += 2)
                if (isPrime(t))
                {
                    ++index;
                    prime = t;
                }
        }
    }

}
