package minecraft.sightworld.bungeeapi.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bungeeapi.SightWorld;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class CommonScheduler implements Runnable {

    @Getter
    private final String identifier;

    public CommonScheduler() {
        this(RandomStringUtils.randomAlphanumeric(32));
    }


    /**
     * Отмена и закрытие потока
     */
    public void cancel() {
        SightWorld.getInstance().getSchedulerManager().cancelScheduler(identifier);
    }

    /**
     * Запустить асинхронный поток
     */
    public void runAsync() {
        SightWorld.getInstance().getSchedulerManager().runAsync(this);
    }

    /**
     * Запустить поток через определенное
     * количество времени
     *
     * @param delay - время
     * @param timeUnit - единица времени
     */
    public void runLater(long delay, TimeUnit timeUnit) {
        SightWorld.getInstance().getSchedulerManager().runLater(identifier, this, delay, timeUnit);
    }

    /**
     * Запустить цикличный поток через
     * определенное количество времени
     *
     * @param delay - время
     * @param period - период цикличного воспроизведения
     * @param timeUnit - единица времени
     */
    public void runTimer(long delay, long period, TimeUnit timeUnit) {
        SightWorld.getInstance().getSchedulerManager().runTimer(identifier, this, delay, period, timeUnit);
    }

}
