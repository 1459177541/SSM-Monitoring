package controller.inteceptor;


import model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class UserInterceptor implements HandlerInterceptor {

    public static ConcurrentHashMap<Integer, User> UUID = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Integer uuid = Arrays
                .stream(request.getCookies())
                .filter(cookie -> "uuid".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .map(Integer::parseInt).get();
        if (UUID != null && UUID.containsKey(uuid)) {
            return true;
        }
        response.setStatus(403);
        request.getRequestDispatcher("/err?err=403").forward(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
