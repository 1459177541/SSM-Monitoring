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

    private boolean checkIp(String ip) {
        return ip == null
                || ip.length() == 0
                || "unknown".equalsIgnoreCase(ip)
                || "null".equalsIgnoreCase(ip);
    }

    private String getIRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (checkIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (checkIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (checkIp(ip)) {
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
            logger.info(String.format("允许ID[%03d] 访问[%-30s] 参数{%s}",
                    user.getId(),
                    request.getRequestURI(),
                    request.getParameterMap()
                            .entrySet()
                            .stream()
                            .reduce(new StringBuilder(),
                                    (builder, entry) -> builder
                                            .append(", ")
                                            .append(entry.getKey())
                                            .append(':')
                                            .append(Arrays.toString(entry.getValue())),
                                    (builder1, builder2) -> {
                                        throw new RuntimeException(builder1.toString() + ", " + builder2.toString());
                                    })
                            .delete(0, 2)
                            .toString()
            ));
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
