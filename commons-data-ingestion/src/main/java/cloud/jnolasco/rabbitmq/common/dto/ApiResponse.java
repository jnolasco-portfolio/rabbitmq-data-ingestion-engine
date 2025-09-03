package cloud.jnolasco.rabbitmq.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * A generic, standardized, immutable response wrapper for all APIs in the system.
 * This implementation uses a Java Record for conciseness and immutability.
 *
 * @param status    The HTTP status code.
 * @param message   A developer-friendly message.
 * @param data      The actual data payload of the response. Can be null.
 * @param timestamp The timestamp for when the response was generated.
 * @param <T>       The type of the data payload.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int status,
    String message,
    T data,
    LocalDateTime timestamp
) {

    /**
     * Creates a standardized success response with a data payload.
     *
     * @param data The data to be returned.
     * @return A new ApiResponse instance.
     * @param <T> The type of the data.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data, LocalDateTime.now());
    }

    /**
     * Creates a standardized success response with a custom message and no data.
     *
     * @param message A custom success message.
     * @return A new ApiResponse instance.
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message, null, LocalDateTime.now());
    }

    /**
     * Creates a standardized error response.
     *
     * @param status The HTTP status code for the error.
     * @param message A descriptive error message.
     * @return A new ApiResponse instance with a null data payload.
     */
    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(status, message, null, LocalDateTime.now());
    }
}
