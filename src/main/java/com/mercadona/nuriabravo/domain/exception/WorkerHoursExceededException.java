package com.mercadona.nuriabravo.domain.exception;

public class WorkerHoursExceededException extends RuntimeException {
    public WorkerHoursExceededException(Long workerId, int available, int requested) {
        super("worker.hoursExceeded:" + workerId + ":available=" + available + ":requested=" + requested);
    }
}