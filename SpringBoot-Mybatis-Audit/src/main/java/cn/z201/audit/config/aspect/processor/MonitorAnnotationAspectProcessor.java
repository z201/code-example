package cn.z201.audit.config.aspect.processor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author z201.coding@gmail.com
 **/
public abstract class MonitorAnnotationAspectProcessor {

    protected MonitorAnnotationAspectProcessor nextMonitorAnnotationAspectProcessor;

    protected ProceedingJoinPoint proceedingJoinPoint;

    public MonitorAnnotationAspectProcessor(ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }
    public MonitorAnnotationAspectProcessor getNextMonitorAnnotationAspectProcessor() {
        return nextMonitorAnnotationAspectProcessor;
    }

    public void setNextMonitorAnnotationAspectProcessor(MonitorAnnotationAspectProcessor nextMonitorAnnotationAspectProcessor) {
        this.nextMonitorAnnotationAspectProcessor = nextMonitorAnnotationAspectProcessor;
    }

    public ProceedingJoinPoint getProceedingJoinPoint() {
        return proceedingJoinPoint;
    }

    public void setProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    public void execute(){
        before();
        if (null != nextMonitorAnnotationAspectProcessor && null != proceedingJoinPoint) {
            nextMonitorAnnotationAspectProcessor.execute();
        }
    }

    /**
     * 执行执行之前
     */
    protected abstract void before();

    /**
     * 执行之后
     */
    protected abstract void after();

}
