package com.etermax.spacehorse.core.common;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class StatsListener {
    private static StatsDClient statsd;
    private static Logger logger = LoggerFactory.getLogger(StatsListener.class);

    static {
        try {
            statsd = new NonBlockingStatsDClient(
                    "orbital1",
                    "localhost",
                    8125,
                    new String[] {"tag:value"}            /* Datadog extension: Constant tags, always applied */
            );
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void incrementCounter(String statName) {
        try {
            if (statsd != null) {
                statsd.incrementCounter(statName);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void decrementCounter(String statName) {
        try{
            if (statsd != null) {
                statsd.decrementCounter(statName);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
