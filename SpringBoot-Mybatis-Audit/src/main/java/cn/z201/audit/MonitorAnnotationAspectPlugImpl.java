package cn.z201.audit;

import cn.z201.audit.config.aspect.MonitorAnnotationAspectPlugI;
import cn.z201.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.audit.config.mdc.MdcTool;
import cn.z201.audit.persistence.entity.BizAuditLog;
import cn.z201.audit.repository.AuditRepository;
import cn.z201.audit.utils.AnnotationTools;
import cn.z201.audit.utils.JsonFormatTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Slf4j
public class MonitorAnnotationAspectPlugImpl implements MonitorAnnotationAspectPlugI {

    long startTime;

    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AnnotationTools.getDeclaredAnnotation(joinPoint);
        MonitorAnnotation monitorAnnotation = method.getDeclaredAnnotation(MonitorAnnotation.class);
        logHttp();
        logArgs(joinPoint);
        Object result = joinPoint.proceed();
        logHttpAfter();
        if (monitorAnnotation.audit()) {
            String type = monitorAnnotation.type();
            String title = monitorAnnotation.title();
            String descriptionExpression = monitorAnnotation.descriptionExpression();
            //格式化描述表达式得到易读的描述
            String description = parseDescriptionExpression(joinPoint.getArgs(), descriptionExpression);
            BizAuditLog bizAuditLog = new BizAuditLog();
            bizAuditLog.setEventType(type);
            bizAuditLog.setEventTitle(title);
            bizAuditLog.setEventDescription(description);
            bizAuditLog.setOpTraceId(MdcTool.getInstance().get());
            auditRepository.add(bizAuditLog);
        }
        return result;
    }

    private void logHttp(){
        startTime = System.currentTimeMillis();
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != httpServletRequest) {
            HttpServletRequest request = httpServletRequest.getRequest();
            // 打印请求相关参数
            log.info(" HTTP URL Method :  {}#{}", request.getRequestURL().toString(), request.getMethod());
        }
    }

    private void logHttpAfter(){
        long consuming = System.currentTimeMillis() - startTime;
        log.info(" Time-Consuming : {} ms", consuming);
    }

    private void logArgs(ProceedingJoinPoint joinPoint){
        log.info(" Class Method    :  {}#{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        log.info(" Args   : {}", JsonFormatTool.toString(arguments));
    }

    private String parseDescriptionExpression(Object[] args, String descriptionExpression) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(descriptionExpression, new TemplateParserContext());
        return expression.getValue(new StandardEvaluationContext(args), String.class);
    }


}
