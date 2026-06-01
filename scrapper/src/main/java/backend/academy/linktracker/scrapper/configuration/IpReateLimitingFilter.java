package backend.academy.linktracker.scrapper.configuration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class IpReateLimitingFilter extends OncePerRequestFilter {
    private final RateLimiterRegistry registry;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        RateLimiter rateLimiter = registry.rateLimiter("ip-limit-%s".formatted(clientIp),"ip-limit");

        boolean isAllowed = rateLimiter.acquirePermission();

        if(!isAllowed){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Превышен лимит запросов\"}");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        filterChain.doFilter(request,response);
    }
}
