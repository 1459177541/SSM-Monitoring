package controller.interceptor;

import model.Power;
import model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import service.SignService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private final SignService signService;
    private final Logger logger = Logger.getLogger(UserInterceptor.class);
    private final List<String> publicUri = List.of("/console", "/console/power");

    public UserInterceptor(SignService signService) {
        this.signService = signService;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private String getIRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = Arrays
                .stream(request.getCookies())
                .filter(cookie -> "uuid".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if (!signService.contain(uuid)) {
            logger.info("拒绝IP("+getIRealIPAddr(request)+")访问["+request.getRequestURI()+"]");
            response.setStatus(403);
            request.getRequestDispatcher("/err?err=403").forward(request, response);
            return false;
        }
        User user = signService.getUser(uuid);
        if (publicUri.stream().anyMatch(s -> s.equals(request.getRequestURI()))) {
            return true;
        }
        boolean hasPower = user
                .getPower()
                .stream()
                .anyMatch(power -> request.getRequestURI().contains(power.name));
        if (!hasPower) {
            logger.info("拒绝ID("+user.getId()+")访问["+request.getRequestURI()+"]");
            response.setStatus(403);
            request.getRequestDispatcher("/err?err=403").forward(request, response);
            return false;
        }
        boolean log = request.getRequestURI().contains(Power.file.name)
                && !"/console/file_watch".equals(request.getRequestURI());
        log = log || request.getRequestURI().contains(Power.user.name);
        if (log) {
            logger.info("允许ID(" + user.getId() + ")" +
                    "访问[" + request.getRequestURI() + "]" +
                    "参数[" + request.getParameterMap()
                    .entrySet()
                    .stream()
                    .map(stringEntry -> stringEntry.getKey() + ":" + Arrays.toString(stringEntry.getValue()))
                    .reduce(" ", String::concat) +
                    "]");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
