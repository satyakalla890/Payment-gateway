package com.gateway.security;

import com.gateway.model.Merchant;
import com.gateway.repository.MerchantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiAuthFilter extends OncePerRequestFilter {

    private final MerchantRepository merchantRepository;

    public ApiAuthFilter(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow OPTIONS (CORS preflight) + health + public
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.startsWith("/health")
                || path.startsWith("/public")
                || path.startsWith("/api/v1/test")) {
            filterChain.doFilter(request, response);
            return;
        }



        String apiKey = request.getHeader("X-Api-Key");
        String apiSecret = request.getHeader("X-Api-Secret");

        if (apiKey == null || apiSecret == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":{\"code\":\"AUTHENTICATION_ERROR\",\"description\":\"Missing API credentials\"}}");
            return;
        }

        Merchant merchant = merchantRepository
                .findByApiKeyAndApiSecret(apiKey, apiSecret)
                .orElse(null);

        if (merchant == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":{\"code\":\"AUTHENTICATION_ERROR\",\"description\":\"Invalid API credentials\"}}");
            return;
        }

        request.setAttribute("merchant", merchant);
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
