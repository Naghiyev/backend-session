package az.training.taskmanagement.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Hər request-də Authorization header-ini yoxlayan filter.
 *
 * Bu, JWT-nin necə işlədiyini göstərən SADƏLƏŞDİRİLMİŞ (introductory) versiyadır.
 * Real production-da bu iş Spring Security (SecurityFilterChain) ilə görülür.
 *
 *  - Token yoxdursa / yanlışdırsa  -> 401 Unauthorized (kimliyi təsdiqlənməyib)
 *  - Token var, amma icazə çatmırsa -> 403 Forbidden (kimlik var, icazə yox)
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Authorization header (Bearer token) tələb olunur");
            return;
        }

        String token = header.substring(7);
        Claims claims;
        try {
            claims = jwtService.parse(token);
        } catch (Exception e) {
            log.warn("Yanlış JWT: {}", e.getMessage());
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token yanlış və ya vaxtı keçmişdir");
            return;
        }

        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        // Authorization nümunəsi: DELETE yalnız ADMIN üçün -> 403
        if ("DELETE".equalsIgnoreCase(request.getMethod()) && !"ADMIN".equals(role)) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Bu əməliyyat üçün ADMIN rolu tələb olunur");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
