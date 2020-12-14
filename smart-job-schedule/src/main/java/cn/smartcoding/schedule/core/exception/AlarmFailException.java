

package cn.smartcoding.schedule.core.exception;

/**
 * @author xuxueli 2019-05-04 23:19:29
 */
public class AlarmFailException extends RuntimeException {

    public AlarmFailException() {
    }
    public AlarmFailException(String message) {
        super(message);
    }

    public AlarmFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
