package nl.tudelft.oopp.qubo.interceptors;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * The Rate limiting handler interceptor.
 */
@Component
public class RateLimitingHandlerInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> buckets;

    private final int requestLimit;
    private final int resetDuration;

    /**
     * Instantiates a RateLimitingHandlerInterceptor.
     *
     * @param requestLimit  The limit.
     * @param resetDuration The reset duration.
     */
    public RateLimitingHandlerInterceptor(
        @Value("${qubo.rate_limit.limit}") int requestLimit,
        @Value("${qubo.rate_limit.reset_duration}") int resetDuration) {
        this.requestLimit = requestLimit;
        this.resetDuration = resetDuration;
        this.buckets = new ConcurrentHashMap<>();
    }

    /**
     * Processes the request and checks whether the user has exceeded the rate limit.
     * Blocks the request if the limit has been exceeded.
     *
     * @param request  The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @param handler  The handler.
     * @return Whether the request is allowed.
     * @throws IOException if an error occurs while creating a response.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        Bucket bucket = this.getBucket(request.getRemoteAddr());

        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);

        if (!consumptionProbe.isConsumed()) {
            response.addHeader("Retry-After",
                String.valueOf(consumptionProbe.getNanosToWaitForRefill() / 1000000000));

            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());

            return false;
        }

        response.addHeader("X-Rate-Limit-Limit",
            String.valueOf(this.requestLimit));

        response.addHeader("X-Rate-Limit-Remaining",
            String.valueOf(consumptionProbe.getRemainingTokens()));

        return true;
    }

    private Bucket getBucket(String ip) {
        return buckets.computeIfAbsent(ip, (z) -> this.createBucket());
    }

    private Bucket createBucket() {
        Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(this.requestLimit, Duration.ofSeconds(this.resetDuration)))
            .build();

        return bucket;
    }
}
