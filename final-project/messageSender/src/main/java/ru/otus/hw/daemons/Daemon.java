package ru.otus.hw.daemons;

import java.time.Duration;

public interface Daemon {

    /**
     * <p>Демон включен?</p>
     *
     * @return Да/Нет.
     */
    boolean isEnabled();

    /**
     * <p>Возвращает задержку перед первым запуском демона.</p>
     *
     * @return Задержка перед первым запуском демона.
     */
    Duration getInitialDelay();

    /**
     * <p>Возвращает временной интервал между повторными запусками демона.</p>
     *
     * @return Временной интервал между повторными запусками демона.
     */
    Duration getInterval();

    /**
     * <p>Исполняемая логика демона.</p>
     */
    void action();
}
