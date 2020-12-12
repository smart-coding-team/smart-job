package com.smartcoding.job.core.log;

import com.smartcoding.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by xuxueli on 17/4/28.
 */
public class XxlJobLogger {
    private static Logger logger = LoggerFactory.getLogger("xxl-job logger");

    /**
     * append log
     *
     * @param logLevel
     * @param callInfo
     * @param appendLog
     */
    private static void logDetail(LogLevel logLevel, StackTraceElement callInfo, String appendLog) {

        /*// "yyyy-MM-dd HH:mm:ss [ClassName]-[MethodName]-[LineNumber]-[ThreadName] log";
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        StackTraceElement callInfo = stackTraceElements[1];*/
        if (LogLevel.DEBUG.equals(logLevel)) {
            logger.debug(">>>>>>>>>>> {}", appendLog);
        } else if (LogLevel.INFO.equals(logLevel)) {
            logger.info(">>>>>>>>>>> {}", appendLog);
        } else if (LogLevel.WARN.equals(logLevel)) {
            logger.warn(">>>>>>>>>>> {}", appendLog);
        } else if (LogLevel.ERROR.equals(logLevel)) {
            logger.error(">>>>>>>>>>> {}", appendLog);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DateUtil.formatDateTime(new Date())).append(" ")
                .append("[" + callInfo.getClassName() + "#" + callInfo.getMethodName() + "]").append("-")
                .append("[" + callInfo.getLineNumber() + "]").append("-")
                .append("[" + Thread.currentThread().getName() + "]").append(" ")
                .append(appendLog != null ? appendLog : "");
        String formatAppendLog = stringBuffer.toString();

        // appendlog
        String logFileName = XxlJobFileAppender.contextHolder.get();
        if (logFileName != null && logFileName.trim().length() > 0) {
            XxlJobFileAppender.appendLog(logFileName, formatAppendLog);
        } else {
            logger.info(">>>>>>>>>>> {}", formatAppendLog);
        }
    }

    /**
     * append log with pattern
     *
     * @param appendLogPattern   like "aaa {} bbb {} ccc"
     * @param appendLogArguments like "111, true"
     */
    public static void log(String appendLogPattern, Object... appendLogArguments) {
        log(LogLevel.DEBUG, appendLogPattern, appendLogArguments);
    }

    public static void log(LogLevel logLevel, String appendLogPattern, Object... appendLogArguments) {
        String message = "";
        if (appendLogPattern != null) {
            if (appendLogArguments != null) {
                FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
                message = ft.getMessage();
            } else {
                message = appendLogPattern;
            }
        }

        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(logLevel, callInfo, message);
    }

    /**
     * append exception stack
     *
     * @param e
     */
    public static void log(Throwable e) {
        log(LogLevel.DEBUG, e);
    }

    public static void log(LogLevel logLevel, Throwable e) {
        log(logLevel, e, null, null);
    }

    public static void log(LogLevel logLevel, Throwable e, String appendLogPattern, Object... appendLogArguments) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        String message = "";
        if (appendLogPattern != null) {
            if (appendLogArguments != null) {
                FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
                message = ft.getMessage();
            } else {
                message = appendLogPattern;
            }
        }
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(logLevel, callInfo, message + appendLog);
    }

}
